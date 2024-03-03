package org.guideme.codegen.code_builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StringSwitch extends CodeBlock{

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
		builder.addLine("switch(%s){", input);
		
		for(Entry<String, CodeBlock> entry : entries.entrySet()) {
			builder.addLine("case \"%s\":", entry.getKey());
			entry.getValue().generate(builder);
			builder.addLine("break;");
		}
		
		if(defaultCase != null) {
			builder.addLine("case default:");
			defaultCase.generate(builder);
			builder.addLine("break;");
		}
		
		builder.addLine("}");
		
	}
	
}
