package org.jepria.versionizer.ui;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jepria.versionizer.text.Text;

/**
 * Agent that delegates every step to be done manually by the user, through Command Line Interface 
 */
public class CLUI implements CommonUserInterface {

  private final CLI cli = CLI.instance;
  

  /**
   * Checks whether a String represents a correct {@link Path} for a module root 
   * @return 0: success<br/>
   * 1: path does not represent a readable directory<br/>
   * 2: path represents a readable directory, but the directory does not contain a readable child directory 'App'<br/>
   * 3: path parse/resolve error
   */
  public static int checkModulePath(String path) {
    try {
      Path path0 = Paths.get(path);
      if (!Files.isDirectory(path0)) {
        return 1;
      } else {
        File[] appChilds = path0.toFile().listFiles(file -> file.isDirectory() && "App".equals(file.getName())); //$NON-NLS-1$
        if (appChilds != null && appChilds.length > 0) {
          return 0;
        } else {
          return 2;
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();//TODO
      
      return 3;
    }
  }
  
  @Override
  public void sof_msgError(String throwbaleClass, String throwableMessage) {
    cli.userMessage(Text.get("sof_msgError") + ":\n  " + throwbaleClass + ": " + throwableMessage); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  @Override
  public void sof_msgOverviewFilesFoundNone() {
    cli.userMessage(Text.get("sof_msgOverviewFilesFoundNone")); //$NON-NLS-1$
  }
  
  @Override
  public void sof_msgOverviewFilesFoundSingle(String overviewPath) {
    cli.userMessage(String.format(Text.get("sof_msgOverviewFilesFoundSingle"), overviewPath)); //$NON-NLS-1$
  }
  
  @Override
  public void sof_msgOverviewFilesFoundMultiple(List<String> overviewPaths) {
    StringBuilder overviewPathsSb = new StringBuilder();
    for (String overviewPath: overviewPaths) {
      overviewPathsSb.append(overviewPath).append('\n');
    }
    cli.userMessage(Text.get("sof_msgOverviewFilesFoundMultiple") + ":\n" + overviewPathsSb.toString() + //$NON-NLS-1$  //$NON-NLS-2$
        "STUB (not implemented yet): will use only first."// TODO
        );
    
  }
  
  @Override
  public int sof_optRecoverErrorOrSkip() {
    return 1 + cli.userPick(Text.get("optRecoverErrorOrSkip"), //$NON-NLS-1$ 
        new String[]{"r", "s"}); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  @Override
  public void sof_reqRecoverError() {
    cli.userWait(Text.get("reqRecoverError")); //$NON-NLS-1$
  }
  
  
  @Override
  public int sof_optOverviewAutoGenerate(String overviewPath) {
    return 1 + cli.userPick(String.format(Text.get("sof_optOverviewAutoGenerate"), overviewPath), //$NON-NLS-1$ 
        new String[]{"a", "m", "s"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  @Override
  public void sof_reqGenerateOverviewManually() {
    cli.userWait(Text.get("sof_reqGenerateOverviewManually")); //$NON-NLS-1$
  }
  
  @Override
  public void wrn_msgError(String overviewPath, String throwbaleClass, String throwableMessage) {
    cli.userMessage(String.format(Text.get("wrn_msgError"), overviewPath) + ":\n " + throwbaleClass + ": " + throwableMessage);  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  
  
  @Override
  public void wrn_msgReleaseNotesBlockNotFound(String overviewPath) {
    cli.userMessage(String.format(Text.get("wrn_msgReleaseNotesBlockNotFound"), overviewPath)); //$NON-NLS-1$
  }
  
  @Override
  public int wrn_optReleaseNotesBlockManualCreate(String overviewPath) {
    return 1 + cli.userPick(String.format(Text.get("wrn_optReleaseNotesBlockManualCreate"), overviewPath), //$NON-NLS-1$
        new String[]{"m", "s"}); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  @Override
  public void wrn_reqReleaseNotesBlockManualCreate(String overviewPath) {
    cli.userWait(String.format(Text.get("wrn_reqReleaseNotesBlockManualCreate"), overviewPath)); //$NON-NLS-1$
  }
  
  
  @Override
  public void wrn_msgReleaseNotesExist(String targetVersion, String overviewPath) {
    cli.userMessage(String.format(Text.get("wrn_msgReleaseNotesExist"), targetVersion, overviewPath)); //$NON-NLS-1$
  }
  
  @Override
  public int wrn_optReleaseNotesExistUpdate(String targetVersion, String overviewPath) {
    return 1 + cli.userPick(String.format(Text.get("wrn_optReleaseNotesExistUpdate"), overviewPath), //$NON-NLS-1$
        new String[]{"a", "m", "s"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  @Override
  public void wrn_reqReleaseNotesExistWriteManually(String targetVersion, String overviewPath) {
    cli.userWait(String.format(Text.get("wrn_reqReleaseNotesExistWriteManually"), targetVersion,  overviewPath)); //$NON-NLS-1$
  }
  
  @Override
  public void wrn_msgAddReleaseNotesSuccess(String targetVersion, String overviewPath) {
    cli.userMessage(String.format(Text.get("wrn_msgAddReleaseNotesSuccess"), targetVersion, overviewPath)); //$NON-NLS-1$
  }
  
  @Override
  public int wrn_optRecoverErrorOrSkip() {
    return 1 + cli.userPick(Text.get("optRecoverErrorOrSkip"), //$NON-NLS-1$ 
        new String[]{"r", "s"}); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  @Override
  public void wrn_reqRecoverError() {
    cli.userWait(Text.get("reqRecoverError")); //$NON-NLS-1$
  }
  
  @Override
  public String[] vcsSvn_obtainCommitMessagesSinceTheLastVersion(String moduleTitle, String moduleRoot) {
    return vcs_obtainCommitMessagesSinceTheLastVersion(moduleTitle, moduleRoot);
  }
  
  @Override
  public String[] vcsGit_obtainCommitMessagesSinceTheLastVersion(String moduleTitle, String moduleRoot) {
    return vcs_obtainCommitMessagesSinceTheLastVersion(moduleTitle, moduleRoot);
  }
  
  public String[] vcs_obtainCommitMessagesSinceTheLastVersion(String moduleTitle, String moduleRoot) {

    cli.userMessage(String.format(Text.get("vcs_obtainCommitMessagesSinceTheLastVersion"), moduleTitle, moduleTitle, moduleRoot)); //$NON-NLS-1$ 

    List<String> messages = new ArrayList<>();

    while (true) {
      final String message = cli.readUserInput();
      if ("end".equals(message)) { //$NON-NLS-1$
        break;
      }
      if (message != null && message.length() > 0) {
        messages.add(message);
      }
    }
    
    return messages.toArray(new String[messages.size()]);
  }
  
  @Override
  public void vcsGit_commitVersion(String moduleTitle, String moduleRoot, String versionTag) {
    cli.userWait(String.format(Text.get("vcsGit_commitVersion"), moduleTitle, moduleRoot, moduleTitle, versionTag)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsGit_commit(String moduleTitle, String moduleRoot, String commitMessage) {
    cli.userWait(String.format(Text.get("vcsGit_commit"), moduleTitle, moduleRoot, moduleTitle, commitMessage)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsSvn_commitVersion(String moduleTitle, String moduleRoot, String versionTag) {
    cli.userWait(String.format(Text.get("vcsSvn_commitVersion"), versionTag, moduleTitle, moduleRoot, versionTag)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsSvn_commit(String moduleTitle, String moduleRoot, String commitMessage) {
    cli.userWait(String.format(Text.get("vcsSvn_commit"), moduleTitle, moduleRoot, commitMessage)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsSvn_synchronize(String moduleTitle, String moduleRoot) {
    cli.userWait(String.format(Text.get("vcsSvn_synchronize"), moduleTitle, moduleRoot)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsGit_pull(String moduleTitle, String moduleRoot) {
    cli.userWait(String.format(Text.get("vcsGit_pull"), moduleTitle, moduleRoot)); //$NON-NLS-1$
  }
  
  @Override
  public void vcsGit_push(String moduleTitle, String moduleRoot) {
    cli.userWait(String.format(Text.get("vcsGit_push"), moduleTitle, moduleRoot)); //$NON-NLS-1$
  }
  
  @Override
  public void td_reqTest(String moduleTitle, String moduleRoot) {
    cli.userWait(String.format(Text.get("td_reqTest"), moduleTitle)); //$NON-NLS-1$
  }
}
