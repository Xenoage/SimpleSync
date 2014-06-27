package com.xenoage.simplesync.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a directory on the server.
 * It has a name and may contain other files or directories.
 * 
 * @author Andreas Wenger
 */
public class ServerDir
	implements ServerFileItem {

	public ServerDir parent = null;
	public String name = null;
	public List<ServerFileItem> items = new ArrayList<>();
	
	@Override public String getPath() {
		if (parent == null)
			return "/"; //root
		else
			return parent.getPath() + name + "/";
	}
	
	/**
	 * Gets the file at the given path, or null if the file does not exist.
	 */
	protected ServerFile findFile(String[] path, int currentPos) {
		if (path.length - 1 == currentPos) {
			//file must be in this directory
			for (ServerFileItem item : items) {
				if (item instanceof ServerFile) {
					ServerFile file = (ServerFile) item;
					if (file.name.equals(path[currentPos]))
						return file; //found
				}
			}
			return null; //not found
		}
		else {
			//go one directory deeper
			for (ServerFileItem item : items) {
				if (item instanceof ServerDir) {
					ServerDir dir = (ServerDir) item;
					if (dir.name.equals(path[currentPos]))
						return dir.findFile(path, currentPos + 1);
				}
			}
			return null; //not found
		}
	}

}
