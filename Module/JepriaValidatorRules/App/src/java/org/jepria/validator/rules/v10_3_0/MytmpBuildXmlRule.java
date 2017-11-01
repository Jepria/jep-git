package org.jepria.validator.rules.v10_3_0;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * В build.xml цель all-gwt-compile компилирует файлы во временную папку mytmp
 */
public class MytmpBuildXmlRule extends ValidatorRule {
  
  @Override
  public Transformation forXmlResource(XmlResource resource) {
    if (resource.getName().equals("build.xml")) {
      Document document = resource.asDocument();
      
      XPath xPath = XPathFactory.newInstance().newXPath();
      
      final Node allGwtCompileNode;
      try {
        allGwtCompileNode = (Node)xPath.evaluate("/project/target[@name='all-gwt-compile']",
            document.getDocumentElement(), XPathConstants.NODE);
      } catch (XPathExpressionException e) {
        throw new RuntimeException(e);
      }
      
      if (allGwtCompileNode != null) {
      
        boolean modified = false;
        
        NodeList gwtCompilerNodes;
        try {
          gwtCompilerNodes = (NodeList)xPath.evaluate(".//java[@classname='com.google.gwt.dev.Compiler']",
              allGwtCompileNode, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
          throw new RuntimeException(e);
        }
        
        for (int i = 0; i < gwtCompilerNodes.getLength(); i++) {
          Node gwtCompilerNode = gwtCompilerNodes.item(i);
          Node workDirArg;
          try {
            workDirArg = (Node)xPath.evaluate("./arg[@value='-workDir']",
                gwtCompilerNode, XPathConstants.NODE);
          } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
          }
          if (workDirArg == null) {
            // модификация кода:
            
            workDirArg = document.createElement("arg");
            ((Element)workDirArg).setAttribute("value", "-workDir");
            gwtCompilerNode.appendChild(workDirArg);
            
            Node workDirValueArg = document.createElement("arg");
            ((Element)workDirValueArg).setAttribute("value", "./mytmp/");
            gwtCompilerNode.appendChild(workDirValueArg);
            
            Node jvmArg = document.createElement("jvmarg");
            ((Element)jvmArg).setAttribute("value", "-Djava.io.tmpdir=./mytmp/");
            gwtCompilerNode.appendChild(jvmArg);
            
            modified = true;
          }
        }
        
        if (modified) {
          
          // модификация кода:
          
          // добавим элемент <mkdir dir="mytmp"/> в цель all-gwt-compile, если его нет
          Node mkdirNode;
          try {
            mkdirNode = (Node)xPath.evaluate("./mkdir[@dir='mytmp']",
                allGwtCompileNode, XPathConstants.NODE);
            if (mkdirNode != null) {
              // проверим что найденный элемент -- первый в родителе
              Node mkdirNodeAt0 = (Node)xPath.evaluate("./*[name()='mkdir' and @dir='mytmp' and position()=1]",
                  allGwtCompileNode, XPathConstants.NODE);
              if (mkdirNodeAt0 == null) {
                // элемент <mkdir dir="mytmp"/> не на первой позиции (а должен быть на первой), отдадим на откуп человеку
                handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM,
                    "The target \"all-gwt-compile\" must have the element <mkdir dir=\"mytmp\"/> as the first child",
                    null));
              }
            }
          } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
          }
          if (mkdirNode == null) {
            mkdirNode = document.createElement("mkdir");
            ((Element)mkdirNode).setAttribute("dir", "mytmp");
            
            // вставим новый элемент в начало родителя:
            
            Node firstNode;
            try {
              firstNode = (Node)xPath.evaluate("./*[position()=1]",
                  allGwtCompileNode, XPathConstants.NODE);
            } catch (XPathExpressionException e) {
              throw new RuntimeException(e);
            }
            if (firstNode != null) {
              allGwtCompileNode.insertBefore(mkdirNode, firstNode);
            } else {
              allGwtCompileNode.appendChild(mkdirNode);
            }
          }
          
          // добавим элемент <delete dir="mytmp" /> в цель war, если его нет
          Node warSequentialNode;
          try {
            warSequentialNode = (Node)xPath.evaluate("/project/target[@name='war']/outofdate/sequential",
                document.getDocumentElement(), XPathConstants.NODE);
            if (warSequentialNode == null) {
              handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM,
                  "The <target name=\"war\">/<outofdate>/<sequential> path not found in the project, insert element <delete dir=\"mytmp\"/> manually",
                  null));
            }
          } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
          }
          if (warSequentialNode != null) {
            Node deleteNode;
            try {
              deleteNode = (Node)xPath.evaluate("./delete[@dir='mytmp']",
                  warSequentialNode, XPathConstants.NODE);
            } catch (XPathExpressionException e) {
              throw new RuntimeException(e);
            }
            if (deleteNode == null) {
              deleteNode = document.createElement("delete");
              ((Element)deleteNode).setAttribute("dir", "mytmp");
              warSequentialNode.appendChild(deleteNode);
            }
          }
          
          return Transformation.of().content(new Action<ContentRefactorer>() {
            @Override
            public void perform(ContentRefactorer refactorer) {
              // сохраним изменения:
              try {
                OutputFormat format = new OutputFormat(document);
                format.setIndenting(true);
                format.setIndent(2);
                Writer out = new OutputStreamWriter(refactorer.getRefactoringStream(), Charset.forName("UTF-8"));
                XMLSerializer serializer = new XMLSerializer(out, format);
                serializer.serialize(document);
                
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
