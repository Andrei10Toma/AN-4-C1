class Main inherits IO{
    lists : List <- new List;
    tokenizer: StringTokenizer <- new StringTokenizer;
    looping : Bool <- true;
    read_list: Bool <- true;
    somestr : String;
    list: List;
    a2i: A2I <- new A2I;

    main():Object {
        {
            while looping loop {
                let
                    type: String
                in
                if read_list then {
                    list <- new List;
                    somestr <- in_string();
                    tokenizer.init(somestr, " ");
                    type <- tokenizer.next();
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
                                    ""
                                fi
                            fi
                        fi
                    fi;
                    
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