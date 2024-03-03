package org.guideme.codegen.code_builder;

public class Type {
	private final String typeFull;

	public Type(String typeFull) {
		this.typeFull = typeFull;
	}

	public String getTypeBrief() {
		String[] ss = typeFull.split("\\.");
		return ss[ss.length - 1];
	}

	public String getTypeFull() {
		return typeFull;
	}

	public boolean isImplicitType() {
		if (typeFull.startsWith("java.lang")) {
			return true;
		}
		if (!typeFull.contains(".")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof Type)) {
//			return false;
			throw new IllegalStateException();
		}
		Type o = (Type) other;
		return typeFull.equals(o.typeFull);
	}

	public int hashCode() {
		return typeFull.hashCode();
	}

	public boolean isType(String t) {
		return (typeFull.equals(t));
	}
}
