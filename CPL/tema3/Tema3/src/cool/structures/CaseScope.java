package cool.structures;

public class CaseScope implements Scope {
    public Scope parent;
    public IdSymbol var;

    public CaseScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        if (!(sym instanceof IdSymbol idSym))
            return false;

        var = idSym;
        return true;
    }

    @Override
    public Symbol lookup(String str) {
        if (str.equals(var.getName()))
            return var;
        if (parent != null)
            return parent.lookup(str);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}
