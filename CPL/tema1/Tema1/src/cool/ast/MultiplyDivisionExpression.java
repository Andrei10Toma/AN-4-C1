package cool.ast;

import org.antlr.v4.runtime.Token;

public class MultiplyDivisionExpression extends Expression {
    public final Expression left;
    public final Expression right;
    public final Token op;

    public MultiplyDivisionExpression(Token token, Expression left, Expression right, Token op) {
        super(token);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
