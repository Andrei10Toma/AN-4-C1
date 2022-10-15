import java.io.IOException;
import java.util.*;

import org.antlr.v4.runtime.*;


public class Test {

    public static void main(String[] args) throws IOException {
        var input = CharStreams.fromFileName("symbols.txt");
        
        var lexer = new CPLangLexer(input);
        var tokenStream = new CommonTokenStream(lexer);
       
        /*
        tokenStream.fill();
        List<Token> tokens = tokenStream.getTokens();
        for (var token : tokens) {
            var text = token.getText();
            var type = CPLangLexer.VOCABULARY.getSymbolicName(token.getType());
            
            System.out.println(text + " : " + type);
        }
        */
        
        var parser = new CPLangParser(tokenStream);
        var tree = parser.expr();
        System.out.println(tree.toStringTree(parser));
        
        // Visitor-ul de mai jos parcurge arborele de derivare și construiește
        // un arbore de sintaxă abstractă (AST).
        var astConstructionVisitor = new CPLangParserBaseVisitor<ASTNode>() {
            @Override
            public ASTNode visitId(CPLangParser.IdContext ctx) {
                return new Id(ctx.ID().getSymbol());
            }
            
            @Override
            public ASTNode visitInt(CPLangParser.IntContext ctx) {
                return new Int(ctx.INT().getSymbol());
            }
            
            @Override
            public ASTNode visitIf(CPLangParser.IfContext ctx) { 
                return new If((Expression)visit(ctx.cond),
                              (Expression)visit(ctx.thenBranch),
                              (Expression)visit(ctx.elseBranch),
                              ctx.start);
            }
            
            @Override
            public ASTNode visitPlus(CPLangParser.PlusContext ctx) {
                return new Plus((Expression)visit(ctx.left),
                                (Expression)visit(ctx.right),
                                ctx.op);
            }
            
            @Override
            public ASTNode visitDef(CPLangParser.DefContext ctx) {
                var id = new Id(ctx.name);
                var type = new Type(ctx.type);
                
                return new VarDef(id, type, ctx.start);
            }
            
            @Override
            public ASTNode visitBlock(CPLangParser.BlockContext ctx) {
                var stmts = new LinkedList<ASTNode>();
                
                for (var child : ctx.children) {
                    var stmt = visit(child);
                    if (stmt != null)
                        stmts.add(stmt);
                }
                
                return new Block(stmts, ctx.start);
            }
        };
        
        // ast este AST-ul proaspăt construit pe baza arborelui de derivare.
        var ast = astConstructionVisitor.visit(tree);
        
        // ABORDAREA 1.
        // onePassVisitor permite rezolvarea simbolurilor într-o singură
        // trecere. Astfel, referirile anticipate NU pot fi rezolvate.
        var onePassVisitor = new ASTVisitor<Void>() {
            // Reținem domeniul de vizibilitate curent
            Scope currentScope = null;            

            @Override
            public Void visit(Id id) {
                // La referirea unei variabile, o căutăm în domeniul
                // de vizibilitate curent.
                var symbol = currentScope.lookup(id.getToken().getText());
                
                // Semnalăm eroare dacă nu există.
                if (symbol == null) {
                    error(id.getToken(),
                          id.getToken().getText() + " undefined");
                    return null;
                }
                
                // Atașăm simbolul nodului din arbore.
                id.setSymbol((IdSymbol)symbol);
                
                return null;
            }

            @Override
            public Void visit(Int intt) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(If iff) {
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                return null;
            }

            @Override
            public Void visit(Plus plus) {
                plus.left.accept(this);
                plus.right.accept(this);
                return null;
            }

            @Override
            public Void visit(Type type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(VarDef definition) {
                var id = definition.id;
                
                // La definirea unei variabile, creăm un nou simbol.
                var symbol = new IdSymbol(id.getToken().getText());
                
                // Adăugăm simbolul în domeniul de vizibilitate curent,
                // semnalând eroare dacă exista deja acolo.
                if (! currentScope.add(symbol)) {
                    error(id.getToken(),
                          id.getToken().getText() + " redefined");
                    return null;
                }
                
                // Atașăm simbolul nodului din arbore.
                id.setSymbol(symbol);
                
                return null;
            }

            @Override
            public Void visit(Block block) {
                // Fiecare bloc constituie un nou domeniu de vizibilitate.
                currentScope = new DefaultScope(currentScope);
                
                for (var stmt : block.stmts)
                    stmt.accept(this);
                
                // La ieșirea din bloc, revenim la domeniul anterior.
                currentScope = currentScope.getParent();
                
                return null;
            }
            
        };
        
        // ABORDAREA 2.
        // În vederea gestiunii referirilor anticipate, utilizăm două treceri,
        // una de definire a simbolurilor, și cealaltă, de rezolvare.
        var definitionPassVisitor = new ASTVisitor<Void>() {
            Scope currentScope = null;            

            @Override
            public Void visit(Id id) {
                // La referirea unei variabile, nu o mai căutăm imediat
                // în domeniul de vizibilitate curent, ci doar memorăm
                // domeniul în nodul de AST, în vederea căutării în trecerea
                // următoare.
                id.setScope(currentScope);
                
                return null;
            }

            @Override
            public Void visit(Int intt) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(If iff) {
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                return null;
            }

            @Override
            public Void visit(Plus plus) {
                plus.left.accept(this);
                plus.right.accept(this);
                return null;
            }

            @Override
            public Void visit(Type type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(VarDef definition) {
                var id = definition.id;
                
                // La definirea unei variabile, creăm un nou simbol.
                var symbol = new IdSymbol(id.getToken().getText());
                
                // Adăugăm simbolul în domeniul de vizibilitate curent,
                // semnalând eroare dacă exista deja acolo.
                if (! currentScope.add(symbol)) {
                    error(id.getToken(),
                          id.getToken().getText() + " redefined");
                    return null;
                }
                
                // Atașăm simbolul nodului din arbore.
                id.setSymbol(symbol);
                
                return null;
            }

            @Override
            public Void visit(Block block) {
                currentScope = new DefaultScope(currentScope);
                
                for (var stmt : block.stmts)
                    stmt.accept(this);
                
                currentScope = currentScope.getParent();
                
                return null;
            }
            
        };
        
        // A doua trecere, pentru rezolvarea simbolurilor în raport cu domeniile
        // de vizibilitate memorate în prima trecere. Observați că, în această
        // trecere, nu mai este necesară gestiunea domeniului curent,
        // ca în prima trecere.
        var resolutionPassVisitor = new ASTVisitor<Void>() {        

            @Override
            public Void visit(Id id) {
                // La referirea unei variabile, o căutăm în domeniul
                // de vizibilitate memorat în trecerea anterioară.
                var symbol = id.getScope().lookup(id.getToken().getText());
                
                // Semnalăm eroare dacă nu există.
                if (symbol == null) {
                    error(id.getToken(),
                          id.getToken().getText() + " undefined");
                    return null;
                }
                
                // Atașăm simbolul nodului din arbore.
                id.setSymbol((IdSymbol)symbol);
                
                return null;
            }

            @Override
            public Void visit(Int intt) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(If iff) {
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                return null;
            }

            @Override
            public Void visit(Plus plus) {
                plus.left.accept(this);
                plus.right.accept(this);
                return null;
            }

            @Override
            public Void visit(Type type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Void visit(VarDef definition) {
                // Acum nu mai avem nimic de făcut cu definițiile de variabile.
                return null;
            }

            @Override
            public Void visit(Block block) {
                for (var stmt : block.stmts)
                    stmt.accept(this);

                return null;
            }
            
        };
        
        // Pentru testare, utilizați fie onePassVisitor,
        // fie definitionPassVisitor și resolutionPassVisitor concomitent.
        ast.accept(onePassVisitor);
        
        //ast.accept(definitionPassVisitor);
        //ast.accept(resolutionPassVisitor);
    }
    
    public static void error(Token token, String message) {
        System.err.println("line " + token.getLine()
                + ":" + (token.getCharPositionInLine() + 1)
                + ", " + message);
    }
   

}
