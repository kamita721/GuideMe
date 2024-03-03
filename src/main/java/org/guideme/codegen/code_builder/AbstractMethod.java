package org.guideme.codegen.code_builder;

public class AbstractMethod extends Method {

	public AbstractMethod(Type returnType, String name) {
		super(returnType, name);
	}

	@Override
	public void generate(CodeBuilder builder) {
		builder.addLine("public abstract %s%s(%s)%s;", getTypePhrase(), getName(), getArgsPhrase(),
				getThrowsPhrase());
	}

	@Override
	public void addCodeBlock(CodeBlock cb) {
		throw new UnsupportedOperationException();
	}
}
