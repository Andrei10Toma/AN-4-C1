package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class BlockExpression extends Expression {
    public final List<Expression> expressions;

    public BlockExpression(Token token, List<Expression> expressions) {
        super(token);
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
