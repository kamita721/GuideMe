package org.guideme.codegen.code_builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Method {

	/* Null for constructors */
	private final Type returnType;
	private final String name;
	private final List<Variable> args = new ArrayList<>();
	private final Set<Type> throwables = new HashSet<>();
	private final List<CodeBlock> body = new ArrayList<>();
	private final List<Type> imports = new ArrayList<>();
	private String staticPhrase = "";
	private String visibility = "public";
	
	private boolean overrides = false;

	public Method(Type returnType, String name) {
		this.returnType = returnType;
		this.name = name;
	}

	public AbstractMethod asAbstract() {
		AbstractMethod ans = new AbstractMethod(returnType, name);
		args.forEach(ans::addArg);
		throwables.forEach(ans::addThrowable);
		return ans;
	}

	public Set<Type> getAllImports() {
		Set<Type> ans = new HashSet<>();
		ans.addAll(imports);
		if (returnType != null) {
			ans.add(returnType);
		}
		for (Variable v : args) {
			ans.add(v.type);
		}
		for (Type t : throwables) {
			ans.add(t);
		}
		for (CodeBlock cb : body) {
			ans.addAll(cb.getImports());
		}
		ans.removeIf(Type::isImplicitType);
		return ans;
	}

	public void addImport(Type t) {
		if (!t.isImplicitType()) {
			imports.add(t);
		}
	}

	public void addImport(String t) {
		addImport(new Type(t));
	}

	public void addCodeBlock(CodeBlock cb) {
		body.add(cb);
		throwables.addAll(cb.getThrowables());
	}

	public void addLine(String line, Object... fmtArgs) {
		addCodeBlock(new Line(line, fmtArgs));
	}

	public void addThrowable(Type t) {
		throwables.add(t);
	}

	public void addThrowable(String t) {
		addThrowable(new Type(t));
	}

	public void makeStatic() {
		staticPhrase = "static ";
	}

	public void makePrivate() {
		visibility = "private";
	}

	public void makeOverride() {
		overrides = true;
	}
	
	public void makeNotOverride() {
		overrides = false;
	}
	
	public void addArg(Variable v) {
		args.add(v);
	}

	public void addArg(String type, String name) {
		addArg(new Variable(type, name));
	}

	public void generate(CodeBuilder builder) {
		if(overrides) {
			builder.addLine("@Override");
		}
		
		builder.addLine("%s %s%s%s(%s) %s{", visibility, staticPhrase, getTypePhrase(), name,
				getArgsPhrase(), getThrowsPhrase());

		for (CodeBlock cb : body) {
			cb.generate(builder);
		}
		
		if(body.isEmpty()) {
			builder.addLine("/* NOP */");
		}

		builder.addLine("}");
	}

	public String getName() {
		return name;
	}

	protected String getTypePhrase() {
		if (returnType == null) {
			return "";
		}
		return "%s ".formatted(returnType.getTypeBrief());
	}

	protected String getArgsPhrase() {
		String[] comps = new String[args.size()];
		for (int i = 0; i < comps.length; i++) {
			comps[i] = args.get(i).getDecl();
		}
		return String.join(", ", comps);
	}

	protected String getThrowsPhrase() {
		if (throwables.isEmpty()) {
			return "";
		}
		String[] comps = new String[throwables.size()];
		int i = 0;
		for (Type t : throwables) {
			comps[i++] = t.getTypeBrief();
		}

		return "throws %s ".formatted(String.join(",", comps));
	}
}
