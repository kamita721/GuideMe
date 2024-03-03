package org.guideme.codegen.code_builder;

public class Variable {
	public final Type type;
	public final String name;

	public Variable(String type, String name) {
		this(new Type(type), name);
	}
	
	public Variable(Type type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String getDecl() {
		return "%s %s".formatted(type.getTypeAbstract().getTypeBrief(), name);
	}
}
