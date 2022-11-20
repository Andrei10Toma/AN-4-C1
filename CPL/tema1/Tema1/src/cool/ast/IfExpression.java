package cool.ast;

import org.antlr.v4.runtime.Token;

public class IfExpression extends Expression {
    public final Expression condition;
    public final Expression ifBranch;
    public final Expression elseBranch;

    public IfExpression(Token token, Expression condition, Expression ifBranch, Expression elseBranch) {
        super(token);
        this.condition = condition;
        this.ifBranch = ifBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
