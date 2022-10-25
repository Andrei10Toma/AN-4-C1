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
