package cool.structures;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;

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
    public int tag;
    private static int currentTag = 0;
    public Set<TypeSymbol> children;
    public final static List<TypeSymbol> tagOrder = new ArrayList<>();

    public TypeSymbol(String name, String inheritsName) {
        super(name);
        children = new LinkedHashSet<>();
        this.inheritsName = inheritsName;
        IdSymbol self = new IdSymbol("self");
        self.setType(SELF_TYPE);
        fieldSymbols.put("self", self);
    }

    public List<TypeSymbol> getInheritanceChain() {
        TypeSymbol start = this;
        List<TypeSymbol> inheritanceChain = new LinkedList<>();
        while (start != null) {
            inheritanceChain.add(0, start);
            start = start.parent;
        }

        return inheritanceChain;
    }

    public ST getInitMethod(STGroupFile template) {
        return template.getInstanceOf("initMethod")
                .add("class", this.name)
                .add("parent", parent != null ? parent.name : null);
    }

    public void setTags() {
        tag = currentTag;
        currentTag++;
        tagOrder.add(this);
        if (children.isEmpty())
            return;

        children.forEach(TypeSymbol::setTags);
    }

    public ST generateDispatchTable(STGroupFile template) {
        TypeSymbol start = this;
        List<TypeSymbol> inheritanceChain = new LinkedList<>();
        while (start != null) {
            inheritanceChain.add(0, start);
            start = start.parent;
        }

        List<FunctionSymbol> methods = new ArrayList<>();
        inheritanceChain.forEach(type -> methods.addAll(type.methodSymbols.values().stream().toList()));
        Map<String, String> finalMethods = new LinkedHashMap<>();
        methods.forEach(method -> finalMethods.put(method.getName(), ((TypeSymbol) method.getParent()).getName()));
        String methodsString = finalMethods.entrySet()
                .stream()
                .map(entry -> ".word\t" + entry.getValue() + "." + entry.getKey())
                .collect(Collectors.joining("\n"));
        return template.getInstanceOf("dispatchTable")
                .add("class", name)
                .add("methods", methodsString);
    }

    private String getDefaultValueForType(TypeSymbol type) {
        if (type == TypeSymbol.INT)
            return "int_const0";
        else if (type == TypeSymbol.STRING)
            return "str_const0";
        else if (type == TypeSymbol.BOOL)
            return "bool_const0";
        return "0";
    }

    public List<IdSymbol> getAllAttributes(STGroupFile template) {
        TypeSymbol start = this;
        List<TypeSymbol> inheritanceChain = new LinkedList<>();
        while (start != null) {
            inheritanceChain.add(0, start);
            start = start.parent;
        }

        List<IdSymbol> fields = new ArrayList<>();
        inheritanceChain.forEach(type -> fields.addAll(type.fieldSymbols
                .values()
                .stream()
                .filter(field -> !field.getName().equals("self"))
                .toList()));

        return fields;
    }

    public ST generateObjectPrototype(STGroupFile template) {
        if (this == TypeSymbol.OBJECT || this == TypeSymbol.IO) {
            return template.getInstanceOf("classProtObj")
                    .add("class", name)
                    .add("tag", tag)
                    .add("size", 3);
        } else if (this == TypeSymbol.INT || this == TypeSymbol.BOOL) {
            return template.getInstanceOf("classProtObj")
                    .add("class", name)
                    .add("tag", tag)
                    .add("size", 4)
                    .add("attributes", ".word\t0");
        } else if (this == TypeSymbol.STRING) {
            return template.getInstanceOf("classProtObj")
                    .add("class", name)
                    .add("tag", tag)
                    .add("size", 5)
                    .add("attributes", ".word\tint_const0\n.asciiz\t\"\"\n.align\t2");
        }

        List<IdSymbol> fields = getAllAttributes(template);
        String fieldsString = fields.stream()
                .map(field -> ".word\t" + getDefaultValueForType(field.type))
                .collect(Collectors.joining("\n"));

        return template.getInstanceOf("classProtObj")
                .add("class", name)
                .add("tag", tag)
                .add("size", 3 + fields.size())
                .add("attributes", fieldsString);
    }

    public ST getClassObjTabPair(STGroupFile template) {
        return template.getInstanceOf("classObjTabPair")
                .add("class", this.name);
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
        return this.equals(TypeSymbol.INT) || this.equals(TypeSymbol.BOOL) || this.equals(TypeSymbol.STRING) ||
                this.equals(TypeSymbol.OBJECT) || this.equals(TypeSymbol.IO);
    }
}
