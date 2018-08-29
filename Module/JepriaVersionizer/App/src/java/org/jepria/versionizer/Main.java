package org.jepria.versionizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jepria.versionizer.ui.CLUI;
import org.jepria.versionizer.ui.CommonUserInterface;
import org.jepria.versionizer.ui.ModuleConfiguratorUI;
import org.jepria.versionizer.ui.UIModuleConfigurator;

public class Main {

  private final List<String> args;
  
  public static void main(String[] args) throws IOException {
    
    List<String> argList = Arrays.asList(args);
    
    new Main(Collections.unmodifiableList(argList)).run();
  }
  
  private Main(List<String> args) {
    this.args = args;
  }
  
  private void run() {

    CommonUserInterface ui = new CLUI();
    
    Module core = null;
    Module toolkit = null;
    Module application = null;
    
    
    // configure modules specified in command line arguments
    
    // lookup module configurations in cmd args
    int indexOptCore = args.indexOf("--core");
    int indexOptToolkit = args.indexOf("--toolkit");
    int indexOptApplication = args.indexOf("--application");
    
    if (indexOptCore != -1 || indexOptToolkit != -1 || indexOptApplication != -1) {
      // modules are configured through cmd arguments
      if (indexOptCore != -1) {
        core = createModuleFromCmdArgs(indexOptCore, ui);
      }
      if (indexOptToolkit != -1) {
        toolkit = createModuleFromCmdArgs(indexOptToolkit, ui);
      }
      if (indexOptApplication != -1) {
        application = createModuleFromCmdArgs(indexOptApplication, ui);
      }
    }
        
    
    // configure the rest of modules with UI
    if (!args.contains("--no-ui-conf") 
        && (core == null || toolkit == null || application == null) //if there is even one non-configured module
        ) {
      // modules will be configured through ModuleConfigurator
      UIModuleConfigurator configurator = new UIModuleConfigurator(new ModuleConfiguratorUI(), ui);
      
      configurator.onModuleConfigBegin();
      
      if (core == null) {
        core = configurator.configureCore();
      }
      if (toolkit == null) {
        toolkit = configurator.configureToolkit();
      }
      if (application == null) {
        application = configurator.configureApplication();
      }
      
      configurator.onModuleConfigEnd();
      
    }
    
    new BasicScenario(ui, core, toolkit, application).run();
  }
  
  /**
   * 
   * @param optIndex index of option itself in cmd args, e.g. the command line "--core C:\module\core\root 1.0.0 --toolkit C:\module\toolkit\root 1.0.0" contains options at indexes 0 and 3.
   * @param ui
   * @return
   */
  private Module createModuleFromCmdArgs(int optIndex, CommonUserInterface ui) {
    String optName = args.get(optIndex);
    String moduleRoot = args.get(optIndex + 1);
    String targetVersion = args.get(optIndex + 2);
    
    int pathCheckResult = ModuleFactory.checkModulePath(moduleRoot);
    if (pathCheckResult == 0) {
      // OK
    } else if (pathCheckResult == 1) {
      throw new IllegalArgumentException("path [" + moduleRoot + "] does not represent a readable directory");
    } else if (pathCheckResult == 2) {
      throw new IllegalArgumentException("path [" + moduleRoot + "] represents a readable directory, but the directory does not contain a readable child directory 'App'");
    } else if (pathCheckResult == 3) {
      throw new IllegalArgumentException("path [" + moduleRoot + "] path parse/resolve error");
    } else {
      throw new IllegalArgumentException();
    }
    
    ModuleFactory moduleFactory = new ModuleFactory(ui);
    
    if ("--core".equals(optName)) {
      return moduleFactory.createCore(moduleRoot, targetVersion);
    } else if ("--toolkit".equals(optName)) {
      return moduleFactory.createToolkit(moduleRoot, targetVersion);
    } else if ("--application".equals(optName)) {
      return moduleFactory.createApplication(moduleRoot, targetVersion);
    } else {
      throw new IllegalArgumentException("Unknown option: " + optName);
    }
  }
}
