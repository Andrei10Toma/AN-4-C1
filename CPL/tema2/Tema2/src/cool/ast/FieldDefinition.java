package cool.ast;

import cool.parser.CoolParser;
import cool.structures.IdSymbol;
import cool.structures.Scope;
import org.antlr.v4.runtime.Token;

public class FieldDefinition extends ASTNode {
    public final Token name;
    public final Token type;
    public final Expression expr;
    public IdSymbol id;
    public Scope scope;

    public FieldDefinition(Token token, Token name, Token type, Expression expr, CoolParser.FieldDefinitionContext ctx) {
        super(token, ctx);
        this.name = name;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
