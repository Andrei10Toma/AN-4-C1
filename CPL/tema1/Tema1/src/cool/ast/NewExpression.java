package cool.ast;

import org.antlr.v4.runtime.Token;

public class NewExpression extends Expression {
    public final Token type;

    public NewExpression(Token token, Token type) {
        super(token);
        this.type = type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
