package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Falsee extends Expression {
    public Falsee(Token token, CoolParser.FalseContext ctx) {
        super(token, ctx);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
