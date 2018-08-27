package org.jepria.versionizer.ui;

import org.jepria.versionizer.ModuleFactory;
import org.jepria.versionizer.text.Text;

public class ModuleConfiguratorUI implements UIModuleConfigurator.UserInterface {

  private final CLI cli = CLI.instance;
  
  @Override
  public void mc_msgModuleConfigBegin() {
    // NO-OP
  }
  
  @Override
  public void mc_msgModuleConfigEnd() {
    // NO-OP
  }
  
  private String[] coreModuleConfig = null;
  
  @Override
  public String[] mc_reqCoreModuleConfig() {
    if (coreModuleConfig == null) {
      coreModuleConfig = reqModuleConfig("Jepria");// TODO extract
    }
    return coreModuleConfig;
  }
  
  private String[] toolkitModuleConfig = null;
  
  @Override
  public String[] mc_reqToolkitModuleConfig() {
    if (toolkitModuleConfig == null) {
      toolkitModuleConfig = reqModuleConfig("JepriaToolkit");// TODO extract 
    }
    return toolkitModuleConfig;
  }
  
  private String[] applicationModuleConfig = null;
  
  @Override
  public String[] mc_reqApplicationModuleConfig() {
    if (applicationModuleConfig == null) {
      applicationModuleConfig = reqModuleConfig("JepriaShowcase");// TODO extract
    }
    return applicationModuleConfig;
  }
  
  /**
   * 
   * @param moduleTitle a module being configured
   * @return array[2] or <code>null</code> if no need to create a version of the module.<br/>
   * array[0]: module root path;<br/>
   * array[1]: module target version.<br/>
   */
  private String[] reqModuleConfig(String moduleTitle) {
    String[] ret = null;
    
    int picked = cli.userPick(String.format(Text.get("mc_willModuleBeAPartOfVersion"), moduleTitle), //$NON-NLS-1$ //$NON-NLS-2$
        new String[]{"y", "n"}); //$NON-NLS-1$ //$NON-NLS-2$
    
    if (picked == 0) {
      ret = new String[2];
      cli.userMessage(String.format(Text.get("mc_enterModuleRootPath"), moduleTitle) + ":"); //$NON-NLS-1$ //$NON-NLS-2$
      
      String moduleRoot;
      
      while (true) {
        moduleRoot = cli.readUserInput();
        
        int pathCheckResult = ModuleFactory.checkModulePath(moduleRoot);
        if (pathCheckResult != 0) {
          errModulePathInvalid(moduleTitle, moduleRoot, pathCheckResult);
        } else {
          break;
        }
      }
      
      ret[0] = moduleRoot;
      
      cli.userMessage(String.format(Text.get("mc_enterTargetVersionForModule"), moduleTitle) + ":"); //$NON-NLS-1$ //$NON-NLS-2$
      ret[1] = cli.readUserInput();
    }
    
    return ret;
  }
  
  private void errModulePathInvalid(String moduleTitle, String moduleRoot, int pathCheckResult) {
    switch (pathCheckResult) {
    case 1: {
      cli.userMessage(moduleTitle + ": " + String.format(Text.get("mc_pathDoesNotRepresentADirectoryTryAnother"), moduleRoot)); //$NON-NLS-1$ //$NON-NLS-2$
      break;
    }
    case 2: {
      cli.userMessage(moduleTitle + ": " + String.format(Text.get("mc_pathDoesNotRepresentADirectoryWithAChildDirectoryAppTryAnother"), moduleRoot)); //$NON-NLS-1$ //$NON-NLS-2$
      break;
    }
    case 3: {
      cli.userMessage(moduleTitle + ": " + String.format(Text.get("mc_pathCannotBeParsedTryAnother"), moduleRoot)); //$NON-NLS-1$ //$NON-NLS-2$
      break;
    }
    }
  }
  
}
