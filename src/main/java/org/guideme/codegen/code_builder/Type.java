package org.guideme.codegen.code_builder;

public class Type {
	private final String typeFull;

	public static final Type VOID = new Type("void");

	public Type(String typeFull) {
		this.typeFull = typeFull;
	}

	public String getTypeBrief() {
		String[] ss = typeFull.split("\\.");
		return ss[ss.length - 1];
	}

	public Type getTypeAbstract() {
		if (isType("java.util.ArrayList")) {
			String innerType = getGenericParameter().getTypeBrief();
			return new Type("java.util.List<%s>".formatted(innerType));
		}
		return this;
	}

	public String getTypeFull() {
		/* If the type contains a generic paramter, exclude it. */
		return typeFull.split("<")[0];
	}

	public boolean isImplicitType() {
		boolean ans = false;
		ans |= typeFull.startsWith("java.lang");
		ans |= !typeFull.contains(".");
		return ans;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != this.getClass()) {
			return false;
		}
		Type o = (Type) other;
		/*
		 * I could make an arguement about type erasure here. But really I am just
		 * abusing the fact that we only use equality for the import statements.
		 */
		return getTypeFull().equals(o.getTypeFull());
	}

	@Override
	public int hashCode() {
		return getTypeFull().hashCode();
	}

	public boolean isType(String t) {
		return (getTypeFull().equals(t));
	}

	public Type getGenericParameter() {
		String[] ss = typeFull.split("[<>]");
		if (ss.length > 2 || ss.length == 0) {
			throw new IllegalStateException();
		}
		if (ss.length == 1) {
			return null;
		}
		return new Type(ss[1]);
	}

}
