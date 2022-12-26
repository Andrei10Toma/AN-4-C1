package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class AssignExpression extends Expression {
    public final Token variable;
    public final Expression expr;

    public AssignExpression(Token token, Token variable, Expression expr, CoolParser.AssignExpressionContext ctx) {
        super(token, ctx);
        this.variable = variable;
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
