package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class PlusMinusExpression extends Expression {
    public final Expression left;
    public final Expression right;
    public final Token op;

    public PlusMinusExpression(Token token, Expression left, Expression right, Token op, CoolParser.PlusMinusExpressionContext ctx) {
        super(token, ctx);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
