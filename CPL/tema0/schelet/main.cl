class Main inherits IO{
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
};