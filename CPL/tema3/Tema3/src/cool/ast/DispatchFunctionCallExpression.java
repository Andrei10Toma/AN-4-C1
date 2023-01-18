package cool.ast;

import cool.parser.CoolParser;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class DispatchFunctionCallExpression extends Expression {
    public final Expression expr;
    public final Token type;
    public final Token name;
    public final List<Expression> callArgs;
    public TypeSymbol dispatchType;

    public DispatchFunctionCallExpression(Token token, Expression expr, Token type, Token name, List<Expression> callArgs, CoolParser.DispatchFunctionCallContext ctx) {
        super(token, ctx);
        this.expr = expr;
        this.type = type;
        this.name = name;
        this.callArgs = callArgs;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
