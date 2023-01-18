package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public abstract class Expression extends ASTNode {
    public final CoolParser.ExprContext ctx;
    public Expression(Token token, CoolParser.ExprContext ctx) {
        super(token, ctx);
        this.ctx = ctx;
    }
}
