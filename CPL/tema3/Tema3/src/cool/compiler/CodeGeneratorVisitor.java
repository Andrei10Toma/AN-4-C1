package cool.compiler;

import cool.ast.*;
import cool.parser.CoolParser;
import cool.structures.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CodeGeneratorVisitor implements ASTVisitor<ST> {
    STGroupFile templates;
    int intCounter;
    int stringCounter;
    int dispatchCounter;
    int ifCounter;
    int isvoidCounter;
    int notCounter;
    Scope currentScope;
    private Map<Integer, String> ints;
    private Map<String, String> strings;
    ST tags;
    ST intConstants;
    ST stringConstants;
    ST classNameTabs;
    ST classObjTabs;
    ST prototypeObjects;
    ST dispatchTabs;
    ST methods;

    // returns reference to the declared int constant
    private String addIntToConstants(int val) {
        if (!ints.containsKey(val)) {
            ST intAdd = templates.getInstanceOf("intConst")
                    .add("tag", TypeSymbol.INT.tag)
                    .add("counter", intCounter)
                    .add("val", val);
            intConstants.add("e", intAdd);
            ints.put(val, "int_const" + intCounter);
            intCounter++;
        }

        return ints.get(val);
    }

    private String addStringToConstants(String val) {
        if (!strings.containsKey(val)) {
            ST stringAdd = templates.getInstanceOf("stringConst")
                    .add("tag", TypeSymbol.STRING.tag)
                    .add("counter", stringCounter)
                    .add("size", 4 + (int) Math.ceil((val.length() + 1) / 4.0))
                    .add("length", addIntToConstants(val.length()))
                    .add("val", val);
            stringConstants.add("e", stringAdd);
            strings.put(val, "str_const" + stringCounter);
            stringCounter++;
        }

        return strings.get(val);
    }

    private String getFileName(ASTNode node) {
        var ctx = node.ctx;
        while (!(ctx.getParent() instanceof CoolParser.ProgramContext)) {
            ctx = ctx.getParent();
        }

        return addStringToConstants(new File(Compiler.fileNames.get(ctx)).getName());
    }

    @Override
    public ST visit(Program program) {
        intCounter = 0;
        stringCounter = 0;
        dispatchCounter = 0;
        ifCounter = 0;
        isvoidCounter = 0;
        notCounter = 0;
        templates = new STGroupFile("cool/compiler/cgen.stg");
        ints = new HashMap<>();
        strings = new HashMap<>();
        tags = templates.getInstanceOf("sequence");
        intConstants = templates.getInstanceOf("sequence");
        stringConstants = templates.getInstanceOf("sequence");
        classNameTabs = templates.getInstanceOf("sequence");
        classObjTabs = templates.getInstanceOf("sequence");
        prototypeObjects = templates.getInstanceOf("sequence");
        dispatchTabs = templates.getInstanceOf("sequence");
        methods = templates.getInstanceOf("sequenceSpaced");
        TypeSymbol.OBJECT.setTags();

        addIntToConstants(0);
        addStringToConstants("");

        ST intTag = templates.getInstanceOf("basicTag")
                .add("name", "int")
                .add("tag", TypeSymbol.INT.tag);
        ST stringTag = templates.getInstanceOf("basicTag")
                .add("name", "string")
                .add("tag", TypeSymbol.STRING.tag);
        ST boolTag = templates.getInstanceOf("basicTag")
                .add("name", "bool")
                .add("tag", TypeSymbol.BOOL.tag);
        tags.add("e", intTag).add("e", stringTag).add("e", boolTag);

        TypeSymbol.tagOrder.forEach(symbol -> {
            if (symbol.isBasicType())
                methods.add("e", symbol.getInitMethod(templates));
            classObjTabs.add("e", symbol.getClassObjTabPair(templates));
            prototypeObjects.add("e", symbol.generateObjectPrototype(templates));
            dispatchTabs.add("e", symbol.generateDispatchTable(templates));
            classNameTabs.add("e", ".word\t" + addStringToConstants(symbol.getName()));
        });

        program.classDefinitions.forEach(classDefinition -> classDefinition.accept(this));
        return templates.getInstanceOf("program")
                .add("tags", tags)
                .add("intConstants", intConstants)
                .add("stringConstants", stringConstants)
                .add("boolTag", TypeSymbol.BOOL.tag)
                .add("classNameTabs", classNameTabs)
                .add("classObjTabs", classObjTabs)
                .add("prototypeObjects", prototypeObjects)
                .add("dispatchTabs", dispatchTabs)
                .add("methods", methods);
    }

    @Override
    public ST visit(ClassDefinition classDefinition) {
        ST attributes = templates.getInstanceOf("sequence");
        currentScope = classDefinition.type;
        classDefinition.features.forEach(feature -> {
            ST featureTemplate = feature.accept(this);
            if (feature instanceof FunctionDefinition)
                methods.add("e", featureTemplate);
            else if (feature instanceof FieldDefinition) {
                if (featureTemplate != null) {
                    attributes.add("e", featureTemplate)
                            .add("e", templates.getInstanceOf("storeField").add("offset", ((FieldDefinition) feature).id.offset));
                }
            }
        });
        currentScope = currentScope.getParent();
        methods.add("e", classDefinition.type.getInitMethod(templates).add("attributes", attributes.render()));
        return null;
    }

    @Override
    public ST visit(FieldDefinition fieldDefinition) {
        if (fieldDefinition.expr == null)
            return null;

        return fieldDefinition.expr.accept(this);
    }

    @Override
    public ST visit(Int intt) {
        return templates.getInstanceOf("literal").add("val", addIntToConstants(Integer.parseInt(intt.token.getText())));
    }

    @Override
    public ST visit(Formal formal) {
        return null;
    }

    @Override
    public ST visit(FunctionDefinition functionDefinition) {
        currentScope = functionDefinition.functionSymbol;
        ST body = functionDefinition.expression.accept(this);
        currentScope = currentScope.getParent();
        return templates.getInstanceOf("functionDefinition")
                .add("class", functionDefinition.functionSymbol.parent.getName())
                .add("functionName", functionDefinition.functionSymbol.getName())
                .add("body", body)
                .add("freeParamsOffset", functionDefinition.arguments.size() != 0 ? 4 * functionDefinition.arguments.size() : null);
    }

    @Override
    public ST visit(Falsee falsee) {
        return templates.getInstanceOf("literal").add("val", "bool_const0");
    }

    @Override
    public ST visit(Id id) {
        Symbol idSymbol = currentScope.lookup(id.token.getText());
        if (idSymbol.getName().equals("self")) {
            return templates.getInstanceOf("self");
        }

        if (((IdSymbol) idSymbol).isField) {
            return templates.getInstanceOf("field")
                    .add("offset", ((IdSymbol) idSymbol).offset);
        }

        if (((IdSymbol) idSymbol).isFormal) {
            return templates.getInstanceOf("getFormalParameter")
                    .add("offset", ((IdSymbol) idSymbol).offset);
        }

        if (((IdSymbol) idSymbol).isLocal) {
            return templates.getInstanceOf("loadVar")
                    .add("offset", ((IdSymbol) idSymbol).offset);
        }
        return null;
    }

    @Override
    public ST visit(Stringg stringg) {
        return templates.getInstanceOf("literal").add("val", addStringToConstants(stringg.token.getText()));
    }

    @Override
    public ST visit(Truee truee) {
        return templates.getInstanceOf("literal").add("val", "bool_const1");
    }

    @Override
    public ST visit(ParenthesisExpression parenthesisExpression) {
        return null;
    }

    @Override
    public ST visit(MultiplyDivisionExpression multiplyDivisionExpression) {
        return null;
    }

    @Override
    public ST visit(PlusMinusExpression plusMinusExpression) {
        return templates.getInstanceOf("arithmExpression")
                .add("left", plusMinusExpression.left.accept(this))
                .add("right", plusMinusExpression.right.accept(this))
                .add("operation", plusMinusExpression.op.getText().equals("+") ? "add" : null);
    }

    @Override
    public ST visit(RelationalExpression relationalExpression) {
        return null;
    }

    @Override
    public ST visit(NotExpression notExpression) {
        notCounter++;
        return templates.getInstanceOf("not")
                .add("expression", notExpression.expr.accept(this))
                .add("counter", notCounter - 1);
    }

    @Override
    public ST visit(ComplementExpression complementExpression) {
        return null;
    }

    @Override
    public ST visit(AssignExpression assignExpression) {
        ST assignmentSequence = templates.getInstanceOf("sequence");
        ST assignment = assignExpression.expr.accept(this);
        assignmentSequence.add("e", assignment);
        Symbol idSymbol = currentScope.lookup(assignExpression.variable.getText());
        if (((IdSymbol) idSymbol).isField) {
            assignmentSequence.add("e", templates.getInstanceOf("storeField").add("offset", ((IdSymbol) idSymbol).offset));
        }

        if (((IdSymbol) idSymbol).isFormal) {
            assignmentSequence.add("e", templates.getInstanceOf("storeFormalParameter").add("offset", ((IdSymbol) idSymbol).offset));
        }

        if (((IdSymbol) idSymbol).isLocal) {
            assignmentSequence.add("e", templates.getInstanceOf("storeVar").add("offset", ((IdSymbol) idSymbol).offset));
        }

        return assignmentSequence;
    }

    @Override
    public ST visit(IsVoidExpression isVoidExpression) {
        isvoidCounter++;
        return templates.getInstanceOf("isvoid")
                .add("expression", isVoidExpression.expr.accept(this))
                .add("counter", isvoidCounter - 1);
    }

    @Override
    public ST visit(NewExpression newExpression) {
        if (newExpression.type.getText().equals("SELF_TYPE")) {
            return templates.getInstanceOf("newSelfType");
        }
        return templates.getInstanceOf("new").add("type", newExpression.type.getText());
    }

    @Override
    public ST visit(DispatchFunctionCallExpression dispatchFunctionCallExpression) {
        dispatchCounter++;
        int currentDispatch = dispatchCounter;
        ST exprTemplate = dispatchFunctionCallExpression.expr.accept(this);
        Symbol functionSymbol = dispatchFunctionCallExpression.dispatchType.lookupFunction(dispatchFunctionCallExpression.name.getText());
        ST parameters = templates.getInstanceOf("sequence");
        for (int i = dispatchFunctionCallExpression.callArgs.size() - 1; i >= 0; i--) {
            ST callArgTemplate = dispatchFunctionCallExpression.callArgs.get(i).accept(this);
            parameters.add("e", templates.getInstanceOf("saveParamOnStack").add("instruction", callArgTemplate));
        }
        return templates.getInstanceOf("dispatchMethod")
                .add("counter", currentDispatch - 1)
                .add("filename", getFileName(dispatchFunctionCallExpression))
                .add("line", dispatchFunctionCallExpression.token.getLine())
                .add("offset", ((FunctionSymbol) functionSymbol).offset)
                .add("explicit", exprTemplate)
                .add("parameters", dispatchFunctionCallExpression.callArgs.isEmpty() ? null : parameters)
                .add("static", dispatchFunctionCallExpression.type != null ? dispatchFunctionCallExpression.type.getText() : null);
    }

    @Override
    public ST visit(FunctionCallExpression functionCallExpression) {
        Scope startScope = currentScope;
        while (!(startScope instanceof TypeSymbol typeScope)) {
            startScope = startScope.getParent();
        }

        Symbol functionSymbol = typeScope.lookupFunction(functionCallExpression.name.getText());
        dispatchCounter++;
        int currentDispatch = dispatchCounter;
        ST parameters = templates.getInstanceOf("sequence");
        for (int i = functionCallExpression.callArgs.size() - 1; i >= 0; i--) {
            ST callArgTemplate = functionCallExpression.callArgs.get(i).accept(this);
            parameters.add("e", templates.getInstanceOf("saveParamOnStack").add("instruction", callArgTemplate));
        }
        return templates.getInstanceOf("dispatchMethod")
                .add("counter", currentDispatch - 1)
                .add("filename", getFileName(functionCallExpression))
                .add("line", functionCallExpression.token.getLine())
                .add("offset", ((FunctionSymbol) functionSymbol).offset)
                .add("parameters", functionCallExpression.callArgs.isEmpty() ? null : parameters);
    }

    @Override
    public ST visit(IfExpression ifExpression) {
        ifCounter++;
        return templates.getInstanceOf("ifExpression")
                .add("ifCounter", ifCounter - 1)
                .add("condition", ifExpression.condition.accept(this))
                .add("thenBranch", ifExpression.ifBranch.accept(this))
                .add("elseBranch", ifExpression.elseBranch.accept(this));
    }

    @Override
    public ST visit(WhileExpression whileExpression) {
        return null;
    }

    @Override
    public ST visit(Local local) {
        ST localSequence = templates.getInstanceOf("sequence");
        if (local.expression == null) {
            TypeSymbol localType = local.idSymbol.getType();
            localSequence.add("e", templates.getInstanceOf("literal").add("val", localType.getDefaultValueForType(localType)))
                    .add("e", templates.getInstanceOf("storeVar").add("offset", local.idSymbol.offset));
        } else {
            ST initLocalExpression = local.expression.accept(this);
            localSequence.add("e", initLocalExpression)
                    .add("e", templates.getInstanceOf("storeVar").add("offset", local.idSymbol.offset));
        }
        return localSequence;
    }

    @Override
    public ST visit(LetExpression letExpression) {
        currentScope = letExpression.scope;
        ST letSequence = templates.getInstanceOf("sequence");
        ST localSequence = templates.getInstanceOf("sequence");
        int localsSize = letExpression.locals.size();
        ST letTemplate = templates.getInstanceOf("letExpression").add("offset", localsSize * 4);
        letExpression.locals.forEach(local -> {
            localSequence.add("e", local.accept(this));
        });
        letTemplate.add("inits", localSequence);
        ST expression = letExpression.expression.accept(this);
        letSequence.add("e", letTemplate)
                .add("e", expression)
                .add("e", templates.getInstanceOf("freeStack").add("offset", localsSize * 4));
        currentScope = currentScope.getParent();
        return letSequence;
    }

    @Override
    public ST visit(CaseBranch caseBranch) {
        return null;
    }

    @Override
    public ST visit(CaseExpression caseExpression) {
        return null;
    }

    @Override
    public ST visit(BlockExpression blockExpression) {
        ST blockTemplate = templates.getInstanceOf("sequence");
        blockExpression.expressions.forEach(expression -> blockTemplate.add("e", expression.accept(this)));
        return blockTemplate;
    }
}
