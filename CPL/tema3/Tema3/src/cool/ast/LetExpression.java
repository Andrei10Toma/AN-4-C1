package cool.ast;

import cool.parser.CoolParser;
import cool.structures.LetScope;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class LetExpression extends Expression {
    public final List<Local> locals;
    public final Expression expression;
    public LetScope scope;

    public LetExpression(Token token, List<Local> locals, Expression expression, CoolParser.LetExpressionContext ctx) {
        super(token, ctx);
        this.locals = locals;
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
