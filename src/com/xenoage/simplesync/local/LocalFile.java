package com.xenoage.simplesync.local;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

/**
 * A file which should be synchronized.
 * 
 * @author Andreas Wenger
 */
public class LocalFile
	extends LocalFileItem {
	
	public String name = null;
	public String md5 = null;
	
	@Override public List<LocalFile> getAllFiles() {
		return alist(this);
	}
}
