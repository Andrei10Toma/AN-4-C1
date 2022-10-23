class List {
    elem: Object;
    next: List;

    (* TODO: store data *)

    add(o : Object):SELF_TYPE {
        let
            iterator: List <- self
        in
        {
            if isVoid elem then
                elem <- o
            else
                if isVoid next then {
                    next <- new List;
                    next.setElem(o);
                }
                else
                    next.add(o)
                fi
            fi;
            self;
        }
    };

    get(index: Int): Object {
        if index = 0 then
            elem
        else
            next.get(index - 1)
        fi
    };

    next(): List {
        next
    };

    elem(): Object {
        elem
    };

    setElem(e : Object): SELF_TYPE {
        {
            elem <- e;
            self;
        }
    };

    setNext(n: List): SELF_TYPE {
        {
            next <- n;
            self;
        }
    };

    toString(index: Int):String {
        case elem of
            l: List => if not isVoid next then new A2I.i2a(index + 1).concat(": [ ").concat(l.toString(index)).concat(" ]\n").concat(next.toString(index + 1)) else new A2I.i2a(index + 1).concat(": [ ").concat(l.toString(index)).concat(" ]\n") fi;
            p: Product => if not isVoid next then p.toString().concat(", ").concat(next.toString(index)) else p.toString() fi;
            r: Rank => if not isVoid next then r.toString().concat(", ").concat(next.toString(index)) else r.toString() fi;
            s: String => if not isVoid next then s.type_name().concat("(").concat(s).concat(")").concat(", ").concat(next.toString(index)) else s.type_name().concat("(").concat(s).concat(")") fi;
            i: Int => if not isVoid next then i.type_name().concat("(").concat(new A2I.i2a(i)).concat(")").concat(", ").concat(next.toString(index)) else i.type_name().concat("(").concat(new A2I.i2a(i)).concat(")") fi;
            b: Bool => if not isVoid next then b.type_name().concat("(").concat(if b = true then "true" else "false" fi).concat(")").concat(", ").concat(next.toString(index)) else b.type_name().concat("(").concat(if b = true then "true" else "false" fi).concat(")") fi;
            io: IO => if not isVoid next then io.type_name().concat("(").concat(")").concat(next.toString(index)) else io.type_name().concat("(").concat(")") fi;
        esac
    };

    merge(other : List): SELF_TYPE {
        if not isVoid other then
            add(other.elem()).merge(other.next())
        else
            self
        fi
    };

    remove_merged(index: Int): SELF_TYPE {
        {
            if index = 1 then
                next <- next.next()
            else
                if index = 0 then
                    next <- next.next()
                else
                    next.remove_merged(index - 1)
                fi
            fi;
            self;
        }
    };

    filterBy(f: Filter): SELF_TYPE {
        {
            let
                iterator_1: List <- self,
                iterator_2: List
            in {
                while not isVoid iterator_1 loop
                    if f.filter(iterator_1.elem()) then
                        self
                    else
                        if not isVoid iterator_2
                            iterator_2.setNext(next)
                        else
                            self <- next
                        fi
                    fi;
                    iterator_2 <- iterator_1;
                    iterator_1 <- iterator_1.next();
                pool;
            };
            self;
        }
    };

    sortBy():SELF_TYPE {
        self (* TODO *)
    };
};