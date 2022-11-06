import org.antlr.v4.runtime.Token;
import java.util.*;

// Rădăcina ierarhiei de clase reprezentând nodurile arborelui de sintaxă
// abstractă (AST). Singura metodă permite primirea unui visitor.
public abstract class ASTNode {
    // Reținem un token descriptiv, pentru a putea afișa ulterior
    // informații legate de linia și coloana eventualelor erori semantice.
    Token token;

    ASTNode(Token token) {
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

// Orice expresie.
abstract class Expression extends ASTNode {
    Expression(Token token) {
        super(token);
    }
}

// Identificatori
class Id extends Expression {
    Id(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Literali întregi
class Int extends Expression {
    Int(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Float extends Expression {
    Float(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Bool extends Expression {
    Bool(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Construcția if.
class If extends Expression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.
    Expression cond;
    Expression thenBranch;
    Expression elseBranch;

    If(Expression cond,
       Expression thenBranch,
       Expression elseBranch,
       Token start) {
        super(start);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// TODO 1: Definiți restul claselor de care ați avea nevoie. Asigurați-vă
// că moșteniți mereu nodul de bază ASTNode
class FunctionCall extends Expression {
    Id name;
    List<Expression> args;

    FunctionCall(Id name, List<Expression> args, Token start) {
        super(start);
        this.name = name;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class UnaryMinus extends Expression {
    Expression e;

    UnaryMinus(Expression e, Token start) {
        super(start);
        this.e = e;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class MultDiv extends Expression {
    Expression left;
    Expression right;
    Token op;

    MultDiv(Expression left, Expression right, Token op, Token start) {
        super(start);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class PlusMinus extends Expression {
    Expression left;
    Expression right;
    Token op;

    PlusMinus(Expression left, Expression right, Token op, Token start) {
        super(start);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class Relational extends Expression {
    Expression left;
    Expression right;
    Token op;

    Relational(Expression left, Expression right, Token op, Token start) {
        super(start);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class Assign extends Expression {
    Id name;
    Expression e;

    Assign(Id id, Expression e, Token start) {
        super(start);
        this.name = id;
        this.e = e;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class Paren extends Expression {
    Expression e;

    Paren(Expression e, Token start) {
        super(start);
        this.e = e;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class VarDef extends ASTNode {
    Token type;
    Id name;
    Expression expr;
    VarDef(Token type, Id name, Expression expr, Token start) {
        super(start);
        this.name = name;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class Formal extends ASTNode {
    Token type;
    Id name;

    Formal(Token type, Id name, Token start) {
        super(start);
        this.type = type;
        this.name = name;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class FuncDef extends ASTNode {
    Token type;
    Id name;
    List<Formal> formals;
    Expression body;

    FuncDef(Token type, Id name, List<Formal> formals, Expression body, Token start) {
        super(start);
        this.type = type;
        this.name = name;
        this.formals = formals;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}

class Prog extends ASTNode {
    List<ASTNode> exprDefs;

    Prog(List<ASTNode> exprDefs, Token start) {
        super(start);
        this.exprDefs = exprDefs;
    }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visit(this); }
}
