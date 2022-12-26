package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Int extends Expression {
    public Int(Token token, CoolParser.IntContext ctx) {
        super(token, ctx);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
