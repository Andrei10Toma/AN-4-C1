package cool.ast;

import org.antlr.v4.runtime.Token;

public class Id extends Expression {
    public Id(Token token) {
        super(token);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
