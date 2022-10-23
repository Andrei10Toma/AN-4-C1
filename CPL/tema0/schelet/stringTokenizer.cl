class StringTokenizer {
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
};