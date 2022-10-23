class A2I {

     c2i(char : String) : Int {
    if char = "0" then 0 else
    if char = "1" then 1 else
    if char = "2" then 2 else
        if char = "3" then 3 else
        if char = "4" then 4 else
        if char = "5" then 5 else
        if char = "6" then 6 else
        if char = "7" then 7 else
        if char = "8" then 8 else
        if char = "9" then 9 else
        { abort(); 0; }  -- the 0 is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   i2c is the inverse of c2i.
*)
     i2c(i : Int) : String {
    if i = 0 then "0" else
    if i = 1 then "1" else
    if i = 2 then "2" else
    if i = 3 then "3" else
    if i = 4 then "4" else
    if i = 5 then "5" else
    if i = 6 then "6" else
    if i = 7 then "7" else
    if i = 8 then "8" else
    if i = 9 then "9" else
    { abort(); ""; }  -- the "" is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   a2i converts an ASCII string into an integer.  The empty string
is converted to 0.  Signed and unsigned strings are handled.  The
method aborts if the string does not represent an integer.  Very
long strings of digits produce strange answers because of arithmetic 
overflow.
*)
     a2i(s : String) : Int {
        if s.length() = 0 then 0 else
    if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
        if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
           a2i_aux(s)
        fi fi fi
     };

(*
  a2i_aux converts the usigned portion of the string.  As a programming
example, this method is written iteratively.
*)
     a2i_aux(s : String) : Int {
    (let int : Int <- 0 in  
           {    
               (let j : Int <- s.length() in
              (let i : Int <- 0 in
            while i < j loop
            {
                int <- int * 10 + c2i(s.substr(i,1));
                i <- i + 1;
            }
            pool
          )
           );
              int;
        }
        )
     };

(*
    i2a converts an integer to a string.  Positive and negative 
numbers are handled correctly.  
*)
    i2a(i : Int) : String {
    if i = 0 then "0" else 
        if 0 < i then i2a_aux(i) else
          "-".concat(i2a_aux(i * ~1)) 
        fi fi
    };
    
(*
    i2a_aux is an example using recursion.
*)      
    i2a_aux(i : Int) : String {
        if i = 0 then "" else 
        (let next : Int <- i / 10 in
        i2a_aux(next).concat(i2c(i - next * 10))
        )
        fi
    };
};class List {
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

    filterBy():SELF_TYPE {
        self (* TODO *)
    };

    sortBy():SELF_TYPE {
        self (* TODO *)
    };
};class Main inherits IO{
    lists : List <- new List;
    tokenizer: StringTokenizer <- new StringTokenizer;
    looping : Bool <- true;
    read_list: Bool <- true;
    somestr : String;
    list: List;
    a2i: A2I <- new A2I;

    cast_object_to_list(obj: Object): List {
        case obj of
            l: List => l;
        esac
    };

    main():Object {
        let
            list: List <- new List
        in
        {
            while looping loop {
                let
                    type: String,
                    print_index_string: String,
                    print_index_int: Int,
                    index_1: Int,
                    index_2: Int,
                    merged_lists: List
                in
                {
                    somestr <- in_string();
                    tokenizer.init(somestr, " ");
                    type <- tokenizer.next();
                    if type = "END" then {
                        read_list <- false;
                        lists.add(list);
                        list <- new List;
                    }
                    else
                        if read_list then {
                            if type = "Soda" then
                                list.add(new Soda.init(tokenizer.next(), tokenizer.next(), a2i.a2i(tokenizer.next())))
                            else
                                if type = "Coffee" then
                                    list.add(new Coffee.init(tokenizer.next(), tokenizer.next(), a2i.a2i(tokenizer.next())))
                                else
                                    if type = "Laptop" then
                                        list.add(new Laptop.init(tokenizer.next(), tokenizer.next(), a2i.a2i(tokenizer.next())))
                                    else
                                        if type = "Router" then
                                            list.add(new Router.init(tokenizer.next(), tokenizer.next(), a2i.a2i(tokenizer.next())))
                                        else
                                            if type = "Private" then
                                                list.add(new Private.init(tokenizer.next()))
                                            else
                                                if type = "Corporal" then
                                                    list.add(new Corporal.init(tokenizer.next()))
                                                else
                                                    if type = "Sergent" then
                                                        list.add(new Sergent.init(tokenizer.next()))
                                                    else
                                                        if type = "Officer" then
                                                            list.add(new Officer.init(tokenizer.next()))
                                                        else
                                                            if type = "String" then
                                                                list.add(tokenizer.next())
                                                            else
                                                                if type = "Int" then
                                                                    list.add(a2i.a2i(tokenizer.next()))
                                                                else
                                                                    if type = "Bool" then
                                                                        if tokenizer.next() = "true" then list.add(true) else list.add(false) fi
                                                                    else
                                                                        if type = "IO" then
                                                                            list.add(new IO)
                                                                        else
                                                                            abort()
                                                                        fi
                                                                    fi
                                                                fi
                                                            fi
                                                        fi
                                                    fi
                                                fi
                                            fi
                                        fi
                                    fi
                                fi
                            fi;
                        }
                        else
                            if type = "print" then {
                                print_index_string <- tokenizer.next();
                                if print_index_string = "" then
                                    out_string(lists.toString(0))
                                else {
                                    print_index_int <- a2i.a2i(print_index_string);
                                    case lists.get(print_index_int - 1) of
                                        x: List => out_string("[ ").out_string(x.toString(0)).out_string(" ]").out_string("\n");
                                    esac;
                                }
                                fi;
                            }
                            else
                                if type = "load" then
                                    read_list <- true
                                else
                                    if type = "merge" then {
                                        index_1 <- a2i.a2i(tokenizer.next());
                                        index_2 <- a2i.a2i(tokenizer.next());
                                        merged_lists <- cast_object_to_list(lists.get(index_1 - 1)).merge(cast_object_to_list(lists.get(index_2 - 1)));
                                        lists.add(merged_lists);
                                        lists.remove_merged(index_1 - 1);
                                        lists.remove_merged(index_2 - 2);
                                    }
                                    else
                                        abort()
                                    fi
                                fi
                            fi
                        fi
                    fi;
                };
            } pool;
        }
    };
};class StringTokenizer {
    delimiter : String;
    string: String;
    index: Int;

    init(s: String, d: String): StringTokenizer {
        {
            delimiter <- d;
            string <- s;
            index <- 0;
            self;
        }
    };

    next(): String {
        let
            current_character: String,
            looping: Bool <- true,
            tmp_index: Int <- index
        in
        {
            if string.length() <= index then
                ""
            else {
                while looping loop
                {
                    current_character <- string.substr(index, 1);
                    if current_character = delimiter then {
                        looping <- false;
                        index <- index + 1;
                    }
                    else
                        if string.length() = index + 1 then {
                            looping <- false;
                            index <- index + 2;
                        }
                        else
                            index <- index + 1
                        fi
                    fi;
                } pool;

                string.substr(tmp_index, index - tmp_index - 1);
            }
            fi;
        }
    };
};(*******************************
 *** Classes Product-related ***
 *******************************)
