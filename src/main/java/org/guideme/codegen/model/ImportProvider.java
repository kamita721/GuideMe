package org.guideme.codegen.model;

import java.util.Set;

public interface ImportProvider {
	public Set<String> getInterfaceImports();
	public Set<String> getClassImports();
}
