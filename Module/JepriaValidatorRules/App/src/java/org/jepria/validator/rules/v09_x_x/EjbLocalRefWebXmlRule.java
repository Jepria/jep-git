package org.jepria.validator.rules.v09_x_x;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Удаляет все элементы ejb-local-ref из web.xml
 */
public class EjbLocalRefWebXmlRule extends ValidatorRule {
  
  @Override
  public Transformation forXmlResource(XmlResource resource) {
    
    if (resource.getName().equals("web.xml")) {
      // Удаляет все элементы ejb-local-ref из web.xml
      
      Document document = resource.asDocument();
      
      XPath xPath = XPathFactory.newInstance().newXPath();
      
      final Node webAppNode;
      
      try {
        webAppNode = (Node)xPath.evaluate("/web-app",
            document.getDocumentElement(), XPathConstants.NODE);
      } catch (XPathExpressionException e) {
        throw new RuntimeException(e);
      }
      
      if (webAppNode != null) {
        final NodeList ejbLocalRefNodes;
        
        try {
          ejbLocalRefNodes = (NodeList)xPath.evaluate("./ejb-local-ref",
              webAppNode, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
          throw new RuntimeException(e);
        }
        
        if (ejbLocalRefNodes != null && ejbLocalRefNodes.getLength() > 0) {
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Remove " + ejbLocalRefNodes.getLength() + " ejb-local-ref elements", null));
          
          return Transformation.of().content(new Action<ContentRefactorer>() {
            @Override
            public void perform(ContentRefactorer refactorer) {
              // модификация кода:
              for (int i = 0; i < ejbLocalRefNodes.getLength(); i++) {
                webAppNode.removeChild(ejbLocalRefNodes.item(i));
              }
              
              // сохранение изменений:
              try {
                Util.prettyPrintXml(document, refactorer.getRefactoringStream());
                handleMessage(new Message("Transformation succeeded"));
              } catch (IOException e) {
                handleThrowable(e);
              }
            }
          });
        }
        
      }
    }
    
    return null;
  }
}
