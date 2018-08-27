package org.jepria.versionizer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jepria.versionizer.ui.CommonUserInterface;
import org.jepria.versionizer.vcs.VCSFacade;

/**
 * Creates {@link Module Modules} for a version-creating scenario
 */
public class ModuleFactory {

  protected final CommonUserInterface ui;
  
  public ModuleFactory(CommonUserInterface ui) {
    this.ui = ui;
  }
  

  // TODO these fields are not semantically related to the class. remove them or move to something VCS-related
  private boolean gitPulled = false;
  private boolean gitPushed = false;
  
  public final Module createCore(String moduleRoot, String targetVersion) {
    return new Module() {
      @Override
      public VCSFacade vcs() {
        return new UserManualGit(title(), root().toString(), ui) {
          @Override
          public void open() {
            if (!gitPulled) {
              super.open();
              gitPulled = true;
            }
          }
  
          @Override
          public void close() {
            if (!gitPushed) {
              super.close();
              gitPushed = true;
            }
          }
        };
      }

      @Override
      public String title() {
        return "Jepria";//TODO extract
      }

      @Override
      public String targetVersion() {
        return targetVersion;
      }

      @Override
      public Path root() {
        return Paths.get(moduleRoot);
      }
      
      @Override
      public TestDriver testDriver() {
        return new UserManualTestDriver(title(), root().toString(), ui);
      }
    };
  }
  
  public final Module createToolkit(String moduleRoot, String targetVersion) {
    return new Module() {
      @Override
      public VCSFacade vcs() {
        return new UserManualGit(title(), root().toString(), ui) {
          @Override
          public void open() {
            if (!gitPulled) {
              super.open();
              gitPulled = true;
            }
          }
  
          @Override
          public void close() {
            if (!gitPushed) {
              super.close();
              gitPushed = true;
            }
          }
        };
      }

      @Override
      public String title() {
        return "JepriaToolkit";//TODO extract
      }

      @Override
      public String targetVersion() {
        return targetVersion;
      }

      @Override
      public Path root() {
        return Paths.get(moduleRoot);
      }
      
      @Override
      public TestDriver testDriver() {
        return new TestDriver() {
          // Toolkit has no tests
          @Override
          public void test() {
          }
        };
      }
    };
  }
  
  public final Module createApplication(String moduleRoot, String targetVersion) {
    return new Module() {
      
      @Override
      public VCSFacade vcs() {
        return new UserManualSvn(title(), root().toString(), ui); 
      }

      @Override
      public String title() {
        return "JepriaShowcase";//TODO extract
      }

      @Override
      public String targetVersion() {
        return targetVersion;
      }

      @Override
      public Path root() {
        return Paths.get(moduleRoot);
      }
      
      @Override
      public TestDriver testDriver() {
        return new UserManualTestDriver(title(), root().toString(), ui);
      }
    };
  }
  
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
}
