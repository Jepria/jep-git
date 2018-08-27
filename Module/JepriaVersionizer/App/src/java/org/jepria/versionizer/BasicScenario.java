package org.jepria.versionizer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jepria.versionizer.text.Text;
import org.jepria.versionizer.ui.CLI;
import org.jepria.versionizer.ui.CommonUserInterface;
import org.jepria.versionizer.vcs.CommitFacade;
import org.jepria.versionizer.vcs.CommitParser;

/**
 * Basic version-creating scenario: core (Jepria), toolkit (JepriaToolkit) and application (JepriaShowcase) 
 */
public class BasicScenario implements Runnable {

  private final CommonUserInterface ui;

  private final Module core;
  private final Module toolkit;
  private final Module application;
  
  public BasicScenario(CommonUserInterface ui, Module core, Module toolkit, Module application) {
    // TODO throw if any argument nulls
    this.ui = ui;
    this.core = core;
    this.toolkit = toolkit;
    this.application = application;
  }


  @Override
  public void run() {
    
    if (core != null) {
      core.vcs().open();
    }
    if (toolkit != null) {
      toolkit.vcs().open();
    }
    if (application != null) {
      application.vcs().open();
    }

    // do tests
    testCore();
    testApplication();


    // do target versions
    if (core != null) {
      doVersionTarget(core);
    }
    if (toolkit != null) {
      doVersionTarget(toolkit);
    }
    if (application != null) {
      doVersionTarget(application);
    }


    // do post-target versions
    if (core != null) {
      doVersionPostTarget(core);
    }
    if (toolkit != null) {
      doVersionPostTarget(toolkit);
    }
    if (application != null) {
      doVersionPostTarget(application);
    }


    // do binaries
    synchronizeBinaries();
    
    if (core != null) {
      addBinaries(core);
    }
    if (toolkit != null) {
      addBinaries(toolkit);
    }

    
    if (core != null) {
      core.vcs().close();
    }
    if (toolkit != null) {
      toolkit.vcs().close();
    }
    if (application != null) {
      application.vcs().close();
    }

    
    publish();
    
    System.out.println("done!");//TODO
  }
  
  private void testCore() {
    if (core != null) {
      core.testDriver().test();
    }
  }
  
  
  private void testApplication() {
    if (application != null) {
      application.testDriver().test();
      
      if (coreTestNeeded()) {
        testCore();
        testApplication();
      }
    }
  }

  private final Map<Module, List<ReleaseNote>> releaseNotesForModules = new HashMap<>();
  

  /**
   * 
   * @param module not null
   */
  private void doVersionTarget(Module module) {
    // create release notes from commit messages
    List<ReleaseNote> releaseNotes = new ArrayList<>();
    List<CommitFacade> commits =  module.vcs().getCommitsSinceLastVersion();
    CommitParser commitParser = module.vcs().newCommitParser();
    for (CommitFacade commit: commits) {
      releaseNotes.addAll(commitParser.extractReleaseNotes(commit));
    }
    
    releaseNotesForModules.put(module, releaseNotes);
    
    SearchOverviewFilesAction searchOverviewFilesAction = new SearchOverviewFilesAction(module.root(), ui);
    searchOverviewFilesAction.run();
    List<Path> overviews = searchOverviewFilesAction.getOverviews();
    if (overviews != null) {
      final Path overview;
      if (overviews.size() > 1) {
        // TODO not implemented yet: will use only first file, but preferrable to use all, or user selects which to use...
      }
      overview = overviews.iterator().next();
      
      new WriteReleaseNotesAction(overview, releaseNotes, ui, module.targetVersion()).run();
    }
    
    setBuildVersionTarget(module);
    
    build(module);
    
    module.vcs().commitVersion(module.targetVersion());
    
    zip(module);
  }
  
