package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class BlockExpression extends Expression {
    public final List<Expression> expressions;

    public BlockExpression(Token token, List<Expression> expressions, CoolParser.BlockExpressionContext ctx) {
        super(token, ctx);
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
