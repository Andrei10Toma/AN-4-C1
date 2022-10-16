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
};class Main inherits IO{
    lists : List <- new List;
    tokenizer: StringTokenizer <- new StringTokenizer;
    looping : Bool <- true;
    read_list: Bool <- true;
    somestr : String;
    list: List;

    main():Object {
        {
            while looping loop {
                let
                    token: String
                in
                if read_list then {
                    list <- new List;
                    somestr <- in_string();
                    -- out_string(somestr).out_string("\n");
                    tokenizer.init(somestr, " ");
                    token <- tokenizer.next();
                    if token = "Soda" then {
                        list.add(new Soda.init(token, tokenizer.next(), 10));
                        case list.elem() of
                            x : Soda => out_string(x.toString()).out_string("\n");
                        esac;
                    }
                    else
                        ""
                    fi;
                    -- if isVoid lists.next() then
                        -- out_string("niceeeee").out_string("\n")
                    -- else
                        -- out_string("not niceeee").out_string("\n")
                    -- fi;
                }
                else {
                    somestr <- in_string();
                    tokenizer.init(somestr, " ");
                }
                fi;
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
        name.concat(" ").concat(model)
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

    init(n : String):String {
        name <- n
    };

    toString():String {
        -- Hint: what are the default methods of Object?
        "TODO: implement me"
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