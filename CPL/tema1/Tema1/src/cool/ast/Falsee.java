package cool.ast;

import org.antlr.v4.runtime.Token;

public class Falsee extends Expression {
    public Falsee(Token token) {
        super(token);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
