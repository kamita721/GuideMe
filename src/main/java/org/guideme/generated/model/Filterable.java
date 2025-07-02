package org.guideme.generated.model;

import java.util.List;
import java.time.LocalTime;
public interface Filterable  {
	public abstract void setIfAfter(LocalTime ifAfter);
	public abstract LocalTime getIfAfter();
	public abstract void setIfNotSet(String ifNotSet);
	public abstract String getIfNotSet();
	public abstract String getIfSet();
	public abstract LocalTime getIfBefore();
	public abstract void setIfBefore(LocalTime ifBefore);
	public abstract void setIfSet(String ifSet);
	
	public boolean canShow(List<String> setList);
	
}
