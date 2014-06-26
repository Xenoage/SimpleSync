package com.xenoage.labs.sync.server;

/**
 * Information about a file on the server.
 * 
 * @author Andreas Wenger
 */
public class ServerFile
	implements ServerFileItem {

	public ServerDir parent = null;
	public String name = null;
	public String md5sum = null;


	@Override public String getDirPath() {
		if (parent == null)
			return ""+name;
		else
			return parent.getDirPath();
	}
}
