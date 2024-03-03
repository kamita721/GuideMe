package org.guideme.codegen.code_builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CodeBlockList extends CodeBlock{
	
	private List<CodeBlock> content = new ArrayList<>();

	public void addContent(CodeBlock cb) {
		content.add(cb);
	}
	
	public void addLine(String fmt, Object...args) {
		addContent(new Line(fmt, args));
	}
	
	@Override
	public void generate(CodeBuilder builder) {
		for(CodeBlock cb : content) {
			cb.generate(builder);
		}	
	}
	
	@Override
	public Set<Type> getImports(){
		Set<Type> ans = super.getImports();
		for(CodeBlock cb : content) {
			ans.addAll(cb.getImports());
		}	
		return ans;
	}
	
	@Override
	public Set<Type> getThrowables(){
		Set<Type> ans = super.getThrowables();
		for(CodeBlock cb : content) {
			ans.addAll(cb.getThrowables());
		}	
		return ans;
	}
	
	public static CodeBlockList fromString(String code) {
		CodeBlockList ans = new CodeBlockList();
		for(String line : code.split("\n")) {
			line=line.strip();
			ans.addContent(new Line(line));
		}
		return ans;
	}

}