  /**
   * 
   * @param module not null
   */
  private void doVersionPostTarget(Module module) {
    setBuildVersionPostTarget(module);
    
    build(module);
    
    module.vcs().commit("Версия сборки изменена на SNAPSHOT");
  }

  private void synchronizeBinaries() {
    if (core != null || toolkit != null || application != null) {// TODO remove this checking from here (probably to the caller)
      
    //TODO extract action into a separate worker
      CLI.instance.userWait(Text.get("bs_synchronizeBothBinaryRepositories"));//$NON-NLS-1$
    }
  }

  /**
   * Add target version, add SNAPSHOT version, remove previous SNAPSHOT version, commit everything
   * @param module
   */
  private void addBinaries(Module module) {
    String commitComment = "[+] " + module.title() + "-" + module.targetVersion(); //$NON-NLS-1$ //$NON-NLS-2$
  //TODO extract action into a separate worker
    CLI.instance.userWait(String.format(Text.get("bs_addBinariesForTheModuleIntoBothBinaryRepositories"), module.title(), commitComment));//$NON-NLS-1$
  }

  private void publish() {
    if (core != null || toolkit != null || application != null) {// TODO remove this checking from here (probably to the caller)
      
      // title of the post
      List<String> postTitleParts = new ArrayList<>();
      
      for (Module module: new Module[]{core, toolkit, application}) {
        if (module != null) {// TODO bad checking: at the beginning we check for ANY module is not null, then here we check EACH module
          postTitleParts.add(module.title() + " " + module.targetVersion());
        }
      }
      
      final String postTitle = "Выпущены новые версии: " + String.join(" / ", postTitleParts);
      
      
      // body of the post
      StringBuilder postBodySb = new StringBuilder();
      
      for (Module module: new Module[]{core, toolkit, application}) {
        if (module != null) {// TODO bad checking: at the beginning we check for ANY module is not null, then here we check EACH module
          List<ReleaseNote> releaseNotes = releaseNotesForModules.get(module);
          
          if (releaseNotes != null && !releaseNotes.isEmpty()) {
            postBodySb.append('\n');
            postBodySb.append("**").append(module.title()).append("&nbsp;").append(module.targetVersion()).append(':').append("**").append('\n');
            for (ReleaseNote releaseNote: releaseNotes) {
              postBodySb.append("&bull;&nbsp;").append(releaseNote.getAction()).append(":&nbsp;").append(releaseNote.getText()).append('\n');
            }
          }
        }
      }
      
      final String postBody = postBodySb.toString();
      
      //
      
      //TODO extract action into a separate worker
      CLI.instance.userWait(String.format(Text.get("bs_publishOnTheSourceforge"), postTitle, postBody));
    }
  }
  
  private boolean coreTestNeeded() {
    if (core != null) {
    //TODO extract action into a separate worker
      return 0 == CLI.instance.userPick(String.format(Text.get("bs_haveModuleSourcesBeenChangedSinceTheLastBuild"), core.title()), new String[]{"y", "n"});
    } else {
      return false;
    }
  }

  public void setBuildVersionTarget(Module module) {
  //TODO extract action into a separate worker
    CLI.instance.userWait(String.format(Text.get("bs_setBuildPropertiesAndNecessaryDependenciesToTargetVersion"), module.title()));
  }

  public void setBuildVersionPostTarget(Module module) {
  //TODO extract action into a separate worker
    CLI.instance.userWait(String.format(Text.get("bs_setBuildPropertiesAndNecessaryDependenciesToTheNextSnapshot"), module.title()));
  }

  public void build(Module module) {
  //TODO extract action into a separate worker
    CLI.instance.userWait(String.format(Text.get("bs_runAntCleanBuildAndDoc"), module.title()));
  }

  public void zip(Module module) {
  //TODO extract action into a separate worker
    CLI.instance.userWait(String.format(Text.get("bs_createZIPArchiveAndPlaceItIntoTheSharedFolder"), module.title()));
  }

}
