package cool.structures;

import java.util.LinkedHashMap;

public class FunctionSymbol extends Symbol implements Scope {
    public final LinkedHashMap<String, IdSymbol> formalParameters = new LinkedHashMap<>();
    public TypeSymbol returnType;
    public TypeSymbol parent;

    public FunctionSymbol(String name) {
        super(name);
    }

    @Override
    public boolean add(Symbol sym) {
        if (!(sym instanceof IdSymbol))
            return false;
        if (formalParameters.containsKey(sym.getName()))
            return false;

        formalParameters.put(sym.getName(), (IdSymbol) sym);
        return true;
    }

    @Override
    public Symbol lookup(String str) {
        var sym = formalParameters.get(str);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(str);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}
