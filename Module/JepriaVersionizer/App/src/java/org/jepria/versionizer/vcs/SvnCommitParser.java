package org.jepria.versionizer.vcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.versionizer.ReleaseNote;

public class SvnCommitParser implements CommitParser {

  @Override
  public List<ReleaseNote> extractReleaseNotes(CommitFacade commit) {
    String message = commit.getCommitMessage();
    
    List<ReleaseNote> ret = new ArrayList<>();
    
    if (message.length() == 0) {
      return Collections.emptyList();
    }
    
    Matcher m = Pattern.compile("((\\[\\*\\])|(\\[\\+\\])|(\\[\\-\\])|(\\[!\\]))\\s*", Pattern.CASE_INSENSITIVE) //$NON-NLS-1$
        .matcher(message);
    
    String action = null, msg = null;
    
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
        
        ret.add(new ReleaseNote(normalizeAction(action), trimMessage(msg)));
      }
      action = m.group(1);
      
      lastFindEnd = m.end();
      iteration++;
    }
    
    if (!matches) {
      return ret;
    }
    
    // last note
    msg = message.substring(lastFindEnd, message.length());
    
    ret.add(new ReleaseNote(normalizeAction(action), trimMessage(msg)));
    
    return ret;
  }
  
  private static String normalizeAction(String s) {
    if ("[*]".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "[*]";//$NON-NLS-1$
    } else if ("[+]".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "[+]";//$NON-NLS-1$
    } else if ("[-]".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "[-]";//$NON-NLS-1$
    } else if ("[!]".equalsIgnoreCase(s)) {//$NON-NLS-1$
      return "[!]";//$NON-NLS-1$
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
