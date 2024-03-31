package org.guideme.codegen.code_builder;

import org.guideme.guideme.util.StringUtil;

public class FieldDecl {

	private final Variable variable;
	private final String initializer;
	private boolean isStatic = false;
	private boolean isFinal = false;

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

	public FieldDecl makeStatic() {
		isStatic = true;
		return this;
	}

	public FieldDecl makeFinal() {
		isFinal = true;
		return this;
	}

	private String getInitializerPhrase() {
		if (initializer == null) {
			return "";
		}
		return " = %s".formatted(initializer);
	}

	private String getModifierPhrase() {
		StringBuilder ans = new StringBuilder();
		ans.append("private");

		if (isStatic) {
			ans.append(" static");
		}

		if (isFinal) {
			ans.append(" final");
		}

		return ans.toString();
	}

	public void generate(CodeBuilder builder) {
		builder.addLine("%s %s%s;", getModifierPhrase(), variable.getDecl(),
				getInitializerPhrase());
	}

	public Method getter() {
		String name = "get" + StringUtil.capitalizeFirstChar(getName());
		if (getType().isType("java.util.ArrayList")) {
			name = name + "s";
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
