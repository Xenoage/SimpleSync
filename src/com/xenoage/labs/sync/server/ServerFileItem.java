package com.xenoage.labs.sync.server;

/**
 * Interface for {@link ServerFile} and {@link ServerDir}.
 * 
 * @author Andreas Wenger
 */
public interface ServerFileItem {

	/**
	 * Gets the full path of this item.
	 * Examples: "/a/b/c.txt" for a file, "/a/b/" for a directory or "/" for the root.
	 */
	public String getPath();
}
