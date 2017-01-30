package com.technology.jep.jepriatoolkit.creator.application.thread;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_WIDGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_EDITABLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_ENABLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_HEIGHT_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_LABEL_WIDTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_LIKE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_MANDATORY_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_MAX_LENGTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_TYPE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_VISIBLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_WIDGET_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_WIDTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_BUILD_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OFF;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PKG_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRESENTER_BOBY_TAG_NAME;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getNameFromID;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FieldType;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Record;
import com.technology.jep.jepriatoolkit.parser.ApplicationStructureDocument;
import com.technology.jep.jepriatoolkit.util.Pair;


public class FormParser implements Callable<Pair<Module, List<ModuleField>>> {

  private Application application;
  private String formName, defaultDataSource;
  // Ссылка на <ApplicationName>Definition.xml
  private ApplicationStructureDocument document = null;
  
  public FormParser(Application application, ApplicationStructureDocument document, String formName, String defaultDataSource){
    this.application = application;
    this.document = document;
    this.formName = formName;
    this.defaultDataSource = defaultDataSource;
  }
  
  @Override
  public Pair<Module, List<ModuleField>> call() throws Exception {
    echoMessage(multipleConcat("Gather information about module '", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase(), DOT, formName, "'"));
    
    Element module = document.getModuleNodeById(formName);
    List<String> moduleRoles = document.getModuleSecurityRoles(formName);
    
    String moduleDataSource = document.getDataSourceById(formName);
    String modulePackage = document.getPackageById(formName);

    Db db = new Db(isEmpty(modulePackage) ? multipleConcat(PKG_PREFIX, application.getName().toLowerCase())
        : modulePackage, isEmpty(moduleDataSource) ? defaultDataSource : moduleDataSource);
    Module m = new Module(formName, isEmpty(module.getAttribute(MODULE_NAME_ATTRIBUTE)) ? NO_NAME : module.getAttribute(
        MODULE_NAME_ATTRIBUTE).trim(), isEmpty(module.getAttribute(MODULE_NAME_EN_ATTRIBUTE)) ? NO_NAME : module
        .getAttribute(MODULE_NAME_EN_ATTRIBUTE).trim(), db, moduleRoles);
    m.setNotRebuild(OFF.equalsIgnoreCase(module.getAttribute(MODULE_BUILD_ATTRIBUTE)));
    m.setTable(document.getTableById(formName));
    // Инициализация данных о тулбаре
    document.detailizedModuleForToolBar(m, module);

    List<ModuleField> mfList = new ArrayList<ModuleField>();
    List<Element> fields = document.getModuleFieldsById(formName);
    if (!isEmpty(fields)){
      String primaryKey = document.getPrimaryKeyById(formName);
      for (int i = 0; i < fields.size(); i++) {
        Element field = (Element) fields.get(i);
        String fieldId = isEmpty(field.getAttribute(FIELD_ID_ATTRIBUTE)) ? new String() : field
            .getAttribute(FIELD_ID_ATTRIBUTE).trim().toUpperCase();
        ModuleField mf = new ModuleField(formName, fieldId, field.getAttribute(FIELD_TYPE_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_NAME_ATTRIBUTE)) ? NO_NAME : field.getAttribute(FIELD_NAME_ATTRIBUTE)
                .trim(),
            isEmpty(field.getAttribute(FIELD_NAME_EN_ATTRIBUTE)) ? getNameFromID(fieldId) : field
                .getAttribute(FIELD_NAME_EN_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_LIKE_ATTRIBUTE)) ? new String() : field.getAttribute(
                FIELD_LIKE_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_WIDGET_ATTRIBUTE)) ? DEFAULT_WIDGET : field.getAttribute(
                FIELD_WIDGET_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_MAX_LENGTH_ATTRIBUTE)) ? new String() : field.getAttribute(
                FIELD_MAX_LENGTH_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_WIDTH_ATTRIBUTE)) ? new String() : field.getAttribute(
                FIELD_WIDTH_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE)) ? new String() : field.getAttribute(
                FIELD_LABEL_WIDTH_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_HEIGHT_ATTRIBUTE)) ? new String() : field.getAttribute(
                FIELD_HEIGHT_ATTRIBUTE).trim(),
            isEmpty(field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase(), 
            isEmpty(field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE).trim().toUpperCase(), 
            isEmpty(field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase(),
            isEmpty(field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase());
        
        if (!isEmpty(field.getAttribute(FIELD_LIKE_ATTRIBUTE)) && !m.hasLikeFields()) {
          m.setHasLikeFields(true);
        }
        mf.setPrimaryKey(fieldId.equalsIgnoreCase(primaryKey));
        // детализируем поле после парсинга форм: детальной и списочной
        document.detailizedModuleField(m, mf);
        mfList.add(mf);
      }
      
      List<ModuleField> recordFields = new ArrayList<ModuleField>(mfList.size());
      List<ModuleField> listFormFields = new ArrayList<ModuleField>();
      List<ModuleField> detailFormFields = new ArrayList<ModuleField>();
      for (ModuleField mf : mfList){
        recordFields.add(new ModuleField(mf, FieldType.RECORD));
        if (mf.getIsListFormField()){
          listFormFields.add(new ModuleField(mf, FieldType.FORM_LIST));
        }
        if (mf.getIsDetailFormField()){
          detailFormFields.add(new ModuleField(mf, FieldType.FORM_DETAIL));
        }
      }
      Record record = new Record(recordFields);
      record.setPrimaryKeyAndTableName(new String[]{primaryKey, m.getTable()});
      m.setRecord(record);
      // --- 
      DetailForm detailForm = new DetailForm(detailFormFields);
      Element formDetailElement = document.getFormDetailElementByModuleId(m.getModuleId());
      if (formDetailElement != null){
        NodeList presenterBodyNodes = formDetailElement.getElementsByTagName(PRESENTER_BOBY_TAG_NAME);
        if (presenterBodyNodes != null && presenterBodyNodes.getLength() > 0){
          Node presenterBody = presenterBodyNodes.item(0).getFirstChild();
          if (presenterBody instanceof CharacterData) {
                  CharacterData cd = (CharacterData) presenterBody;
                  detailForm.setPresenterBody(cd.getData());
              }
        }
      }
      
      m.setForms(detailForm, new ListForm(listFormFields));
    }
    
    return new Pair<Module, List<ModuleField>>(m, mfList);
  }

}
