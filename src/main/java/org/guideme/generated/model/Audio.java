package org.guideme.generated.model;

public interface Audio extends FlagSet, Playable, Filterable  {
	public abstract void setTarget(String target);
	public abstract void setScriptVar(String scriptVar);
	public abstract String getScriptVar();
	public abstract String getId();
	public abstract String getTarget();
	public abstract void setId(String id);
}
