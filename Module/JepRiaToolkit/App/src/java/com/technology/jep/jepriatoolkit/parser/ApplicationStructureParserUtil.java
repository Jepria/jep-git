package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.END_OF_LINE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_JAVA_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.THREAD_POOL_SIZE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPathSupressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getResourceByPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.technology.jep.jepriatoolkit.ApplicationDefinition;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleDeclaration;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.log.Logger;
import com.technology.jep.jepriatoolkit.parser.thread.ModuleParser;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureParserUtil {

  /**
   * Получение ссылки на модуль компиляции для указанного пути к файлу
   * 
   * @param fileNameOrPath  путь до файла  
   * @return модуль компиляции
   * @throws FileNotFoundException 
   * @throws UnsupportedEncodingException 
   */
  public static CompilationUnit getCompilationUnit(String fileNameOrPath) throws FileNotFoundException {
    try {
      if (!isEmpty(fileNameOrPath))
        return JavaParser.parse(new InputStreamReader(new FileInputStream(fileNameOrPath), UTF_8), true);
        } catch (ParseException e) {
            echoMessage(multipleConcat(ERROR_PREFIX, "Check the file '", fileNameOrPath, "'! It contains compilation errors!"));
        } catch (UnsupportedEncodingException e) {
            echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
        } 
        return null;
    }
  
  /**
   * Получение списка методов и их тел для указанного по входному параметру пути модуля компиляци
   * 
   * @param complitionUnitPath    путь до файла модуля компиляции
   * @return список методов модуля компиляции
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static ModuleDeclaration getModuleDeclaration(String complitionUnitPath) throws FileNotFoundException {
    CompilationUnit compilationUnit = getCompilationUnit(complitionUnitPath);
    if (compilationUnit == null) return null;
    
    final ModuleDeclaration module = new ModuleDeclaration(compilationUnit);
    compilationUnit.accept(new VoidVisitorAdapter() {
      @Override
        public void visit(MethodDeclaration m, Object arg) {
        if (m.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
          module.getMethods().add(m);
        }
        super.visit(m, arg);
      }
      @Override
        public void visit(FieldDeclaration f, Object arg) {
        if (f.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
          module.getFields().add(f);
        }
        super.visit(f, arg);
      }
      @Override
        public void visit(ConstructorDeclaration c, Object arg) {
        if (c.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
          module.getConstructors().add(c);
        }
        super.visit(c, arg);
      }
      
      @Override
        public void visit(ImportDeclaration f, Object arg) {
        module.getImports().add(f);
        super.visit(f, arg);
      }
    }, null);
    return module;
  }
  
  /**
   * Получение информации о модуле компиляции, игнорируя исключительные ситуации
   * 
   * @param complitionUnitPath    исследуемый модуль компиляции
   * @return информация о модуле компиляции
   */
  public static ModuleDeclaration getModuleDeclarationSuppressException(String complitionUnitPath){
    try {
      return getModuleDeclaration(complitionUnitPath);
    }
    catch(FileNotFoundException e){}    
    return null;
  }
  
  /**
   * Получение структуры приложения по исходному коду, ориентируясь на ресурсный файл структуры нужного версии JepRia
   * 
   * @param jepRiaVersion        используемая версия JepRia
   * @return структура приложения
   */
  public static Application getApplicationBySourceCode(String jepRiaVersion){
    ResourceBundle resource = ApplicationDefinition.LAST.getResource();
    if (!isEmptyOrNotInitializedParameter(jepRiaVersion)){
//      Pattern p = Pattern.compile("Tag/(\\d+)\\.(\\d+)\\.(\\d+)");
      Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\-SNAPSHOT");
      Matcher m = p.matcher(jepRiaVersion);
      if (m.find()){
        resource = ApplicationDefinition.valueOf(multipleConcat("JEPRIA_", m.group(1))).getResource();
      }
      else {
        ApplicationStructureParserUtil.log(multipleConcat(ERROR_PREFIX, "The Version is '", jepRiaVersion, "' isn't supported!"), null);
        return null;
      }
    }
    
    // Obtain info about application from configuration file application.xml
    // TODO Необходимы изменения в связи с упразднением application.xml.
    String relativeApplicationXmlPath = getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/{2}/{0}/{1}/application.xml", resource);
    String applicationName = getApplicationName(convertPatternInRealPath(relativeApplicationXmlPath));
    String fileName = getApplicationFileName(applicationName);
    if (JepRiaToolkitUtil.isEmpty(fileName)) return null;
        
    String applicationDataSource = DEFAULT_DATASOURCE;
    Application structure = new Application();
    structure.setApplicationFileName(fileName);
    structure.setName(applicationName);
    structure.setDefaultDatasource(applicationDataSource);
    structure.setProjectPackage(extractFileNamesByPattern(relativeApplicationXmlPath).iterator().next());
    // The information about modules will be given from main.gwt.xml
    String mainGwtXmlPropertyPath = convertPatternInRealPath(getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
      multipleConcat(PREFIX_DESTINATION_JAVA_CODE, "/{3}/{0}/{1}/main/{2}.gwt.xml"), resource
    ));
    List<String> moduleNames = getModuleNames(mainGwtXmlPropertyPath);
    structure.setModuleIds(moduleNames);
    int moduleCount = moduleNames.size();
    List<Module> modules = new ArrayList<Module>(moduleCount);
    
    String mainModuleResourcePath = convertPatternInRealPathSupressException(getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
        multipleConcat(PREFIX_DESTINATION_JAVA_CODE, "/{3}/{0}/{1}/main/shared/text/{2}Text_Source.properties"), resource));
    
    String mainModuleResourceEnPath = convertPatternInRealPathSupressException(getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
        multipleConcat(PREFIX_DESTINATION_JAVA_CODE, "/{3}/{0}/{1}/main/shared/text/{2}Text_en.properties"), resource));
    
    ResourceBundle mainModuleResourceBundle = getResourceByPath(mainModuleResourcePath);
    ResourceBundle mainModuleResourceBundleEn = getResourceByPath(mainModuleResourceEnPath);
    
    if (moduleCount > 0) {
      ExecutorService service = null;
      try {  
        service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CompletionService<Module> completionService = new ExecutorCompletionService<Module>(service);
        
        for(String moduleId : moduleNames){
          completionService.submit(new ModuleParser(structure, moduleId, resource, mainModuleResourceBundle, mainModuleResourceBundleEn));
        }
        //now retrieve the futures after computation (auto wait for it)
        int received = 0;
        while (received < moduleCount) {
          modules.add(completionService.take().get());
          received++;
        }
      }
      // important: shutdown your ExecutorService
      catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
      finally {
        if (service != null) {
          service.shutdown();
        }
      }
      
      structure.setModules(new Modules(modules));
    }
    return structure;
  }
  
  private static String getApplicationFileName(String applicationName) {
    return multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, applicationName, APPLICATION_SETTING_FILE_ENDING);
  }
  
  public static void log(String message, String moduleId){
    echoMessage(message);
    Logger.appendMessageToTheEndOfForm(moduleId, message);
  }  
  
  /**
   * Функция записи строки в файл
   * 
   * @param cu      содержимое модуля компиляции
   * @param filePath    путь до файла, в который будет произведена запись содержимого
   */
  public static void saveToFile(CompilationUnit cu, String filePath) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), UTF_8));

      StringTokenizer tokenizer = new StringTokenizer(cu.toString(), END_OF_LINE);

      while (tokenizer.hasMoreTokens()) {
        String content = tokenizer.nextToken();
        writer.print(content);
      }

      writer.close();
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
  }
}
