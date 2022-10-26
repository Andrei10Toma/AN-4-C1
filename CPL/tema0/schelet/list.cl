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
        if isVoid elem then
            ""
        else
            case elem of
                l: List => if not isVoid next then new A2I.i2a(index + 1).concat(": [ ").concat(l.toString(index)).concat(" ]\n").concat(next.toString(index + 1)) else new A2I.i2a(index + 1).concat(": [ ").concat(l.toString(index)).concat(" ]\n") fi;
                p: Product => if not isVoid next then p.toString().concat(", ").concat(next.toString(index)) else p.toString() fi;
                r: Rank => if not isVoid next then r.toString().concat(", ").concat(next.toString(index)) else r.toString() fi;
                s: String => if not isVoid next then s.type_name().concat("(").concat(s).concat(")").concat(", ").concat(next.toString(index)) else s.type_name().concat("(").concat(s).concat(")") fi;
                i: Int => if not isVoid next then i.type_name().concat("(").concat(new A2I.i2a(i)).concat(")").concat(", ").concat(next.toString(index)) else i.type_name().concat("(").concat(new A2I.i2a(i)).concat(")") fi;
                b: Bool => if not isVoid next then b.type_name().concat("(").concat(if b = true then "true" else "false" fi).concat(")").concat(", ").concat(next.toString(index)) else b.type_name().concat("(").concat(if b = true then "true" else "false" fi).concat(")") fi;
                io: IO => if not isVoid next then io.type_name().concat("(").concat(")").concat(next.toString(index)) else io.type_name().concat("(").concat(")") fi;
            esac
        fi
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

    setElemAtIndex(el: Object, index: Int): SELF_TYPE {
        {
            if index = 0 then
                setElem(el)
            else
                next.setElemAtIndex(el, index - 1)
            fi;
            self;
        }
    };

    filterBy(f: Filter): List {
        let
            iterator: List <- self,
            result: List <- new List
        in 
        {
            while not isVoid iterator loop {
                if f.filter(iterator.elem()) then {
                    result.add(iterator.elem());
                    iterator <- iterator.next();
                }
                else
                    iterator <- iterator.next()
                fi;
            } pool;
            result;
        }
    };

    sortBy(c: Comparator, sort_type: String): SELF_TYPE {
        let
            iterator: List <- self,
            sorted: Bool <- false,
            swap_obj: Object
        in
        {
            if isVoid elem then
                self
            else
                if isVoid next then
                    self
                else {
                    while not sorted loop {
                        sorted <- true;
                        iterator <- self;
                        while not isVoid iterator.next() loop {
                            if 0 < if sort_type = "ascendent" then c.compareTo(iterator.elem(), iterator.next().elem()) else c.compareTo(iterator.next().elem(), iterator.elem()) fi
                            then {
                                swap_obj <- iterator.elem();
                                iterator.setElem(iterator.next().elem());
                                iterator.next().setElem(swap_obj);
                                iterator <- iterator.next();
                                sorted <- false;
                            }
                            else
                                iterator <- iterator.next()
                            fi;
                        }
                        pool;
                    }
                    pool;
                }
                fi
            fi;
            self;
        }
    };
};