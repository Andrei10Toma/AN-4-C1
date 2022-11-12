package cool.ast;

import org.antlr.v4.runtime.Token;

public abstract class ASTNode {
    public final Token token;

    ASTNode(Token token) {
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

