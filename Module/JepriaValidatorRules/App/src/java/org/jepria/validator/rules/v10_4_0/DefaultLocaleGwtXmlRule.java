package org.jepria.validator.rules.v10_4_0;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.exception.TransformationException;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Добавляет дефолтную локаль ru в gwt.xml-файл debug-конфигурации
 */
public class DefaultLocaleGwtXmlRule extends ValidatorRule {
  
  @Override
  public Transformation forXmlResource(XmlResource resource) {
    if (resource.getPathName().matches("(.+/)?config/debug/.+\\.gwt\\.xml")) {
      Document document = resource.asDocument();
      
      XPath xPath = XPathFactory.newInstance().newXPath();
      Node node;
      try {
        node = (Node)xPath.evaluate("/module/extend-property[@name='locale']",
                document.getDocumentElement(), XPathConstants.NODE);
      } catch (XPathExpressionException e) {
        throw new RuntimeException(e);
      }
      
      if (node == null) {
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Add default locale RU", null));
        
        return Transformation.of().content(new Action<ContentRefactorer>() {
          @Override
          public void perform(ContentRefactorer refactorer) {
            // модификация кода:
            
            Node moduleNode;
            try {
              moduleNode = (Node)xPath.evaluate("/module",
                      document.getDocumentElement(), XPathConstants.NODE);
            } catch (XPathExpressionException e) {
              throw new RuntimeException(e);
            } 
            
            if (moduleNode == null) {
              throw new TransformationException("The file contains no <module> tag");
            }

            Node extendPropertyNode = document.createElement("extend-property");
            ((Element)extendPropertyNode).setAttribute("name", "locale");
            ((Element)extendPropertyNode).setAttribute("values", "ru");
            moduleNode.appendChild(extendPropertyNode);
            
            Node setPropertyNode = document.createElement("set-property");
            ((Element)setPropertyNode).setAttribute("name", "locale");
            ((Element)setPropertyNode).setAttribute("value", "ru");
            moduleNode.appendChild(setPropertyNode);
            
            Node setPropertyFallbackNode = document.createElement("set-property-fallback");
            ((Element)setPropertyFallbackNode).setAttribute("name", "locale");
            ((Element)setPropertyFallbackNode).setAttribute("value", "ru");
            moduleNode.appendChild(setPropertyFallbackNode);
            
            
            // сохраним изменения:
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
    
    return null;
  }
}
