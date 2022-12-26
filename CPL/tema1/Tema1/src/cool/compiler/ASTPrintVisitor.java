package cool.compiler;

import cool.ast.*;

public class ASTPrintVisitor implements ASTVisitor<Void> {
    int indent = 0;

    @Override
    public Void visit(Program program) {
        printIndent("program");
        indent++;
        for (ClassDefinition node : program.classDefinitions)
            node.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(ClassDefinition classDefinition) {
        printIndent("class");
        indent++;
        printIndent(classDefinition.name.getText());
        if (classDefinition.inheritsName != null)
            printIndent(classDefinition.inheritsName.getText());
        classDefinition.features.forEach(feature -> feature.accept(this));
        indent--;
        return null;
    }

    @Override
    public Void visit(FieldDefinition fieldDefinition) {
        printIndent("attribute");
        indent++;
        printIndent(fieldDefinition.name.getText());
        printIndent(fieldDefinition.type.getText());
        if (fieldDefinition.expr != null)
            fieldDefinition.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Int intt) {
        printIndent(intt.token.getText());
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        printIndent("formal");
        indent++;
        printIndent(formal.name.getText());
        printIndent(formal.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {
        printIndent("method");
        indent++;
        printIndent(functionDefinition.name.getText());
        functionDefinition.arguments.forEach(argument -> argument.accept(this));
        printIndent(functionDefinition.type.getText());
        functionDefinition.expression.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Falsee falsee) {
        printIndent(falsee.token.getText());
        return null;
    }

    @Override
    public Void visit(Id id) {
        printIndent(id.token.getText());
        return null;
    }

    @Override
    public Void visit(Stringg stringg) {
        printIndent(stringg.token.getText());
        return null;
    }

    @Override
    public Void visit(Truee truee) {
        printIndent(truee.token.getText());
        return null;
    }

    @Override
    public Void visit(ParenthesisExpression parenthesisExpression) {
        parenthesisExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(MultiplyDivisionExpression multiplyDivisionExpression) {
        printIndent(multiplyDivisionExpression.op.getText());
        indent++;
        multiplyDivisionExpression.left.accept(this);
        multiplyDivisionExpression.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(PlusMinusExpression plusMinusExpression) {
        printIndent(plusMinusExpression.op.getText());
        indent++;
        plusMinusExpression.left.accept(this);
        plusMinusExpression.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(RelationalExpression relationalExpression) {
        printIndent(relationalExpression.op.getText());
        indent++;
        relationalExpression.left.accept(this);
        relationalExpression.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(NotExpression notExpression) {
        printIndent("not");
        indent++;
        notExpression.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(ComplementExpression complementExpression) {
        printIndent("~");
        indent++;
        complementExpression.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(AssignExpression assignExpression) {
        printIndent("<-");
        indent++;
        printIndent(assignExpression.variable.getText());
        assignExpression.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(IsVoidExpression isVoidExpression) {
        printIndent("isvoid");
        indent++;
        isVoidExpression.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(NewExpression newExpression) {
        printIndent("new");
        indent++;
        printIndent(newExpression.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(DispatchFunctionCallExpression dispatchFunctionCallExpression) {
        printIndent(".");
        indent++;
        dispatchFunctionCallExpression.expr.accept(this);
        if (dispatchFunctionCallExpression.type != null)
            printIndent(dispatchFunctionCallExpression.type.getText());
        printIndent(dispatchFunctionCallExpression.name.getText());
        dispatchFunctionCallExpression.callArgs.forEach(callArg -> callArg.accept(this));
        indent--;
        return null;
    }

    @Override
    public Void visit(FunctionCallExpression functionCallExpression) {
        printIndent("implicit dispatch");
        indent++;
        printIndent(functionCallExpression.name.getText());
        functionCallExpression.callArgs.forEach(callArg -> callArg.accept(this));
        indent--;
        return null;
    }

    @Override
    public Void visit(IfExpression ifExpression) {
        printIndent("if");
        indent++;
        ifExpression.condition.accept(this);
        ifExpression.ifBranch.accept(this);
        ifExpression.elseBranch.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(WhileExpression whileExpression) {
        printIndent("while");
        indent++;
        whileExpression.condition.accept(this);
        whileExpression.expr.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Local local) {
        printIndent("local");
        indent++;
        printIndent(local.name.getText());
        printIndent(local.type.getText());
        if (local.expression != null)
            local.expression.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(LetExpression letExpression) {
        printIndent("let");
        indent++;
        letExpression.locals.forEach(local -> local.accept(this));
        letExpression.expression.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(CaseBranch caseBranch) {
        printIndent("case branch");
        indent++;
        printIndent(caseBranch.name.getText());
        printIndent(caseBranch.type.getText());
        caseBranch.expression.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(CaseExpression caseExpression) {
        printIndent("case");
        indent++;
        caseExpression.expression.accept(this);
        caseExpression.branches.forEach(branch -> branch.accept(this));
        indent--;
        return null;
    }

    @Override
    public Void visit(BlockExpression blockExpression) {
        printIndent("block");
        indent++;
        blockExpression.expressions.forEach(expression -> expression.accept(this));
        indent--;
        return null;
    }

    void printIndent(String str) {
        for (int i = 0; i < indent; i++)
            System.out.print("  ");
        System.out.println(str);
    }
}
