class Main inherits IO{
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
                    aux_string: String,
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
                                aux_string <- tokenizer.next();
                                if aux_string = "" then
                                    out_string(lists.toString(0))
                                else {
                                    print_index_int <- a2i.a2i(aux_string);
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
                                        if type = "filterBy" then {
                                            index_1 <- a2i.a2i(tokenizer.next());
                                            aux_string <- tokenizer.next();
                                            if aux_string = "ProductFilter" then
                                                lists.setElemAtIndex(cast_object_to_list(lists.get(index_1 - 1)).filterBy(new ProductFilter), index_1 - 1)
                                            else
                                                if aux_string = "RankFilter" then
                                                    lists.setElemAtIndex(cast_object_to_list(lists.get(index_1 - 1)).filterBy(new RankFilter), index_1 - 1)
                                                else
                                                    if aux_string = "SamePriceFilter" then
                                                        lists.setElemAtIndex(cast_object_to_list(lists.get(index_1 - 1)).filterBy(new SamePriceFilter), index_1 - 1)
                                                    else
                                                        abort()
                                                    fi
                                                fi
                                            fi;
                                        }
                                        else
                                            if type = "sortBy" then {
                                                index_1 <- a2i.a2i(tokenizer.next());
                                                aux_string <- tokenizer.next();
                                                if aux_string = "PriceComparator" then
                                                    cast_object_to_list(lists.get(index_1 - 1)).sortBy(new PriceComparator, tokenizer.next())
                                                else
                                                    if aux_string = "RankComparator" then
                                                        cast_object_to_list(lists.get(index_1 - 1)).sortBy(new RankComparator, tokenizer.next())
                                                    else
                                                        if aux_string = "AlphabeticComparator" then
                                                            cast_object_to_list(lists.get(index_1 - 1)).sortBy(new AlphabeticComparator, tokenizer.next())
                                                        else
                                                            abort()
                                                        fi
                                                    fi
                                                fi;
                                            }
                                            else
                                                abort()
                                            fi
                                        fi
                                    fi
                                fi
                            fi
                        fi
                    fi;
                };
            } pool;
        }
    };
};