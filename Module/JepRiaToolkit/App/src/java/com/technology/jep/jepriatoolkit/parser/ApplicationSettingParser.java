package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ALPHABET_LOWER_CASE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ALPHABET_UPPER_CASE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_DATASOURCE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_TAG_NAME;
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
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_WIDGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DETAIL_FORM_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.EXCEL_BUTTON_ID;
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
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_BUILD_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_DATASOURCE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_PRIMARY_KEY_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_STATUSBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TABLE_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TOOLBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OFF;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PKG_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRESENTER_BOBY_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PROJECT_PACKAGE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_PRESENTER_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_VIEW_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDOM;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getNameFromID;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static org.w3c.dom.Node.ELEMENT_NODE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FieldType;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.creator.module.Record;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationSettingParser {

	// объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения
	private Application application;
	// Ссылка на <ApplicationName>Definition.xml
	private Document jepApplicationDoc = null;
	private List<String> forms = new ArrayList<String>();
	private List<List<String>> formWithTheirDependencies = new ArrayList<List<String>>();
	private List<String> securityRoles = new ArrayList<String>();
	private Map<Module, List<ModuleField>> formFields = new HashMap<Module, List<ModuleField>>();
	
	private ApplicationSettingParser(){}
	
	public static ApplicationSettingParser getInstance() throws ParserConfigurationException {
		return getInstance(getApplicationDefinitionFile());
	}
	
	public static ApplicationSettingParser getInstance(String applicationStructureFile) throws ParserConfigurationException {
		ApplicationSettingParser parser = new ApplicationSettingParser();
		if (parser.parseApplicationSettingXML(applicationStructureFile)){
			return parser;
		}
		throw new ParserConfigurationException();
	}
	
	/**
	 * Разбор и проверка <ApplicationName>Definition.xml
	 * 
	 * @return флаг успешности/неуспешности парсинга <ApplicationName>Definition.xml
	 * @throws ParserConfigurationException
	 */
	public boolean parseApplicationSettingXML(String applicationStructureFile) throws ParserConfigurationException {
		applicationStructureFile = isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile;
		echoMessage(multipleConcat("Parsing ", applicationStructureFile, "..."));
		try {
			jepApplicationDoc = getDOM(applicationStructureFile);
			application = new Application();
			NodeList nodes = jepApplicationDoc.getElementsByTagName(APPLICATION_TAG_NAME);
			if (isEmpty(nodes)) {
				echoMessage(multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! There is no mandatory tag 'application'!"));
				return false;
			}
			Element applicationNode = (Element) nodes.item(0);
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
			else if (!moduleName.equals(new File(applicationStructureFile).getName().split(APPLICATION_SETTING_FILE_ENDING)[0])){
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
			forms = getAllModuleNodes(jepApplicationDoc);
			application.setModuleIds(forms);
			List<Module> modules = new ArrayList<Module>(forms.size());
			for (int index = 0; index < forms.size(); index++) {
				String formName = forms.get(index);
				echoMessage(multipleConcat("Gather information about module '", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase(), DOT, formName, "'"));
				
				Element module = getModuleNodeById(formName);
				List<String> moduleRoles = getModuleSecurityRoles(formName);
				// заполнение общего списка ролей, необходимого для генерации web.xml
				for (String moduleRole : moduleRoles) {
					if (!securityRoles.contains(moduleRole))
						securityRoles.add(moduleRole);
				}

				String moduleDataSource = getDataSourceById(formName);
				String modulePackage = getPackageById(formName);

				Db db = new Db(isEmpty(modulePackage) ? multipleConcat(PKG_PREFIX, application.getName().toLowerCase())
						: modulePackage, isEmpty(moduleDataSource) ? defaultDataSource : moduleDataSource);
				Module m = new Module(formName, isEmpty(module.getAttribute(MODULE_NAME_ATTRIBUTE)) ? NO_NAME : module.getAttribute(
						MODULE_NAME_ATTRIBUTE).trim(), isEmpty(module.getAttribute(MODULE_NAME_EN_ATTRIBUTE)) ? NO_NAME : module
						.getAttribute(MODULE_NAME_EN_ATTRIBUTE).trim(), db, moduleRoles);
				m.setNotRebuild(OFF.equalsIgnoreCase(module.getAttribute(MODULE_BUILD_ATTRIBUTE)));
				m.setTable(getTableById(formName));
				// Инициализация данных о тулбаре
				detailizedModuleForToolBar(m, module);

				List<ModuleField> mfList = new ArrayList<ModuleField>();
				List<Element> fields = getModuleFieldsById(formName);
				if (!isEmpty(fields)){
					String primaryKey = getPrimaryKeyById(formName);
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
						detailizedModuleField(m, mf);
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
					Element formDetailElement = getFormDetailElementByModuleId(m.getModuleId());
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
				modules.add(m);
				// добавляем соответствие модуля его списку полей
				formFields.put(m, mfList);
			}
			application.setModules(new Modules(modules));
			for (int i = 0; i < forms.size(); i++) {
				List<String> nodesWithChildren = getNodesWithChildren(forms.get(i));
				if (!isEmpty(nodesWithChildren))
					formWithTheirDependencies.add(nodesWithChildren);
			}

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
	 * Получение списка идентификаторов полей, отсутствующих в списке
	 * перечисления record'ов, но указанных среди полей списочной или детальной
	 * форм
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
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
				if (formDetailNodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element formDetailNode = (Element) formDetailNodes.item(i);
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
				if (formListNodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element formListNode = (Element) formListNodes.item(i);
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * Детализация элемента-поля GWT-модуля как представителя списочной формы
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
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
				if (node.getNodeType() == ELEMENT_NODE) {
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
	 * @param jepApplicationDoc Файл структуры проекта
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
				if (node.getNodeType() == ELEMENT_NODE) {
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
	 * @param recordField Элемент поле
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * Детализация элемента-поля GWT-модуля
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
	 * @param recordField  Элемент-поле
	 */
	public void detailizedModuleField(Module module, ModuleField recordField) {
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
	 * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
	 * @param module Элемент-модуль
	 * @param moduleNode DOM-элемент модуля
	 */
	public void detailizedModuleForToolBar(Module module, Element moduleNode) {

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
			if (Boolean.FALSE.equals(module.hasToolBarPresenter()) && !isEmpty(toolbarPresenter))
				module.setHasToolBarPresenter(!OFF.equalsIgnoreCase(toolbarPresenter));
			if (Boolean.FALSE.equals(module.hasToolBarView()) && !isEmpty(toolbarView))
				module.setHasToolBarView(!OFF.equalsIgnoreCase(toolbarView));
		}
		if (!isEmpty(nodes)) {
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element node = (Element) nodes.item(i);
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
	 * @param jepApplicationDoc Файл структуры проекта
	 * @return список всех описанных модулей
	 */
	public List<String> getAllModuleNodes(Document jepApplicationDoc) {
		NodeList nodes = null;
		List<String> result = new ArrayList<String>();
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
				if (!result.contains(moduleId)) // avgrishaev: lets try to skip duplicated...
					result.add(moduleId);
			}
		}

		return result;
	}
	
	/**
	 * Получение списка модулей одного Scope
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * Получение списка элементов-полей для модуля
	 * 
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
	 * @param jepApplicationDoc Файл структуры проекта
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
			List<String> absentFields = getListOfAbsentFieldNameByModuleId(formName, moduleFields);
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
}
