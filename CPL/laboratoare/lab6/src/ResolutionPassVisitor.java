import org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    @Override
    public TypeSymbol visit(Program prog) {
        for (var stmt: prog.stmts) {
            stmt.accept(this);
        }
        return null;
    }
    
    @Override
    public TypeSymbol visit(Id id) {
        // Verificăm dacă într-adevăr avem de-a face cu o variabilă
        // sau cu o funcție, al doilea caz constituind eroare.
        // Puteți folosi instanceof.
        var symbol = id.getScope().lookup(id.getToken().getText());

        if (symbol instanceof FunctionSymbol) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " is not a variable");
            return null;
        }

        // TODO 2: Întoarcem informația de tip salvată deja în simbol încă de la
        // definirea variabilei.
        return id.getSymbol().getType();
    }
    
    @Override
    public TypeSymbol visit(VarDef varDef) {
        var varType  = varDef.id.getSymbol().getType();
        if (varDef.initValue != null) {
            var initType = varDef.initValue.accept(this);
            
            // TODO 5: Verificăm dacă expresia de inițializare are tipul potrivit
            // cu cel declarat pentru variabilă.
            if (varType != initType) {
                ASTVisitor.error(varDef.initValue.getToken(),
                        "Type of initilization expression does not match variable type");
                return null;
            }
        }
        
        return varType;
    }
    
    @Override
    public TypeSymbol visit(FuncDef funcDef) {
        var returnType = funcDef.id.getSymbol().getType();
        var bodyType = funcDef.body.accept(this);
        
        // TODO 5: Verificăm dacă tipul de retur declarat este compatibil
        // cu cel al corpului.
        if (returnType != bodyType) {
            ASTVisitor.error(funcDef.body.getToken(),
                    "Return type does not match body type");
            return null;
        }
        
        return returnType;
    }

    @Override
    public TypeSymbol visit(Call call) {
        // Verificăm dacă funcția există în scope. Nu am putut face
        // asta în prima trecere din cauza a forward references.
        //
        // De asemenea, verificăm că Id-ul pe care se face apelul de funcție
        // este, într-adevăr, o funcție și nu o variabilă.
        //
        // Hint: pentru a obține scope-ul, putem folosi call.id.getScope(),
        // setat la trecerea anterioară.
        var id = call.id;
        var symbol = id.getScope().lookup(id.getToken().getText());

        if (symbol == null) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " function undefined");
            return null;
        }

        if (!(symbol instanceof FunctionSymbol)) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " is not a function");
            return null;
        }
        
        var functionSymbol = (FunctionSymbol)symbol;
        id.setSymbol(functionSymbol);
        
        // TODO 6: Verificați dacă numărul parametrilor actuali coincide
        // cu cel al parametrilor formali, și că tipurile sunt compatibile.
        
        var formals = functionSymbol.getFormals();
        if (formals.size() != call.args.size()) {
            ASTVisitor.error(call.token,
                    call.id.getToken().getText() + " applied to wrong number of arguments");
            return null;
        }

        List<Symbol> types = new ArrayList<>(formals.values());

        for (int i = 0; i < call.args.size(); i++) {
            TypeSymbol arg_type = call.args.get(i).accept(this);
            if (((IdSymbol) types.get(i)).getType() != arg_type) {
                ASTVisitor.error(call.args.get(i).getToken(),
                                "Argument " + (i + 1) + " of " + call.id.getToken().getText() + " has wrong type.");
                return null;
            }
        }
        
        return functionSymbol.getType();
    }   
    
    @Override
    public TypeSymbol visit(Assign assign) {
        var idType   = assign.id.accept(this);
        var exprType = assign.expr.accept(this);
        
        // TODO 5: Verificăm dacă expresia cu care se realizează atribuirea
        // are tipul potrivit cu cel declarat pentru variabilă.
        if (idType != exprType && exprType != null) {
                ASTVisitor.error(assign.expr.getToken(),
                        "Assignment with incompatible types");
        }

        return null;
    }

    @Override
    public TypeSymbol visit(If iff) {        
        var condType = iff.cond.accept(this);
        var thenType = iff.thenBranch.accept(this);
        var elseType = iff.elseBranch.accept(this);

        boolean error = false;

        // TODO 4: Verificați tipurile celor 3 componente, afișați eroare
        // dacă este cazul, și precizați tipul expresiei.
        if (condType != TypeSymbol.BOOL) {
            error = true;
            ASTVisitor.error(iff.cond.getToken(),
                    "Condition of if expression has type other than Bool");
        }

        if (thenType != elseType) {
            if ((thenType == TypeSymbol.INT && elseType != TypeSymbol.FLOAT) ||
                    (thenType == TypeSymbol.FLOAT && elseType != TypeSymbol.INT)) {
                error = true;
                ASTVisitor.error(iff.getToken(),
                        "Branches of if expression have incompatible types");
            }
        }

        if (error)
            return null;
        return thenType;
    }

    @Override
    public TypeSymbol visit(Type type) {
        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        return formal.id.getSymbol().getType();
    }

    // Operații aritmetice.
    @Override
    public TypeSymbol visit(UnaryMinus uMinus) {
        var exprType = uMinus.expr.accept(this);
        
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        if (exprType != TypeSymbol.INT && exprType != TypeSymbol.FLOAT) {
            ASTVisitor.error(uMinus.token,
                    "Operands of unary - have incompatible types");
            return null;
        }
        
        return exprType;
    }

    @Override
    public TypeSymbol visit(Div div) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol left = div.left.accept(this);
        TypeSymbol right = div.right.accept(this);
        if (left != right) {
            ASTVisitor.error(div.token,
                    "Operands of / have incompatible types");
            return null;
        }
        return left;
    }

    // 2 + (2/x)

    @Override
    public TypeSymbol visit(Mult mult) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol left = mult.left.accept(this);
        TypeSymbol right = mult.right.accept(this);
        if (left != right) {
            ASTVisitor.error(mult.token,
                    "Operands of * have incompatible types");
            return null;
        }
        return left;
    }

    @Override
    public TypeSymbol visit(Plus plus) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol left = plus.left.accept(this);
        TypeSymbol right = plus.right.accept(this);
        if (left != right) {
            ASTVisitor.error(plus.token,
                    "Operands of + have incompatible types");
            return null;
        }
        return left;
    }

    @Override
    public TypeSymbol visit(Minus minus) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol left = minus.left.accept(this);
        TypeSymbol right = minus.right.accept(this);
        if (left != right) {
            ASTVisitor.error(minus.token,
                    "Operands of - have incompatible types");
            return null;
        }
        return left;
    }

    @Override
    public TypeSymbol visit(Relational relational) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        // Puteți afla felul operației din relational.getToken().getType(),
        // pe care îl puteți compara cu CPLangLexer.EQUAL etc.
        TypeSymbol left = relational.left.accept(this);
        TypeSymbol right = relational.right.accept(this);
        if (left != right) {
            ASTVisitor.error(relational.token,
                    "Operands of " + relational.getToken().getText() + " have incompatible types");
            return null;
        }
        return TypeSymbol.BOOL;
    }

    // Tipurile de bază
    @Override
    public TypeSymbol visit(Int intt) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Bool bool) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(FloatNum floatNum) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.FLOAT;
    }
    
};