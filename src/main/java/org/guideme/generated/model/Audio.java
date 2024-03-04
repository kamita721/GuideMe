package org.guideme.generated.model;

public interface Audio extends FlagSet, Playable, Filterable  {
	public abstract void setScriptVar(String scriptVar);
	public abstract void setId(String id);
	public abstract String getId();
	public abstract String getTarget();
	public abstract String getScriptVar();
	public abstract void setTarget(String target);
}
