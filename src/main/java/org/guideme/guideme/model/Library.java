package org.guideme.guideme.model;

import java.util.Date;
public class Library {
	public final String image;
	public final String title;
	public final String file;
	public final String author;
	public final Date date;

	public Library(String pimage, String ptitle, String pfile, String pauthor, Date pdate) {
		image = pimage;
		title = ptitle;
		file = pfile;
		author = pauthor;
		date = pdate;
	}

}
