package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class CaseExpression extends Expression {
    public final Expression expression;
    public final List<CaseBranch> branches;

    public CaseExpression(Token token, Expression expression, List<CaseBranch> branches, CoolParser.CaseExpressionContext ctx) {
        super(token, ctx);
        this.expression = expression;
        this.branches = branches;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
