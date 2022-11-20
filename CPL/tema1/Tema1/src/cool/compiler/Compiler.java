package cool.compiler;

import cool.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);
            
            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);

            // Test lexer only.
            /*tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });*/

            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);
            
            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;
                
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                                        line + ":" + (charPositionInLine + 1) + ", ";
                    
                    Token token = (Token)offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;
                    
                    System.err.println(newMsg);
                    errors = true;
                }
            };
            
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            
            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));
                    
            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }
            
            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;
        }

        // Stop before semantic analysis phase, in case errors occurred.
        if (lexicalSyntaxErrors) {
            System.err.println("Compilation halted");
            return;
        }
        
        var astConstructorVisitor =  new CoolParserBaseVisitor<ASTNode>() {
            @Override
            public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                List<ClassDefinition> classDefinitions = new ArrayList<>();
                ctx.classDefinition().forEach(classDefinition -> classDefinitions.add((ClassDefinition) visit(classDefinition)));
                return new Program(classDefinitions, ctx.start);
            }

            @Override
            public ASTNode visitClassDefinition(CoolParser.ClassDefinitionContext ctx) {
                List<ASTNode> features = new ArrayList<>();
                ctx.feature().forEach(feature -> features.add(visit(feature)));
                return new ClassDefinition(ctx.name, ctx.inheritName, ctx.start, features);
            }

            @Override
            public ASTNode visitFieldDefinition(CoolParser.FieldDefinitionContext ctx) {
                return new FieldDefinition(ctx.start, ctx.name, ctx.type, ctx.expr() != null ? ((Expression) visit(ctx.expression)) : null);
            }

            @Override
            public ASTNode visitInt(CoolParser.IntContext ctx) {
                return new Int(ctx.INT().getSymbol());
            }

            @Override
            public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                return new Formal(ctx.start, ctx.name, ctx.type);
            }

            @Override
            public ASTNode visitFunctionDefinition(CoolParser.FunctionDefinitionContext ctx) {
                List<Formal> formals = new ArrayList<>();
                ctx.formals.forEach(formal -> formals.add((Formal) visit(formal)));
                return new FunctionDefinition(ctx.start, ctx.name, formals, ctx.type, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new Id(ctx.ID().getSymbol());
            }

            @Override
            public ASTNode visitString(CoolParser.StringContext ctx) {
                return new Stringg(ctx.STRING().getSymbol());
            }

            @Override
            public ASTNode visitFalse(CoolParser.FalseContext ctx) {
                return new Falsee(ctx.FALSE().getSymbol());
            }

            @Override
            public ASTNode visitTrue(CoolParser.TrueContext ctx) {
                return new Truee(ctx.TRUE().getSymbol());
            }

            @Override
            public ASTNode visitParenthesisExpression(CoolParser.ParenthesisExpressionContext ctx) {
                return new ParenthesisExpression(ctx.start, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitNotExpression(CoolParser.NotExpressionContext ctx) {
                return new NotExpression(ctx.start, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitMultiplyDivisionExpression(CoolParser.MultiplyDivisionExpressionContext ctx) {
                return new MultiplyDivisionExpression(ctx.start,
                        (Expression) visit(ctx.left),
                        (Expression) visit(ctx.right),
                        ctx.op);
            }

            @Override
            public ASTNode visitPlusMinusExpression(CoolParser.PlusMinusExpressionContext ctx) {
                return new PlusMinusExpression(ctx.start,
                        (Expression) visit(ctx.left),
                        (Expression) visit(ctx.right),
                        ctx.op);
            }

            @Override
            public ASTNode visitRelationalExpression(CoolParser.RelationalExpressionContext ctx) {
                return new RelationalExpression(ctx.start,
                        (Expression) visit(ctx.left),
                        (Expression) visit(ctx.right),
                        ctx.op);
            }

            @Override
            public ASTNode visitComplementExpression(CoolParser.ComplementExpressionContext ctx) {
                return new ComplementExpression(ctx.start, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitAssignExpression(CoolParser.AssignExpressionContext ctx) {
                return new AssignExpression(ctx.start,
                        ctx.variable,
                        (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitNewExpression(CoolParser.NewExpressionContext ctx) {
                return new NewExpression(ctx.start, ctx.type);
            }

            @Override
            public ASTNode visitIsVoidExpression(CoolParser.IsVoidExpressionContext ctx) {
                return new IsVoidExpression(ctx.start, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitDispatchFunctionCall(CoolParser.DispatchFunctionCallContext ctx) {
                List<Expression> callArgs = new ArrayList<>();
                if (ctx.callArgs != null)
                    ctx.callArgs.forEach(callArg -> callArgs.add((Expression) visit(callArg)));
                return new DispatchFunctionCallExpression(ctx.start,
                        (Expression) visit(ctx.expression),
                        ctx.type,
                        ctx.name,
                        callArgs);
            }

            @Override
            public ASTNode visitFunctionCall(CoolParser.FunctionCallContext ctx) {
                List<Expression> callArgs = new ArrayList<>();
                if (ctx.callArgs != null)
                    ctx.callArgs.forEach(callArg -> callArgs.add((Expression) visit(callArg)));
                return new FunctionCallExpression(ctx.start, ctx.name, callArgs);
            }

            @Override
            public ASTNode visitIfExpression(CoolParser.IfExpressionContext ctx) {
                return new IfExpression(ctx.start,
                        (Expression) visit(ctx.cond),
                        (Expression) visit(ctx.ifBranch),
                        (Expression) visit(ctx.elseBranch)
                );
            }

            @Override
            public ASTNode visitWhileExpression(CoolParser.WhileExpressionContext ctx) {
                return new WhileExpression(ctx.start,
                        (Expression) visit(ctx.cond),
                        (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitLocal(CoolParser.LocalContext ctx) {
                return new Local(ctx.start, ctx.name, ctx.type,
                        ctx.expression != null ? (Expression) visit(ctx.expression) : null);
            }

            @Override
            public ASTNode visitLetExpression(CoolParser.LetExpressionContext ctx) {
                List<Local> locals = new ArrayList<>();
                ctx.variables.forEach(variable -> locals.add((Local) visit(variable)));
                return new LetExpression(ctx.start, locals, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitCaseBranch(CoolParser.CaseBranchContext ctx) {
                return new CaseBranch(ctx.start, ctx.name, ctx.type, (Expression) visit(ctx.expression));
            }

            @Override
            public ASTNode visitCaseExpression(CoolParser.CaseExpressionContext ctx) {
                List<CaseBranch> caseBranches = new ArrayList<>();
                ctx.caseBranch().forEach(caseBranch -> caseBranches.add((CaseBranch) visit(caseBranch)));
                return new CaseExpression(ctx.start, (Expression) visit(ctx.expression), caseBranches);
            }

            @Override
            public ASTNode visitBlockExpression(CoolParser.BlockExpressionContext ctx) {
                List<Expression> expressions = new ArrayList<>();
                ctx.expr().forEach(expression -> expressions.add((Expression) visit(expression)));
                return new BlockExpression(ctx.start, expressions);
            }
        };

        var ast = astConstructorVisitor.visit(globalTree);

        var printVisitor = new ASTVisitor<Void>() {
            int indent = 0;
            @Override
            public Void visit(Program program) {
                printIndent("program");
                indent++;
                for (ClassDefinition node : program.classDefinitions)
                    node.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(ClassDefinition classDefinition) {
                printIndent("class");
                indent++;
                printIndent(classDefinition.name.getText());
                if (classDefinition.inheritsName != null)
                    printIndent(classDefinition.inheritsName.getText());
                classDefinition.features.forEach(feature -> feature.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(FieldDefinition fieldDefinition) {
                printIndent("attribute");
                indent++;
                printIndent(fieldDefinition.name.getText());
                printIndent(fieldDefinition.type.getText());
                if (fieldDefinition.expr != null)
                    fieldDefinition.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Int intt) {
                printIndent(intt.token.getText());
                return null;
            }

            @Override
            public Void visit(Formal formal) {
                printIndent("formal");
                indent++;
                printIndent(formal.name.getText());
                printIndent(formal.type.getText());
                indent--;
                return null;
            }

            @Override
            public Void visit(FunctionDefinition functionDefinition) {
                printIndent("method");
                indent++;
                printIndent(functionDefinition.name.getText());
                functionDefinition.arguments.forEach(argument -> argument.accept(this));
                printIndent(functionDefinition.type.getText());
                functionDefinition.expression.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Falsee falsee) {
                printIndent(falsee.token.getText());
                return null;
            }

            @Override
            public Void visit(Id id) {
                printIndent(id.token.getText());
                return null;
            }

            @Override
            public Void visit(Stringg stringg) {
                printIndent(stringg.token.getText());
                return null;
            }

            @Override
            public Void visit(Truee truee) {
                printIndent(truee.token.getText());
                return null;
            }

            @Override
            public Void visit(ParenthesisExpression parenthesisExpression) {
                parenthesisExpression.expr.accept(this);
                return null;
            }

            @Override
            public Void visit(MultiplyDivisionExpression multiplyDivisionExpression) {
                printIndent(multiplyDivisionExpression.op.getText());
                indent++;
                multiplyDivisionExpression.left.accept(this);
                multiplyDivisionExpression.right.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(PlusMinusExpression plusMinusExpression) {
                printIndent(plusMinusExpression.op.getText());
                indent++;
                plusMinusExpression.left.accept(this);
                plusMinusExpression.right.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RelationalExpression relationalExpression) {
                printIndent(relationalExpression.op.getText());
                indent++;
                relationalExpression.left.accept(this);
                relationalExpression.right.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(NotExpression notExpression) {
                printIndent("not");
                indent++;
                notExpression.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(ComplementExpression complementExpression) {
                printIndent("~");
                indent++;
                complementExpression.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(AssignExpression assignExpression) {
                printIndent("<-");
                indent++;
                printIndent(assignExpression.variable.getText());
                assignExpression.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(IsVoidExpression isVoidExpression) {
                printIndent("isvoid");
                indent++;
                isVoidExpression.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(NewExpression newExpression) {
                printIndent("new");
                indent++;
                printIndent(newExpression.type.getText());
                indent--;
                return null;
            }

            @Override
            public Void visit(DispatchFunctionCallExpression dispatchFunctionCallExpression) {
                printIndent(".");
                indent++;
                dispatchFunctionCallExpression.expr.accept(this);
                if (dispatchFunctionCallExpression.type != null)
                    printIndent(dispatchFunctionCallExpression.type.getText());
                printIndent(dispatchFunctionCallExpression.name.getText());
                dispatchFunctionCallExpression.callArgs.forEach(callArg -> callArg.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(FunctionCallExpression functionCallExpression) {
                printIndent("implicit dispatch");
                indent++;
                printIndent(functionCallExpression.name.getText());
                functionCallExpression.callArgs.forEach(callArg -> callArg.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(IfExpression ifExpression) {
                printIndent("if");
                indent++;
                ifExpression.condition.accept(this);
                ifExpression.ifBranch.accept(this);
                ifExpression.elseBranch.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(WhileExpression whileExpression) {
                printIndent("while");
                indent++;
                whileExpression.condition.accept(this);
                whileExpression.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Local local) {
                printIndent("local");
                indent++;
                printIndent(local.name.getText());
                printIndent(local.type.getText());
                if (local.expression != null)
                    local.expression.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(LetExpression letExpression) {
                printIndent("let");
                indent++;
                letExpression.locals.forEach(local -> local.accept(this));
                letExpression.expression.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(CaseBranch caseBranch) {
                printIndent("case branch");
                indent++;
                printIndent(caseBranch.name.getText());
                printIndent(caseBranch.type.getText());
                caseBranch.expression.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(CaseExpression caseExpression) {
                printIndent("case");
                indent++;
                caseExpression.expression.accept(this);
                caseExpression.branches.forEach(branch -> branch.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(BlockExpression blockExpression) {
                printIndent("block");
                indent++;
                blockExpression.expressions.forEach(expression -> expression.accept(this));
                indent--;
                return null;
            }

            void printIndent(String str) {
                for (int i = 0; i < indent; i++)
                    System.out.print("  ");
                System.out.println(str);
            }
        };

        ast.accept(printVisitor);
    }
}
