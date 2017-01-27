package com.technology.jep.jepriatoolkit.parser.thread;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_FIELD_WIDTH;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PKG_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_SOURCE_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclaration;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclarationSuppressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPathSupressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractStringFromQuotes;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getResourceByPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.initSmall;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.readFromFile;
import static java.text.MessageFormat.format;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.Statement;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FieldType;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleDeclaration;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Record;
import com.technology.jep.jepriatoolkit.log.Logger;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ModuleParser implements Callable<Module> {

  private static final String JDBC_JNDI_PREFIX = "jdbc/";
  private static final String UPDATE_DAO_FUNCTION = "update";
  private static final String CREATE_DAO_FUNCTION = "create";
  private static final String FIND_DAO_FUNCTION = "find";
  private static final String DATA_SOURCE_JNDI_NAME_CONSTANT_NAME = "DATA_SOURCE_JNDI_NAME";
  
  private Application application;
  private String moduleId;
  private ResourceBundle jepRiaResourceBundle;
  private ResourceBundle mainModuleResourceBundle;
  private ResourceBundle mainModuleResourceBundleEn;
  
  public ModuleParser(Application application, String moduleId, ResourceBundle... resources){
    this.application = application;
    this.moduleId = moduleId;
    this.jepRiaResourceBundle = resources[0];
    this.mainModuleResourceBundle = resources[1];
    this.mainModuleResourceBundleEn = resources[2];
  }
  
  @Override
  public Module call() throws Exception {
    String clientModuleDaoPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
          getDefinitionProperty(CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY, 
              multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}Dao.java"), jepRiaResourceBundle
          ), moduleId
        ));
    String packageName = null;
    String moduleDataSource = "";
    StringBuilder findParameters = new StringBuilder();
    StringBuilder createParameters = new StringBuilder();
    StringBuilder updateParameters = new StringBuilder();
    ModuleDeclaration declaration;
    declaration = getModuleDeclaration(clientModuleDaoPath);
    
    for (MethodDeclaration method : declaration.getMethods()){
      String methodName = method.getName();
      if (FIND_DAO_FUNCTION.equalsIgnoreCase(methodName) || 
          CREATE_DAO_FUNCTION.equalsIgnoreCase(methodName) ||
            UPDATE_DAO_FUNCTION.equalsIgnoreCase(methodName)){
        
        Pattern p = Pattern.compile("get\\((.*?)\\)");
        String methodBody = method.getBody().toStringWithoutComments();
        Matcher m = p.matcher(methodBody);
        while(m.find()){
          String value = m.group(1);
          if (FIND_DAO_FUNCTION.equalsIgnoreCase(methodName))
            findParameters.append(findParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
          else if (CREATE_DAO_FUNCTION.equalsIgnoreCase(methodName))
            createParameters.append(createParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
          else if (UPDATE_DAO_FUNCTION.equalsIgnoreCase(methodName))
            updateParameters.append(updateParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
        }
        
        p = Pattern.compile(multipleConcat(":=\\s*(.*?)\\.", FIND_DAO_FUNCTION));
        m = p.matcher(methodBody);
        if (m.find()){
          packageName = m.group(1);
        }
      }
    }
    
    String clientModuleServerConstantPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
            getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY, 
                multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/{3}ServerConstant.java"), jepRiaResourceBundle)
        , moduleId));
    declaration = getModuleDeclaration(clientModuleServerConstantPath);
    
    for (FieldDeclaration field : declaration.getFields()){
      VariableDeclarator var = field.getVariables().iterator().next();
      String fieldName = var.getId().getName();
      if (DATA_SOURCE_JNDI_NAME_CONSTANT_NAME.equalsIgnoreCase(fieldName)){
        moduleDataSource = extractStringFromQuotes(var.getInit().toStringWithoutComments()).split(JDBC_JNDI_PREFIX)[1];
        break;
      }
    }
    
    Db db = new Db();
    if (!JepRiaToolkitUtil.isEmpty(packageName) && !packageName.equalsIgnoreCase(multipleConcat(PKG_PREFIX, application.getName()))){
      db.setPackageName(packageName);
    }
    if (!JepRiaToolkitUtil.isEmpty(moduleDataSource) && !application.getDefaultDatasource().equalsIgnoreCase(moduleDataSource)){
      db.setDatasource(moduleDataSource);
    }
    if (!findParameters.toString().isEmpty())
      db.setFind(new FunctionParameters(findParameters.toString()));
    if (!createParameters.toString().isEmpty())
      db.setCreate(new FunctionParameters(createParameters.toString()));
    if (!updateParameters.toString().isEmpty())
      db.setUpdate(new FunctionParameters(updateParameters.toString()));
    
    String submoduleResourceKey = multipleConcat("submodule.", moduleId.toLowerCase(), ".title");
    
    String moduleText = null, moduleTextEn = null;
    try {
      moduleText = mainModuleResourceBundle.getString(submoduleResourceKey);
    }
     catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
    try {
      moduleTextEn = mainModuleResourceBundleEn.getString(submoduleResourceKey);
    }
     catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
    Module mod = new Module(moduleId, moduleText, moduleTextEn, db, Arrays.asList(getRoles(moduleId, jepRiaResourceBundle).split("\\s*,\\s*")));
    String clientModuleFieldNamesPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
            getDefinitionProperty(CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY, 
                multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}FieldNames.java"), jepRiaResourceBundle), moduleId
        ));
    declaration = getModuleDeclaration(clientModuleFieldNamesPath);
    
    List<ModuleField> recordFields = new ArrayList<ModuleField>();
    List<ModuleField> detailFormFields = new ArrayList<ModuleField>();
    List<ModuleField> listFormFields = new ArrayList<ModuleField>();
    if (declaration != null) {
      for (FieldDeclaration fieldDeclaration : declaration.getFields()){
        String fieldId = extractStringFromQuotes(fieldDeclaration.getVariables().iterator().next().getInit().toStringWithoutComments()).toUpperCase();
        ModuleField field = new ModuleField(moduleId, fieldId);
        prepareDetailFormModuleField(moduleId, field, jepRiaResourceBundle);
        prepareListFormModuleField(moduleId, field, jepRiaResourceBundle);
        recordFields.add(new ModuleField(field, FieldType.RECORD));
        if (field.getIsDetailFormField())
          detailFormFields.add(new ModuleField(field, FieldType.FORM_DETAIL));
        if (field.getIsListFormField())
          listFormFields.add(new ModuleField(field, FieldType.FORM_LIST));
      }
    }
    Record rec = new Record(recordFields);
    rec.setPrimaryKeyAndTableName(getPrimaryKeyAndTableName(moduleId, jepRiaResourceBundle));
    mod.setRecord(rec);
    
    String clientModuleDetailPresenterPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
            getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
                multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormPresenter.java"), jepRiaResourceBundle), moduleId
        ));
    
    declaration = getModuleDeclarationSuppressException(clientModuleDetailPresenterPath);
    DetailForm detailForm = new DetailForm(detailFormFields);
    if (declaration != null) {
      detailForm.setPresenterBody(declaration.getBusinessLogic());
    }
    ListForm listForm = new ListForm(listFormFields);
    mod.setForms(detailForm, listForm);
    
    String clientModuleToolBarViewPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
            getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
                multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarViewImpl.java"), jepRiaResourceBundle), moduleId
        ));
    declaration = getModuleDeclarationSuppressException(clientModuleToolBarViewPath);
    List<ModuleButton> buttons = new ArrayList<ModuleButton>();
    boolean hasToolBarView = declaration != null;
    mod.setHasToolBarView(hasToolBarView);
    if (hasToolBarView) {
      ConstructorDeclaration constructor = declaration.getConstructors().iterator().next();
      for (Statement st : constructor.getBlock().getStmts()){
        String line = st.toStringWithoutComments();
        if (line.startsWith("addButton")){
          Pattern p = Pattern.compile("addButton\\s*\\(\\s*(.*?)\\s*,\\s*(.*?)\\s*(?:,\\s*(.*?)\\s*)?\\);");
          Matcher m = p.matcher(line);
          if (m.find()){
            ModuleButton button = new ModuleButton(m.group(1));
            if (button.getIsCustomButton()) {
              String imageOrText = m.group(2);
              String text = m.group(3);
              if (!JepRiaToolkitUtil.isEmpty(text)){
                button.setImage(imageOrText);
                button.setText(text);
              }
              else {
                button.setText(imageOrText);
              }
            }
            buttons.add(button);
          }
        }
      }
    }
    String clientModuleToolBarPresenterPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(
            getDefinitionProperty(CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY, 
                multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarPresenter.java"), jepRiaResourceBundle), moduleId
        ));
    declaration = getModuleDeclarationSuppressException(clientModuleToolBarPresenterPath);
    boolean hasToolBarPresenter = declaration != null;
    mod.setHasToolBarPresenter(hasToolBarPresenter);
    if (declaration != null)
      for (MethodDeclaration md : declaration.getMethods()){
        if ("bind".equalsIgnoreCase(md.getName())){
          for (ModuleButton button : buttons){
            if (button.getIsCustomButton()) {
              Pattern p = Pattern.compile(multipleConcat("bindButton\\s*\\(\\s*", button.getButtonId(), "\\s*,\\s*new\\s*WorkstateEnum\\s*\\[\\s*\\]\\s*\\{(.*?)\\}\\s*,\\s*new\\s*ClickHandler\\s*\\(\\s*\\)\\s*\\{\\s*public\\s*void\\s*onClick\\s*\\(\\s*ClickEvent\\s*event\\s*\\)\\s*\\{\\s*(.*?)\\s*\\}\\s*\\}\\s*\\)"));
              Matcher m = p.matcher(md.toStringWithoutComments());
              if (m.find()){
                String workstateAsString = m.group(1);
                if (!JepRiaToolkitUtil.isEmpty(workstateAsString)){
                  button.setWorkStatesAsString(workstateAsString);
                }
                button.setEvent(m.group(2));
              }
            }
          }
          break;
        }
      }
    mod.getToolBarButtons().addAll(buttons);
    
    return mod;
  }
  
  public String[] getPrimaryKeyAndTableName(String moduleId, ResourceBundle resource) throws FileNotFoundException {
    ModuleDeclaration clientModuleDeclaration = getClientModuleRecordDefinition(moduleId, resource);
    String superClassInConstructorBody = clientModuleDeclaration.getConstructors().iterator().next().getBlock().getStmts().iterator().next().toStringWithoutComments();
    Pattern p = Pattern.compile("new\\s*String\\s*\\[\\s*\\]\\s*\\{\\s*(.*?)\\s*\\}(\\s*,\\s*\\\"(.*?)\\\")*");
    Matcher m = p.matcher(superClassInConstructorBody);
    if(m.find()){
      if (m.groupCount() >= 3){
        return new String[]{m.group(1), m.group(3)};
      }
      else {
        return new String[]{m.group(1)};
      }
    }
    return null;
  }
  
  public void prepareDetailFormModuleField(String moduleId, ModuleField field, ResourceBundle resource){
    String clientModuleDetailFormViewImplPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
            multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "src/java/com/technology/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormViewImpl.java"), resource
        ), moduleId));
    String fieldId = field.getFieldId();
    ModuleDeclaration clientModuleDetailFormDeclaration = getModuleDeclarationSuppressException(clientModuleDetailFormViewImplPath);
    if (clientModuleDetailFormDeclaration != null) {
      ConstructorDeclaration clientModuleDetailFormConstructor = clientModuleDetailFormDeclaration.getConstructors().iterator().next();
      
      Pattern p = Pattern.compile(multipleConcat("fields\\s*\\.\\s*put\\s*\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*", JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\s*\\);"));
      Matcher m = p.matcher(clientModuleDetailFormConstructor.getBlock().toStringWithoutComments());
      if (!m.find()){
        return;
      }
      
      p = Pattern.compile(multipleConcat("(.*?)\\s*", JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\s*=\\s*new\\s*(.*?)\\("));
      m = p.matcher(clientModuleDetailFormConstructor.getBlock().toStringWithoutComments());
      if (m.find()){
        field.setFieldWidget(m.group(3));
      }
      p = Pattern.compile(multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\.setFieldWidth\\((.*?)\\);"));
      m = p.matcher(clientModuleDetailFormConstructor.getBlock().toStringWithoutComments());
      if (m.find()){
        field.setFieldWidth(m.group(2));
      }
      p = Pattern.compile(multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\.setLabelWidth\\((.*?)\\);"));
      m = p.matcher(clientModuleDetailFormConstructor.getBlock().toStringWithoutComments());
      if (m.find()){
        field.setLabelWidth(m.group(2));
      }
      p = Pattern.compile(multipleConcat("setFieldMaxLength\\(", fieldId.toUpperCase(),",\\s*(.*?)\\);"));
      m = p.matcher(clientModuleDetailFormConstructor.getBlock().toStringWithoutComments());
      if (m.find()){
        field.setFieldMaxLength(m.group(1));
      }
    }
    
    ModuleDeclaration clientModuleDeclaration;
    try {
      clientModuleDeclaration = getClientModuleRecordDefinition(moduleId, resource);
    }
    catch(FileNotFoundException e){
      log(multipleConcat(ERROR_PREFIX, "File '", getClientModuleRecordDefinitionPath(moduleId, resource), "' is not found!"), moduleId);
      return;
    }
    field.setDetailFormField(true);
    for (MethodDeclaration method : clientModuleDeclaration.getMethods()){
      if ("buildtypemap".equalsIgnoreCase(method.getName())){
        Pattern p = Pattern.compile(multipleConcat("put\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*(.*?)\\)"));
        Matcher m = p.matcher(method.getBody().toStringWithoutComments());
        if (m.find()){
          // устанавливаем тип поля в исходном поле
          field.setFieldType(m.group(1));
        }
        else {
          Logger.appendMessageToTheEndOfForm(moduleId, multipleConcat("You should define type for field '", fieldId, "'"));
        }
      }
      else if ("buildlikemap".equalsIgnoreCase(method.getName())){
        Pattern p = Pattern.compile(multipleConcat("put\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*(.*?)\\)"));
        Matcher m = p.matcher(method.getBody().toStringWithoutComments());
        if (m.find()){
          field.setFieldLike(m.group(1));
        }
      }
    }
    
    String clientModuleResourcePath = convertPatternInRealPathSupressException(
          replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
              multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties"), resource
          ), moduleId));
    
    String clientModuleResourceEnPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
            multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties"), resource
        ), moduleId));
    
    ResourceBundle clientModuleResourceBundle = getResourceByPath(clientModuleResourcePath);
    ResourceBundle clientModuleResourceBundleEn = getResourceByPath(clientModuleResourceEnPath);
    
    try {
      String fieldDetailFormName = clientModuleResourceBundle.getString(multipleConcat(initSmall(moduleId), ".detail.", fieldId.toLowerCase()));
      field.setFieldDetailFormName(NO_NAME.equalsIgnoreCase(fieldDetailFormName) ? null : fieldDetailFormName);
    }
    catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
    
    try {
      String fieldDetailFormNameEn = clientModuleResourceBundleEn.getString(multipleConcat(initSmall(moduleId), ".detail.", fieldId.toLowerCase()));
      field.setFieldDetailFormNameEn(NO_NAME.equalsIgnoreCase(fieldDetailFormNameEn) ? null : fieldDetailFormNameEn);
    }
    catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
  }
  
  public static void prepareListFormModuleField(String moduleId, ModuleField field, ResourceBundle resource){
    
    String clientModuleListFormViewImplPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
            multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "src/java/com/technology/{0}/{1}/{2}/client/ui/form/list/{3}ListFormViewImpl.java"), resource
        ), moduleId));
    
    ModuleDeclaration clientModuleListFormDeclaration = getModuleDeclarationSuppressException(clientModuleListFormViewImplPath);
    if (clientModuleListFormDeclaration == null) return;
    
    String columnConfiguration = null;
    
    for (MethodDeclaration method : clientModuleListFormDeclaration.getMethods()){
      if ("getColumnConfigurations".equalsIgnoreCase(method.getName())){
        columnConfiguration = method.toStringWithoutComments();
        break;
      }
    }
    String fieldId = field.getFieldId();
    Pattern p = Pattern.compile(multipleConcat("new\\s*JepColumn\\s*\\(\\s*", fieldId.toUpperCase(),",\\s*", initSmall(moduleId),"Text\\.", initSmall(moduleId), "_list_", fieldId.toLowerCase(), "\\s*\\(\\s*\\)\\s*,\\s*(.*?)\\s*,"));
    Matcher m = p.matcher(columnConfiguration);
    if (m.find()){
      String columnWidth = m.group(1);
      field.setColumnWidth(DEFAULT_FIELD_WIDTH.equalsIgnoreCase(columnWidth) ? null : columnWidth);
    }
    else { 
      return;
    }
    field.setListFormField(true);
    String clientModuleResourcePath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
            multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties"), resource
        ), moduleId));
    
    String clientModuleResourceEnPath = convertPatternInRealPathSupressException(
        replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
            multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties"), resource
        ), moduleId));
    
    ResourceBundle clientModuleResourceBundle = getResourceByPath(clientModuleResourcePath);
    ResourceBundle clientModuleResourceBundleEn = getResourceByPath(clientModuleResourceEnPath);
    
    try {
      String fieldListFormName = clientModuleResourceBundle.getString(multipleConcat(initSmall(moduleId), ".list.", fieldId.toLowerCase()));
      field.setFieldListFormName(NO_NAME.equalsIgnoreCase(fieldListFormName) ? null : fieldListFormName);
    } 
    catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
    
    try {
      String fieldListFormNameEn = clientModuleResourceBundleEn.getString(multipleConcat(initSmall(moduleId), ".list.", fieldId.toLowerCase()));
      field.setFieldListFormNameEn(NO_NAME.equalsIgnoreCase(fieldListFormNameEn) ? null : fieldListFormNameEn);
    } 
    catch(MissingResourceException e){
      Logger.appendMessageToTheEndOfForm(moduleId, e.getLocalizedMessage());
    }
  }
  
  public String getRoles(String moduleId, ResourceBundle resource){
    String mainModulePresenterFilePath = convertPatternInRealPathSupressException(getDefinitionProperty(MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
        multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/ui/main/{2}MainModulePresenter.java"), resource));
    
    Pattern p = Pattern.compile(multipleConcat(moduleId.toUpperCase(), "_MODULE_ID,\\s*\"(.*?)\""));
    String methodBody;
    try {
      methodBody = readFromFile(mainModulePresenterFilePath, UTF_8);
    } catch (FileNotFoundException e) {
      log(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()), null);
      return "";
    }
    Matcher m = p.matcher(methodBody);
    if(m.find()){
      return m.group(1);
    }
    return "";
  }

  private static ModuleDeclaration getClientModuleRecordDefinition(String moduleId, ResourceBundle resource) throws FileNotFoundException {
    ModuleDeclaration clientModuleDeclaration = getModuleDeclaration(getClientModuleRecordDefinitionPath(moduleId, resource));
    return clientModuleDeclaration;
  }

  public static String getClientModuleRecordDefinitionPath(String moduleId, ResourceBundle resource) {
    return convertPatternInRealPathSupressException(
        replacePathWithModuleId(
          getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY, 
              multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/record/{3}RecordDefinition.java"), resource
          ),
        moduleId));
  }
  
  private static String replacePathWithModuleId(String path, String moduleId){
    return format(path, "{0}", "{1}", moduleId.toLowerCase(), "{2}");
  }
  
  private static void log(String message, String moduleId){
    echoMessage(message);
    Logger.appendMessageToTheEndOfForm(moduleId, message);
  }
}
