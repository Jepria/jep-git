package com.technology.jep.jepriatoolkit.creator.application;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.Forms;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class ApplicationStructureCreatorUtil {
  
  private static Configuration cfg = null;
  
  /**
   * Путь к папке с шаблонами. <br/>
   * TODO: некрасиво сделано переопределение.
   */
  private static String templatePath = "/com/technology/jep/jepriatoolkit/creator/template"; 
  
  /**
   * Получает путь к папке с шаблонами.
   * @return путь к папке с шаблонами.
   */
  public static String getTemplatePath() {
    return templatePath;
  }

  /**
   * Устанавливает путь к папке с шаблонами.
   * @param templatePath Путь к папке с шаблонами.
   */
  public static void setTemplatePath(String templatePath) {
    ApplicationStructureCreatorUtil.templatePath = templatePath;
  }

  /**
   * Получение конфигурации для шаблонов
   * 
   * @return конфигурация шаблонизатора
   */
  public static Configuration getTemplateConfiguration(){
    if (cfg == null) {
      // Create your Configuration instance, and specify if up to what FreeMarker
      // version (here 2.3.22) do you want to apply the fixes that are not 100%
      // backward-compatible. See the Configuration JavaDoc for details.
      cfg = new Configuration(Configuration.VERSION_2_3_23);
  
      // Specify the source where the template files come from. Here I set a
      // plain directory for it, but non-file-system sources are possible too:
      cfg.setClassForTemplateLoading(ApplicationStructureCreatorUtil.class, getTemplatePath());
      // Set the preferred charset template files are stored in. UTF-8 is
      // a good choice in most applications:
      cfg.setDefaultEncoding(UTF_8);
      //charset of the output
      cfg.setOutputEncoding(UTF_8);
      //default locale
        cfg.setLocale(Locale.US);
      // Sets how errors will appear.
      // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }
    return cfg;
  }
  
  /**
   * Конвертация шаблона на основании данных. <br/>
   * Добавлен для обратной совместимости, - см. аналогичный метод с 4 параметрами.
   * 
   * @param templateFile    Файл шаблона.
   * @param dataToMap       Данные для мэппинга.
   * @param resultFilePath  Путь результирующего файла.
   */
  public static void convertTemplateToFile(String templateFile, Map<String, Object> dataToMap, String resultFilePath) {
    convertTemplateToFile(templateFile, dataToMap, resultFilePath, Boolean.TRUE);
  }
  
  /**
   * Конвертация шаблона на основании данных
   * 
   * @param templateFile    Файл шаблона.
   * @param dataToMap       Данные для мэппинга.
   * @param resultFilePath  Путь результирующего файла.
   * @param isOverride      Флаг, если true, то перезаписывает итоговый файл, иначе нет.
   */
  public static void convertTemplateToFile(String templateFile, Map<String, Object> dataToMap, String resultFilePath, 
      Boolean isOverride) {
    
    File resultFile = new File(resultFilePath);
    if(Boolean.FALSE.equals(isOverride) && resultFile.exists()) {
      echoMessage(multipleConcat("Try to override ", resultFilePath, ". Canceled!"));
      return; //Если файл существует и не стоит флаг перезаписи, то завершаем работу.
    }
    
    try {
      Template webXmlTemplate = getTemplateConfiguration().getTemplate(templateFile);
      Writer bufferedWriter = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(resultFile), 
          UTF_8
        )
      );
      try {
        webXmlTemplate.process(dataToMap, bufferedWriter);
      }
      finally {
        bufferedWriter.close();
      }
      
    } catch (TemplateNotFoundException e) {
      e.printStackTrace();
    } catch (MalformedTemplateNameException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TemplateException e) {
      e.printStackTrace();
    }
  }
    
  /**
   * Подготовка данных для мэппинга на подготовленные шаблоны 
   * 
   * @return данные для мэпирования
   */
  public static final Map<String, Object> prepareData(ApplicationSettingParser applicationParser){
    Map<String, Object> resultData = new HashMap<String, Object>();
    Application application = applicationParser.getApplication();
    List<String> forms = applicationParser.getForms();
    resultData.put(MODULE_NAME_TEMPLATE_PARAMETER, application.getName());
    resultData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, application.getProjectPackage());
    resultData.put(SECURITY_ROLES_TEMPLATE_PARAMETER, applicationParser.getRoles());
    resultData.put(DEFAULT_DATASOURCE_TEMPLATE_PARAMETER, application.getDefaultDatasource());
    List<ModuleInfo> mods = new ArrayList<ModuleInfo>(forms.size());
    boolean hasTextFile = false;
    boolean hasBinaryFile = false;
    for (int i = 0; i < forms.size(); i++) {
      String formName = (String) forms.get(i);
      Map<Module, List<ModuleField>> hm = applicationParser.getModuleWithFieldsById(formName);
      Module module = hm.keySet().iterator().next();
      
      ModuleInfo modInfo = new ModuleInfo();
      modInfo.setFormName(formName);
      modInfo.setFormTitle(module.getModuleName());
      modInfo.setFormTitleEn(module.getModuleNameEn());
      modInfo.setFieldLabelWidth(module.getFieldLabelWidth());
      modInfo.setDataSource(module.getDb().getDatasource());
      modInfo.setPrimaryKey(applicationParser.getDocument().getPrimaryKeyById(formName));
      modInfo.setTable(module.getTable());
      modInfo.setDbPackage(module.getDb().getPackageName());
      modInfo.setIsExcelAvailable(module.isExcelAvailable());
      modInfo.setNotRebuild(module.isNotRebuild());
      modInfo.setDefaultParameterPrefix(module.getDefaultParameterPrefix());
      modInfo.setCreateParameterPrefix(module.getCreateParameterPrefix());
      modInfo.setFindParameterPrefix(module.getFindParameterPrefix());
      modInfo.setUpdateParameterPrefix(module.getUpdateParameterPrefix());
      Forms fms = module.getForms();
      if (!isEmpty(fms)) {
        DetailForm detailForm = fms.getDetailForm();
        if(!isEmpty(detailForm)) {
          modInfo.setPresenterBody(detailForm.getPresenterBody());
        }
      }
      String mainFormIfExist = applicationParser.getMainFormNameIfExist(formName);
      modInfo.setMainFormName(mainFormIfExist);
      if (!isEmpty(mainFormIfExist)) {
        String mainFormParentKey = applicationParser.getDocument().getPrimaryKeyById(mainFormIfExist);
        mainFormParentKey = isEmpty(mainFormParentKey) ? multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX) : mainFormParentKey;
        modInfo.setMainFormParentKey(mainFormParentKey);
      }
      boolean isJepToolBar = module.isStandardToolBar() && !module.isToolBarOff();
      modInfo.setIsJepToolBarPresenter(isJepToolBar && !Boolean.TRUE.equals(module.hasToolBarPresenter()));
      modInfo.setIsJepToolBarView(isJepToolBar && !Boolean.TRUE.equals(module.hasToolBarView()));
      modInfo.setIsDblClickOff(module.isDblClickOff());
      modInfo.setStandardToolBar(module.isStandardToolBar());
      modInfo.setIsToolBarOff(module.isToolBarOff());
      modInfo.setIsStatusBarOff(module.isStatusBarOff());
      modInfo.setHasToolBarView(module.hasToolBarView());
      modInfo.setHasToolBarPresenter(module.hasToolBarPresenter());
      modInfo.setHasLikeField(module.hasLikeFields());
      modInfo.setScopeModuleIds(applicationParser.getDependencyNodesIfExists(formName));
      modInfo.setToolBarButtons(module.getToolBarButtons());
      modInfo.setToolBarCustomButtons(module.getToolBarCustomButtons());
      modInfo.setModuleRoleNames(module.getModuleRoleNames());
      
      List<ModuleField> moduleFields = hm.values().iterator().next();
      boolean hasLobFields = false;
      boolean hasOptionField = false;
      for (ModuleField moduleField : moduleFields) {
        if (moduleField.isCLOB())
          hasTextFile = true;
        if (moduleField.isBLOB())
          hasBinaryFile = true;
        if (moduleField.getIsLOB())
          hasLobFields = true;  
        if (moduleField.getIsOptionField()){
          hasOptionField = true;
        }
      }
      modInfo.setHasLobFields(hasLobFields);
      modInfo.setHasOptionField(hasOptionField);
      modInfo.setFields(moduleFields);
      mods.add(modInfo);
    }
    resultData.put(HAS_TEXT_FILE_TEMPLATE_PARAMETER, hasTextFile);
    resultData.put(HAS_BINARY_FILE_TEMPLATE_PARAMETER, hasBinaryFile);
    resultData.put(FORMS_TEMPLATE_PARAMETER, mods);
    return resultData;
  }
  
  /**
   * Подготавливает данные для шаблона обработки одного форму (одного модуля приложения) [есть проблема формулировок].
   * @param moduleInfo Описание модуля приложения (одной форму).
   * @param allDataForTemplates Данные для всех шаблонов.
   * @return
   */
  public static Map<String, Object> prepareFormData(ModuleInfo moduleInfo, Map<String, Object> allDataForTemplates) {
    Map<String, Object> formData = new HashMap<String, Object>();
    formData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
    if(allDataForTemplates != null) {
      formData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, allDataForTemplates.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
      formData.put(MODULE_NAME_TEMPLATE_PARAMETER, allDataForTemplates.get(MODULE_NAME_TEMPLATE_PARAMETER));
    }
    return formData;
  }
}
