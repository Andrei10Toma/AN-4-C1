package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class IsVoidExpression extends Expression {
    public final Expression expr;

    public IsVoidExpression(Token token, Expression expr, CoolParser.IsVoidExpressionContext ctx) {
        super(token, ctx);
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
