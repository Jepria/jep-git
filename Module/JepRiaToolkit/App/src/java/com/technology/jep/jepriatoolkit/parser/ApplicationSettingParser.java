package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_DATASOURCE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PROJECT_PACKAGE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.THREAD_POOL_SIZE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDOM;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.technology.jep.jepriatoolkit.creator.application.thread.FormParser;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.util.Pair;

public class ApplicationSettingParser {

  // объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения
  private Application application;
  // Ссылка на <ApplicationName>Definition.xml
  private ApplicationStructureDocument document = null;
  private List<String> forms = new ArrayList<String>();
  private List<List<String>> formWithTheirDependencies = new ArrayList<List<String>>();
  private List<String> securityRoles = new ArrayList<String>();
  private Map<Module, List<ModuleField>> formFields = new HashMap<Module, List<ModuleField>>();
  
  private ApplicationSettingParser(){}
  
  public static ApplicationSettingParser getInstance() throws ParserConfigurationException {
    return getInstance(getApplicationDefinitionFile());
  }
  
  public static ApplicationSettingParser getInstance(String applicationStructureFile, Boolean... isPossibleAnotherFile) throws ParserConfigurationException {
    ApplicationSettingParser parser = new ApplicationSettingParser();
    if (parser.parseApplicationSettingXML(applicationStructureFile, isPossibleAnotherFile)){
      return parser;
    }
    throw new ParserConfigurationException("Parsing problems");
  }
  
