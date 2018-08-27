package org.jepria.versionizer;

public class ReleaseNote {
  
  private final String action;
  private final String text;
  
  public ReleaseNote(String action, String text) {
    this.action = action;
    this.text = text;
  }

  public String getAction() {
    return action;
  }

  public String getText() {
    return text;
  }
}
