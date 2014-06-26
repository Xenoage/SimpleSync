package com.xenoage.labs.sync.local;

import static com.xenoage.utils.jse.xml.XMLReader.attribute;
import static com.xenoage.utils.jse.xml.XMLReader.element;
import static com.xenoage.utils.jse.xml.XMLReader.elements;
import static com.xenoage.utils.jse.xml.XMLReader.root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;

/**
 * Index of files and their MD5 sums and
 * cleaning information on the local side.
 * 
 * @author Andreas Wenger
 */
public class LocalIndex
	extends LocalFileSet {
	
	public String server = null;
	public LocalClean clean = null;
	
	/**
	 * Parses an {@link LocalIndex} from the given XML index {@link Document}.
	 */
	public static LocalIndex parse(Document doc) {
		LocalIndex ret = new LocalIndex();
		//read server
		String server = attribute(root(doc), "server");
		if (server == null)
			throw new IllegalStateException("server not set");
		ret.server = server;
		//read directories for cleanup
		ret.clean = new LocalClean();
		Element eClean = element(root(doc), "clean");
		if (eClean != null)
			parseClean(eClean, ret.clean);
		//read fileset attributes and children
		parseFileSet(root(doc), ret);
		return ret;
	}
	
	private static void parseClean(Element e, LocalClean clean) {
		for (Element eChild : elements(e)) {
			if (eChild.getLocalName().equals("dir")) {
				String name = attribute(eChild, "name");
				if (name == null)
					throw new IllegalStateException("dir without name in clean");
				clean.dirs.add(name);
			}
		}
	}
	
	private static void parseFileSet(Element e, LocalFileSet set) {
		//read sourcedir (may be null)
		set.sourceDir = attribute(e, "sourcedir");
		//read targetdir (may be null)
		set.targetDir = attribute(e, "targetdir");
		//read children
		for (Element eChild : elements(e)) {
			if (eChild.getLocalName().equals("fileset")) {
				LocalFileSet childSet = new LocalFileSet();
				childSet.parent = set;
				parseFileSet(eChild, childSet);
				set.items.add(childSet);
			}
			else if (eChild.getLocalName().equals("file")) {
				LocalFile childFile = new LocalFile();
				childFile.parent = set;
				parseFile(eChild, childFile);
				set.items.add(childFile);
			}
		}
	}
	
	private static void parseFile(Element e, LocalFile file) {
		//read name
		String name = XMLReader.attribute(e, "name");
		if (name == null)
			throw new IllegalStateException("file without name in target dir " + file.getTargetDir());
		file.name = name;
		//read md5 sum
		String md5 = XMLReader.attribute(e, "md5");
		if (md5 == null)
			throw new IllegalStateException("file without md5: " + file.name);
		file.md5 = md5;
	}

}
