package org.jepria.versionizer.ui;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Singleton
 */
public class CLI {
  
  private final PrintStream out;
  
  private final Scanner sc;
  
  private CLI() {
    this(System.in, System.out);
  }
  
  private CLI(InputStream in, OutputStream out) {
    this(in, new PrintStream(out));
  }
  
  private CLI(InputStream in, PrintStream out) {
    this.out = out;
    this.sc = new Scanner(in);
  }
  
  // TODO make completely synchronized singleton
  public static final CLI instance = new CLI();
  
  
  /**
   * Read a line of user's input
   */
  public String readUserInput() {
    addLineBeforeNextMessage = false;
    return sc.nextLine();
  }

  boolean addLineBeforeNextMessage = false;
  
  /**
   * Show a message to the user
   */
  public void userMessage(Object message) {
    if (message != null) {
      String messageAsString = message.toString();
      if (messageAsString.length() > 0) {
        if (addLineBeforeNextMessage) {
          out.println();
        }
        
        out.println(messageAsString);
        
        addLineBeforeNextMessage = true;
      }
    }
  }
  
  /**
   * Wait for the user's confirmation
   */
  public void userWait(Object message) {
    userMessage(message);
    out.println("[when done, type anything]");
    readUserInput();
  }
  
  /**
   * Proposes the user to pick one of options.
   * @param subject Prompt or message to show to the user
   * @param optionTags exact strings which the user types in to pick an option, non null.
   * @return index of the option picked, in the optionTags array
   */
  public int userPick(Object subject, String[] optionTags) {
    
    if (optionTags == null) {
      throw new NullPointerException("optionTags");
    }
    
    if (subject != null) {
      String subjectAsString = subject.toString();
      if (subjectAsString.length() > 0) {
        out.println(subjectAsString);
      }
    }
    
    StringBuilder optionsTagsSb = new StringBuilder();
    
    for (int i = 0; i < optionTags.length; i++) {
      if (i > 0) {
        if (i == optionTags.length - 1) {
          optionsTagsSb.append(" or ");
        } else {
          optionsTagsSb.append(", ");
        }
      }
      optionsTagsSb.append('\'').append(optionTags[i]).append('\'');
    }

    
    out.println(String.format("[type %s to pick an option]", optionsTagsSb.toString()));
    
    String pick = readUserInput();
    
    // find tag by user input
    int pickTagIndex = -1;
    for (int i = 0; i < optionTags.length; i++) {
      if (pick.equals(optionTags[i])) {
        pickTagIndex = i;
        break;
      }
    }
    
    if (pickTagIndex == -1) {
      // unknown tag
      out.println(String.format("Unknown input: %s", pick));
      // fallback: repeat
      return userPick(subject, optionTags);
    } 
    
    return pickTagIndex;
  }
}
