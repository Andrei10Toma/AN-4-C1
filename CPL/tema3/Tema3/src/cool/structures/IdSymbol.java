package cool.structures;

public class IdSymbol extends Symbol {
    protected TypeSymbol type;
    public int offset;
    public boolean isField;
    public boolean isFormal;
    public boolean isLocal;

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
