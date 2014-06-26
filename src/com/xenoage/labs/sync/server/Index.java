package com.xenoage.labs.sync.server;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Index of directories and files and their MD5 sums.
 * 
 * @author Andreas Wenger
 */
public class Index
	extends ServerDir {
	
	/**
	 * Parses an {@link Index} from the given XML index {@link Document}.
	 */
	public static Index parse(Document doc) {
		Index ret = new Index();
		parseDir(doc.getDocumentElement(), ret);
		return ret;
	}
	
	private static void parseDir(Element e, ServerDir dir) {
		//read name
		String name = e.getAttribute("name");
		if (name.length() == 0)
			throw new IllegalStateException("dir contains dir without name: " + dir.getDirPath());
		dir.name = name;
		//read children
		for (e.get)
	}
	
}
