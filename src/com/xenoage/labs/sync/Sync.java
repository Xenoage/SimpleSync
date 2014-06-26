package com.xenoage.labs.sync;

import java.util.List;

/**
 * A very simple sync tool, that synchronizes files from a HTTP server
 * to a local directory.
 * 
 * The file paths are given in the configuration file </code>sync.xml</code>.
 * Files, which are not listed there, are removed from the target directory.
 * 
 * Here is an example of the configuration file:
 * <pre>{@literal
 * <sync server="http://files.xenoage.com" targetdir="lib/">
 *   <!-- a single file -->
 *   <file file="gwt-servlet.jar" sourcedir="gwt/2.5.1/" targetdir="../webapp/war/WEB-INF/lib/"/>
 *   <!-- multiple files from the same source directory -->
 *   <fileset sourcedir="android-midi-lib/3.49/">
 *     <file file="android-midi-lib-3.49.jar"/>
 *     <file file="android-midi-lib-license.txt"/>
 *   </fileset>
 * </sync>
 * }</pre>
 * 
 * On each level, the local <code>targetdir</code> and the <code>sourcedir</code>
 * on the server can be defined. When missing, the directory from the parent
 * level is used.
 * 
 * @author Andreas Wenger
 */
public class Sync {
	
	/**
	 * Base class for each level.
	 */
	public static abstract class Level {
		public String sourceDir = null;
		public String targetDir = null;
	}
	
	/**
	 * Configuration.
	 */
	public static class Configuration
		extends Level {
		public String server = null;
		public List<FileItem> items = new List<FileItem>();
	}
	
	/**
	 * Interface for file items.
	 */
	public static interface FileItem {
		public List<String> getFilePaths();
	}

}
