package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Stringg extends Expression {
    public Stringg(Token token, CoolParser.StringContext ctx) {
        super(token, ctx);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
