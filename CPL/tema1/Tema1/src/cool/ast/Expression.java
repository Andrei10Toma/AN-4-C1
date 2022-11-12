package cool.ast;

import org.antlr.v4.runtime.Token;

public abstract class Expression extends ASTNode {
    public Expression(Token token) {
        super(token);
    }
}
