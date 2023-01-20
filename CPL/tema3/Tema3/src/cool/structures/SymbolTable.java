package cool.structures;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;

        // define parents for the basic COOL classes
        TypeSymbol.INT.parent = TypeSymbol.OBJECT;
        TypeSymbol.STRING.parent = TypeSymbol.OBJECT;
        TypeSymbol.BOOL.parent = TypeSymbol.OBJECT;
        TypeSymbol.IO.parent = TypeSymbol.OBJECT;
        TypeSymbol.SELF_TYPE.parent = TypeSymbol.OBJECT;

        TypeSymbol.OBJECT.children.addAll(Arrays.asList(TypeSymbol.IO,
                TypeSymbol.INT,
                TypeSymbol.STRING,
                TypeSymbol.BOOL));

        defineObjectClassFunctions();
        defineIOClassFunctions();
        defineStringClassFunctions();

        globals.add(TypeSymbol.OBJECT);
        globals.add(TypeSymbol.INT);
        globals.add(TypeSymbol.STRING);
        globals.add(TypeSymbol.BOOL);
        globals.add(TypeSymbol.IO);
        globals.add(TypeSymbol.SELF_TYPE);
    }

    private static void defineStringClassFunctions() {
        FunctionSymbol lengthFunction = new FunctionSymbol("length");
        lengthFunction.returnType = TypeSymbol.INT;
        lengthFunction.parent = TypeSymbol.STRING;
        lengthFunction.offset = 12;
        TypeSymbol.STRING.add(lengthFunction);

        FunctionSymbol concatFunction = new FunctionSymbol("concat");
        IdSymbol concatFunctionParam = new IdSymbol("s");
        concatFunctionParam.setType(TypeSymbol.STRING);
        concatFunction.add(concatFunctionParam);
        concatFunction.returnType = TypeSymbol.STRING;
        concatFunction.parent = TypeSymbol.STRING;
        concatFunction.offset = 16;
        TypeSymbol.STRING.add(concatFunction);

        FunctionSymbol substrFunction = new FunctionSymbol("substr");
        IdSymbol substrFunctionParam1 = new IdSymbol("i");
        IdSymbol substrFunctionParam2 = new IdSymbol("l");
        substrFunctionParam1.setType(TypeSymbol.INT);
        substrFunctionParam2.setType(TypeSymbol.INT);
        substrFunction.add(substrFunctionParam1);
        substrFunction.add(substrFunctionParam2);
        substrFunction.returnType = TypeSymbol.STRING;
        substrFunction.parent = TypeSymbol.STRING;
        substrFunction.offset = 20;
        TypeSymbol.STRING.add(substrFunction);
    }

    private static void defineIOClassFunctions() {
        FunctionSymbol outStringFunction = new FunctionSymbol("out_string");
        IdSymbol outStringFunctionParam = new IdSymbol("x");
        outStringFunctionParam.setType(TypeSymbol.STRING);
        outStringFunction.add(outStringFunctionParam);
        outStringFunction.returnType = TypeSymbol.SELF_TYPE;
        outStringFunction.parent = TypeSymbol.IO;
        outStringFunction.offset = 12;
        TypeSymbol.IO.add(outStringFunction);

        FunctionSymbol outIntFunction = new FunctionSymbol("out_int");
        IdSymbol outIntFunctionParam = new IdSymbol("x");
        outIntFunctionParam.setType(TypeSymbol.INT);
        outIntFunction.add(outIntFunctionParam);
        outIntFunction.returnType = TypeSymbol.SELF_TYPE;
        outIntFunction.parent = TypeSymbol.IO;
        outIntFunction.offset = 16;
        TypeSymbol.IO.add(outIntFunction);

        FunctionSymbol inStringFunction = new FunctionSymbol("in_string");
        inStringFunction.returnType = TypeSymbol.STRING;
        inStringFunction.parent = TypeSymbol.IO;
        inStringFunction.offset = 20;
        TypeSymbol.IO.add(inStringFunction);

        FunctionSymbol inIntFunction = new FunctionSymbol("in_int");
        inIntFunction.returnType = TypeSymbol.INT;
        inIntFunction.parent = TypeSymbol.IO;
        inIntFunction.offset = 24;
        TypeSymbol.IO.add(inIntFunction);
    }

    private static void defineObjectClassFunctions() {
        FunctionSymbol abortFunction = new FunctionSymbol("abort");
        abortFunction.returnType = TypeSymbol.OBJECT;
        abortFunction.parent = TypeSymbol.OBJECT;
        abortFunction.offset = 0;
        TypeSymbol.OBJECT.add(abortFunction);

        FunctionSymbol typeNameFunction = new FunctionSymbol("type_name");
        typeNameFunction.returnType = TypeSymbol.STRING;
        typeNameFunction.parent = TypeSymbol.OBJECT;
        typeNameFunction.offset = 4;
        TypeSymbol.OBJECT.add(typeNameFunction);

        FunctionSymbol copyFunction = new FunctionSymbol("copy");
        copyFunction.returnType = TypeSymbol.SELF_TYPE;
        copyFunction.parent = TypeSymbol.OBJECT;
        copyFunction.offset = 8;
        TypeSymbol.OBJECT.add(copyFunction);
    }

    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}