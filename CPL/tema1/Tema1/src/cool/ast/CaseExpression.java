package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class CaseExpression extends Expression {
    public final Expression expression;
    public final List<CaseBranch> branches;

    public CaseExpression(Token token, Expression expression, List<CaseBranch> branches) {
        super(token);
        this.expression = expression;
        this.branches = branches;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
