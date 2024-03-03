package org.guideme.codegen.code_builder;

public class Line extends CodeBlock {

	private final String content;

	public Line(String content, Object... args) {
		content = content.formatted(args);
		if (!content.endsWith(";")) {
			content = content + ";";
		}
		this.content = content;
	}

	@Override
	public void generate(CodeBuilder builder) {
		builder.addLine(content);
	}

	public static Line getFinalAssignment(Variable left, String right) {
		Line ans = new Line(
				"final %s %s = %s;".formatted(left.type.getTypeBrief(), left.name, right));
		ans.addImport(left.type);
		return ans;
	}

}
