package cool.ast;

import org.antlr.v4.runtime.Token;

public class WhileExpression extends Expression {
    public final Expression condition;
    public final Expression expr;

    public WhileExpression(Token token, Expression condition, Expression expr) {
        super(token);
        this.condition = condition;
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
