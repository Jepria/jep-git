package org.jepria.versionizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SearchOverviewFilesAction implements Runnable {
  
  private final Path moduleRoot;
  private final UserInterface ui;
  
  /**
   * Simple, low-level (uses primitive types for communication) user interface
   */
  public interface UserInterface {
    
    void sof_msgError(String throwableClass, String throwableMessage);
    
    /**
     * Message no overview files found
     */
    void sof_msgOverviewFilesFoundNone();
    
    /**
     * When overview.html not found in the module,
     * ask whether to auto-generate overview file, create manually or skip searching overviews in the module
     * @param overviewPath suggested path for generating/creating missing overview file
     * @return 1: auto-generate; 2: user creates overview file manually; else: skip searching overviews in the module
     */
    int sof_optOverviewAutoGenerate(String overviewPath);
    
    /**
     * Request (and wait) the user to create the overview file manually
     */
    void sof_reqGenerateOverviewManually();
    
    /**
     * Message found single overview file
     */
    void sof_msgOverviewFilesFoundSingle(String overviewPath);
    
    /**
     * Message found multiple overview files
     */
    void sof_msgOverviewFilesFoundMultiple(List<String> overviewPaths);
    
    /**
     * When error occured,
     * ask whether to recover the error manually or skip searching overviews in the module
     * @return 1: recover manually; else: skip searching overviews in the module
     */
    int sof_optRecoverErrorOrSkip();
    
    /**
     * Request (and wait) the user to recover the error manually
     */
    void sof_reqRecoverError();
  }
  
  
  public SearchOverviewFilesAction(Path moduleRoot, UserInterface ui) {
    this.moduleRoot = moduleRoot;
    this.ui = ui;
  }

  /**
   * overview.html files found in the module
   */
  private List<Path> overviews;

  public List<Path> getOverviews() {
    return overviews;
  }

  @Override
  public void run() {
    while (!searchOverviews());
  }
  
  /**
   * @return {@code false} if need to invoke again, {@code true} if search complete
   */
  private boolean searchOverviews() {
    try {
      Path moduleApp = moduleRoot.resolve("App");
      
      overviews = Files.walk(moduleApp)
          .filter(path -> path.getFileName().toString().equals("overview.html")) //$NON-NLS-1$
          .collect(Collectors.toList());
      
      if (overviews == null || overviews.size() == 0) {
        // no overview files found
        ui.sof_msgOverviewFilesFoundNone();
        
        String path = moduleRoot.resolve(Paths.get("App", "src", "java", "overview.html")).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  
        // ask to auto-generate overview file or create manually
        int picked = ui.sof_optOverviewAutoGenerate(path);
        
        if (picked == 1) {
          System.out.println("STUB (not implemented yet): Auto-generate overview.html...");//TODO
          
          return false;
          
        } else if (picked == 2) {
          ui.sof_reqGenerateOverviewManually();
  
          return false;
          
        } else {
          return true;
          
        }
        
      } else if (overviews.size() == 1) {
        // found single overview file
        ui.sof_msgOverviewFilesFoundSingle(overviews.get(0).toString());
        
        return true;
        
      } else {
        // found multiple overview file
        ui.sof_msgOverviewFilesFoundMultiple(overviews.stream().map(path -> path.toString()).collect(Collectors.toList()));
        
        return true;
      }
      
    } catch (Throwable e) {
      ui.sof_msgError(e.getClass().getName(), e.getMessage());
      
      e.printStackTrace();//TODO
      
      int opt = ui.sof_optRecoverErrorOrSkip();
      if (opt == 1) {
        ui.sof_reqRecoverError();
        return false;
        
      } else {
        return true;
      }
    }
  }
}
