package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Program extends ASTNode {
    public final List<ClassDefinition> classDefinitions;

    public Program(List<ClassDefinition> classDefinitions, Token start) {
        super(start);
        this.classDefinitions = classDefinitions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
