package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Truee extends Expression {
    public Truee(Token token, CoolParser.TrueContext ctx) {
        super(token, ctx);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
