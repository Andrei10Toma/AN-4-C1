(* Think of these as abstract classes *)
class Comparator {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class Filter {
    filter(o : Object):Bool {true};
};

(* TODO: implement specified comparators and filters*)

class ProductFilter inherits Filter {
    filter(o : Object): Bool {
        case o of
            x: Product => true;
            y: Object => false;
        esac
    };
};

class RankFilter inherits Filter {
    filter(o : Object): Bool {
        case o of
            x: Rank => true;
            y: Object => false;
        esac
    };
};

class SamePriceFilter inherits Filter {
    filter(o : Object): Bool {
        case o of
            x: Product => x@Product.getprice() = x.getprice();
            y: Object => false;
        esac
    };
};

class PriceComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object): Int {
        let
            diff: Int
        in
        {
            case o1 of
                p1: Product => {
                    case o2 of
                        p2: Product => diff <- p1.getprice() - p2.getprice();
                        y: Object => abort();
                    esac;
                };
                x: Object => abort();
            esac;
            diff;
        }
    };
};

class RankComparator inherits Comparator {
    compareTo(o1: Object, o2: Object): Int {
        let
            diff: Int
        in
        {
            case o1 of
                r1: Rank => {
                    case o2 of
                        r2: Rank => diff <- r1.getScore() - r2.getScore();
                        y: Object => abort();
                    esac;
                };
                x: Object => abort();
            esac;
            diff;
        }
    };
};

class AlphabeticComparator inherits Comparator {
    compareTo(o1: Object, o2: Object): Int {
        let
            diff: Int
        in
        {
            case o1 of
                s1: String => {
                    case o2 of
                        s2: String => diff <- if s1 < s2 then (0 - 1) else if s1 = s2 then 0 else 1 fi fi;
                        y: Object => abort();
                    esac;
                };
                x: Object => abort();
            esac;
            diff;
        }
    };
};
