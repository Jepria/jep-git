package org.jepria.versionizer;

import java.nio.file.Path;

import org.jepria.versionizer.vcs.VCSFacade;

/**
 * Represents a single module in a version-creating context 
 */
public interface Module {
  String title();
  String targetVersion();
  VCSFacade vcs();
  Path root();// returns namely Path, not a String
  TestDriver testDriver();
}
