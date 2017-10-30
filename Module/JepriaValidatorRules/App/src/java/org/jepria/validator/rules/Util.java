package org.jepria.validator.rules;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.w3c.dom.Document;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
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
  
}
