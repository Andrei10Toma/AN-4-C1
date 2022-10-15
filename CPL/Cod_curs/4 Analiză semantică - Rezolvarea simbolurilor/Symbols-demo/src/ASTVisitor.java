public interface ASTVisitor<T> {
    T visit(Id id);
    T visit(Int intt);
    T visit(If iff);
    T visit(Plus plus);
    T visit(Type type);
    T visit(VarDef definition);
    T visit(Block block);
}
