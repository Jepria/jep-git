package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ALPHABET_LOWER_CASE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ALPHABET_UPPER_CASE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_ENABLE_STATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_EVENT_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_IMAGE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_TEXT_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_PARAMETERS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_PREFIX_ATTRIBUTE_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DBL_CLICK_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DB_PACKAGE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DETAIL_FORM_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.EXCEL_BUTTON_ID;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_EDITABLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_ENABLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_HEIGHT_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_LABEL_WIDTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_MANDATORY_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_MAX_LENGTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_TYPE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_VISIBLE_WORKSTATES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_WIDGET_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_WIDTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_DND_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_PRESENTER_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_DATASOURCE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_PRIMARY_KEY_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_STATUSBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TABLE_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TOOLBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OFF;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_PRESENTER_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_VIEW_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureDocument {

  private Document jepApplicationDoc;

  public ApplicationStructureDocument(Document jepApplicationDoc) {
    this.jepApplicationDoc = jepApplicationDoc;
  }
  
  /**
   * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
   * 
   * @param module Элемент-модуль
   * @param moduleNode DOM-элемент модуля
   */
  public synchronized void detailizedModuleForToolBar(Module module, Element moduleNode) {

    module.setToolBarOff(OFF.equalsIgnoreCase(moduleNode.getAttribute(MODULE_TOOLBAR_ATTRIBUTE)));
    module.setStatusbarOff(OFF.equalsIgnoreCase(moduleNode.getAttribute(MODULE_STATUSBAR_ATTRIBUTE)));

    String moduleId = module.getModuleId();
    NodeList nodes = null;
    Element toolbar = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", PATH_SEPARATOR, TOOLBAR_TAG_NAME, PATH_SEPARATOR, BUTTON_TAG_NAME, " | ", PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@",
          MODULE_ID_ATTRIBUTE, ") = '", moduleId, "']", PATH_SEPARATOR, TOOLBAR_TAG_NAME, PATH_SEPARATOR, SEPARATOR_TAG_NAME));
      nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);

      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']", PATH_SEPARATOR, TOOLBAR_TAG_NAME));
      toolbar = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    if (!isEmpty(toolbar)) {
      String toolbarPresenter = toolbar.getAttribute(TOOLBAR_PRESENTER_ATTRIBUTE), toolbarView = toolbar.getAttribute(TOOLBAR_VIEW_ATTRIBUTE);
      if (!Boolean.TRUE.equals(module.hasToolBarPresenter()) && !isEmpty(toolbarPresenter))
        module.setHasToolBarPresenter(!OFF.equalsIgnoreCase(toolbarPresenter));
      if (!Boolean.TRUE.equals(module.hasToolBarView()) && !isEmpty(toolbarView))
        module.setHasToolBarView(!OFF.equalsIgnoreCase(toolbarView));
    }
    if (!isEmpty(nodes)) {
      for (int i = 0; i < nodes.getLength(); i++) {
        Node n = nodes.item(i);
        if (n instanceof Element) {
          Element node = (Element) n;
          String buttonId = node.getAttribute(BUTTON_ID_ATTRIBUTE);
          if (EXCEL_BUTTON_ID.equals(buttonId)) {
            module.setExcelAvailable(true);
          }
          String buttonEnableStatesAttribute = node.getAttribute(BUTTON_ENABLE_STATES_ATTRIBUTE);
          buttonEnableStatesAttribute = isEmpty(buttonEnableStatesAttribute) ? buttonEnableStatesAttribute
              : buttonEnableStatesAttribute.toUpperCase();

          List<WorkstateEnum> workstates = JepRiaToolkitUtil.getWorkStates(buttonEnableStatesAttribute);
          module.getToolBarButtons().add(
              new ModuleButton(buttonId, !isEmpty(workstates) ? workstates.toArray(new WorkstateEnum[workstates.size()])
                  : new WorkstateEnum[] {}, node.getAttribute(BUTTON_IMAGE_ATTRIBUTE), node.getAttribute(BUTTON_EVENT_ATTRIBUTE), node
                  .getAttribute(BUTTON_TEXT_ATTRIBUTE))
                  .setSeparator(!nodes.item(i).getNodeName().equalsIgnoreCase(BUTTON_TAG_NAME)));
        }
      }
    }
  }
  
  /**
   * Получение списка всех модулей, описанных в файле настроек сборщика
   * приложения
   * 
   * @return список всех описанных модулей
   */
  public List<String> getAllModuleNodes() {
    NodeList nodes = null;
    Set<String> result = new HashSet<String>();
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME));
      nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    for (int i = 0; i < nodes.getLength(); i++) {
      Element node = (Element) nodes.item(i);
      String moduleId = node.getAttribute(MODULE_ID_ATTRIBUTE);
      if (!isEmpty(moduleId)) {
        result.add(moduleId);
      }
    }

    return new ArrayList<String>(result);
  }
  
  /**
   * Получение списка модулей одного Scope
   * 
   * @param moduleId идентификатор модуля, для которого имеется множество вложенных подмодулей
   * 
   * @return список модулей
   */
  public List<String> getNodesWithChildren(String moduleId) {
    NodeList nodes = null;
    List<String> result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/", MODULE_TAG_NAME));
      nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    if (nodes.getLength() > 0) {
      result = new ArrayList<String>();
      result.add(moduleId.trim());
      for (int i = 0; i < nodes.getLength(); i++) {
        String modulId = ((Element) nodes.item(i)).getAttribute(MODULE_ID_ATTRIBUTE);
        if (isEmpty(modulId))
          echoMessage(multipleConcat(ERROR_PREFIX, "Attribute ", MODULE_ID_ATTRIBUTE,
              " is mandatory! Please fill it."));
        else
          result.add(modulId.trim());
      }
    }

    return result;
  }

  /**
   * Получение XML-элемента, являющегося GWT-модулем по его ID
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return элемент-модуль
   */
  public Element getModuleNodeById(String moduleId) {
    Element module = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']"));
      module = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return module;
  }
  
  /**
   * Получение XML-элемента, являющегося GWT-модулем по его ID, игнорируя регистр букв
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return элемент-модуль
   */
  public Element getModuleNodeByIdIgnoringCase(String moduleId) {
    Element module = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, 
          "[translate(normalize-space(@", MODULE_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", moduleId.toUpperCase(), "']"));
      module = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return module;
  }

  /**
   * Получение списка элементов-полей для модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return список элементов-полей
   */
  public List<Element> getModuleFieldsById(String moduleId) {
    NodeList fields = null;
    List<Element> result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/record/field"));
      fields = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    if (fields.getLength() > 0) {
      result = new ArrayList<Element>();
      for (int i = 0; i < fields.getLength(); i++) {
        result.add((Element) fields.item(i));
      }
    }
    return result;
  }

  /**
   * Получение первичного ключа для модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return значение первичного ключа
   */
  public String getPrimaryKeyById(String moduleId) {
    String result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/record[@", MODULE_PRIMARY_KEY_ATTRIBUTE, "]/@", MODULE_PRIMARY_KEY_ATTRIBUTE));
      result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return !isEmpty(result) ? result.trim().toUpperCase() : null;
  }

  /**
   * Получение наименования таблицы для модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return наименование таблицы
   */
  public String getTableById(String moduleId) {
    String result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/record[@", MODULE_TABLE_NAME_ATTRIBUTE, "]/@", MODULE_TABLE_NAME_ATTRIBUTE));
      result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return !isEmpty(result) ? result.trim().toLowerCase() : null;
  }

  /**
   * Получение списка ролей GWT-модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return список ролей
   */
  public List<String> getModuleSecurityRoles(String moduleId) {
    NodeList result = null;
    List<String> moduleRoles = new ArrayList<String>();
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/", MODULE_ROLES_ATTRIBUTE, PATH_SEPARATOR, MODULE_ROLE_ATTRIBUTE));
      result = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
      for (int i = 0; i < result.getLength(); i++) {
        moduleRoles.add(((Node) result.item(i)).getTextContent().trim());
      }

    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return moduleRoles;
  }

  /**
   * Получение источника данных для GWT-модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return источник данных
   */
  public String getDataSourceById(String moduleId) {
    String result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", MODULE_DATASOURCE_ATTRIBUTE, "]/@", MODULE_DATASOURCE_ATTRIBUTE));
      result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return !isEmpty(result) ? result.trim() : null;
  }

  /**
   * Получение наименования пакета для GWT-модуля
   * 
   * @param moduleId идентификатор модуля
   * 
   * @return наименование пакета
   */
  public String getPackageById(String moduleId) {
    String result = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DB_PACKAGE_ATTRIBUTE, "]/@", DB_PACKAGE_ATTRIBUTE));
      result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return !isEmpty(result) ? result.trim() : null;
  }
  


  
  /**
   * Получение списка идентификаторов полей, отсутствующих в списке
   * перечисления record'ов, но указанных среди полей списочной или детальной
   * форм
   * 
   * @param moduleId Идентификатор модуля
   * @param mfList Имеющийся список полей данного модуля
   * @return список идентификаторов отсутствующих полей
   */
  public List<String> getListOfAbsentFieldNameByModuleId(String moduleId, List<ModuleField> mfList) {
    List<String> result = new ArrayList<String>();
    Element formDetailElement = getFormDetailElementByModuleId(moduleId);
    Element formListElement = getFormListElementByModuleId(moduleId);

    if (!isEmpty(formDetailElement)) {
      NodeList formDetailNodes = formDetailElement.getChildNodes();
      for (int i = 0; i < formDetailNodes.getLength(); i++) {
        Node node = formDetailNodes.item(i); 
        if (node instanceof Element) {
          Element formDetailNode = (Element) node;
          if (!FIELD_TAG_NAME.equalsIgnoreCase(formDetailNode.getTagName())) continue;
          
          String formDetailNodeId = formDetailNode.getAttribute(FIELD_ID_ATTRIBUTE).toUpperCase();
          boolean existSuchNode = false;
          for (ModuleField field : mfList) {
            if (field.getFieldId().equalsIgnoreCase(formDetailNodeId)) {
              existSuchNode = true;
              break;
            }
          }
          if (!existSuchNode && !result.contains(formDetailNodeId)) {
            result.add(formDetailNodeId);
          }
        }
      }
    }

    if (!isEmpty(formListElement)) {
      NodeList formListNodes = formListElement.getChildNodes();
      for (int i = 0; i < formListNodes.getLength(); i++) {
        Node node = formListNodes.item(i); 
        if (node instanceof Element) {
          Element formListNode = (Element) node;
          if (!FIELD_TAG_NAME.equalsIgnoreCase(formListNode.getTagName())) continue;
          
          String formListNodeId = formListNode.getAttribute(FIELD_ID_ATTRIBUTE).toUpperCase();
          boolean existSuchNode = false;
          for (ModuleField field : mfList) {
            if (field.getFieldId().equalsIgnoreCase(formListNodeId)) {
              existSuchNode = true;
              break;
            }
          }
          if (!existSuchNode && !result.contains(formListNodeId)) {
            result.add(formListNodeId);
          }
        }
      }
    }

    return result;
  }
  
  /**
   * Получение ссылки на узел детальной формы по указанному идентификатору
   * модуля
   * @param moduleId Идентификатор модуля
   * 
   * @return XML-узел в <ApplicationName>Definition.xml
   */
  public Element getFormDetailElementByModuleId(String moduleId) {
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", PATH_SEPARATOR, FORMS_TAG_NAME, PATH_SEPARATOR, DETAIL_FORM_TAG_NAME));
      return (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return null;
  }
  
  /**
   * Получение ссылки на узел списочной формы по указанному идентификатору
   * модуля
   * @param moduleId Идентификатор модуля
   * 
   * @return XML-узел в <ApplicationName>Definition.xml
   */
  public Element getFormListElementByModuleId(String moduleId) {
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", PATH_SEPARATOR, FORMS_TAG_NAME, PATH_SEPARATOR, LIST_FORM_TAG_NAME));
      return (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return null;
  }
  
  /**
   * Детализация элемента-поля GWT-модуля
   * 
   * @param module Ссылка на модуль
   * @param recordField  Элемент-поле
   */
  public synchronized void detailizedModuleField(Module module, ModuleField recordField) {
    try {
      detailizedModuleFieldAsDetailed(module, recordField);
      detailizedModuleFieldAsListed(module, recordField);
      detailizedModuleFieldAsParameters(module, recordField);
      detailizedPrefixModuleFieldParameters(module);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Детализация элемента-поля GWT-модуля как представителя списочной формы
   * 
   * @param module Ссылка на модуль
   * @param recordField Элемент-поле
   */
  private void detailizedModuleFieldAsDetailed(Module module, ModuleField recordField) {
    Element field = null;
    String fieldId = recordField.getFieldId();
    String moduleId = recordField.getModuleId();
    Element formDetailElement = getFormDetailElementByModuleId(moduleId);
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", PATH_SEPARATOR, FORMS_TAG_NAME, PATH_SEPARATOR, DETAIL_FORM_TAG_NAME, PATH_SEPARATOR, FIELD_TAG_NAME, "[translate(normalize-space(@",
          FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
      field = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    if (!isEmpty(formDetailElement) && isEmpty(module.getFieldLabelWidth())) {
      module.setFieldLabelWidth(formDetailElement.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE));
    }
    if (!isEmpty(field)) {
      recordField.setDetailFormField(true);
      String fieldWidget = isEmpty(field.getAttribute(FIELD_WIDGET_ATTRIBUTE)) ? null : field.getAttribute(FIELD_WIDGET_ATTRIBUTE)
          .trim();
      if (!isEmpty(fieldWidget)) {
        recordField.setFieldWidget(fieldWidget);
      }
      String name = new String();
      if (!isEmpty(name = field.getAttribute(FIELD_NAME_ATTRIBUTE))) {
        recordField.setFieldDetailFormName(name.trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_NAME_EN_ATTRIBUTE))) {
        recordField.setFieldDetailFormNameEn(name.trim());
      }
      if (isEmpty(recordField.getFieldType())) {
        recordField.setFieldType(isEmpty(field.getAttribute(FIELD_TYPE_ATTRIBUTE)) ? null : field.getAttribute(
            FIELD_TYPE_ATTRIBUTE).trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_MAX_LENGTH_ATTRIBUTE))) {
        recordField.setFieldMaxLength(name.trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_WIDTH_ATTRIBUTE))) {
        recordField.setFieldWidth(name.trim());
      }
      String labelWidth = module.getFieldLabelWidth();
      if (!isEmpty(name = field.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE))) {
        recordField.setLabelWidth(name.trim());
      } else if (isEmpty(recordField.getLabelWidth()) && !isEmpty(labelWidth)) {
        recordField.setLabelWidth(labelWidth);
      }
      if (!isEmpty(name = field.getAttribute(FIELD_HEIGHT_ATTRIBUTE))) {
        recordField.setFieldHeight(name.trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE))) {
        recordField.setVisibleWorkStates(name.trim().toUpperCase());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE))) {
        recordField.setMandatoryWorkStates(name.trim().toUpperCase());
      }
      
      if (!isEmpty(name = field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE))) {
        recordField.setEditableWorkStates(name.trim().toUpperCase());
      } else if (field.hasAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE) 
          && isEmpty(field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE)))
      {
        recordField.setIsEditable(false);
      }
        
      if (!isEmpty(name = field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE))) {
        recordField.setEnableWorkStates(name.trim().toUpperCase());
      }

      NodeList detailFormNodeList = field.getParentNode().getChildNodes();
      int counter = 0;
      for (int index = 0; index < detailFormNodeList.getLength(); index++) {
        Node node = detailFormNodeList.item(index);
        if (node instanceof Element) {
          Element nodeEl = (Element) node;
          if (fieldId.equalsIgnoreCase(nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
            recordField.setDetailFormIndex(counter);
          } else if (isRecordField(moduleId, nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
            counter++;
          }
        }
      }
    }
  }

  

  /**
   * Детализация элемента-поля GWT-модуля как представителя детальной формы
   * 
   * @param module Ссылка на модуль
   * @param recordField Элемент-поле
   */
  private void detailizedModuleFieldAsListed(Module module, ModuleField recordField) {
    Element field = null;
    String fieldId = recordField.getFieldId();
    String moduleId = recordField.getModuleId();
    Element formList = getFormListElementByModuleId(moduleId);
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", PATH_SEPARATOR, FORMS_TAG_NAME, PATH_SEPARATOR, LIST_FORM_TAG_NAME, PATH_SEPARATOR, FIELD_TAG_NAME, "[translate(normalize-space(@",
          FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
      field = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    if (!isEmpty(formList)) {
      String groupFieldName = formList.getAttribute(LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE);
      recordField.setGroupFormField(recordField.getFieldId().equalsIgnoreCase(groupFieldName));

      if (module.isDNDOff())
        module.setDNDOff(isEmpty(formList.getAttribute(LIST_FORM_DND_NAME_ATTRIBUTE))
            || OFF.equalsIgnoreCase(formList.getAttribute(LIST_FORM_DND_NAME_ATTRIBUTE)));

      if (!module.isDblClickOff())
        module.setDblClickOff(OFF.equalsIgnoreCase(formList.getAttribute(DBL_CLICK_NAME_ATTRIBUTE)));

      String presenterAttribute = formList.getAttribute(LIST_FORM_PRESENTER_ATTRIBUTE);
      if (!module.hasListFormPresenter() && !isEmpty(presenterAttribute))
        module.setHasListFormPresenter(!OFF.equalsIgnoreCase(presenterAttribute));
    }
    if (!isEmpty(field)) {
      recordField.setListFormField(true);
      String name = new String();
      if (!isEmpty(name = field.getAttribute(FIELD_NAME_ATTRIBUTE))) {
        recordField.setFieldListFormName(name.trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_NAME_EN_ATTRIBUTE))) {
        recordField.setFieldListFormNameEn(name.trim());
      }
      if (isEmpty(recordField.getFieldType())) {
        recordField.setFieldType(isEmpty(field.getAttribute(FIELD_TYPE_ATTRIBUTE)) ? null : field.getAttribute(
            FIELD_TYPE_ATTRIBUTE).trim());
      }
      if (!isEmpty(name = field.getAttribute(FIELD_WIDTH_ATTRIBUTE))) {
        recordField.setColumnWidth(name.trim());
      }
    }
    
    if (!isEmpty(formList)) {
      NodeList listFormNodeList = formList.getChildNodes();
      int counter = 0;
      for (int index = 0; index < listFormNodeList.getLength(); index++) {
        Node node = listFormNodeList.item(index);
        if (node instanceof Element) {
          Element nodeEl = (Element) node;
          if (fieldId.equalsIgnoreCase(nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
            recordField.setListFormIndex(counter);
          } else if (isRecordField(moduleId, nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
            counter++;
          }
        }
      }
    }
  }

  /**
   * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
   * 
   * @param module Ссылка на модуль
   * @param recordField Элемент-поле
   */
  private void detailizedModuleFieldAsParameters(Module module, ModuleField recordField) {
    String fieldId = recordField.getFieldId();
    String moduleId = recordField.getModuleId();
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(JepRiaToolkitUtil
          .multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId, "']", PATH_SEPARATOR,
              DATABASE_TAG_NAME, "/find[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
      String findParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      List<String> parameters = Arrays.asList(findParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
      Db db = module.getDb();
      if (!isEmpty(findParameters)) db.setFind(new FunctionParameters(findParameters));
      recordField.setFindParameter(!isEmpty(findParameters) && parameters.contains(fieldId));

      xpath.reset();
      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']", PATH_SEPARATOR, DATABASE_TAG_NAME, "/create[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
      String createParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      parameters = Arrays.asList(createParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
      if (!isEmpty(createParameters)) db.setCreate(new FunctionParameters(createParameters));
      recordField.setCreateParameter(!isEmpty(createParameters) && parameters.contains(fieldId));

      xpath.reset();
      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']", PATH_SEPARATOR, DATABASE_TAG_NAME, "/update[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
      String updateParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      parameters = Arrays.asList(updateParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
      if (!isEmpty(updateParameters)) db.setUpdate(new FunctionParameters(updateParameters));
      recordField.setUpdateParameter(!isEmpty(updateParameters) && parameters.contains(fieldId));

      recordField.setDeleteParameter(recordField.getIsPrimaryKey());
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
  }

  /**
   * Детализация префиксов для параметров полей
   * 
   * @param module Ссылка на модуль
   */
  private void detailizedPrefixModuleFieldParameters(Module module) {
    String defaultPrefix = null;
    String moduleId = module.getModuleId();
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
      defaultPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      defaultPrefix = !isEmpty(defaultPrefix) ? defaultPrefix.trim() : null;
      module.setDefaultParameterPrefix(defaultPrefix);

      xpath.reset();
      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']/", DATABASE_TAG_NAME, "/create[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
      String createParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      createParameterPrefix = !isEmpty(createParameterPrefix) ? createParameterPrefix.trim() : defaultPrefix;
      if (!isEmpty(createParameterPrefix))
        module.setCreateParameterPrefix(createParameterPrefix);

      xpath.reset();
      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']/", DATABASE_TAG_NAME, "/update[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
      String updateParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      updateParameterPrefix = !isEmpty(updateParameterPrefix) ? updateParameterPrefix.trim() : defaultPrefix;
      if (!isEmpty(updateParameterPrefix))
        module.setUpdateParameterPrefix(updateParameterPrefix);

      xpath.reset();
      expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
          "']/", DATABASE_TAG_NAME, "/find[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
      String findParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
      findParameterPrefix = !isEmpty(findParameterPrefix) ? findParameterPrefix.trim() : defaultPrefix;
      if (!isEmpty(findParameterPrefix))
        module.setFindParameterPrefix(findParameterPrefix);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
  }
  
  /**
   * Является ли поле с указанным ID рекорд дефинишном
   * 
   * @param moduleId Идентификатор модуля
   * @param fieldId Идентификатор поля
   * 
   * @return флаг, указывающий является ли поле рекорд дефинишном
   */
  private boolean isRecordField(String moduleId, String fieldId) {
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
          ") = '", moduleId, "']", "/record/", FIELD_TAG_NAME, "[translate(normalize-space(@", FIELD_ID_ATTRIBUTE, "),'",
          ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
      return !isEmpty((Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE));
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    return false;
  }
  
  /**
   * Получение ссылки на узел DOM-дерева по имени тэга
   * 
   * @param tagName    наименование искомого тэга узла дерева
   * @return узел DOM-дерева или null, если узел не найден
   */
  public Element getElementByTagName(String tagName){
    Element result = null;
    NodeList nodes = jepApplicationDoc.getElementsByTagName(tagName);
    if (nodes.getLength() > 0){
      Node node = nodes.item(0);
      if (node instanceof Element) {
        result = (Element) node;
      }
    }
    return result;
  }
}
