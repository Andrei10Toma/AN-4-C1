package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class FunctionCallExpression extends Expression {
    public final Token name;
    public final List<Expression> callArgs;

    public FunctionCallExpression(Token token, Token name, List<Expression> callArgs) {
        super(token);
        this.name = name;
        this.callArgs = callArgs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}