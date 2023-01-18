package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class ASTNode {
    public final Token token;
    public final ParserRuleContext ctx;

    ASTNode(Token token, ParserRuleContext ctx) {
        this.token = token;
        this.ctx = ctx;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

