package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class LetExpression extends Expression {
    public final List<Local> locals;
    public final Expression expression;

    public LetExpression(Token token, List<Local> locals, Expression expression) {
        super(token);
        this.locals = locals;
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
