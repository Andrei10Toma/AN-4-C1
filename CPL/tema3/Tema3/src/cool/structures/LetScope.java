package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class LetScope implements Scope {
    public final Map<String, IdSymbol> variables = new LinkedHashMap<>();
    public Scope parent;

    public LetScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        variables.put(sym.getName(), (IdSymbol) sym);
        return true;
    }

    @Override
    public Symbol lookup(String name) {
        var sym = variables.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(name);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}
