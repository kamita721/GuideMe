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
	private String staticPhrase = "";

	public Method(Type returnType, String name) {
		this.returnType = returnType;
		this.name = name;
	}

	public Set<Type> getAllImports() {
		Set<Type> ans = new HashSet<>();
		if (returnType != null) {
			ans.add(returnType);
		}
		for (Variable v : args) {
			ans.add(v.type);
		}
		for(Type t : throwables) {
			ans.add(t);
		}
		for(CodeBlock cb : body) {
			ans.addAll(cb.getImports());
		}
		ans.removeIf(Type::isImplicitType);
		return ans;
	}

	public void addCodeBlock(CodeBlock cb) {
		body.add(cb);
		throwables.addAll(cb.getThrowables());
	}

	public void makeStatic() {
		staticPhrase = "static ";
	}

	public void addArg(Variable v) {
		args.add(v);
	}

	public void generate(CodeBuilder builder) {
		builder.addLine("public %s%s%s(%s) %s{", staticPhrase, getTypePhrase(), name,
				getArgsPhrase(), getThrowsPhrase());

		for (CodeBlock cb : body) {
			cb.generate(builder);
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
