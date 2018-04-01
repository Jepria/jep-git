package com.technology.jep.jepriatoolkit.auto.test.creator;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.auto.test.creator.AutoTestConstant.*;
import static com.technology.jep.jepriatoolkit.auto.test.creator.AutoTestDefinition.getAutoTestDefinitionProperty;
import static com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil.convertTemplateToFile;
import static com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil.prepareData;
import static com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil.prepareFormData;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.makeDir;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;
import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

@SuppressWarnings("unchecked")
public class AutoTestStructureCreator extends Task {
  
  /**
   * Атрибут таска. Путь к файлу описания структуры приложения.
   */
  public String applicationStructureFile;
  
  /**
   * Атрибут таска. Флаг, означающий - перезаписывать ли файлы при генерации. 
   * Только для прикладных модулей (по аналогии с isBuild).
   */
  public boolean isOverrideExistsFiles;
  
  /**
   *  Объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения.  
   */
  private Application application;
  
  private List<String> forms = new ArrayList<String>();
  
  /**
   *  Данные для шаблонизатора.
   */
  private Map<String, Object> dataForTemplates = null;
  
  /**
   * Получает данные для шаблонизатора.
   * @return Данные для шаблонизатора.
   */
  private Map<String, Object> getDataForTemplates() {
    return dataForTemplates;
  }
  
  /**
   * Префикс пакета (например: com.technology).
   */
  private String packagePrefix;
  
  /**
   * Пакет проекта.
   */
  private String packageProject;
  
  /**
   * Пакет приложения.
   */
  private String packageApplication;
  
  /**
   * Путь к папке, в которой лежат шаблоны
   */
  private final static String AUTO_TEST_TEMPLATE_PATH = "/com/technology/jep/jepriatoolkit/auto/test/creator/template";
  
  /**
   * Основной метод, который выполняет Task
   */
  @Override
  public void execute() throws BuildException {
    try {
      /*
       * Код ниже для отладки. TODO: продумать общее решение. 
       *
      echoMessage("Begin sleep");
      echoMessage("Time to start remote debug.");
      Thread.sleep(3000);
      echoMessage("End sleep");
       */
      
      // Разбор и проверка applicationStructureFile (по умолчанию, <ApplicationName>Definition.xml)
      ApplicationSettingParser applicationParser = ApplicationSettingParser.getInstance(isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile);
      
      application = applicationParser.getApplication();
      packagePrefix = JepRiaToolkitUtil.packagePrefixToPath(application.getPackagePrefix().toLowerCase());
      packageProject = application.getProjectPackage().toLowerCase();
      packageApplication = application.getName().toLowerCase();
      
      forms = applicationParser.getForms();
      dataForTemplates = prepareData(applicationParser);
      
      applicationParser.notifyAboutAbsentFields();
      //TODO: код выше получился одинаковый для appcreator и testcreator
        
      //переопределяем путь
      ApplicationStructureCreatorUtil.setTemplatePath(AUTO_TEST_TEMPLATE_PATH);
      
      echoMessage(multipleConcat("Create Test Structure for '", packagePrefix, DOT, packageProject, DOT, packageApplication, "' module"));
      createTestFileStructure();
      
      echoMessage("Create Java Classes of Auto");
      createAuto();
      
      echoMessage("Create Java Classes of Auto Test");
      createAutoTest();
      
      echoMessage("Create Java Classes of Auto Test Constant");
      createAutoTestConstant();
      
      echoMessage("Generate XML for Module Auto Test");
      createModuleAutoTestXml();
      
      echoMessage("Generate Data for Module Auto Test");
      createModuleAutoTestData();
      
      echoMessage("Generate test.properties");
      createTestProperties();

    } catch (Exception e) {
      throw new BuildException(e);
    }

  }
  

