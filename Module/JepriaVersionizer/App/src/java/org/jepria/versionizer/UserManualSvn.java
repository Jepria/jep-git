package org.jepria.versionizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jepria.versionizer.vcs.CommitFacade;
import org.jepria.versionizer.vcs.CommitParser;
import org.jepria.versionizer.vcs.SimpleCommit;
import org.jepria.versionizer.vcs.SvnCommitParser;
import org.jepria.versionizer.vcs.VCSFacade;

/**
 * Svn VCS that delegates every action to be done manually by the user 
 */
public class UserManualSvn implements VCSFacade {
  
  private final String moduleTitle, moduleRoot;
  private final UserInterface ui;
  
  public interface UserInterface {
    String[] vcsSvn_obtainCommitMessagesSinceTheLastVersion(String moduleTitle, String moduleRoot);
    void vcsSvn_commitVersion(String moduleTitle, String moduleRoot, String versionTag);
    void vcsSvn_commit(String moduleTitle, String moduleRoot, String commitMessage);
    void vcsSvn_synchronize(String moduleTitle, String moduleRoot);
  }
  
  public UserManualSvn(String moduleTitle, String moduleRoot, UserInterface ui) {
    this.moduleTitle = moduleTitle;
    this.moduleRoot = moduleRoot;
    this.ui = ui;
  }

  @Override
  public List<CommitFacade> getCommitsSinceLastVersion() {
    String[] commitMessages = ui.vcsSvn_obtainCommitMessagesSinceTheLastVersion(moduleTitle, moduleRoot);
    
    if (commitMessages != null && commitMessages.length > 0) {
      return Arrays.stream(commitMessages).map(message -> new SimpleCommit(message)).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public void commitVersion(String versionTag) {
    ui.vcsSvn_commitVersion(moduleTitle, moduleRoot, versionTag);
  };

  @Override
  public void commit(String commitMessage) {
    ui.vcsSvn_commit(moduleTitle, moduleRoot, commitMessage);
  }

  @Override
  public CommitParser newCommitParser() {
    return new SvnCommitParser();
  }

  @Override
  public void open() {
    ui.vcsSvn_synchronize(moduleTitle, moduleRoot);
  }

  @Override
  public void close() {
    // NO-OP
  };
}
