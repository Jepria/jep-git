package org.jepria.validator.rules;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Вспомогательные методы для реализации прикладных валидационных правил
 */
public class Util {
  
  /**
   * Поиск в {@link TypeDeclaration} метода с заданной сигнатурой
   * @param typeDeclaration тип для поиска в нём методов
   * @param signature сигнатура (название метода с типами параметров).
   * <br>
   * Сигнатура <b>не включает</b> названия аргументов метода и параметры типизации.
   * <br>
   * Например:
   * <pre>
   * "getMainView()" // для поиска метода public IsWidget getMainView()
   * "getPlainClientFactory(String, LoadAsyncCallback)" // для поиска метода public void getPlainClientFactory(String moduleId, final LoadAsyncCallback&lt;PlainClientFactory&lt;PlainEventBus, JepDataServiceAsync&gt;&gt; callback)
   * </pre> 
   * @return метод, если он есть в типе или {@code null}, если такого метода нет
   */
  public static MethodDeclaration getMethodBySignature(TypeDeclaration<?> typeDeclaration, String signature) {
    for (Node child: typeDeclaration.getChildNodes()) {
      if (child instanceof MethodDeclaration) {
        MethodDeclaration method = (MethodDeclaration)child;
        if (signature.equals(method.getSignature().asString())) {
          return method;
        }
      }
    }
    return null;
  }
  
  public static final PrettyPrinter PRETTY_PRINTER = new PrettyPrinter(new PrettyPrinterConfiguration().setIndent("  ")); 
  
  /**
   * Красиво (с отступами в 2 пробела и в кодировке UTF-8) выводит java-Node
   * в выходной поток.
   * @param node java-Node или весь {@link com.github.javaparser.ast.CompilationUnit}
   * @param outputStream
   * @throws IOException 
   */
  public static void prettyPrintJava(Node node, OutputStream outputStream) throws IOException {
    outputStream.write(PRETTY_PRINTER.print(node).getBytes(Charset.forName("UTF-8")));
  }
  
  /**
   * Красиво (с отступами в 2 пробела и в кодировке UTF-8) выводит xml-Document
   * в выходной поток.
   * @param document xml-Document
   * @param outputStream
   * @throws IOException 
   */
  public static void prettyPrintXml(Document document, OutputStream outputStream) throws IOException {
    OutputFormat format = new OutputFormat(document);
    format.setIndenting(true);
    format.setIndent(2);
    Writer out = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
    XMLSerializer serializer = new XMLSerializer(out, format);
    serializer.serialize(document);
  }
  
  /**
   * Является ли тип прикладной модульной клиентской фабрикой
   * (наследует {@code PlainClientFactoryImpl} или {@code StandardClientFactoryImpl})
   */
  public static boolean isPlainClientFactoryImpl(TypeDeclaration<?> typeDeclaration) {
    if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
      ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)typeDeclaration;
      if (!coid.isInterface()) {
        NodeList<ClassOrInterfaceType> extendedTypes = coid.getExtendedTypes();
        for (ClassOrInterfaceType extType: extendedTypes) {
          if (extType.getNameAsString().equals("PlainClientFactoryImpl") ||
              extType.getNameAsString().equals("StandardClientFactoryImpl")) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Является ли тип прикладной главной клиентской фабрикой
   * (наследует {@code MainClientFactoryImpl})
   */
  public static boolean isMainClientFactoryImpl(TypeDeclaration<?> typeDeclaration) {
    if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
      ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)typeDeclaration;
      if (!coid.isInterface()) {
        NodeList<ClassOrInterfaceType> extendedTypes = coid.getExtendedTypes();
        for (ClassOrInterfaceType extType: extendedTypes) {
          if (extType.getNameAsString().equals("MainClientFactoryImpl")) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Является ли тип реализацией gwt-сервиса
   * (наследует {@code JepDataServiceServlet})
   */
  public static boolean isGwtServiceImpl(TypeDeclaration<?> typeDeclaration) {
    if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
      ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)typeDeclaration;
      if (!coid.isInterface()) {
        NodeList<ClassOrInterfaceType> extendedTypes = coid.getExtendedTypes();
        for (ClassOrInterfaceType extType: extendedTypes) {
          if (extType.getNameAsString().equals("JepDataServiceServlet")) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Упрощает поиск узлов по XPATH
   * @param root корневой узел для поиска.  
   * @param xpath
   * @return unmodifiable list
   * @throws RuntimeException в случае XPathExpressionException
   */
  public static List<org.w3c.dom.Node> getNodesByXpath(org.w3c.dom.Node root, String xpath) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    
    try {
      org.w3c.dom.NodeList nodeList = (org.w3c.dom.NodeList)xPath.evaluate(xpath,
          root, XPathConstants.NODESET);
      
      return Collections.unmodifiableList(new AbstractList<org.w3c.dom.Node>() {
        @Override
        public org.w3c.dom.Node get(int index) {
          return nodeList.item(index);
        }
        @Override
        public int size() {
          return nodeList.getLength();
        }
      });
    } catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }
}
