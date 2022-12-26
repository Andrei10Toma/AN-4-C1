package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class ComplementExpression extends Expression {
    public final Expression expr;

    public ComplementExpression(Token token, Expression expr, CoolParser.ComplementExpressionContext ctx) {
        super(token, ctx);
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
