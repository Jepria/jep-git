package org.jepria.versionizer.ui;

import org.jepria.versionizer.Module;
import org.jepria.versionizer.ModuleFactory;

public class UIModuleConfigurator {
  
  public interface UserInterface {
    
    void mc_msgModuleConfigBegin();
    
    /**
     * @return array[2].<br/>
     * array[0]: path to Jepria module root;<br/>
     * array[1]: Jepria target version.<br/>
     * <br/>
     * or <code>null</code> if no need to create core version.
     */
    String[] mc_reqCoreModuleConfig();
    
    /**
     * @return array[2].<br/>
     * array[0]: <b>already valid</b> path to JepriaToolkit module root;<br/>
     * array[1]: JepriaToolkit target version.<br/>
     * <br/>
     * or <code>null</code> if no need to create core version.
     */
    String[] mc_reqToolkitModuleConfig();
    
    /**
     * @return array[2].<br/>
     * array[0]: path to JepriaShowcase module root;<br/>
     * array[1]: JepriaShowcase target version.<br/>
     * <br/>
     * or <code>null</code> if no need to create core version.
     */
    String[] mc_reqApplicationModuleConfig();
    
    void mc_msgModuleConfigEnd();
  }
  
  private final ModuleFactory moduleFactory;
  
  private final UserInterface ui;
  
  /**
   * 
   * @param userInterface UI for configuring modules
   * @param commonUserInterface basic UI for creating Modules with
   */
  public UIModuleConfigurator(UserInterface userInterface, CommonUserInterface commonUserInterface) {
    this.ui = userInterface;
    moduleFactory = new ModuleFactory(commonUserInterface);
  }
  
  public void onModuleConfigBegin() {
    ui.mc_msgModuleConfigBegin();
  }
  
  public void onModuleConfigEnd() {
    ui.mc_msgModuleConfigEnd();
  }
  
  public Module configureCore() {
    String[] moduleConfig = ui.mc_reqCoreModuleConfig();
    if (moduleConfig != null) {
      return moduleFactory.createCore(moduleConfig[0], moduleConfig[1]);
    } else {
      return null;
    }
  }
  
  public Module configureToolkit() {
    String[] moduleConfig = ui.mc_reqToolkitModuleConfig();
    if (moduleConfig != null) {
      return moduleFactory.createToolkit(moduleConfig[0], moduleConfig[1]);
    } else {
      return null;
    }
  }
  
  public Module configureApplication() {
    String[] moduleConfig = ui.mc_reqApplicationModuleConfig();
    if (moduleConfig != null) {
      return moduleFactory.createApplication(moduleConfig[0], moduleConfig[1]);
    } else {
      return null;
    }
  }
  
}
