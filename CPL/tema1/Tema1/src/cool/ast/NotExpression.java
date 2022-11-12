package cool.ast;

import org.antlr.v4.runtime.Token;

public class NotExpression extends Expression {
    public final Expression expr;

    public NotExpression(Token token, Expression expr) {
        super(token);
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
