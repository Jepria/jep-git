package org.jepria.validator.rules.v10_6_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Правило проверяет, что в web.xml в welcome-file-list есть указание унифицированной страницы Entry.jsp.
 * Если нет, то выдает предупреждение.
 * Не производит автоматических трансформаций. 
 */
public class EntryJspRule extends ValidatorRule {
  @Override
  public Transformation forXmlResource(XmlResource resource) {
    
    if ("web.xml".equals(resource.getName())) {
      Document doc = resource.asDocument();
      
      List<Node> welcomeFileList = Util.getNodesByXpath(doc, "/web-app/welcome-file-list/welcome-file");
      
      if (welcomeFileList.size() == 1) {
        String welcomeFileName = welcomeFileList.get(0).getTextContent().trim();
        if (!"Entry.jsp".equals(welcomeFileName)) {
          handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
              "Is the main JSP file [" + welcomeFileName + "] really custom? "
              + "If not, remove it from the application resources (the file will be copied from the system resources), "
              + "and specify the only \"<welcome-file>Entry.jsp</welcome-file>\" in tag <web-app>/<welcome-file-list>", null));
        }
      } else if (welcomeFileList.size() > 1) {
        List<String> welcomeFileNames = new ArrayList<String>();
        boolean entryJspFound = false;
        for (Node welcomeFile: welcomeFileList) {
          String welcomeFileName = welcomeFile.getTextContent().trim(); 
          if ("Entry.jsp".equals(welcomeFileName)) {
            entryJspFound = true;
            break;
          }
          welcomeFileNames.add(welcomeFileName);
        }
        if (!entryJspFound) {
          handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
              "Is each of the main JSP files " + Arrays.toString(welcomeFileNames.toArray()) + " really custom? "
              + "If one is not, remove it from the application resources (the file will be copied from the system resources), "
              + "and specify \"<welcome-file>Entry.jsp</welcome-file>\" for it in tag <web-app>/<welcome-file-list>", null));
        }
      }
    }
    
    return null;
  }
}
