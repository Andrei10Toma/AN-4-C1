package cool.compiler;

import cool.ast.*;
import cool.structures.*;
import org.antlr.v4.runtime.Token;

import java.util.*;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    Scope currentScope;
    int fieldCounter = 0;
    int methodCounter = 0;
    int localCounter = 0;

    @Override
    public TypeSymbol visit(Program program) {
        currentScope = SymbolTable.globals;
        program.classDefinitions.forEach(classDefinition -> classDefinition.accept(this));
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public TypeSymbol visit(ClassDefinition classDefinition) {
        if (classDefinition.type == null)
            return null;
        currentScope = classDefinition.type;
        if (classDefinition.type.parent != null) {
            classDefinition.type.parent.children.add(classDefinition.type);
        }
        fieldCounter = 0;
        methodCounter = 0;
        classDefinition.features.forEach(feature -> feature.accept(this));
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public TypeSymbol visit(FieldDefinition fieldDefinition) {
        if (fieldDefinition.id == null)
            return null;
        List<TypeSymbol> inheritanceChain = ((TypeSymbol) currentScope).getInheritanceChain();
        int parentAttributesNumber = 0;
        for (TypeSymbol type : inheritanceChain)
            if (type != currentScope)
                parentAttributesNumber += type.fieldSymbols.size() - 1;
        fieldDefinition.id.offset = 12 + 4 * (parentAttributesNumber + fieldCounter);
        if (fieldDefinition.expr != null) {
            TypeSymbol initExpressionType = fieldDefinition.expr.accept(this);
            if (initExpressionType != null && !initExpressionType.inherits(fieldDefinition.id.getType())) {
                SymbolTable.error(fieldDefinition.ctx, fieldDefinition.expr.token,
                        "Type " + initExpressionType.getName() +
                                " of initialization expression of attribute " + fieldDefinition.name.getText() +
                                " is incompatible with declared type " + fieldDefinition.type.getText());
                return null;
            }
        }
        fieldDefinition.id.isField = true;
        fieldCounter++;
        return null;
    }

    @Override
    public TypeSymbol visit(FunctionDefinition functionDefinition) {
        FunctionSymbol functionSymbol = functionDefinition.functionSymbol;
        if (functionSymbol == null || functionSymbol.returnType == null)
            return null;

        Token name = functionDefinition.name;
        Token type = functionDefinition.type;
        TypeSymbol functionScope = functionSymbol.parent;

        currentScope = functionSymbol;
        functionDefinition.arguments.forEach(formal -> formal.accept(this));
        currentScope = currentScope.getParent();

        List<TypeSymbol> inheritanceChain = ((TypeSymbol) currentScope).getInheritanceChain();
        Set<String> definedFunctions = new HashSet<>();
        for (TypeSymbol typeSymbol : inheritanceChain)
            if (typeSymbol != currentScope) {
                for (Map.Entry<String, FunctionSymbol> entry : typeSymbol.methodSymbols.entrySet()) {
                    definedFunctions.add(entry.getValue().getName());
                }
            }
        if (!definedFunctions.contains(functionDefinition.name.getText()))
            functionDefinition.functionSymbol.offset = 4 * (definedFunctions.size() + methodCounter);
        else
            functionDefinition.functionSymbol.offset = ((FunctionSymbol) ((TypeSymbol) currentScope.getParent()).lookupFunction(functionDefinition.name.getText())).offset;

        Symbol parentFunction = ((TypeSymbol) functionSymbol.getParent().getParent()).lookupFunction(name.getText());
        if (parentFunction instanceof FunctionSymbol parentFunctionSymbol) {

            if (parentFunctionSymbol.returnType != functionSymbol.returnType) {
                SymbolTable.error(functionDefinition.ctx, type,
                        "Class " + functionScope.getName() +
                                " overrides method " + name.getText() +
                                " but changes return type from " + parentFunctionSymbol.returnType.getName() +
                                " to " + functionSymbol.returnType.getName());
                return null;
            }

            if (parentFunctionSymbol.formalParameters.size() != functionSymbol.formalParameters.size()) {
                SymbolTable.error(functionDefinition.ctx, name,
                        "Class " + functionScope.getName() +
                                " overrides method " + name.getText() +
                                " with different number of formal parameters");
                return null;
            }

            List<IdSymbol> formalSymbols = new ArrayList<>(functionSymbol.formalParameters.values());
            List<IdSymbol> parentFormalSymbols = new ArrayList<>(parentFunctionSymbol.formalParameters.values());

            for (int i = 0; i < formalSymbols.size(); i++) {
                if (formalSymbols.get(i).getType() != parentFormalSymbols.get(i).getType()) {
                    SymbolTable.error(functionDefinition.arguments.get(i).ctx,
                            functionDefinition.arguments.get(i).type,
                            "Class " + functionScope.getName() +
                                    " overrides method " + name.getText() +
                                    " but changes type of formal parameter " + formalSymbols.get(i).getName() +
                                    " from " + parentFormalSymbols.get(i).getType().getName() +
                                    " to " + formalSymbols.get(i).getType().getName());
                }
            }
        }

        currentScope = functionSymbol;
        TypeSymbol functionReturn = functionDefinition.expression.accept(this);
        if (functionReturn != TypeSymbol.SELF_TYPE && functionSymbol.returnType == TypeSymbol.SELF_TYPE) {
            TypeSymbol nearestTypeSymbol = getNearestTypeSymbol();
            if (!functionReturn.inherits(nearestTypeSymbol)) {
                SymbolTable.error(functionDefinition.ctx, functionDefinition.expression.token,
                        "Type " + functionReturn.getName() +
                                " of the body of method " + functionDefinition.name.getText() +
                                " is incompatible with declared return type " + functionSymbol.returnType.getName());
                currentScope = currentScope.getParent();
                return null;
            }
        }
        if (functionReturn == TypeSymbol.SELF_TYPE) functionReturn = getNearestTypeSymbol();
        TypeSymbol functionSymbolReturnType = (functionSymbol.returnType == TypeSymbol.SELF_TYPE ?
                getNearestTypeSymbol() : functionSymbol.returnType);
        if (functionReturn != null && !functionReturn.inherits(functionSymbolReturnType)) {
            SymbolTable.error(functionDefinition.ctx, functionDefinition.expression.token,
                    "Type " + functionReturn.getName() +
                            " of the body of method " + functionDefinition.name.getText() +
                            " is incompatible with declared return type " + functionSymbol.returnType.getName());
        }
        currentScope = currentScope.getParent();
        if (!definedFunctions.contains(functionDefinition.name.getText()))
            methodCounter++;

        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        return null;
    }

    @Override
    public TypeSymbol visit(LetExpression letExpression) {
        letExpression.scope = new LetScope(currentScope);
        currentScope = letExpression.scope;
        localCounter = 0;
        letExpression.locals.forEach(local -> local.accept(this));
        TypeSymbol letReturnType = letExpression.expression.accept(this);
        currentScope = currentScope.getParent();
        return letReturnType;
    }

    @Override
    public TypeSymbol visit(Local local) {
        if (local.idSymbol == null)
            return null;
        Token type = local.type;
        Token name = local.name;

        Symbol localSymbol = SymbolTable.globals.lookup(type.getText());
        if (!(localSymbol instanceof TypeSymbol typeLocalSymbol)) {
            SymbolTable.error(local.ctx, type,
                    "Let variable " + name.getText() +
                            " has undefined type " + type.getText());
            return null;
        }

        local.idSymbol.setType(typeLocalSymbol);
        if (local.expression != null) {
            TypeSymbol initExpressionType = local.expression.accept(this);
            if (initExpressionType != null && !initExpressionType.inherits(typeLocalSymbol)) {
                SymbolTable.error(local.ctx, local.expression.token,
                        "Type " + initExpressionType.getName() +
                                " of initialization expression of identifier " + name.getText() +
                                " is incompatible with declared type " + type.getText());
            }
        }
        currentScope.add(local.idSymbol);
        local.idSymbol.isLocal = true;
        localCounter++;
        local.idSymbol.offset = 4 * localCounter;

        return null;
    }

    @Override
    public TypeSymbol visit(CaseExpression caseExpression) {
        currentScope = new CaseScope(currentScope);
        List<TypeSymbol> caseBranchesTypes = new ArrayList<>();
        caseExpression.branches.forEach(caseBranch -> {
            TypeSymbol caseBranchType = caseBranch.accept(this);
            if (caseBranchType != null) caseBranchesTypes.add(caseBranchType);
        });
        if (caseBranchesTypes.isEmpty()) return null;
        TypeSymbol joinTypeSymbol = caseBranchesTypes.stream().reduce(caseBranchesTypes.get(0), TypeSymbol::join);
        currentScope = currentScope.getParent();
        return joinTypeSymbol;
    }

    @Override
    public TypeSymbol visit(CaseBranch caseBranch) {
        IdSymbol symbol = caseBranch.symbol;
        if (symbol == null)
            return null;

        Symbol branchSymbol = SymbolTable.globals.lookup(caseBranch.type.getText());
        if (!(branchSymbol instanceof TypeSymbol branchTypeSymbol)) {
            SymbolTable.error(caseBranch.ctx, caseBranch.type,
                    "Case variable " + caseBranch.name.getText() +
                            " has undefined type " + caseBranch.type.getText());
            return null;
        }

        currentScope.add(symbol);
        symbol.setType(branchTypeSymbol);

        return caseBranch.expression.accept(this);
    }

    @Override
    public TypeSymbol visit(Id id) {
        Symbol idSymbol = currentScope.lookup(id.token.getText());
        if (idSymbol == null) {
            SymbolTable.error(id.ctx, id.token,
                    "Undefined identifier " + id.token.getText());
            return null;
        }
        id.symbol = (IdSymbol) idSymbol;
        return id.symbol.getType();
    }

    @Override
    public TypeSymbol visit(Int intt) {
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Falsee falsee) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Stringg stringg) {
        return TypeSymbol.STRING;
    }

    @Override
    public TypeSymbol visit(Truee truee) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(ParenthesisExpression parenthesisExpression) {
        return parenthesisExpression.expr.accept(this);
    }

    @Override
    public TypeSymbol visit(MultiplyDivisionExpression multiplyDivisionExpression) {
        TypeSymbol leftTypeSymbol = multiplyDivisionExpression.left.accept(this);
        TypeSymbol rightTypeSymbol = multiplyDivisionExpression.right.accept(this);

        if (leftTypeSymbol != TypeSymbol.INT && leftTypeSymbol != null) {
            SymbolTable.error(multiplyDivisionExpression.ctx, multiplyDivisionExpression.left.token,
                    "Operand of " + multiplyDivisionExpression.op.getText() +
                            " has type " + leftTypeSymbol.getName() + " instead of Int");
            return null;
        }

        if (rightTypeSymbol != TypeSymbol.INT && rightTypeSymbol != null) {
            SymbolTable.error(multiplyDivisionExpression.ctx, multiplyDivisionExpression.right.token,
                    "Operand of " + multiplyDivisionExpression.op.getText() +
                            " has type " + rightTypeSymbol.getName() + " instead of Int");
            return null;
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(PlusMinusExpression plusMinusExpression) {
        TypeSymbol leftTypeSymbol = plusMinusExpression.left.accept(this);
        TypeSymbol rightTypeSymbol = plusMinusExpression.right.accept(this);

        if (leftTypeSymbol != TypeSymbol.INT && leftTypeSymbol != null) {
            SymbolTable.error(plusMinusExpression.ctx, plusMinusExpression.left.token,
                    "Operand of " + plusMinusExpression.op.getText() +
                            " has type " + leftTypeSymbol.getName() + " instead of Int");
            return null;
        }

        if (rightTypeSymbol != TypeSymbol.INT && rightTypeSymbol != null) {
            SymbolTable.error(plusMinusExpression.ctx, plusMinusExpression.right.token,
                    "Operand of " + plusMinusExpression.op.getText() +
                            " has type " + rightTypeSymbol.getName() + " instead of Int");
            return null;
        }

        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(RelationalExpression relationalExpression) {
        TypeSymbol leftTypeSymbol = relationalExpression.left.accept(this);
        TypeSymbol rightTypeSymbol = relationalExpression.right.accept(this);

        if (relationalExpression.op.getText().equals("=")) {
            if ((leftTypeSymbol.isBasicType() && leftTypeSymbol != rightTypeSymbol) ||
                    (rightTypeSymbol.isBasicType() && leftTypeSymbol != rightTypeSymbol)) {
                SymbolTable.error(relationalExpression.ctx, relationalExpression.op,
                        "Cannot compare " + leftTypeSymbol.getName() +
                                " with " + rightTypeSymbol.getName());
                return null;
            }
        } else {

            if (leftTypeSymbol != TypeSymbol.INT && leftTypeSymbol != null) {
                SymbolTable.error(relationalExpression.ctx, relationalExpression.left.token,
                        "Operand of " + relationalExpression.op.getText() +
                                " has type " + leftTypeSymbol.getName() + " instead of Int");
                return null;
            }

            if (rightTypeSymbol != TypeSymbol.INT && rightTypeSymbol != null) {
                SymbolTable.error(relationalExpression.ctx, relationalExpression.right.token,
                        "Operand of " + relationalExpression.op.getText() +
                                " has type " + rightTypeSymbol.getName() + " instead of Int");
                return null;
            }
        }
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(NotExpression notExpression) {
        TypeSymbol expressionTypeSymbol = notExpression.expr.accept(this);
        if (expressionTypeSymbol != TypeSymbol.BOOL && expressionTypeSymbol != null) {
            SymbolTable.error(notExpression.ctx, notExpression.expr.token,
                    "Operand of not has type " + expressionTypeSymbol.getName() +
                            " instead of Bool");
            return null;
        }
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(ComplementExpression complementExpression) {
        TypeSymbol expressionType = complementExpression.expr.accept(this);

        if (expressionType != TypeSymbol.INT && expressionType != null) {
            SymbolTable.error(complementExpression.ctx, complementExpression.expr.token,
                    "Operand of " + complementExpression.token.getText() +
                            " has type " + expressionType.getName() + " instead of Int");
            return null;
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(AssignExpression assignExpression) {
        TypeSymbol exprTypeSymbol = assignExpression.expr.accept(this);
        if (exprTypeSymbol == null)
            return null;
        Symbol symbol = currentScope.lookup(assignExpression.variable.getText());
        if (!(symbol instanceof IdSymbol idSymbol))
            return null;

        if (!exprTypeSymbol.inherits(idSymbol.getType())) {
            SymbolTable.error(assignExpression.ctx, assignExpression.expr.token,
                    "Type " + exprTypeSymbol.getName() +
                            " of assigned expression is incompatible with declared type " + idSymbol.getType().getName() +
                            " of identifier " + idSymbol.getName());
            return exprTypeSymbol;
        }

        return exprTypeSymbol;
    }

    @Override
    public TypeSymbol visit(IsVoidExpression isVoidExpression) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(NewExpression newExpression) {
        Symbol type = SymbolTable.globals.lookup(newExpression.type.getText());
        if (!(type instanceof TypeSymbol)) {
            SymbolTable.error(newExpression.ctx, newExpression.type,
                    "new is used with undefined type " + newExpression.type.getText());
            return null;
        }

        return (TypeSymbol) type;
    }

    @Override
    public TypeSymbol visit(DispatchFunctionCallExpression dispatchFunctionCallExpression) {
        TypeSymbol dispatchType = dispatchFunctionCallExpression.expr.accept(this);
        if (dispatchType == null)
            return null;

        TypeSymbol searchDispatchType = dispatchType;
        if (dispatchFunctionCallExpression.type != null) {
            if (dispatchFunctionCallExpression.type.getText().equals("SELF_TYPE")) {
                SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.type,
                        "Type of static dispatch cannot be SELF_TYPE");
                return null;
            }
            TypeSymbol staticDispatchType = (TypeSymbol) SymbolTable.globals.lookup(dispatchFunctionCallExpression.type.getText());
            if (staticDispatchType == null) {
                SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.type,
                        "Type " + dispatchFunctionCallExpression.type.getText() +
                                " of static dispatch is undefined");
                return null;
            }

            if (dispatchType == TypeSymbol.SELF_TYPE)
                dispatchType = getNearestTypeSymbol();

            if (!dispatchType.inherits(staticDispatchType)) {
                SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.type,
                        "Type " + staticDispatchType.getName() +
                                " of static dispatch is not a superclass of type " + dispatchType.getName());
                return null;
            }

            searchDispatchType = staticDispatchType;
        }

        FunctionSymbol dispatchFunction = searchDispatchType != TypeSymbol.SELF_TYPE ?
                (FunctionSymbol) searchDispatchType.lookupFunction(dispatchFunctionCallExpression.name.getText()) :
                (FunctionSymbol) getNearestTypeSymbol().lookupFunction(dispatchFunctionCallExpression.name.getText());
        dispatchFunctionCallExpression.dispatchType = searchDispatchType != TypeSymbol.SELF_TYPE ? searchDispatchType : getNearestTypeSymbol();
        if (dispatchFunction == null) {
            SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.name,
                    "Undefined method " + dispatchFunctionCallExpression.name.getText() +
                            " in class " + searchDispatchType.getName());
            return null;
        }

        List<TypeSymbol> argumentsTypeSymbols = new ArrayList<>();
        dispatchFunctionCallExpression.callArgs.forEach(callArg -> {
            TypeSymbol callArgTypeSymbol = callArg.accept(this);
            if (callArgTypeSymbol != null) argumentsTypeSymbols.add(callArgTypeSymbol);
        });

        if (dispatchFunction.formalParameters.size() != argumentsTypeSymbols.size()) {
            SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.name,
                    "Method " + dispatchFunction.getName() +
                            " of class " + searchDispatchType.getName() +
                            " is applied to wrong number of arguments");
            return null;
        }

        List<IdSymbol> formalParameters = new ArrayList<>(dispatchFunction.formalParameters.values());
        for (int i = 0; i < dispatchFunction.formalParameters.size(); i++) {
            if (!argumentsTypeSymbols.get(i).inherits(formalParameters.get(i).getType())) {
                String actualType = searchDispatchType != TypeSymbol.SELF_TYPE ? searchDispatchType.getName() : getNearestTypeSymbol().getName();
                SymbolTable.error(dispatchFunctionCallExpression.ctx, dispatchFunctionCallExpression.callArgs.get(i).token,
                        "In call to method " + dispatchFunctionCallExpression.name.getText() +
                                " of class " + actualType +
                                ", actual type " + argumentsTypeSymbols.get(i) +
                                " of formal parameter " + formalParameters.get(i).getName() +
                                " is incompatible with declared type " + formalParameters.get(i).getType().getName());
            }
        }

        if (dispatchFunction.returnType == TypeSymbol.SELF_TYPE)
            return dispatchType;
        return dispatchFunction.returnType;
    }

    @Override
    public TypeSymbol visit(FunctionCallExpression functionCallExpression) {
        Symbol symbol = getNearestTypeSymbol().lookupFunction(functionCallExpression.name.getText());
        if (!(symbol instanceof FunctionSymbol dispatchFunction)) {
            SymbolTable.error(functionCallExpression.ctx, functionCallExpression.name,
                    "Undefined method " + functionCallExpression.name.getText() +
                            " in class " + ((TypeSymbol) currentScope.getParent()).getName());
            return null;
        }

        List<TypeSymbol> argumentsTypeSymbols = new ArrayList<>();
        functionCallExpression.callArgs.forEach(callArg -> {
            TypeSymbol callArgTypeSymbol = callArg.accept(this);
            if (callArgTypeSymbol != null) argumentsTypeSymbols.add(callArgTypeSymbol);
        });

        if (dispatchFunction.formalParameters.size() != argumentsTypeSymbols.size()) {
            SymbolTable.error(functionCallExpression.ctx, functionCallExpression.name,
                    "Method " + dispatchFunction.getName() +
                            " of class " + ((TypeSymbol) currentScope.getParent()).getName() +
                            " is applied to wrong number of arguments");
            return null;
        }
        List<IdSymbol> formalParameters = new ArrayList<>(dispatchFunction.formalParameters.values());

        for (int i = 0; i < dispatchFunction.formalParameters.size(); i++) {
            if (!argumentsTypeSymbols.get(i).inherits(formalParameters.get(i).getType())) {
                SymbolTable.error(functionCallExpression.ctx, functionCallExpression.callArgs.get(i).token,
                        "In call to method " + functionCallExpression.name.getText() +
                                " of class " + ((TypeSymbol) dispatchFunction.getParent()).getName() +
                                ", actual type " + argumentsTypeSymbols.get(i) +
                                " of formal parameter " + formalParameters.get(i).getName() +
                                " is incompatible with declared type " + formalParameters.get(i).getType().getName());
            }
        }

        return dispatchFunction.returnType;
    }

    @Override
    public TypeSymbol visit(IfExpression ifExpression) {
        TypeSymbol ifConditionType = ifExpression.condition.accept(this);
        TypeSymbol thenBranchType = ifExpression.ifBranch.accept(this);
        TypeSymbol elseBranchType = ifExpression.elseBranch.accept(this);

        TypeSymbol joinType = thenBranchType.join(elseBranchType);

        if (ifConditionType != TypeSymbol.BOOL && ifConditionType != null) {
            SymbolTable.error(ifExpression.ctx, ifExpression.condition.token,
                    "If condition has type " + ifConditionType.getName() + " instead of Bool");
        }

        return joinType;
    }

    @Override
    public TypeSymbol visit(WhileExpression whileExpression) {
        TypeSymbol whileConditionType = whileExpression.condition.accept(this);
        if (whileConditionType != TypeSymbol.BOOL && whileConditionType != null) {
            SymbolTable.error(whileExpression.ctx, whileExpression.condition.token,
                    "While condition has type " + whileConditionType.getName() + " instead of Bool");
            return TypeSymbol.OBJECT;
        }
        whileExpression.expr.accept(this);
        return TypeSymbol.OBJECT;
    }

    @Override
    public TypeSymbol visit(BlockExpression blockExpression) {
        int size = blockExpression.expressions.size();
        for (int i = 0; i < size - 1; i++) {
            blockExpression.expressions.get(i).accept(this);
        }

        return blockExpression.expressions.get(size - 1).accept(this);
    }

    public TypeSymbol getNearestTypeSymbol() {
        Scope startScope = currentScope;
        while (!(startScope instanceof TypeSymbol)) {
            startScope = startScope.getParent();
        }
        return (TypeSymbol) startScope;
    }
}
