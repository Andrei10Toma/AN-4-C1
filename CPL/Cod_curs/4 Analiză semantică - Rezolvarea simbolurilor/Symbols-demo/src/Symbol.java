
public abstract class Symbol {
    protected String name;
    
    public Symbol(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

class IdSymbol extends Symbol {
    public IdSymbol(String name) {
        super(name);
    }
};

/*
// O funcție este atât simbol, cât și domeniu de vizibilitate pentru parametrii
// săi formali.
class FunctionSymbol extends IdSymbol implements Scope {
 
}
*/