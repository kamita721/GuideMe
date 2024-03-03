package org.guideme.codegen.code_builder;

import org.guideme.guideme.util.StringUtil;

public class FieldDecl {

	private final Variable variable;
	private final String initializer;

	public FieldDecl(Variable variable, String initializer) {
		this.variable = variable;
		this.initializer = initializer;
	}

	public String getName() {
		return variable.name;
	}

	public Type getType() {
		return variable.type;
	}

	private String getInitializerPhrase() {
		if (initializer == null) {
			return "";
		}
		return " = %s".formatted(initializer);
	}

	public void generate(CodeBuilder builder) {
		builder.addLine("private %s%s;", variable.getDecl(), getInitializerPhrase());
	}

	public Method getter() {
		String name = "get" + StringUtil.capitalizeFirstChar(getName());
		if(getType().isType("java.util.ArrayList")) {
			name = name +"s";
		}
		Method ans = new Method(getType().getTypeAbstract(), name);
		ans.addCodeBlock(new Line("return %s;", getName()));
		return ans;
	}

	public Method setter() {
		Method ans = new Method(Type.VOID, "set" + StringUtil.capitalizeFirstChar(getName()));
		ans.addArg(new Variable(variable.type.getTypeAbstract(), getName()));
		ans.addCodeBlock(new Line("this.%s = %s;", getName(), getName()));
		return ans;
	}

}
