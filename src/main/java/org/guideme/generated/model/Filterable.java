package org.guideme.generated.model;

import java.util.List;
import java.time.LocalTime;
public interface Filterable  {
	public abstract LocalTime getIfBefore();
	public abstract String getIfNotSet();
	public abstract void setIfBefore(LocalTime ifBefore);
	public abstract void setIfAfter(LocalTime ifAfter);
	public abstract void setIfNotSet(String ifNotSet);
	public abstract void setIfSet(String ifSet);
	public abstract LocalTime getIfAfter();
	public abstract String getIfSet();
	
	public boolean canShow(List<String> setList);
	
}
