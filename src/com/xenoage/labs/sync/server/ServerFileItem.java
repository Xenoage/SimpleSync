package com.xenoage.labs.sync.server;

/**
 * Interface for {@link ServerFile} and {@link ServerDir}.
 * 
 * @author Andreas Wenger
 */
public interface ServerFileItem {

	/**
	 * Gets the directory path to this item, e.g. "a/b/" for the file "a/b/c.txt".
	 */
	public String getDirPath();
}
