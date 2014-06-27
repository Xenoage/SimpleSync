package com.xenoage.simplesync.local;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LocalFileSet
	extends LocalFileItem {

	public List<LocalFileItem> items = new ArrayList<>();
	
	@Override public List<LocalFile> getAllFiles() {
		List<LocalFile> ret = new LinkedList<>();
		for (LocalFileItem item : items)
			ret.addAll(item.getAllFiles());
		return ret;
	}
}
