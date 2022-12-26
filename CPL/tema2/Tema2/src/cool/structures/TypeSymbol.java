package cool.structures;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TypeSymbol extends Symbol implements Scope {
    public static final TypeSymbol INT = new TypeSymbol("Int", "Object");
    public static final TypeSymbol STRING = new TypeSymbol("String", "Object");
    public static final TypeSymbol BOOL = new TypeSymbol("Bool", "Object");
    public static final TypeSymbol IO = new TypeSymbol("IO", "Object");
    public static final TypeSymbol OBJECT = new TypeSymbol("Object", null);
    public static final TypeSymbol SELF_TYPE = new TypeSymbol("SELF_TYPE", "Object");

    public final String inheritsName;
    public final Map<String, IdSymbol> fieldSymbols = new LinkedHashMap<>();
    public final Map<String, FunctionSymbol> methodSymbols = new LinkedHashMap<>();
    public TypeSymbol parent;

    public TypeSymbol(String name, String inheritsName) {
        super(name);
        this.inheritsName = inheritsName;
        IdSymbol self = new IdSymbol("self");
        self.setType(SELF_TYPE);
        fieldSymbols.put("self", self);
    }

    @Override
    public boolean add(Symbol sym) {
        if (sym instanceof IdSymbol) {
            if (fieldSymbols.containsKey(sym.getName()))
                return false;

            fieldSymbols.put(sym.getName(), (IdSymbol) sym);
            return true;
        } else if (sym instanceof FunctionSymbol) {
            if (methodSymbols.containsKey(sym.getName()))
                return false;

            methodSymbols.put(sym.getName(), (FunctionSymbol) sym);
            return true;
        }

        return false;
    }

    @Override
    public Symbol lookup(String name) {
        var sym = fieldSymbols.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(name);

        return null;
    }

    public Symbol lookupFunction(String name) {
        var sym = methodSymbols.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookupFunction(name);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public boolean inherits(TypeSymbol other) {
        if (other == this)
            return true;

        if (parent == other)
            return true;

        if (parent != null)
            return parent.inherits(other);

        return false;
    }

    public TypeSymbol join(TypeSymbol other) {
        if (this == other || other == null)
            return this;

        Set<TypeSymbol> inheritanceChain = new LinkedHashSet<>();
        Set<TypeSymbol> otherInheritanceChain = new LinkedHashSet<>();

        for (TypeSymbol otherIterator = other; otherIterator != null; otherIterator = otherIterator.parent)
            otherInheritanceChain.add(otherIterator);

        for (TypeSymbol iterator = this; iterator != null; iterator = iterator.parent)
            inheritanceChain.add(iterator);

        for (TypeSymbol typeSymbol : inheritanceChain)
            if (otherInheritanceChain.contains(typeSymbol))
                return typeSymbol;

        return TypeSymbol.OBJECT;
    }

    public boolean isBasicType() {
        return this.equals(TypeSymbol.INT) || this.equals(TypeSymbol.BOOL) || this.equals(TypeSymbol.STRING);
    }
}
