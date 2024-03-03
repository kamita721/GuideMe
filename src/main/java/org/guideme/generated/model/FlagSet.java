package org.guideme.generated.model;

import java.util.List;
public interface FlagSet  {
	public abstract void setUnSet(String unSet);
	public abstract void setSet(String set);
	public abstract String getUnSet();
	public abstract String getSet();
	
	public void setUnSet(List<String> setList);
	
}
