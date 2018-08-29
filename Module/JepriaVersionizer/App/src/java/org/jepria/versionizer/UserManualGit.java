package org.jepria.versionizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jepria.versionizer.vcs.CommitFacade;
import org.jepria.versionizer.vcs.CommitParser;
import org.jepria.versionizer.vcs.GitCommitParser;
import org.jepria.versionizer.vcs.SimpleCommit;
import org.jepria.versionizer.vcs.VCSFacade;

/**
 * Git VCS that delegates every action to be done manually by the user 
 */
public class UserManualGit implements VCSFacade {
  
  private final String moduleTitle, moduleRoot;
  private final UserInterface ui;
  
  public interface UserInterface {
    String[] vcsGit_obtainCommitMessagesSinceTheLastVersion(String moduleTitle, String moduleRoot);
    void vcsGit_commitVersion(String moduleTitle, String moduleRoot, String versionTag);
    void vcsGit_commit(String moduleTitle, String moduleRoot, String commitMessage);
    void vcsGit_pull(String moduleTitle, String moduleRoot);
    void vcsGit_push(String moduleTitle, String moduleRoot);
  }
  
  public UserManualGit(String moduleTitle, String moduleRoot, UserInterface ui) {
    this.moduleTitle = moduleTitle;
    this.moduleRoot = moduleRoot;
    this.ui = ui;
  }

  @Override
  public List<CommitFacade> getCommitsSinceLastVersion() {
    String[] commitMessages = ui.vcsGit_obtainCommitMessagesSinceTheLastVersion(moduleTitle, moduleRoot);
    
    if (commitMessages != null && commitMessages.length > 0) {
      return Arrays.stream(commitMessages).map(message -> new SimpleCommit(message)).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public void commitVersion(String versionTag) {
    ui.vcsGit_commitVersion(moduleTitle, moduleRoot, versionTag);
  };

  @Override
  public void commit(String commitMessage) {
    ui.vcsGit_commit(moduleTitle, moduleRoot, commitMessage);
  }

  @Override
  public CommitParser newCommitParser() {
    return new GitCommitParser() {
      @Override
      protected boolean checkScope(String scope) {
        return moduleTitle.equalsIgnoreCase(scope);
      }
    };
  }

  @Override
  public void open() {
    ui.vcsGit_pull(moduleTitle, moduleRoot);
  }

  @Override
  public void close() {
    ui.vcsGit_push(moduleTitle, moduleRoot);
  }
}
