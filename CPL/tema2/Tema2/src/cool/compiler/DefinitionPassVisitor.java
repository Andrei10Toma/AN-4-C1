package cool.compiler;

import cool.ast.*;
import cool.structures.*;
import org.antlr.v4.runtime.Token;

import java.util.Set;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    private Scope currentScope;
    private final Set<String> illegalParents = Set.of("Int", "Bool", "String", "SELF_TYPE");

    @Override
    public Void visit(Program program) {
        program.classDefinitions.forEach(classDefinition -> classDefinition.accept(this));
        return null;
    }

    @Override
    public Void visit(ClassDefinition classDefinition) {
        Token id = classDefinition.name;
        Token inheritsId = classDefinition.inheritsName;

        if (id.getText().equals("SELF_TYPE")) {
            SymbolTable.error(classDefinition.ctx, id, "Class has illegal name SELF_TYPE");
            return null;
        }

        TypeSymbol type = new TypeSymbol(id.getText(), inheritsId == null ? "Object" : inheritsId.getText());
        if (!SymbolTable.globals.add(type)) {
            SymbolTable.error(classDefinition.ctx, id, "Class " + id.getText() + " is redefined");
            return null;
        }

        if (illegalParents.contains(type.inheritsName)) {
            SymbolTable.error(classDefinition.ctx, inheritsId,
                    "Class " + id.getText() + " has illegal parent " + inheritsId.getText());
            return null;
        }

        classDefinition.type = type;
        currentScope = type;
        classDefinition.features.forEach(feature -> feature.accept(this));
        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(FieldDefinition fieldDefinition) {
        Token name = fieldDefinition.name;
        if (name.getText().equals("self")) {
            SymbolTable.error(fieldDefinition.ctx, name,
                    "Class " + ((TypeSymbol) currentScope).getName() + " has attribute with illegal name self");
            return null;
        }

        IdSymbol idSymbol = new IdSymbol(name.getText());
        if (!currentScope.add(idSymbol)) {
            SymbolTable.error(fieldDefinition.ctx, name,
                    "Class " + ((TypeSymbol) currentScope).getName() + " redefines attribute " + name.getText());
            return null;
        }

        fieldDefinition.id = idSymbol;
        fieldDefinition.scope = currentScope;

        return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {
        Token name = functionDefinition.name;

        FunctionSymbol functionSymbol = new FunctionSymbol(name.getText());
        if (!currentScope.add(functionSymbol)) {
            SymbolTable.error(functionDefinition.ctx, name,
                    "Class " + ((TypeSymbol) currentScope).getName() + " redefines method " + name.getText());
            return null;
        }
        functionSymbol.parent = (TypeSymbol) currentScope;
        functionDefinition.functionSymbol = functionSymbol;
        currentScope = functionSymbol;
        functionDefinition.arguments.forEach(argument -> argument.accept(this));
        functionDefinition.expression.accept(this);
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        Token name = formal.name;
        Token type = formal.type;

        if (!(currentScope instanceof FunctionSymbol functionScope))
            return null;

        if (name.getText().equals("self")) {
            SymbolTable.error(formal.ctx, name,
                    "Method " + functionScope.getName() +
                            " of class " + ((TypeSymbol) functionScope.getParent()).getName() +
                            " has formal parameter with illegal name self");
            return null;
        }

        if (type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(formal.ctx, type,
                    "Method " + functionScope.getName() +
                            " of class " + ((TypeSymbol) functionScope.getParent()).getName() +
                            " has formal parameter " + name.getText() + " with illegal type SELF_TYPE");
            return null;
        }

        IdSymbol formalSymbol = new IdSymbol(name.getText());
        if (!currentScope.add(formalSymbol)) {
            SymbolTable.error(formal.ctx, name,
                    "Method " + functionScope.getName() +
                            " of class " + ((TypeSymbol) functionScope.getParent()).getName() +
                            " redefines formal parameter " + name.getText());
            return null;
        }
        formal.formalSymbol = formalSymbol;
        formal.formalScope = currentScope;

        return null;
    }

    @Override
    public Void visit(LetExpression letExpression) {
        currentScope = new LetScope(currentScope);
        letExpression.locals.forEach(local -> local.accept(this));
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Void visit(Local local) {
        Token name = local.name;
        if (name.getText().equals("self")) {
            SymbolTable.error(local.ctx, name, "Let variable has illegal name self");
            return null;
        }

        IdSymbol idSymbol = new IdSymbol(name.getText());
        currentScope.add(idSymbol);
        local.idSymbol = idSymbol;

        return null;
    }

    @Override
    public Void visit(CaseExpression caseExpression) {
        currentScope = new CaseScope(currentScope);
        caseExpression.branches.forEach(caseBranch -> caseBranch.accept(this));
        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(CaseBranch caseBranch) {
        Token name = caseBranch.name;
        Token type = caseBranch.type;

        if (name.getText().equals("self")) {
            SymbolTable.error(caseBranch.ctx, name,
                    "Case variable has illegal name self");
            return null;
        }

        if (type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(caseBranch.ctx, type,
                    "Case variable " + name.getText() + " has illegal type SELF_TYPE");
            return null;
        }

        caseBranch.symbol = new IdSymbol(name.getText());

        return null;
    }

    @Override
    public Void visit(AssignExpression assignExpression) {
        Token variable = assignExpression.variable;
        if (variable.getText().equals("self")) {
            SymbolTable.error(assignExpression.ctx, variable,
                    "Cannot assign to self");
            return null;
        }

        assignExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(Int intt) {
        return null;
    }

    @Override
    public Void visit(Falsee falsee) {
        return null;
    }

    @Override
    public Void visit(Id id) {
        return null;
    }

    @Override
    public Void visit(Stringg stringg) {
        return null;
    }

    @Override
    public Void visit(Truee truee) {
        return null;
    }

    @Override
    public Void visit(ParenthesisExpression parenthesisExpression) {
        parenthesisExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(MultiplyDivisionExpression multiplyDivisionExpression) {
        multiplyDivisionExpression.left.accept(this);
        multiplyDivisionExpression.right.accept(this);
        return null;
    }

    @Override
    public Void visit(PlusMinusExpression plusMinusExpression) {
        plusMinusExpression.left.accept(this);
        plusMinusExpression.right.accept(this);
        return null;
    }

    @Override
    public Void visit(RelationalExpression relationalExpression) {
        relationalExpression.left.accept(this);
        relationalExpression.right.accept(this);
        return null;
    }

    @Override
    public Void visit(NotExpression notExpression) {
        notExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(ComplementExpression complementExpression) {
        complementExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(IsVoidExpression isVoidExpression) {
        isVoidExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(NewExpression newExpression) {
        return null;
    }

    @Override
    public Void visit(DispatchFunctionCallExpression dispatchFunctionCallExpression) {
        return null;
    }

    @Override
    public Void visit(FunctionCallExpression functionCallExpression) {
        return null;
    }

    @Override
    public Void visit(IfExpression ifExpression) {
        ifExpression.condition.accept(this);
        ifExpression.ifBranch.accept(this);
        ifExpression.elseBranch.accept(this);
        return null;
    }

    @Override
    public Void visit(WhileExpression whileExpression) {
        whileExpression.condition.accept(this);
        whileExpression.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(BlockExpression blockExpression) {
        blockExpression.expressions.forEach(expression -> expression.accept(this));
        return null;
    }
}
