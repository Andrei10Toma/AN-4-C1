package cool.compiler;

import cool.ast.*;
import cool.structures.FunctionSymbol;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.Token;

public class TypeDefinitionsVisitor implements ASTVisitor<Void> {
    int formalOffset;
    @Override
    public Void visit(Program program) {
        Symbol mainClass = SymbolTable.globals.lookup("Main");
        if (!(mainClass instanceof TypeSymbol mainClassSymbol))
            return null;

        Symbol mainFunction = mainClassSymbol.lookupFunction("main");
        if (mainFunction == null) {
            SymbolTable.error(program.ctx, program.token,
                    "No method main in class Main");
            return null;
        }
        program.classDefinitions.forEach(classDefinition -> classDefinition.accept(this));
        return null;
    }

    @Override
    public Void visit(ClassDefinition classDefinition) {
        if (classDefinition.type == null)
            return null;
        Symbol inheritSymbolClass = SymbolTable.globals.lookup(classDefinition.type.inheritsName);

        // check if the parent of the class was defined
        if (inheritSymbolClass == null) {
            SymbolTable.error(classDefinition.ctx, classDefinition.inheritsName,
                    "Class " + classDefinition.name.getText() + " has undefined parent " + classDefinition.type.inheritsName);
            return null;
        }

        TypeSymbol startType = classDefinition.type;
        while (startType != null) {
            Symbol inheritSymbol = SymbolTable.globals.lookup(startType.inheritsName);
            if (inheritSymbol == null) {
                startType.parent = null;
                break;
            }
            if (!(inheritSymbol instanceof TypeSymbol))
                break;
            if (inheritSymbol.getName().equals(classDefinition.name.getText())) {
                SymbolTable.error(classDefinition.ctx, classDefinition.name,
                        "Inheritance cycle for class " + classDefinition.name.getText());
                return null;
            }
            startType.parent = (TypeSymbol) inheritSymbol;
            startType = (TypeSymbol) inheritSymbol;
        }

        classDefinition.features.forEach(feature -> feature.accept(this));

        return null;
    }

    @Override
    public Void visit(FieldDefinition fieldDefinition) {
        Token type = fieldDefinition.type;
        Token name = fieldDefinition.name;
        if (!(fieldDefinition.scope instanceof TypeSymbol scope))
            return null;

        Symbol typeSymbol = SymbolTable.globals.lookup(type.getText());
        if (typeSymbol == null) {
            SymbolTable.error(fieldDefinition.ctx, type,
                    "Class " + scope.getName() +
                            " has attribute " + name.getText() +
                            " with undefined type " + type.getText());
            return null;
        }

        if (scope.parent != null && scope.parent.lookup(name.getText()) != null) {
            SymbolTable.error(fieldDefinition.ctx, name,
                    "Class " + scope.getName() + " redefines inherited attribute " + name.getText());
            return null;
        }

        fieldDefinition.id.setType((TypeSymbol) typeSymbol);
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        if (formal.formalSymbol == null)
            return null;

        if (!(formal.formalScope instanceof FunctionSymbol formalScope))
            return null;

        Token type = formal.type;
        Token name = formal.name;

        Symbol formalType = SymbolTable.globals.lookup(type.getText());
        if (formalType == null) {
            SymbolTable.error(formal.ctx, type,
                    "Method " + formalScope.getName() +
                            " of class " + ((TypeSymbol) formalScope.getParent()).getName() +
                            " has formal parameter " + name.getText() +
                            " with undefined type " + type.getText());
        }

        formal.formalSymbol.setType((TypeSymbol) formalType);
        formal.formalSymbol.isFormal = true;
        formal.formalSymbol.offset = formalOffset;
        formalOffset += 4;
        return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {
        FunctionSymbol functionSymbol = functionDefinition.functionSymbol;
        if (functionSymbol == null)
            return null;

        Token type = functionDefinition.type;
        Token name = functionDefinition.name;
        TypeSymbol functionScope = functionSymbol.parent;
        TypeSymbol returnType = (TypeSymbol) SymbolTable.globals.lookup(type.getText());
        if (returnType == null) {
            SymbolTable.error(functionDefinition.ctx, type,
                    "Class " + functionScope.getName() +
                            " has method " + name.getText() +
                            " with undefined return type " + type.getText());
            return null;
        }
        functionSymbol.returnType = returnType;
        formalOffset = 12;
        functionDefinition.arguments.forEach(formal -> formal.accept(this));

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
        return null;
    }

    @Override
    public Void visit(MultiplyDivisionExpression multiplyDivisionExpression) {
        return null;
    }

    @Override
    public Void visit(PlusMinusExpression plusMinusExpression) {
        return null;
    }

    @Override
    public Void visit(RelationalExpression relationalExpression) {
        return null;
    }

    @Override
    public Void visit(NotExpression notExpression) {
        return null;
    }

    @Override
    public Void visit(ComplementExpression complementExpression) {
        return null;
    }

    @Override
    public Void visit(AssignExpression assignExpression) {
        return null;
    }

    @Override
    public Void visit(IsVoidExpression isVoidExpression) {
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
        return null;
    }

    @Override
    public Void visit(WhileExpression whileExpression) {
        return null;
    }

    @Override
    public Void visit(Local local) {
        return null;
    }

    @Override
    public Void visit(LetExpression letExpression) {
        return null;
    }

    @Override
    public Void visit(CaseBranch caseBranch) {
        return null;
    }

    @Override
    public Void visit(CaseExpression caseExpression) {
        return null;
    }

    @Override
    public Void visit(BlockExpression blockExpression) {
        return null;
    }
}
