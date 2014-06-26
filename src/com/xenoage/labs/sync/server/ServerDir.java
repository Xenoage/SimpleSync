package com.xenoage.labs.sync.server;

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

}
