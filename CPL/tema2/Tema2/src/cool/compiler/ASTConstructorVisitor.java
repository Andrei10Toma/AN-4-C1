package cool.compiler;

import cool.ast.*;
import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;

public class ASTConstructorVisitor extends CoolParserBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        List<ClassDefinition> classDefinitions = new ArrayList<>();
        ctx.classDefinition().forEach(classDefinition -> classDefinitions.add((ClassDefinition) visit(classDefinition)));
        return new Program(classDefinitions, ctx.start, ctx);
    }

    @Override
    public ASTNode visitClassDefinition(CoolParser.ClassDefinitionContext ctx) {
        List<ASTNode> features = new ArrayList<>();
        ctx.feature().forEach(feature -> features.add(visit(feature)));
        return new ClassDefinition(ctx.name, ctx.inheritName, ctx.start, features, ctx);
    }

    @Override
    public ASTNode visitFieldDefinition(CoolParser.FieldDefinitionContext ctx) {
        return new FieldDefinition(ctx.start, ctx.name, ctx.type, ctx.expr() != null ? ((Expression) visit(ctx.expression)) : null, ctx);
    }

    @Override
    public ASTNode visitInt(CoolParser.IntContext ctx) {
        return new Int(ctx.INT().getSymbol(), ctx);
    }

    @Override
    public ASTNode visitFormal(CoolParser.FormalContext ctx) {
        return new Formal(ctx.start, ctx.name, ctx.type, ctx);
    }

    @Override
    public ASTNode visitFunctionDefinition(CoolParser.FunctionDefinitionContext ctx) {
        List<Formal> formals = new ArrayList<>();
        ctx.formals.forEach(formal -> formals.add((Formal) visit(formal)));
        return new FunctionDefinition(ctx.start, ctx.name, formals, ctx.type, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitId(CoolParser.IdContext ctx) {
        return new Id(ctx.ID().getSymbol(), ctx);
    }

    @Override
    public ASTNode visitString(CoolParser.StringContext ctx) {
        return new Stringg(ctx.STRING().getSymbol(), ctx);
    }

    @Override
    public ASTNode visitFalse(CoolParser.FalseContext ctx) {
        return new Falsee(ctx.FALSE().getSymbol(), ctx);
    }

    @Override
    public ASTNode visitTrue(CoolParser.TrueContext ctx) {
        return new Truee(ctx.TRUE().getSymbol(), ctx);
    }

    @Override
    public ASTNode visitParenthesisExpression(CoolParser.ParenthesisExpressionContext ctx) {
        return new ParenthesisExpression(ctx.start, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitNotExpression(CoolParser.NotExpressionContext ctx) {
        return new NotExpression(ctx.start, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitMultiplyDivisionExpression(CoolParser.MultiplyDivisionExpressionContext ctx) {
        return new MultiplyDivisionExpression(ctx.start,
                (Expression) visit(ctx.left),
                (Expression) visit(ctx.right),
                ctx.op, ctx);
    }

    @Override
    public ASTNode visitPlusMinusExpression(CoolParser.PlusMinusExpressionContext ctx) {
        return new PlusMinusExpression(ctx.start,
                (Expression) visit(ctx.left),
                (Expression) visit(ctx.right),
                ctx.op, ctx);
    }

    @Override
    public ASTNode visitRelationalExpression(CoolParser.RelationalExpressionContext ctx) {
        return new RelationalExpression(ctx.start,
                (Expression) visit(ctx.left),
                (Expression) visit(ctx.right),
                ctx.op, ctx);
    }

    @Override
    public ASTNode visitComplementExpression(CoolParser.ComplementExpressionContext ctx) {
        return new ComplementExpression(ctx.start, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitAssignExpression(CoolParser.AssignExpressionContext ctx) {
        return new AssignExpression(ctx.start,
                ctx.variable,
                (Expression) visit(ctx.expression),
                ctx
        );
    }

    @Override
    public ASTNode visitNewExpression(CoolParser.NewExpressionContext ctx) {
        return new NewExpression(ctx.start, ctx.type, ctx);
    }

    @Override
    public ASTNode visitIsVoidExpression(CoolParser.IsVoidExpressionContext ctx) {
        return new IsVoidExpression(ctx.start, (Expression) visit(ctx.expression), ctx);
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
                callArgs, ctx);
    }

    @Override
    public ASTNode visitFunctionCall(CoolParser.FunctionCallContext ctx) {
        List<Expression> callArgs = new ArrayList<>();
        if (ctx.callArgs != null)
            ctx.callArgs.forEach(callArg -> callArgs.add((Expression) visit(callArg)));
        return new FunctionCallExpression(ctx.start, ctx.name, callArgs, ctx);
    }

    @Override
    public ASTNode visitIfExpression(CoolParser.IfExpressionContext ctx) {
        return new IfExpression(ctx.start,
                (Expression) visit(ctx.cond),
                (Expression) visit(ctx.ifBranch),
                (Expression) visit(ctx.elseBranch),
                ctx);
    }

    @Override
    public ASTNode visitWhileExpression(CoolParser.WhileExpressionContext ctx) {
        return new WhileExpression(ctx.start,
                (Expression) visit(ctx.cond),
                (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitLocal(CoolParser.LocalContext ctx) {
        return new Local(ctx.start, ctx.name, ctx.type,
                ctx.expression != null ? (Expression) visit(ctx.expression) : null, ctx);
    }

    @Override
    public ASTNode visitLetExpression(CoolParser.LetExpressionContext ctx) {
        List<Local> locals = new ArrayList<>();
        ctx.variables.forEach(variable -> locals.add((Local) visit(variable)));
        return new LetExpression(ctx.start, locals, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitCaseBranch(CoolParser.CaseBranchContext ctx) {
        return new CaseBranch(ctx.start, ctx.name, ctx.type, (Expression) visit(ctx.expression), ctx);
    }

    @Override
    public ASTNode visitCaseExpression(CoolParser.CaseExpressionContext ctx) {
        List<CaseBranch> caseBranches = new ArrayList<>();
        ctx.caseBranch().forEach(caseBranch -> caseBranches.add((CaseBranch) visit(caseBranch)));
        return new CaseExpression(ctx.start, (Expression) visit(ctx.expression), caseBranches, ctx);
    }

    @Override
    public ASTNode visitBlockExpression(CoolParser.BlockExpressionContext ctx) {
        List<Expression> expressions = new ArrayList<>();
        ctx.expr().forEach(expression -> expressions.add((Expression) visit(expression)));
        return new BlockExpression(ctx.start, expressions, ctx);
    }
}
