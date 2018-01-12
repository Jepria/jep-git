package org.jepria.validator.core.base.resource;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ast.CompilationUnit;

public class JavaResource extends AbstractResource {
  
  public JavaResource(PlainResource resource) {
    super(resource);
  }

  public CompilationUnit asCompilationUnit() {
    
    checkJsniMethods();
    
    return ResourceUtils.asJava(newInputStream());
  }

  
  /**
   * Обход бага: JavaParser удаляет имплементации JSNI-методов, заключенных в последовательности / * - { ... } - * /
   * Данный метод сканирует файл в поисках таких имплементаций и выводит предупреждение в System.out.
   * 
   * @deprecated remove the method with all use cases when the bug gets fixed. 
   */
  @Deprecated
  private void checkJsniMethods() {
    Pattern pattern = Pattern.compile("/\\*\\-\\{");
    try (Scanner sc = new Scanner(newInputStream(), "UTF-8")) {
      int lineNum = 0;
      while (sc.hasNextLine()) {
        lineNum++;
        String line = sc.nextLine();
        Matcher m = pattern.matcher(line);
        int col = 0;
        while(m.find(col)) {
          col = m.start() + 1;
          System.out.println("/WARNING: Found JSNI method implementation at " + getPathName() + ":" + lineNum + ":" + col + ". "
              + "This will be erased by JavaParser (because it is not supported), restore manually after validation.");
        }
      }
    }
  }
}
