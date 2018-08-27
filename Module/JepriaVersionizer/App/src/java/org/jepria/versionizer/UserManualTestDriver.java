package org.jepria.versionizer;

public class UserManualTestDriver implements TestDriver {
  
  public interface UserInterface {
    void td_reqTest(String moduleTitle, String moduleRoot);
  }
  
  
  private final String moduleTitle, moduleRoot;
  private final UserInterface ui;
  
  public UserManualTestDriver(String moduleTitle, String moduleRoot, UserInterface ui) {
    this.moduleTitle = moduleTitle;
    this.moduleRoot = moduleRoot;
    this.ui = ui;
  }



  @Override
  public void test() {
    ui.td_reqTest(moduleTitle, moduleRoot);
  }

}
