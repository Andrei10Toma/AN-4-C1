import java.io.IOException;
import java.util.*;

import org.antlr.v4.runtime.*;


public class Test {

    public static void main(String[] args) throws IOException {
        var input = CharStreams.fromFileName("types.txt");
        
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
        
        // Domeniul de vizibilitate global conține inițial doar numele
        // tipurilor.
        var globals = new DefaultScope(null);
        globals.add(TypeSymbol.INT);
        globals.add(TypeSymbol.BOOL);
        
        // onePassVisitor permite rezolvarea simbolurilor și verificarea
        // tipurilor într-o singură trecere. Observați că utilizăm TypeSymbol
        // pentru rezultatul întors de metodele de vizitare.
        var onePassVisitor = new ASTVisitor<TypeSymbol>() {
            // Reținem domeniul de vizibilitate curent
            Scope currentScope = globals;            

            @Override
            public TypeSymbol visit(Id id) {
                // La referirea unei variabile, o căutăm în domeniul
                // de vizibilitate curent.
                var symbol = (IdSymbol)currentScope.lookup(id.getToken().getText());
                
                // Semnalăm eroare dacă nu există.
                if (symbol == null) {
                    error(id.getToken(),
                          id.getToken().getText() + " undefined");
                    return null;
                }
                
                // Atașăm simbolul nodului din arbore.
                id.setSymbol(symbol);
                
                // Întoarcem informația de tip salvată deja în simbol încă de la
                // definirea variabilei.
                return symbol.getType();
            }

            @Override
            public TypeSymbol visit(Int intt) {
                // Pentru un literal întrg, putem menționa explicit tipul Int.
                return TypeSymbol.INT;
            }

            @Override
            public TypeSymbol visit(If iff) {
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                return null;
            }

            @Override
            public TypeSymbol visit(Plus plus) {
                // Pentru un nod +, avem nevoie de tipurile celor doi operanzi.
                var leftType  = plus.left.accept(this);
                var rightType = plus.right.accept(this);
                
                // Verificarea evită propagarea în cascadă a erorilor
                // către expresiile exterioare. O puteți elimina pentru
                // a observa efectul.
                if (leftType == null || rightType == null)
                    return null;
                
                // Este necesar ca ambii operanzi să aibă tipul Int.
                if (leftType  != TypeSymbol.INT ||
                    rightType != TypeSymbol.INT) {
                    error(plus.getToken(),
                          "Operand of + has type other than Int");
                    return null;
                }
                
                // Dacă este îndeplinită condiția de mai sus, tipul sumei
                // este tot Int.
                return TypeSymbol.INT;
            }

            @Override
            public TypeSymbol visit(Type type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public TypeSymbol visit(VarDef definition) {
                var id   = definition.id;
                var type = definition.type;
                
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
                
                // Căutăm tipul variabilei.
                var typeSymbol = (TypeSymbol)globals.lookup(type.getToken().getText());
                
                // Semnalăm eroare dacă nu există.
                if (typeSymbol == null) {
                    error(type.getToken(),
                          id.getToken().getText() + " has undefined type " +
                            type.getToken().getText());
                    return null;
                }
                
                // Reținem informația de tip în cadrul simbolului aferent
                // variabilei
                symbol.setType(typeSymbol);
                
                // Tipul unei definiții ca instrucțiune în sine nu este relevant
                return null;
            }

            @Override
            public TypeSymbol visit(Block block) {
                // Fiecare bloc constituie un nou domeniu de vizibilitate.
                currentScope = new DefaultScope(currentScope);
                
                for (var stmt : block.stmts)
                    stmt.accept(this);
                
                // La ieșirea din bloc, revenim la domeniul anterior.
                currentScope = currentScope.getParent();
                
                return null;
            }
            
        };
        
        ast.accept(onePassVisitor);
    }
    
    public static void error(Token token, String message) {
        System.err.println("line " + token.getLine()
                + ":" + (token.getCharPositionInLine() + 1)
                + ", " + message);
    }
   

}
