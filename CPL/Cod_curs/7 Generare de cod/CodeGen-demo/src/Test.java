import java.io.IOException;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;


public class Test {

    public static void main(String[] args) throws IOException {
        var input = CharStreams.fromFileName("program.txt");
        
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
        
        // Obiectele ST reprezintă șabloane StringTemplate, care conțin text
        // obișnuit și atribute, ultimele fiind flancate între < și >.
        // Valorile atributelor sunt definite utilizând metoda add, și pot
        // fi de orice tip, inclusiv alte șabloane imbricate.
        // Sunt permise multiple valori pentru același atribut, acestea fiind
        // enumerate folosind opțiunea separator.
        // Șirul final de caractere este obținut cu metoda render.
        ST st = new ST("Cool <name; separator=\", \">");
        st.add("name", "compiler");
        st.add("name", "interpreter");
        //System.out.println(st.render());
        
        ST st2 = new ST("Cool <name; separator=\", \">");
        st2.add("name", st);
        //System.out.println(st2.render());
        
        // Definiții mai modulare pentru șabloane se pot da în fișiere .stg
        // (string template group). Șabloanele pot fi parametrizate, similar
        // funcțiilor și pot fi accesate prin metoda getInstanceOf.
        var group = new STGroupFile("cgen.stg");
        ST literal = group.getInstanceOf("literal");
        literal.add("value", 5);
        //System.out.println(literal.render());
        
        ST plus = group.getInstanceOf("plus");
        plus.add("e1", literal);
        plus.add("e2", literal);
        //System.out.println(plus.render());
        
        // codeGenVisitor generează codul MIPS aferent programului de la intrare
        var codeGenVisitor = new ASTVisitor<ST>() {
            @Override
            public ST visit(Id id) {
                return null;
            }

            @Override
            public ST visit(Int intt) {
                return group.getInstanceOf("literal")
                        .add("value", intt.getToken().getText());
            }

            @Override
            public ST visit(If iff) {
                return null;
            }

            @Override
            public ST visit(Plus plus) {
                var plusST = group.getInstanceOf("plus");
                plusST.add("e1", plus.left.accept(this));
                plusST.add("e2", plus.right.accept(this));
                return plusST;
            }

            @Override
            public ST visit(Type type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ST visit(VarDef definition) {
                return null;
            }

            @Override
            public ST visit(Block block) {
                // Aici, exploatăm posibilitatea precizării de valori multiple
                // ale atributului expresie dintr-o secvență.
                var seq = group.getInstanceOf("sequence");
                for (var e : block.stmts)
                    seq.add("e", e.accept(this));
                return seq;
            }
            
        };
        
        System.out.println(ast.accept(codeGenVisitor).render());
    }
    
    public static void error(Token token, String message) {
        System.err.println("line " + token.getLine()
                + ":" + (token.getCharPositionInLine() + 1)
                + ", " + message);
    }
   

}
