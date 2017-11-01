package org.jepria.validator.rules.v10_3_0;

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
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Добавляет библиотеку ${GROOVY-ALL_LIB} в ветку для отчётов цели war в build.xml
 */
public class GroovyAllBuildXmlRule extends ValidatorRule {
  @Override
  public Transformation forXmlResource(XmlResource resource) {
    if (resource.getName().equals("build.xml")) {
      Document document = resource.asDocument();
      
      XPath xPath = XPathFactory.newInstance().newXPath();
      
      final Node warSequentialNode;
      try {
        warSequentialNode = (Node)xPath.evaluate("/project/target[@name='war']/outofdate/sequential",
            document.getDocumentElement(), XPathConstants.NODE);
      } catch (XPathExpressionException e) {
        throw new RuntimeException(e);
      }
      
      if (warSequentialNode != null) {
        Node warCopyNode;
        try {
          warCopyNode = (Node)xPath.evaluate("./copy[@todir='temp-war/WEB-INF/lib']",
              warSequentialNode, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
          throw new RuntimeException(e);
        }
        
        boolean modified = false;
        
        if (warCopyNode != null) {
          
          // удалим комментарий с ${GROOVY-ALL_LIB} из тега copy
          for (int i = 0; i < warCopyNode.getChildNodes().getLength(); i++) {
            Node node = warCopyNode.getChildNodes().item(i);
            if (node instanceof Comment && ((Comment)node).getTextContent().contains("${GROOVY-ALL_LIB}")) {
              handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Remove the comment with '${GROOVY-ALL_LIB}' at <target war>/outofdate/sequential/copy", null));
              
              // модификация кода:
              
              warCopyNode.removeChild(node);
              modified = true;
              break;
            }
          }
        }
        
        // добавим ${GROOVY-ALL_LIB} в ветку для отчётов в тег copy
        Node warCopyReportNode;
        try {
          warCopyReportNode = (Node)xPath.evaluate("./if[isset/@property='reportfile.exists']/then/copy[@todir='temp-war/WEB-INF/lib']",
              warSequentialNode, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
          throw new RuntimeException(e);
        }
        if (warCopyReportNode != null) {
          Node warCopyReportGroovyAllElement;
          try {
            warCopyReportGroovyAllElement = (Node)xPath.evaluate("./fileset[@file='${GROOVY-ALL_LIB}']",
                warCopyReportNode, XPathConstants.NODE);
          } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
          }
          if (warCopyReportGroovyAllElement == null) {
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Add '${GROOVY-ALL_LIB}' to <target war>/outofdate/sequential/if/then/copy", null));
            
            // модификация кода:
            
            warCopyReportGroovyAllElement = document.createElement("fileset");
            ((Element)warCopyReportGroovyAllElement).setAttribute("file", "${GROOVY-ALL_LIB}");
            warCopyReportNode.appendChild(warCopyReportGroovyAllElement);
            
            modified = true;
          }
        }
        
        if (modified) {
          return Transformation.of().content(new Action<ContentRefactorer>() {
            @Override
            public void perform(ContentRefactorer refactorer) {
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
    }
    
    return null;
  }
}
