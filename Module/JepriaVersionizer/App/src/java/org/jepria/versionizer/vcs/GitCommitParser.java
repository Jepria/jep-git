package org.jepria.versionizer.vcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.versionizer.ReleaseNote;

public class GitCommitParser implements CommitParser {

  @Override
  public List<ReleaseNote> extractReleaseNotes(CommitFacade commit) {
    String message = commit.getCommitMessage();
    
    List<ReleaseNote> ret = new ArrayList<>();
    
    if (message.length() == 0) {
      return Collections.emptyList();
    }
    
    Matcher m = Pattern.compile("((feat)|(feature)|(fix)|(doc)|(docs)|(style)|(refactor)|(test)|(chore)|(maintain))\\s*\\(([^\\)]+)\\)", Pattern.CASE_INSENSITIVE) //$NON-NLS-1$
        .matcher(message);
    
    String action = null, scope = null, msg = null;
    
    int lastFindEnd = 0;
    int iteration = 0;
    
    boolean matches = false;
    
    while (m.find()) {
      matches = true;
      
      if (iteration == 0 && m.start() > 0) {
        // initial note
        msg = message.substring(0, m.start());
        String trimmedMsg = trimMessage(msg);
        if (trimmedMsg != null && trimmedMsg.length() > 0) {
          
          // TODO what means action is null?
          ret.add(new ReleaseNote(null, trimmedMsg));
        }
      }
      
      msg = message.substring(lastFindEnd, m.start());
      if (iteration > 0) {
        // intermediate notes

        if (checkScope(scope)) {
          ret.add(new ReleaseNote(normalizeAction(action), trimMessage(msg)));
        }
      }
      action = m.group(1);
      scope = m.group(12);
      
      lastFindEnd = m.end();
      iteration++;
    }
    
    if (!matches) {
      return ret;
    }
    
    // last note
    msg = message.substring(lastFindEnd, message.length());
    
    if (checkScope(scope)) {
      ret.add(new ReleaseNote(normalizeAction(action), trimMessage(msg)));
    }
    
    return ret;
  }
  
  /**
   * Checks whether the commit's scope belongs to a particular module.
   * Used for creating release notes for commits with proper scope only.
   */
  protected boolean checkScope(String scope) {
    return true;
  }
  
  private static String normalizeAction(String s) {
    if ("feat".equalsIgnoreCase(s) || "feature".equalsIgnoreCase(s)) {//$NON-NLS-1$ //$NON-NLS-2$
      return "feat";//$NON-NLS-1$
    } else if ("fix".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "fix";//$NON-NLS-1$
    } else if ("doc".equalsIgnoreCase(s) || "docs".equalsIgnoreCase(s)) {//$NON-NLS-1$ //$NON-NLS-2$
      return "doc";//$NON-NLS-1$
    } else if ("style".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "style";//$NON-NLS-1$
    } else if ("refactor".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "refactor";//$NON-NLS-1$
    } else if ("test".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "test";//$NON-NLS-1$
    } else if ("chore".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "chore";//$NON-NLS-1$
    } else if ("maintain".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "maintain";//$NON-NLS-1$
    } else {
      return null;
    }
  }
  
  private static String trimMessage(String message) {
    if (message == null) {
      return null;
    }
    
    int start = 0;
    while (start < message.length() && trimChar(message.charAt(start))) {
      start++;
    }
    int end = message.length() - 1;
    while (start < end && trimChar(message.charAt(end))) {
      end--;
    }
    
    return message.substring(start, end + 1);
  }
  
  private static boolean trimChar(char c) {
    return c == ';' ||
        c == ':' ||
        c == '.' ||
        c == ',' ||
        c == ' ';
  }
}