class Product {
    name : String;
    model : String;
    price : Int;

    init(n : String, m: String, p : Int):SELF_TYPE {{
        name <- n;
        model <- m;
        price <- p;
        self;
    }};

    getprice():Int{ price * 119 / 100 };

    toString():String {
        type_name().concat("(").concat(name).concat(",").concat(model).concat(")")
    };
};

class Edible inherits Product {
    -- VAT tax is lower for foods
    getprice():Int { price * 109 / 100 };
};

class Soda inherits Edible {
    -- sugar tax is 20 bani
    getprice():Int {price * 109 / 100 + 20};
};

class Coffee inherits Edible {
    -- this is technically poison for ants
    getprice():Int {price * 119 / 100};
};

class Laptop inherits Product {
    -- operating system cost included
    getprice():Int {price * 119 / 100 + 499};
};

class Router inherits Product {};

(****************************
 *** Classes Rank-related ***
 ****************************)
class Rank {
    name : String;

    init(n : String):SELF_TYPE {
        {
            name <- n;
            self;
        }
    };

    toString():String {
        type_name().concat("(").concat(name).concat(")")
    };
};

class Private inherits Rank {};

class Corporal inherits Private {};

class Sergent inherits Corporal {};

class Officer inherits Sergent {};(* Think of these as abstract classes *)
class Comparator {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class Filter {
    filter(o : Object):Bool {true};
};

(* TODO: implement specified comparators and filters*)