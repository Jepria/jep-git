package org.jepria.versionizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * Adds particular list of {@link ReleaseNote}s into the overview file.
 * <br><br>
 * Invoke {@link #parse()}, then {@link #addReleaseNotes(boolean)}, then {@link #save(OutputStream)}
 */
public class ReleaseNotesProcessor {
  
  private final List<ReleaseNote> releaseNotes;
  private final String targetVersion;
  
  /**
   * @param releaseNotes release notes for the target version to add
   * @param targetVersion target version to mention, e.g. {@code 10.2.0}
   */
  public ReleaseNotesProcessor(List<ReleaseNote> releaseNotes, String targetVersion) {
    this.releaseNotes = releaseNotes;
    this.targetVersion = targetVersion;
  }
  
  
  private class ReleaseNotesBody {
    Element h5;
    Element ul;
  }
  
  /**
   * Existing release notes headers for the target version
   */
  private final List<Element> existingRnHeaders = new ArrayList<>();
  /**
   * Existing release notes bodies for the target version
   */
  private final List<ReleaseNotesBody> existingRnBodies = new ArrayList<>();
  
  /**
   * 
   * @return whether the overview already contains Release Notes Header or Body for the target version
   */
  public boolean hasExistingReleaseNotes() {
    return !existingRnHeaders.isEmpty() || !existingRnBodies.isEmpty();
  }
  
  private Document overviewDoc;
  
  public static class ReleaseNotesBlockNotFoundException extends Exception {
    private static final long serialVersionUID = -1402098991882005591L;
  }
  
  private Element ulReleaseNotesHeaders = null;
  
  private boolean parseSuccess = false;
  
  /**
   * Add releaseNotes into a single overview html
   * <br>
   * <b>Does not close the stream</b> after return
   * 
   * @param inputStream the original overview.html stream to add release notes to
   * @throws IOException 
   */
  public void parse(InputStream inputStream) throws IOException, ReleaseNotesBlockNotFoundException {
    
    parseSuccess = false;
    
    overviewDoc = Jsoup.parse(inputStream, "UTF-8", ""); //$NON-NLS-1$ //$NON-NLS-2$
    
    // Storage for (the first) h3 which has child <a name="releaseNotes">
    Element h3ReleaseNotes = null;
    
    for (Element h3: overviewDoc.select("h3")) { //$NON-NLS-1$
      Element a0 = h3.selectFirst("h3 > a[name=\"releaseNotes\"]"); //$NON-NLS-1$
      if (a0 != null) {
        // this h3 is "releaseNotes"
        h3ReleaseNotes = h3;
        break;
      }
    }
    
    if (h3ReleaseNotes == null) {
      throw new ReleaseNotesBlockNotFoundException();
    }
    
    
    // find ul for release note headers
    Element elem = h3ReleaseNotes;
    while (true) {
      Element next = elem.nextElementSibling(); 
      if (next == null || 
          "h3".equals(next.tagName()) || //$NON-NLS-1$
          "h2".equals(next.tagName()) || //$NON-NLS-1$
          "h1".equals(next.tagName())) { //$NON-NLS-1$
        
        // XXX dangerous self-deciding code!
        
        // existing ul not found, create new
        ulReleaseNotesHeaders = new Element("ul"); //$NON-NLS-1$
        elem.after(ulReleaseNotesHeaders);
        break;
        
      } else if ("ul".equals(next.tagName())) { //$NON-NLS-1$
        
        // found existing ul
        ulReleaseNotesHeaders = next;
        break;
        
      }
      
      elem = next;
    }
    
    collectExistingRnHeaders(ulReleaseNotesHeaders);
    
    collectExistingRnBodies(overviewDoc);
    
    parseSuccess = true;
  }
  
  
  /**
   * @param updateExistingReleaseNotes whether or not to update the existing Release Notes for the target version (if any)
   * 
   */
  public void addReleaseNotes(boolean updateExistingReleaseNotes) {
    
    if (!parseSuccess) {
      throw new IllegalStateException("The overview has not been successfully parsed yet"); //$NON-NLS-1$
    }
    
    // auto-update:
    
    if (updateExistingReleaseNotes) {
      // remove all existing elements
      existingRnHeaders.forEach(li -> {li.remove();});
      
      existingRnBodies.forEach(rnBody -> {
        if (rnBody.h5 != null) {
          rnBody.h5.remove();
        }
        if (rnBody.ul != null) {
          rnBody.ul.remove();
        }
      });
    }
    
    
    // create new elements
    Element headerLi = new Element("li"); //$NON-NLS-1$
    ulReleaseNotesHeaders.insertChildren(0, headerLi);
    headerLi.html(String.format("<a href=\"#%s\">%s (%tF)</a>", targetVersion, targetVersion, new Date())); //$NON-NLS-1$
    
    Element bodyH5 = new Element("h5"); //$NON-NLS-1$
    ulReleaseNotesHeaders.after(bodyH5);
    bodyH5.html(String.format("<a name=\"%s\">%s (%tF)</a>", targetVersion, targetVersion, new Date())); //$NON-NLS-1$
    
    Node releaseNotesBodyNode = createReleaseNotesBodyNode(releaseNotes);
    if (releaseNotesBodyNode != null) {
      bodyH5.after(releaseNotesBodyNode);
    }
  }
  
  public void save(OutputStream outputStream) {
    if (overviewDoc == null) {
      return;
    }
    
    String str = overviewDoc.toString(); 
    
    // TODO: after save manually fix HTML bugs
    {
      str = str.replaceAll("@Override", "{@literal @}Override"); //$NON-NLS-1$ //$NON-NLS-2$
    }
        
    try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"))) { //$NON-NLS-1$
      pw.print(str);
    } catch (UnsupportedEncodingException e) {
      // impossible
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Collects existing release notes headers (lis within the ul) for the target version
   * @param releaseNotesUl
   */
  private void collectExistingRnHeaders(Element releaseNotesUl) {
    // pattern: <li><a href="#10.8.0">10.8.0 (2018-03-22)</a></li>
    for (Element li: releaseNotesUl.select("ul > li")) { //$NON-NLS-1$
      Element a = li.selectFirst("li > a:matchesOwn(" + targetVersion + " .*)"); //$NON-NLS-1$ //$NON-NLS-2$
      if (a != null) {
        existingRnHeaders.add(li);
      }
    }
  }
  
  /**
   *  Collects existing release notes bodies (h5 + ul) for the target version 
   */
  private void collectExistingRnBodies(Document overviewDoc) {
    // pattern: <h5><a name="10.8.0">10.8.0 (2018-03-22)</a></h5>
    for (Element h5: overviewDoc.select("h5")) { //$NON-NLS-1$
      Element a = h5.selectFirst("h5 > a[name=\"" + targetVersion + "\"]"); //$NON-NLS-1$ //$NON-NLS-2$
      if (a == null) {
        a = h5.selectFirst("h5 > a:matchesOwn(" + targetVersion + " .*)"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      
      if (a != null) {
        
        ReleaseNotesBody rnBody = new ReleaseNotesBody();
        rnBody.h5 = h5;
        
        Element bodyUl = h5.nextElementSibling();
        if (bodyUl != null && "ul".equals(bodyUl.tagName())) { //$NON-NLS-1$
          rnBody.ul = bodyUl;
        }
        
        existingRnBodies.add(rnBody);
      }
    }
  }
  
  
  /**
   * 
   * @param releaseNotes
   * @return null if no body must be created (i.e. no commit messages present)
   */
  protected Node createReleaseNotesBodyNode(List<ReleaseNote> releaseNotes) {
    Node node = null;
    if (releaseNotes != null && !releaseNotes.isEmpty()) {
      Element ul = new Element("ul"); //$NON-NLS-1$
      for (ReleaseNote releaseNote: releaseNotes) {
        Element li = new Element("li"); //$NON-NLS-1$
        
        String action = releaseNote.getAction();
        String text = releaseNote.getText();
        String liHtml = (action != null ? ("<b>" + action + ":</b> ") : "") + (text != null ? text : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        
        if (liHtml.length() > 0) {
          li.html(liHtml);
          ul.appendChild(li);
        }
      }

      node = ul;
    }
    return node;
  }
}
