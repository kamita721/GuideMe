package org.guideme.codegen.code_builder;

import java.util.HashSet;
import java.util.Set;

public abstract class CodeBlock {
	private final Set<Type> imports = new HashSet<>();
	private final Set<Type> throwables = new HashSet<>();

	public abstract void generate(CodeBuilder builder);

	public void addImport(Type t) {
		imports.add(t);
	}

	public void addImport(String s) {
		addImport(new Type(s));
	}

	public void addThrowable(Type t) {
		throwables.add(t);
	}

	public void addThrowable(String s) {
		addThrowable(new Type(s));
	}

	public Set<Type> getImports() {
		return new HashSet<>(imports);
	}

	public Set<Type> getThrowables() {
		return new HashSet<>(throwables);
	}

}
