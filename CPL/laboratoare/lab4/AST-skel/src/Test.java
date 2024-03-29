import java.io.IOException;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class Test {

    public static void main(String[] args) throws IOException {
        var input = CharStreams.fromFileName("input3.txt");

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
        var tree = parser.prog();
        System.out.println(tree.toStringTree(parser));

        // Visitor-ul de mai jos parcurge arborele de derivare și construiește
        // un arbore de sintaxă abstractă (AST).
        var astConstructionVisitor = new CPLangParserBaseVisitor<ASTNode>() {
            @Override
            public ASTNode visitId(CPLangParser.IdContext ctx) {
                return new Id(ctx.ID().getSymbol());
            }

            @Override
            public ASTNode visitBool(CPLangParser.BoolContext ctx) {
                return new Bool(ctx.BOOL().getSymbol());
            }

            @Override
            public ASTNode visitFloat(CPLangParser.FloatContext ctx) {
                return new Float(ctx.FLOAT().getSymbol());
            }

            @Override
            public ASTNode visitMultDiv(CPLangParser.MultDivContext ctx) {
                return new MultDiv((Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.op, ctx.start);
            }

            @Override
            public ASTNode visitPlusMinus(CPLangParser.PlusMinusContext ctx) {
                return new PlusMinus((Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.op, ctx.start);
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
            public ASTNode visitProg(CPLangParser.ProgContext ctx) {
                List<ASTNode> nodes = new ArrayList<>();
                for (CPLangParser.ExprContext expr: ctx.expr()) {
                    nodes.add(visit(expr));
                }
                for (CPLangParser.DefinitionContext def: ctx.definition()) {
                    nodes.add(visit(def));
                }
                return new Prog(nodes, ctx.start);
            }

            // TODO 3: Completati cu alte metode pentru a trece din arborele
            // generat automat in reprezentarea AST-ului dorit

        };

        // ast este AST-ul proaspăt construit pe baza arborelui de derivare.
        var ast = astConstructionVisitor.visit(tree);

        // Acest visitor parcurge AST-ul generat mai sus.
        // ATENȚIE! Avem de-a face cu două categorii de visitori:
        // (1) Cei pentru arborele de derivare, care extind
        //     CPLangParserBaseVisitor<T> și
        // (2) Cei pentru AST, care extind ASTVisitor<T>.
        // Aveți grijă să nu îi confundați!
        var printVisitor = new ASTVisitor<Void>() {
            int indent = 0;

            @Override
            public Void visit(Id id) {
                printIndent("ID " + id.token.getText());
                return null;
            }

            @Override
            public Void visit(Int intt) {
                printIndent("INT " + intt.token.getText());
                return null;
            }

            @Override
            public Void visit(Float floatt) {
                printIndent("FLOAT " + floatt.token.getText());
                return null;
            }

            @Override
            public Void visit(Bool booll) {
                printIndent("BOOL " + booll.token.getText());
                return null;
            }

            @Override
            public Void visit(MultDiv multDiv) {
                printIndent(multDiv.op.getText());
                indent++;
                multDiv.right.accept(this);
                multDiv.left.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Prog prog) {
                printIndent("PROG ");
                indent++;
                for (ASTNode node : prog.exprDefs) {
                    node.accept(this);
                }
                indent--;
                return null;
            }

            @Override
            public Void visit(If iff) {
                printIndent("IF");
                indent++;
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(PlusMinus multDiv) {
                printIndent(multDiv.op.getText());
                indent++;
                multDiv.right.accept(this);
                multDiv.left.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(FunctionCall functionCall) {
                return null;
            }

            @Override
            public Void visit(UnaryMinus unaryMinus) {
                return null;
            }

            @Override
            public Void visit(Relational relational) {
                return null;
            }

            @Override
            public Void visit(Assign assign) {
                return null;
            }

            @Override
            public Void visit(Paren paren) {
                return null;
            }

            @Override
            public Void visit(VarDef varDef) {
                return null;
            }

            @Override
            public Void visit(Formal formal) {
                return null;
            }

            @Override
            public Void visit(FuncDef funcDef) {
                return null;
            }

            // TODO 4: Afisati fiecare nod astfel incat nivelul pe care acesta
            // se afla in AST sa fie reprezentat de numarul de tab-uri.
            // Folositi functia de mai jos 'printIndent' si incrementati /
            // decrementati corespunzator numarul de tab-uri

            void printIndent(String str) {
                for (int i = 0; i < indent; i++)
                    System.out.print("\t");
                System.out.println(str);
            }
        };

        // TODO 5: Creati un program CPLang care sa cuprinda cat mai multe
        // constructii definite in laboratorul de astazi. Scrieti codul CPLang
        // intr-un fisier txt si modificati fisierul de input de la inceputul
        // metodei 'main'

        System.out.println("The AST is");
        ast.accept(printVisitor);
    }


}
