package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class FunctionDefinition extends ASTNode {
    public final Token name;
    public final List<Formal> arguments;
    public final Token type;
    public final Expression expression;

    public FunctionDefinition(Token token, Token name, List<Formal> arguments, Token type, Expression expression, CoolParser.FunctionDefinitionContext ctx) {
        super(token, ctx);
        this.name = name;
        this.arguments = arguments;
        this.type = type;
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
