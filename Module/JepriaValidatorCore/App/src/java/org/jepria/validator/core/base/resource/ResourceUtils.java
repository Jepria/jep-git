package org.jepria.validator.core.base.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Утилиты для работы с ресурсами
 */
public class ResourceUtils {
  
  /**
   * Преобразует поток в {@link CompilationUnit} и закрывает поток.
   */
  public static CompilationUnit asJava(InputStream inputStream) {
    CompilationUnit unit = JavaParser.parse(inputStream);// TODO specify charset
    try {
      inputStream.close();
      return unit;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Преобразует поток в {@link Document} и закрывает поток.
   */
  public static Document asXml(InputStream inputStream) {
    try {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);//TODO where to specify charset?
      inputStream.close();
      return document;
    } catch (SAXException | IOException | ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }
}
