import java.util.List;

import org.antlr.v4.runtime.Token;

// Rădăcina ierarhiei de clase reprezentând nodurile arborelui de sintaxă
// abstractă (AST). Singura metodă permite primirea unui visitor.
public abstract class ASTNode {
    // Reținem un token descriptiv al expresiei, pentru a putea afișa ulterior
    // informații legate de linia și coloana eventualelor erori semantice.
    protected Token token;
    
    ASTNode(Token token) {
        this.token = token;
    }
    
    Token getToken() {
        return token;
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
    private IdSymbol symbol;
    private Scope scope;
    
    Id(Token token) {
        super(token);
    }
    
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    IdSymbol getSymbol() {
        return symbol;
    }

    void setSymbol(IdSymbol symbol) {
        this.symbol = symbol;
    }
    
    Scope getScope() {
        return scope;
    }

    void setScope(Scope scope) {
        this.scope = scope;
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

// Plus
class Plus extends Expression {
    Expression left;
    Expression right;
    
    Plus(Expression left, Expression right, Token op) {
        super(op);
        this.left = left;
        this.right = right;
    }
    
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Type extends ASTNode {
    Type(Token token) {
        super(token);
    }
    
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class VarDef extends ASTNode {
    Id id;
    Type type;
    
    VarDef(Id id, Type type, Token token) {
        super(token);
        this.id = id;
        this.type = type;
    }
    
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Bloc
class Block extends Expression {
    List<ASTNode> stmts;
     
    Block(List<ASTNode> stmts, Token token) {
        super(token);
        this.stmts = stmts;
    }
    
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
