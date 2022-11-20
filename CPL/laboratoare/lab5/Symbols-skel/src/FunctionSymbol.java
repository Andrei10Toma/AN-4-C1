import java.util.*;

// O functie este atât simbol, cât și domeniu de vizibilitate pentru parametrii
// săi formali.

/*
 TODO 1: Implementați clasa FunctionSymbol, suprascriind metodele din interfață
        și adăugându-i un nume.
 */
public class FunctionSymbol extends IdSymbol implements Scope {
    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    protected Scope parent;

    public FunctionSymbol(String name, Scope parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol s) {
        if (symbols.containsKey(s.name))
            return false;
        symbols.put(s.name, s);
        return true;
    }

    @Override
    public Symbol lookup(String s) {
        if (symbols.containsKey(s))
            return symbols.get(s);

        return parent.lookup(s);
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}