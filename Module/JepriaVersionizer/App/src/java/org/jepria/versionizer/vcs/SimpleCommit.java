package org.jepria.versionizer.vcs;

public class SimpleCommit implements CommitFacade {

  private final String message;
  
  public SimpleCommit(String message) {
    this.message = message;
  }
  
  @Override
  public String getCommitMessage() {
    return message;
  }

}
