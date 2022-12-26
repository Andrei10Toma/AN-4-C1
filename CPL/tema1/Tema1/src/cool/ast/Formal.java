package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Formal extends ASTNode {
    public final Token name;
    public final Token type;

    public Formal(Token token, Token name, Token type, CoolParser.FormalContext ctx) {
        super(token, ctx);
        this.name = name;
        this.type = type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
