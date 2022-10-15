
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
    // Fiecare identificator posedă un tip.
    protected TypeSymbol type;
    
    public IdSymbol(String name) {
        super(name);
    }
    
    public void setType(TypeSymbol type) {
        this.type = type;
    }
    
    public TypeSymbol getType() {
        return type;
    }
}

class TypeSymbol extends Symbol {
    public TypeSymbol(String name) {
        super(name);
    }
    
    // Symboluri aferente tipurilor, definite global
    public static final TypeSymbol INT  = new TypeSymbol("Int");
    public static final TypeSymbol BOOL = new TypeSymbol("Bool");
}

/*
// O funcție este atât simbol, cât și domeniu de vizibilitate pentru parametrii
// săi formali.
class FunctionSymbol extends IdSymbol implements Scope {
 
}
*/