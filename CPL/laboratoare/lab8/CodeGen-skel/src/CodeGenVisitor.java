import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class CodeGenVisitor implements ASTVisitor<ST>{
	static STGroupFile templates = new STGroupFile("cgen.stg");
	static int ifCounter = 0;
	static int offset = 0;
	
	ST mainSection;	// filled directly (through visitor returns)
	ST dataSection; // filled collaterally ("global" access)
	ST funcSection; // filled collaterally ("global" access)

	/* 
	 * Plain numbers
	 * TODO 1:
	 */

    @Override
    public ST visit(Int val) {
        return templates.getInstanceOf("literal")
                .add("value", val.getToken().getText());
    }
    
    @Override
    public ST visit(FloatNum val) {
        return templates.getInstanceOf("literal").add("value", val.getToken().getText());
    }

    @Override
    public ST visit(Bool val) {
		var st = templates.getInstanceOf("literal");
		if (val.getToken().getText().equals("true"))
			return st.add("value", 1);
		else
			return st.add("value", 0);
    }
    
    /* 
     * Unary operations
     * TODO 1:
     */
    
	@Override
	public ST visit(UnaryMinus uMinus) {
		return templates.getInstanceOf("uMinus").add("e", uMinus.expr.accept(this));
	}
    
	/* 
	 * Binary operations
	 * TODO 2:
	 */
    
    @Override
    public ST visit(Plus expr) {
    	var st = templates.getInstanceOf("plus");
    	st.add("e1", expr.left.accept(this))
    		.add("e2",  expr.right.accept(this))
    		.add("dStr", expr.debugStr);
    	
    	return st;
    }
    
    @Override
    public ST visit(Minus expr) {
		return templates.getInstanceOf("minus")
				.add("e1", expr.left.accept(this))
				.add("e2", expr.left.accept(this))
				.add("dStr", expr.debugStr);
    }
    
    @Override
    public ST visit(Mult expr) {
    	return templates.getInstanceOf("mult")
				.add("e1", expr.left.accept(this))
				.add("e2", expr.right.accept(this))
				.add("dStr", expr.debugStr);
    }
    
    @Override
    public ST visit(Div expr) {
    	return templates.getInstanceOf("div")
				.add("e1", expr.left.accept(this))
				.add("e2", expr.right.accept(this))
				.add("dStr", expr.debugStr);
    }
	
	@Override
	public ST visit(Relational expr) {
		return null;
	}

    /*
     * Control structures
     * TODO 3:
     */
    
    @Override
	public ST visit(If iff) {
		ifCounter++;
		return templates.getInstanceOf("ifExpr")
				.add("cond", iff.cond.accept(this))
				.add("thenBranch", iff.thenBranch.accept(this))
				.add("elseBranch", iff.elseBranch.accept(this))
				.add("counter", ifCounter);
	}

	@Override
	public ST visit(Call call) {
		ST paramDefines = templates.getInstanceOf("paramDefines");
		ST functionCall = templates.getInstanceOf("functionCall");
		for (int i = call.args.size() - 1; i >= 0; i--) {
			functionCall.add("params", templates.getInstanceOf("paramDefines").add(
			"param", call.args.get(i).accept(this)
			));
		}
		return functionCall
				.add("id", call.id.getToken().getText());
	}

    /*
     * Definitions & assignments
     * TODO 4&5:
     */

	@Override
	public ST visit(Assign assign) {
		// TODO 4: generare cod pentru main()
		return templates.getInstanceOf("assign")
				.add("id", assign.id.getToken().getText())
				.add("e2", assign.expr.accept(this));
	}

	@Override
	public ST visit(VarDef varDef) {
		if (varDef.type.getToken().getText().equals("Int")) {
			dataSection
					.add("e", templates.getInstanceOf("intVarDef")
							.add("id", varDef.id.getToken().getText())
							.add("initValue", varDef.initValue));
			if (varDef.initValue != null)
				return templates.getInstanceOf("assign")
						.add("id", varDef.id.getToken().getText())
						.add("e2", varDef.initValue.accept(this));
		}
		// TODO 4: generare cod pentru main() și etichetă în .data
		return null;
	}

	@Override
	public ST visit(FuncDef funcDef) {
		// TODO 5: generare cod pentru funcSection. Fără cod în main()!
		offset = 1;
		funcDef.formals.forEach(formal -> formal.accept(this));
		funcSection.add("e", templates.getInstanceOf("functionDefinition")
				.add("id", funcDef.id.getToken().getText())
				.add("e1", funcDef.body.accept(this))
				.add("offset", 4 * funcDef.formals.size() + 8));
		return null;
	}

	/*
	 * META
	 */
	
	@Override
	public ST visit(Id id) {
		// TODO 5
		if (id.getSymbol().isFormal) {
			return templates.getInstanceOf("getFromStack")
					.add("offset", id.getSymbol().offset);
		} else {
			return templates.getInstanceOf("getFromData")
					.add("id", id.token.getText());
		}
	}

	@Override
	public ST visit(Formal formal) {
		formal.id.getSymbol().isFormal = true;
		formal.id.getSymbol().offset = 4 * offset;
		offset++;
		return null;
	}
	
	@Override
	public ST visit(Type type) {
		return null;
	}

	@Override
	public ST visit(Program program) {
		dataSection = templates.getInstanceOf("sequenceSpaced");
		funcSection = templates.getInstanceOf("sequenceSpaced");
		mainSection = templates.getInstanceOf("sequence");
		
		for (ASTNode e : program.stmts)
			mainSection.add("e", e.accept(this));
		
		//assembly-ing it all together. HA! get it?
		var programST = templates.getInstanceOf("program");
		programST.add("data", dataSection);
		programST.add("textFuncs", funcSection);
		programST.add("textMain", mainSection);
		
		return programST;
	}

}
