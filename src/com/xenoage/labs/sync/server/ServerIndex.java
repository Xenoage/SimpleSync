package com.xenoage.labs.sync.server;

import static com.xenoage.utils.jse.xml.XMLReader.elements;
import static com.xenoage.utils.jse.xml.XMLReader.root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;

/**
 * Index of directories and files and their MD5 sums
 * on the server.
 * 
 * @author Andreas Wenger
 */
public class ServerIndex
	extends ServerDir {
	
	/**
	 * Parses an {@link ServerIndex} from the given XML index {@link Document}.
	 */
	public static ServerIndex parse(Document doc) {
		ServerIndex ret = new ServerIndex();
		parseDir(root(doc), ret);
		return ret;
	}
	
	private static void parseDir(Element e, ServerDir dir) {
		//read name
		String name = XMLReader.attribute(e, "name");
		if (name == null)
			throw new IllegalStateException("dir without name: " + dir.getPath());
		dir.name = name;
		//read children
		for (Element eChild : elements(e)) {
			if (eChild.getLocalName().equals("dir")) {
				ServerDir childDir = new ServerDir();
				childDir.parent = dir;
				parseDir(eChild, childDir);
				dir.items.add(childDir);
			}
			else if (eChild.getLocalName().equals("file")) {
				ServerFile childFile = new ServerFile();
				childFile.parent = dir;
				parseFile(eChild, childFile);
				dir.items.add(childFile);
			}
		}
	}
	
	private static void parseFile(Element e, ServerFile file) {
		//read name
		String name = XMLReader.attribute(e, "name");
		if (name == null)
			throw new IllegalStateException("file without name: " + file.getPath());
		file.name = name;
		//read md5 sum
		String md5 = XMLReader.attribute(e, "md5");
		if (md5 == null)
			throw new IllegalStateException("file without md5: " + file.getPath());
		file.md5 = md5;
	}
	
}
