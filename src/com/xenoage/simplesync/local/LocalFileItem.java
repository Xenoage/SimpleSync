package com.xenoage.simplesync.local;

import java.util.List;

/**
 * Base class for a {@link LocalFile} or {@link LocalFileSet}.
 * It can define its own source directory and target directory,
 * otherwise the setting of the parent item is used.
 * 
 * @author Andreas Wenger
 */
public abstract class LocalFileItem {
	
	public LocalFileSet parent = null;
	public String sourceDir = null;
	public String targetDir = null;
	
	/**
	 * Gets the (maybe inherited) source directory of this item,
	 * or "./" if undefined.
	 */
	public String getSourceDir() {
		if (sourceDir != null)
			return sourceDir;
		else if (parent == null)
			return "./";
		else
			return parent.getSourceDir();
	}
	
	/**
	 * Gets the (maybe inherited) target directory of this item,
	 * or "./" if undefined.
	 */
	public String getTargetDir() {
		if (targetDir != null)
			return targetDir;
		else if (parent == null)
			return "./";
		else
			return parent.getTargetDir();
	}
	
	/**
	 * Gets all files of this item.
	 */
	public abstract List<LocalFile> getAllFiles();
}
