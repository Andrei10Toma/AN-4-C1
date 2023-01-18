package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class NewExpression extends Expression {
    public final Token type;

    public NewExpression(Token token, Token type, CoolParser.NewExpressionContext ctx) {
        super(token, ctx);
        this.type = type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
