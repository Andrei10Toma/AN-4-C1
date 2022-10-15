import requests


def ex3():
    with requests.Session() as s:
        data = {
            'username': 'sprc',
            'password': 'admin',
            'nume': 'Toma Andrei',
        }
        response: requests.Response = s.post('https://sprc.dfilip.xyz/lab1/task3/login', json=data)
        print(response.status_code)
        print(response.text)

        response_get: requests.Response = s.get('https://sprc.dfilip.xyz/lab1/task3/check')
        print(response_get.status_code)
        print(response_get.text)


def ex2():
    data = {
        'username': 'sprc',
        'password': 'admin',
        'nume': 'Toma Andrei',
    }
    response: requests.Response = requests.post('https://sprc.dfilip.xyz/lab1/task2', json=data)
    print(response.status_code)
    print(response.text)


def ex1():
    header = {
        'secret2' : 'SPRCisBest'
    }
    data = {
        'nume': 'toma',
        'grupa': '343C1',
        'secret': 'SPRCisNice'
    }
    response: requests.Response = requests.post('https://sprc.dfilip.xyz/lab1/task1/check?nume=toma&grupa=343C1', data=data , headers=header)
    print(response.status_code)
    print(response.text)

ex1()
ex2()
ex3()
