class List {
    elem: Object;
    next: List;

    (* TODO: store data *)

    add(o : Object):SELF_TYPE {
        {
            if isVoid elem then
                elem <- o
            else
                self
            fi;
            self; (* TODO *)
        }
    };

    next(): List {
        next
    };

    elem(): Object {
        elem
    };

    toString():String {
        "[TODO: implement me]"
    };

    merge(other : List):SELF_TYPE {
        self (* TODO *)
    };

    filterBy():SELF_TYPE {
        self (* TODO *)
    };

    sortBy():SELF_TYPE {
        self (* TODO *)
    };
};