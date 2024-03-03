package org.guideme.guideme.model;

import java.util.HashMap;
import java.util.Map;

import org.guideme.generated.model.Page;

public class Chapter {
	private String id;
	private Map<String, Page> pages = new HashMap<>();

	public Chapter(String id) {
		this.id = id;
	}
	
	public Map<String, Page> getPages() {
		return pages;
	}

	public void setPages(Map<String, Page> pages) {
		this.pages = pages;
	}

	public String getId() {
		return id;
	}

}
