package org.guideme.guideme.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.guideme.generated.model.Page;

public class Chapter {
	private String id;
	private Map<String, Page> pages = new HashMap<>();

	public Chapter(String id) {
		this.id = id;
	}
	
	public Collection<Page> getPageCollection(){
		return pages.values();
	}
	
	public Page getPage(String pageName) {
		return pages.get(pageName);
	}
	
	public int getPageCount() {
		return pages.size();
	}
	
	public boolean hasPage(String pageName) {
		return pages.containsKey(pageName);
	}
	
	public void addPage(Page toAdd) {
		pages.put(toAdd.getId(), toAdd);
	}

	public String getId() {
		return id;
	}

}
