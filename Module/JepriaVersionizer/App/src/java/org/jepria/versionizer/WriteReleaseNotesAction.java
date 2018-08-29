package org.jepria.versionizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import org.jepria.versionizer.ReleaseNotesProcessor.ReleaseNotesBlockNotFoundException;

public class WriteReleaseNotesAction implements Runnable {
  
  private final Path overview;
  private final List<ReleaseNote> releaseNotes;
  private final UserInterface ui;
  private final String targetVersion;
  
  /**
   * Simple, low-level (uses primitive types for communication) user interface
   */
  public interface UserInterface {
    void wrn_msgReleaseNotesBlockNotFound(String overviewPath);
    
    /**
     * When no releaseNotes block found in the overview file,
     * ask whether to create it manually or to skip writing release notes for the module
     * @param overviewPath
     * @return 1: user creates releaseNotes block manually; else: skip writing release notes for the module
     */
    int wrn_optReleaseNotesBlockManualCreate(String overviewPath);
    
    /**
     * Request (and wait) the user to create Release Notes Block manually in the overview file (just an empty block)
     */
    void wrn_reqReleaseNotesBlockManualCreate(String overviewPath);
    
    /**
     * Message error writing release notes into the overview file
     */
    void wrn_msgError(String overviewPath, String throwableClass, String throwableMessage);
    
    void wrn_msgReleaseNotesExist(String targetVersion, String overviewPath);
    
    /**
     * When release notes for the target version already exist in the overview file,
     * ask whether to auto-update existing release notes, to update them manually or to skip writing release notes for the module 
     * @param targetVersion
     * @param overviewPath
     * @return 1: auto-update existing release notes for the target version;
     * 2: user writes release notes manually; else: skip writing release notes for the module
     */
    int wrn_optReleaseNotesExistUpdate(String targetVersion, String overviewPath);
    void wrn_reqReleaseNotesExistWriteManually(String targetVersion, String overviewPath);
    void wrn_msgAddReleaseNotesSuccess(String targetVersion, String overviewPath);
    
    /**
     * When error occured,
     * ask whether to recover the error manually or skip writing release notes to the file
     * @return 1: recover manually; else: skip writing release notes to the file
     */
    int wrn_optRecoverErrorOrSkip();
    
    /**
     * Request (and wait) the user to recover the error manually
     */
    void wrn_reqRecoverError();
  }
  
  public WriteReleaseNotesAction(Path overview, List<ReleaseNote> releaseNotes, 
      UserInterface ui, String targetVersion) {
    this.overview = overview;
    this.releaseNotes = releaseNotes;
    this.ui = ui;
    this.targetVersion = targetVersion;
  }

  private ReleaseNotesProcessor rnProcessor;
  
  @Override
  public void run() {
    while (!writeReleaseNotes());
  }
  
  /**
   * @return {@code false} if need to invoke again, {@code true} if search complete
   */
  private boolean writeReleaseNotes() {
    
    // for displaying to the UI
    final String overviewPath = overview.toString();
    
    try {
    
      rnProcessor = new ReleaseNotesProcessor(releaseNotes, targetVersion);
    
      while (true) {
        
        try (InputStream in = new FileInputStream(overview.toFile())) {
          
          rnProcessor.parse(in);
          break;
        
        } catch (ReleaseNotesBlockNotFoundException e) {
          
          ui.wrn_msgReleaseNotesBlockNotFound(overviewPath);
          
          int picked = ui.wrn_optReleaseNotesBlockManualCreate(overviewPath);
          
          if (picked == 1) {
            ui.wrn_reqReleaseNotesBlockManualCreate(overviewPath);
          }
        }
      };
      
      
      //////////
      
      
      boolean updateExistingReleaseNotes = false;
      
      if (rnProcessor.hasExistingReleaseNotes()) {
        ui.wrn_msgReleaseNotesExist(targetVersion, overviewPath);
        
        int picked = ui.wrn_optReleaseNotesExistUpdate(targetVersion, overviewPath);
        
        if (picked == 1) {
          // update existing automatically
          updateExistingReleaseNotes = true;
          
        } else if (picked == 2) {
          // user updates existing manually
          ui.wrn_reqReleaseNotesExistWriteManually(targetVersion, overviewPath);
          return true;
          
        } else {
          // skip
          return true;
        }
      }
      
      rnProcessor.addReleaseNotes(updateExistingReleaseNotes);
      
      try (OutputStream out = new FileOutputStream(overview.toFile())) {
        rnProcessor.save(out);
      }
      
      ui.wrn_msgAddReleaseNotesSuccess(targetVersion, overview.toString());
    
      return true;
          
    } catch (Throwable e) {
      ui.wrn_msgError(overviewPath, e.getClass().getName(), e.getMessage());
      
      e.printStackTrace();//TODO
      
      int opt = ui.wrn_optRecoverErrorOrSkip();
      if (opt == 1) {
        ui.wrn_reqRecoverError();
        return false;
        
      } else {
        return true;
      }
      
      //TODO ask to continue or wait until user fixes and then repeat.. 
    }
  }
}
