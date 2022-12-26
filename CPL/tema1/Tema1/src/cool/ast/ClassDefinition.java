package cool.ast;

import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class ClassDefinition extends ASTNode {
    public final Token name;
    public final Token inheritsName;
    public final List<ASTNode> features;

    public ClassDefinition(Token name, Token inheritsName, Token start, List<ASTNode> features, CoolParser.ClassDefinitionContext ctx) {
        super(start, ctx);
        this.name = name;
        this.inheritsName = inheritsName;
        this.features = features;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
