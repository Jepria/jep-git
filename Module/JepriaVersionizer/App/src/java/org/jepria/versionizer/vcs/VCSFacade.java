package org.jepria.versionizer.vcs;

import java.util.List;

/**
 * Facade for Version Control System API for the particular module
 */
public interface VCSFacade {
  
  /**
   * Connect to the remote, prepare and synchronize (git pull or svn update)
   */
  void open();
  
  List<CommitFacade> getCommitsSinceLastVersion();
  
  /**
   * Do a version commit (contains nothing but version)
   */
  void commitVersion(String versionTag);
  
  /**
   * Do regular commit with a message
   */
  void commit(String commitMessage);
  
  /**
   * @return new CommitParser for this VCS
   */
  CommitParser newCommitParser();
  
  /**
   * Commit changes (git push) and disconnect from remote.
   * <br>
   * Process invocations correctly even if {@link VCSFacade#open()} failed or has not been invoked at all.
   */
  void close();
}
