package org.guideme.codegen.code_builder;

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
		if(initializer == null) {
			return "";
		}
		return " = %s".formatted(initializer);
	}
	
	public void generate(CodeBuilder builder) {
		builder.addLine("private final %s%s;", variable.getDecl(), getInitializerPhrase());
	}

}
