package org.guideme.codegen.code_builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StringSwitch extends CodeBlock {

	private final Map<String, CodeBlock> entries = new HashMap<>();
	private final String input;
	private CodeBlock defaultCase = null;

	public StringSwitch(String input) {
		this.input = input;
	}

	public void addCase(String key, CodeBlock value) {
		entries.put(key, value);
	}

	public void setDefault(CodeBlock defaultCase) {
		this.defaultCase = defaultCase;
	}

	@Override
	public void generate(CodeBuilder builder) {
		int caseCount = entries.size();
		if (caseCount == 0 && defaultCase == null) {
			return;
		}

		if (caseCount == 0) {
			defaultCase.generate(builder);
			return;
		}

		if (caseCount == 1) {
			@SuppressWarnings("unchecked")
			Entry<String, CodeBlock> entry = entries.entrySet().toArray(new Entry[] {})[0];
			builder.addLine("if(%s.equals(\"%s\")){", input, entry.getKey());
			entry.getValue().generate(builder);
			if (defaultCase == null) {
				builder.addLine("}");
			} else {
				builder.addLine("} else {");
				defaultCase.generate(builder);
				builder.addLine("}");
			}
			return;
		}

		builder.addLine("switch(%s){", input);

		for (Entry<String, CodeBlock> entry : entries.entrySet()) {
			builder.addLine("case \"%s\":", entry.getKey());
			entry.getValue().generate(builder);
			builder.addLine("break;");
		}

		if (defaultCase != null) {
			builder.addLine("default:");
			defaultCase.generate(builder);
			builder.addLine("break;");
		}

		builder.addLine("}");

	}

}
