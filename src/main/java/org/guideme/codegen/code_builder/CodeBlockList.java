package org.guideme.codegen.code_builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeBlockList extends CodeBlock{
	
	private List<CodeBlock> content = new ArrayList<>();

	public void addContent(CodeBlock cb) {
		content.add(cb);
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

}