  /**
   * Создание файловой структуры тестов приложения
   */
  private void createTestFileStructure() {

    makeDir(format(getAutoTestDefinitionProperty(AUTO_DIRECTORY_PROPERTY), packageProject, packageApplication, packagePrefix));
    
    makeDir(format(getAutoTestDefinitionProperty(TEST_DIRECTORY_PROPERTY), packageProject, packageApplication, packagePrefix));

    if (forms.size() == 0) {
      echoMessage(multipleConcat(WARNING_PREFIX, "Tag-parameter '", MODULE_TAG_NAME,
          "' is desirable. To create structure for forms, fill this parameter in ",
          normalizePath(applicationStructureFile), "!"));
    } else {
      for (int i = 0; i < forms.size(); i++) {
        String formName = ((String) forms.get(i)).toLowerCase();
        makeDir(format(getAutoTestDefinitionProperty(AUTO_MODULE_DIRECTORY_PROPERTY),
            packageProject, packageApplication, formName, packagePrefix));
        
        makeDir(format(getAutoTestDefinitionProperty(TEST_MODULE_DIRECTORY_PROPERTY),
            packageProject, packageApplication, formName, packagePrefix));
        
        makeDir(format(getAutoTestDefinitionProperty(MODULE_AUTO_TEST_RESOURCES_DIRECTORY_PROPERTY),
            packageProject, packageApplication, formName, packagePrefix));
      }
    }
  }
  
  /**
   * Создание test.properties, параметры авто-тестов.
   */
  private void createTestProperties() {
    convertTemplateToFile(
        getAutoTestDefinitionProperty(AUTO_TEST_CONSTANT_TEMPLATE_PROPERTY),
        getDataForTemplates(), 
        format(getAutoTestDefinitionProperty(AUTO_TEST_CONSTANT_PATH_TEMPLATE_PROPERTY),
            packageProject, packageApplication, application.getName(), packagePrefix));
  }

  /**
   * Создание класса констант тестов
   */
  private void createAutoTestConstant() {
    convertTemplateToFile(
        getAutoTestDefinitionProperty(TEST_PROPERTIES_TEMPLATE_PROPERTY ),
        getDataForTemplates(), 
        format(getAutoTestDefinitionProperty(TEST_PROPERTIES_PATH_TEMPLATE_PROPERTY)));
  }
  
  /**
   * Создание класса автоматизации (интерфейсов и реализации). <br/> 
   * Создание классов автоматизации модулей, и класса предоставляющих доступ к их объектам.
   */
  private void createAuto() {
    convertTemplateToFile(
        getAutoTestDefinitionProperty(AUTO_TEMPLATE_PROPERTY),
        dataForTemplates,
        format(getAutoTestDefinitionProperty(AUTO_PATH_TEMPLATE_PROPERTY),
            packageProject, packageApplication, application.getName(), packagePrefix));
    convertTemplateToFile(
        getAutoTestDefinitionProperty(AUTO_IMPL_TEMPLATE_PROPERTY),
        dataForTemplates,
        format(getAutoTestDefinitionProperty(AUTO_IMPL_PATH_TEMPLATE_PROPERTY),
            packageProject, packageApplication, application.getName(), packagePrefix));
    
    Map<String, Object> allDataForTemplates = getDataForTemplates();
    List<ModuleInfo> moduleInfos = (List<ModuleInfo>) allDataForTemplates.get(FORMS_TEMPLATE_PARAMETER);
    for (ModuleInfo moduleInfo : moduleInfos) {
      
      String formName = moduleInfo.getFormName();
      convertTemplateToFile(
          getAutoTestDefinitionProperty(MODULE_AUTO_TEMPLATE_PROPERTY),
          prepareFormData(moduleInfo, allDataForTemplates), 
          format(getAutoTestDefinitionProperty(MODULE_AUTO_PATH_TEMPLATE_PROPERTY),
              packageProject, packageApplication, formName.toLowerCase(), formName, packagePrefix),
          isOverrideExistsFiles);
      
      convertTemplateToFile(
          getAutoTestDefinitionProperty(MODULE_AUTO_IMPL_TEMPLATE_PROPERTY),
          prepareFormData(moduleInfo, allDataForTemplates), 
          format(getAutoTestDefinitionProperty(MODULE_AUTO_IMPL_PATH_TEMPLATE_PROPERTY),
              packageProject, packageApplication, formName.toLowerCase(), formName, packagePrefix),
          isOverrideExistsFiles);
    }
  }
  
