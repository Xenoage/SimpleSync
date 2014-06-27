package com.xenoage.simplesync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import com.xenoage.simplesync.local.LocalFile;
import com.xenoage.simplesync.local.LocalIndex;
import com.xenoage.simplesync.server.ServerFile;
import com.xenoage.simplesync.server.ServerIndex;
import com.xenoage.utils.jse.io.DownloadTools;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.security.Md5Sum;
import com.xenoage.utils.jse.xml.XMLReader;

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
	 * Starting point of the application.
	 */
	public static void main(String... args) {
		//create temp folder
		File tempDir = JseFileUtils.createNewTempFolder();
		
		try {
			
			//read local index
			File localIndexFile = new File("sync.xml");
			System.out.println("Read local index from " + localIndexFile.getName());
			if (false == localIndexFile.exists())
				throw new FileNotFoundException(localIndexFile.getName() + " not found");
			LocalIndex localIndex = null;
			try {
				Document doc = XMLReader.readFile(localIndexFile.getAbsolutePath());
				localIndex = LocalIndex.parse(doc);
			} catch (Exception ex) {
				throw new IOException("Could not parse " + localIndexFile.getName() + ": " + ex);
			}
			
			//server index - do not read before it is required
			ServerIndex serverIndex = null;
			
			//collect files to clean (if any)
			Set<File> cleanFiles = new HashSet<>();
			if (localIndex.clean != null) {
				for (String dir : localIndex.clean.dirs)
					cleanFiles.addAll(JseFileUtils.listFiles(new File(dir), true));
			}
			
			//go through the files: download them, if not existing, or update them if they
			//exist but when the md5 sum is different
			List<LocalFile> syncFiles = localIndex.getAllFiles();
			int filesCountOK = 0;
			int filesCountUpdated = 0;
			while (false == syncFiles.isEmpty()) {
				LocalFile syncFile = syncFiles.remove(0);
				//update file?
				boolean updateRequired = false;
				File targetFile = new File(syncFile.getTargetDir() + "/" + syncFile.name);
				if (false == targetFile.exists()) {
					//file does not exist yet. download it.
					updateRequired = true;
				}
				else if (false == Md5Sum.GetMd5Sum(targetFile).equals(syncFile.md5)) {
					//md5 sum has changed. update it.
					updateRequired = true;
				}
				//start update, if needed
				if (updateRequired) {
					filesCountUpdated++;
					//read server index, if not already done
					if (serverIndex == null) {
						System.out.println("Download server index from " + localIndex.server);
						File serverIndexFile = new File(tempDir, "sync.index");
						DownloadTools.downloadFile(localIndex.server + "/sync.index", serverIndexFile);
						System.out.println("Read server index");
						try {
							Document doc = XMLReader.readFile(serverIndexFile.getAbsolutePath());
							serverIndex = ServerIndex.parse(doc);
						} catch (Exception ex) {
							throw new IOException("Could not parse server index: " + ex);
						}
					}
					//look if server provides the required file
					String serverFilePath = syncFile.getSourceDir() + "/" + syncFile.name;
					ServerFile serverFile = serverIndex.getFile(serverFilePath);
					if (serverFile == null)
						throw new IllegalStateException("File not provided by server: " + serverFilePath);
					else if (false == serverFile.md5.equals(syncFile.md5))
						throw new IllegalStateException("File provided by server only in different version: " + serverFilePath);
					System.out.println("Update file: " + targetFile.getPath());
					targetFile.getParentFile().mkdirs(); //create directory, when required
					DownloadTools.downloadFile(localIndex.server + "/" + serverFilePath, targetFile);
				}
				else {
					filesCountOK++;
				}
				//remove file from the cleanup list
				cleanFiles.remove(targetFile);
			}
			
			//clean up
			int filesCountDeleted = 0;
			for (File cleanFile : cleanFiles) {
				cleanFile.delete();
			}
			
			//print statistics
			System.out.println(filesCountUpdated + " files updated, " + filesCountOK + " already up to date, " +
				filesCountDeleted + " files deleted");
			
			//finished
			System.out.println("Sync finished");
			
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex);
			//delete temp folder
			JseFileUtils.deleteDirectory(tempDir);
			System.exit(1);
		}
	}

}