  /**
   * Разбор и проверка <ApplicationName>Definition.xml
   * 
   * @return флаг успешности/неуспешности парсинга <ApplicationName>Definition.xml
   * @throws ParserConfigurationException
   */
  public boolean parseApplicationSettingXML(String applicationStructureFile, Boolean... isPossibleAnotherFile) throws ParserConfigurationException {
    applicationStructureFile = isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile;
    echoMessage(multipleConcat("Parsing ", normalizePath(applicationStructureFile), "..."));
    ExecutorService service = null;
    try {
      document = new ApplicationStructureDocument(getDOM(applicationStructureFile));
      application = new Application();
      Element applicationNode = document.getElementByTagName(APPLICATION_TAG_NAME);
      if (isEmpty(applicationNode)) {
        echoMessage(multipleConcat(ERROR_PREFIX,
            "Application setting XML is not correct! There is no mandatory tag 'application'!"));
        return false;
      }
      String packageName = applicationNode.getAttribute(PROJECT_PACKAGE_ATTRIBUTE);
      application.setProjectPackage(packageName);
      if (isEmpty(application.getProjectPackage())) {
        echoMessage(multipleConcat(ERROR_PREFIX,
            "Application setting XML is not correct! There is no mandatory attribute '", PROJECT_PACKAGE_ATTRIBUTE,
            "' of tag 'application'!"));
        return false;
      }
      String moduleName = applicationNode.getAttribute(APPLICATION_NAME_ATTRIBUTE);
      application.setName(moduleName);
      if (isEmpty(moduleName)) {
        echoMessage(multipleConcat(ERROR_PREFIX,
            "Application setting XML is not correct! There is no mandatory attribute '", APPLICATION_NAME_ATTRIBUTE,
            "' of tag 'application'!"));
        return false;
      }
      else if (isPossibleAnotherFile.length == 1 && isPossibleAnotherFile[0] ? false : !moduleName.equals(new File(applicationStructureFile).getName().split(APPLICATION_SETTING_FILE_ENDING)[0])){
        echoMessage(multipleConcat(ERROR_PREFIX,
            "Application setting XML is not correct! The attribute '", APPLICATION_NAME_ATTRIBUTE, "'='", moduleName,
            "' doesn't match the file name '", applicationStructureFile, "'!"));
        return false;
      }
      String defaultDataSource = applicationNode.getAttribute(APPLICATION_DATASOURCE_ATTRIBUTE);
      application.setDefaultDatasource(defaultDataSource);
      if (isEmpty(defaultDataSource)) {
        defaultDataSource = DEFAULT_DATASOURCE;
      }

      // инициализация списка форм с их потомками
      forms = document.getAllModuleNodes();
      application.setModuleIds(forms);
      int moduleCount = forms.size();
      List<Module> modules = new ArrayList<Module>(moduleCount);
      
      if (moduleCount > 0) {
        service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        CompletionService<Pair<Module, List<ModuleField>>> completionService = new ExecutorCompletionService<Pair<Module, List<ModuleField>>>(service);
        
        for(String moduleId : forms){
          completionService.submit(new FormParser(application, document, moduleId, defaultDataSource));
        }
        //now retrieve the futures after computation (auto wait for it)
        int received = 0;
        Set<String> roles = new HashSet<String>();
        while (received < moduleCount) {
          Future<Pair<Module, List<ModuleField>>> resultFuture = null;
          try {
            resultFuture = completionService.take();
            Pair<Module, List<ModuleField>> pair = resultFuture.get(); 
            Module m = pair.getKey();
            modules.add(m);
            // добавляем соответствие модуля его списку полей
            formFields.put(m, pair.getValue());
            // заполнение общего списка ролей, необходимого для генерации web.xml
            if (m.getModuleRoleNames() != null) {
              roles.addAll(m.getModuleRoleNames());
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          }
          received++;
        }
        
        this.securityRoles = new ArrayList<String>(roles);
        
        // поиск зависимостей у модулей
        for (int i = 0; i < forms.size(); i++) {
          List<String> nodesWithChildren = document.getNodesWithChildren(forms.get(i));
          if (!isEmpty(nodesWithChildren)){
            formWithTheirDependencies.add(nodesWithChildren);
          }
        }
      }
      // определим зависимости между модулями
      modules = prepareModulesToInheritance(modules);
      application.setModules(new Modules(modules));
      return true;
    } catch (IOException e) {
      echoMessage(multipleConcat(ERROR_PREFIX, "File ", applicationStructureFile,
          " is not found!! Fill and put it in Application Directory!"));
      return false;
    } catch (SAXException e) {
      echoMessage(multipleConcat(ERROR_PREFIX, "File ", applicationStructureFile,
          " is not valid!! Check and correct it!"));
      return false;
    }
    finally {
      // important: shutdown your ExecutorService
      if (service != null){
        service.shutdown();
      }
    }
  }

  public List<Module> prepareModulesToInheritance(List<Module> modules) {
    List<Module> preparedModules = new ArrayList<Module>(modules);
    for (Module module : modules) {
      preparedModules = prepareModuleToInheritance(module, preparedModules);
    }
    return preparedModules;
  }
  
  public List<Module> prepareModuleToInheritance(Module mod, List<Module> allModules){
    String moduleId = mod.getModuleId();
    List<String> dependentForms = getDependencyNodesIfExists(moduleId);
    if (!isEmpty(dependentForms)) {
      List<Module> dependentModules = new ArrayList<Module>(dependentForms.size());
      for (Iterator<Module> iter = allModules.iterator(); iter.hasNext();){
        Module module = iter.next();
        String newModuleId = module.getModuleId();
        if (dependentForms.contains(newModuleId)){
          dependentModules.add(module);
          iter.remove();
        }
      }
      mod.setChildModules(dependentModules);
    }
    return new ArrayList<Module>(allModules);
  }
  
  /**
   * Функция, определяющая является ли форма зависимой. Если да, то от какой
   * 
   * @param formName наименование проверяемой на зависимость формы
   * @return наименование главной формы
   */
  public String getMainFormNameIfExist(String formName) {
    for (int index = 0; index < formWithTheirDependencies.size(); index++) {
      List<String> list = formWithTheirDependencies.get(index);
      String mainFormName = list.get(0);
      for (int j = 1; j < list.size(); j++) {
        String dependentForm = list.get(j);
        if (dependentForm.equals(formName))
          return mainFormName;
      }

    }
    return null;
  }
  
  
  
  /**
   * Получение списка зависимых узлов (форм) для требуемой
   * 
   * @param formName требуемая форма
   * @return список имеющихся зависимостей
   */
  public List<String> getDependencyNodesIfExists(String formName) {
    List<String> result = new ArrayList<String>();
    for (int index = 0; index < formWithTheirDependencies.size(); index++) {
      List<String> list = formWithTheirDependencies.get(index);
      String mainFormName = list.get(0);
      if (mainFormName.equals(formName)) {
        result = list.subList(1, list.size());
        break;
      }
    }
    return result;
  }
  
  /**
   * Оповещение о полях, присутствующих на детальной или списочной формах, но
   * не указанных в секции &lt;record&gt;
   */
  public void notifyAboutAbsentFields() {
    for (int i = 0; i < forms.size(); i++) {
      String formName = forms.get(i);
      Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
      List<ModuleField> moduleFields = hm.values().iterator().next();
      List<String> absentFields = document.getListOfAbsentFieldNameByModuleId(formName, moduleFields);
      if (!absentFields.isEmpty()) {
        echoMessage(multipleConcat(WARNING_PREFIX, "Field", (absentFields.size() > 1 ? "s" : ""), WHITE_SPACE, Arrays.toString(absentFields.toArray()), WHITE_SPACE, 
            (absentFields.size() > 1 ? "are" : "is"), " absent in 'record' section for module with ID : '", formName, "'"));
      }
    }
  }
  
  /**
   * Получение хэшмэпа модуля и списка его полей по идентификатору
   * 
   * @param moduleId идентификатор модуля
   * @return хэшмэп модуля и списка полей
   */
  public Map<Module, List<ModuleField>> getModuleWithFieldsById(String moduleId) {
    Map<Module, List<ModuleField>> hm = new HashMap<Module, List<ModuleField>>();

    for (Module module : formFields.keySet()) {
      if (module.getModuleId().equalsIgnoreCase(moduleId)) {
        hm.put(module, formFields.get(module));
        break;
      }
    }
    return hm;
  }
  
  public Application getApplication() {
    return application;
  }
  
  public List<String> getForms(){
    return forms;
  }

  public List<String> getRoles() {
    return securityRoles;
  }
  
  public ApplicationStructureDocument getDocument(){
    return document;
  }
}