  /**
   * Создание родительского класса тестов (тестов на уровне приложения). <br/> 
   * Создание классов тестов на уровне модулей приложения.
   */
  private void createAutoTest() {
    convertTemplateToFile(
        getAutoTestDefinitionProperty(AUTO_TEST_TEMPLATE_PROPERTY),
        dataForTemplates,
        format(getAutoTestDefinitionProperty(AUTO_TEST_PATH_TEMPLATE_PROPERTY),
            packageProject, packageApplication, application.getName(), packagePrefix));
    
    Map<String, Object> allDataForTemplates = getDataForTemplates();
    List<ModuleInfo> moduleInfos = (List<ModuleInfo>) allDataForTemplates.get(FORMS_TEMPLATE_PARAMETER);
    for (ModuleInfo moduleInfo : moduleInfos) {
      
      String formName = moduleInfo.getFormName();
      convertTemplateToFile(
          getAutoTestDefinitionProperty(MODULE_AUTO_TEST_TEMPLATE_PROPERTY),
          prepareFormData(moduleInfo, allDataForTemplates), 
          format(getAutoTestDefinitionProperty(MODULE_AUTO_TEST_PATH_TEMPLATE_PROPERTY),
              packageProject, packageApplication, formName.toLowerCase(), formName, packagePrefix),
              isOverrideExistsFiles);
    }
  }

  /** 
   * Создание файлов с входными данными для тестов модулей приложения.
   */
  private void createModuleAutoTestData() {
    echoMessage("    TBD...");
  }
  
  /** 
   * Создание xml для запуска тестов модулей приложения.
   */
  private void createModuleAutoTestXml() {
    
    Map<String, Object> allDataForTemplates = getDataForTemplates();
    List<ModuleInfo> moduleInfos = (List<ModuleInfo>) allDataForTemplates.get(FORMS_TEMPLATE_PARAMETER);
    for (ModuleInfo moduleInfo : moduleInfos) {
      
      String formName = moduleInfo.getFormName();
      convertTemplateToFile(
          getAutoTestDefinitionProperty(MODULE_AUTO_TEST_XML_TEMPLATE_PROPERTY),
          prepareFormData(moduleInfo, allDataForTemplates), 
          format(getAutoTestDefinitionProperty(MODULE_AUTO_TEST_XML_PATH_TEMPLATE_PROPERTY),
              packageProject, packageApplication, formName.toLowerCase(), formName, packagePrefix),
          isOverrideExistsFiles);
    }
  }
  
  /**
   * Устанавливает значение applicationStructureFile из соответствуюего атбируба Task.
   * @param applicationStructureFile
   */
  public void setApplicationStructureFile(String applicationStructureFile) {
    this.applicationStructureFile = applicationStructureFile;
  }

  /**
   * Устанавливает значение isOverrideExistsFiles из соответствуюего атбируба Task. <br/>
   * Приводит значение к boolean.
   * @param isOverrideExistsFiles 
   */
  public void setIsOverrideExistsFiles(String isOverrideExistsFiles) {
    
    this.isOverrideExistsFiles = isEmptyOrNotInitializedParameter(isOverrideExistsFiles) 
        ? false : Boolean.parseBoolean(isOverrideExistsFiles);
    echoMessage("Task parameter - IS_OVERRIDE_EXISTS_FILES: " + this.isOverrideExistsFiles);
  }
}