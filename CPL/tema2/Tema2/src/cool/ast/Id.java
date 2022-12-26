package cool.ast;

import cool.parser.CoolParser;
import cool.structures.IdSymbol;
import org.antlr.v4.runtime.Token;

public class Id extends Expression {
    public IdSymbol symbol;
    public Id(Token token, CoolParser.IdContext ctx) {
        super(token, ctx);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
