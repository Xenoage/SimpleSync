Xenoage SimpleSync 0.1
======================

A very simple sync tool, that synchronizes files from a HTTP server
to a local directory.

The file paths are given in the local configuration file sync.xml.

Here is an example of the local sync.xml file:

```xml
<sync server="http://files.xenoage.com" targetdir="lib/">
  <!-- directories which to clean -->
  <clean>
    <dir name="lib/"/>
  </clean>
  <!-- a single file -->
  <file file="gwt-servlet.jar" md5="d1d1a7b60f7a445688e89206a608c5ed" sourcedir="gwt/2.5.1/" targetdir="lib/webapp/war/WEB-INF/lib/"/>
  <!-- multiple files from the same source directory -->
  <fileset sourcedir="android-midi-lib/3.49/">
    <file name="android-midi-lib-3.49.jar" md5="1e2dce28260eb0fe12ce83b07341b6a3"/>
    <file name="android-midi-lib-license.txt" md5="cafc597722cfad668c26d84e214993bb"/>
  </fileset>
</sync>
```

On each level, the local targetdir and the sourcedir
on the server can be defined. When missing, the directory from the parent
level is used.

Files, which are in the directories under &lt;clean&gt;, but are not listed in the files,
will be removed. The &lt;clean&gt; element is optional. When it misses, no old files will be deleted,
but just updated if required.

On the server side, the file sync.index is required at the root level
(which is the level of the server attribute in the sync.xml file).
It lists the available directories, files and their md5 sums.

Here is an example of the remote sync.index file:

```xml
<index>
  <dir name="android-midi-lib">
    <dir name="3.49">
      <file name="android-midi-lib-3.49.jar" md5="1e2dce28260eb0fe12ce83b07341b6a3"/>
      <file name="android-midi-lib-license.txt" md5="cafc597722cfad668c26d84e214993bb"/>
    </dir>
  </dir>
  <dir name="gwt">
    <dir name="2.5.1">
      <file name="gwt-servlet.jar" md5="d1d1a7b60f7a445688e89206a608c5ed"/>
    </dir>
  </dir>
</index>
```

(c) 2014 by Andreas Wenger, Xenoage Software, info@xenoage.com
