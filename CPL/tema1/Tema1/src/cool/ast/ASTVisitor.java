package cool.ast;

public interface ASTVisitor<T> {
    T visit(Program program);
    T visit(ClassDefinition classDefinition);
    T visit(FieldDefinition fieldDefinition);
    T visit(Int intt);
    T visit(Formal formal);
    T visit(FunctionDefinition functionDefinition);
    T visit(Falsee falsee);
    T visit(Id id);
    T visit(Stringg stringg);
    T visit(Truee truee);
    T visit(ParenthesisExpression parenthesisExpression);
    T visit(MultiplyDivisionExpression multiplyDivisionExpression);
    T visit(PlusMinusExpression plusMinusExpression);
    T visit(RelationalExpression relationalExpression);
    T visit(NotExpression notExpression);
    T visit(ComplementExpression complementExpression);
    T visit(AssignExpression assignExpression);
}
