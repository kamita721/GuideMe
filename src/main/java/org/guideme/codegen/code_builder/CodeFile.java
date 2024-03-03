package org.guideme.codegen.code_builder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CodeFile {
	public enum CodeFileType {
		CLASS, INTERFACE
	}

	private final String[] packageName;
	private final Set<Type> imports = new HashSet<>();
	private final String fileName;
	private final Set<String> interfaces = new HashSet<>();
	private final Map<String, FieldDecl> fieldDecls = new HashMap<>();
	public final Method constructor;
	private final Set<Method> methods = new HashSet<>();

	private final CodeFileType type;

	public CodeFile(CodeFileType type, String[] packageName, String fileName) {
		this.type = type;
		this.packageName = packageName;
		this.fileName = fileName;
		this.constructor = new Method(null, fileName);
	}

	public void addInterface(String interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public void addFieldDecls(FieldDecl[] fds) {
		for (FieldDecl fd : fds) {
			addImport(fd.getType());
			fieldDecls.put(fd.getName(), fd);
		}
	}

	public void addMethods(Method[] ms) {
		for (Method m : ms) {
			addMethod(m);
		}
	}

	public void addMethod(Method m) {
		methods.add(m);
		imports.addAll(m.getAllImports());

	}

	public void addImport(Type t) {
		if (!t.isImplicitType()) {
			imports.add(t);
		}
	}

	private String getFileType() {
		switch (type) {
		case CLASS:
			return "class";
		case INTERFACE:
			return "interface";
		default:
			throw new IllegalStateException();
		}
	}

	private String getImplementsPhrase() {
		if (interfaces.isEmpty()) {
			return "";
		}
		String keyword = "implements";
		if (type == CodeFileType.INTERFACE) {
			keyword = "extends";
		}
		return "%s %s ".formatted(keyword, String.join(", ", interfaces));
	}

	public void generate(File srcRoot) throws IOException {
		/*
		 * Since we create the constructor at the initializatoin (instead of when it is done), we have no earlier opportunity
		 * to collect its imports
		 */
		imports.addAll(constructor.getAllImports());
		
		CodeBuilder builder = new CodeBuilder();
		builder.addLine("package %s;", String.join(".", packageName));
		builder.addLine();

		for (Type t : imports) {
			builder.addLine("import %s;", t.getTypeFull());
		}

		builder.addLine("public %s %s %s {", getFileType(), fileName, getImplementsPhrase());

		if (this.type != CodeFileType.INTERFACE) {
			builder.addLine();
		}

		for (FieldDecl decl : fieldDecls.values()) {
			decl.generate(builder);
		}
		if (!fieldDecls.isEmpty()) {
			builder.addLine();
		}

		if (this.type != CodeFileType.INTERFACE) {
			constructor.generate(builder);
			builder.addLine();
		}

		for (Method m : methods) {
			m.generate(builder);
		}

		builder.addLine("}");

		if(fileName.equals("Parsers")) {
			System.err.println();
		}
		
		builder.generate(srcRoot, packageName, fileName);
	}

}
