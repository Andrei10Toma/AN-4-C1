public interface ASTVisitor<T> {
    T visit(Id id);
    T visit(Int intt);
    T visit(Float floatt);
    T visit(Bool booll);
    T visit(If iff);
    // TODO 2: Adăugați metode pentru fiecare clasă definită anterior
    T visit(FunctionCall functionCall);
    T visit(UnaryMinus unaryMinus);
    T visit(MultDiv multDiv);
    T visit(PlusMinus multDiv);
    T visit(Relational relational);
    T visit(Assign assign);
    T visit(Paren paren);
    T visit(VarDef varDef);
    T visit(Formal formal);
    T visit(FuncDef funcDef);
    T visit(Prog prog);
}
