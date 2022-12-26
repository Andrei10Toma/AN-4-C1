package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class CaseBranch extends ASTNode {
    public final Token name;
    public final Token type;
    public final Expression expression;

    public CaseBranch(Token token, Token name, Token type, Expression expression, CoolParser.CaseBranchContext ctx) {
        super(token, ctx);
        this.name = name;
        this.type = type;
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
