direct = {
    'M': {
        'Rd': [('M', '-')],
        'Wr': [('M', '-')],
    },
    'E': {
        'Rd': [('E', '-')],
        'Wr': [('M', '-')],
    },
    'S': {
        'Rd': [('S', '-')],
        'Wr': [('M', 'BusRdX')],
    },
    'I': {
        'Rd': [('E', 'BusRd(S#)'), ('S', 'BusRd(S)')],
        'Wr': [('M', 'BusRdX')],
    },
}

dot = {
    'M': {
        'BusRd': ('S', 'Flush'),
        'BusRdX': ('I', 'Flush'),
    },
    'E': {
        'BusRd': ('S', 'Flush'),
        'BusRdX': ('I', 'Flush*'),
    },
    'S': {
        'BusRd': ('S', 'Flush*'),
        'BusRdX': ('I', 'Flush*'),
    },
    'I': {},
}

if __name__ == '__main__':
    actions = ['initial', 'P2Wr', 'P0Rd', 'P1Rd', 'P0Wr', 'P2Rd']
    actions_result = []
    processor_states = ['I', 'I', 'I']
    op = 0
    for action in actions:
        if action == 'initial':
            print(processor_states)
            continue
        processor = int(action[1])
        processor_action = action[2:]
        processor_state = processor_states[processor]
        if processor_state == 'I' and (processor_action == 'Rd' or processor_action == 'Wr'):
            processor_states[processor] = direct[processor_state][processor_action][op][0]
            actions_result.append(direct[processor_state][processor_action][op][1])
        else:
            processor_states[processor] = direct[processor_state][processor_action][0][0]
            actions_result.append(direct[processor_state][processor_action][0][1])
        for p in range(3):
            if p != processor:
                act = 'BusRd' if processor_action == 'Rd' else 'BusRdX'
                if processor_states[p] != 'I':
                    processor_states[p] = dot[processor_states[p]][act][0]
        print(processor_states)
        op = 1
    print(actions_result)
