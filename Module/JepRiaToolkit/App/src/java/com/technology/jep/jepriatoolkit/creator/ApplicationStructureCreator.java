package com.technology.jep.jepriatoolkit.creator;

import static com.technology.jep.jepriatoolkit.creator.module.ModuleButton.STANDARD_TOOLBAR;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static java.text.MessageFormat.format;
import static org.w3c.dom.Node.ELEMENT_NODE;

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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.FORM;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

@SuppressWarnings("unchecked")
public class ApplicationStructureCreator extends Task implements JepRiaToolkitConstant {

	// Атрибут таска
	private String applicationStructureFile;
	// характеристики приложения, извлекаемые из конфигурационного файла приложения
	public String moduleName, packageName, defaultDataSource;	
	
	public List<String> forms = new ArrayList<String>();
	private List<List<String>> formWithTheirDependencies = new ArrayList<List<String>>();
	private List<String> securityRoles = new ArrayList<String>();
	// Соответствие формы списку полей, где указано наименование поля и его тип
	private Map<Module, List<ModuleField>> formFields = new HashMap<Module, List<ModuleField>>();
	// Ссылка на JepApplication.xml
	private Document jepApplicationDoc = null;

	public static final Map<String, String> FIELD_WIDGET = new HashMap<String, String>();
	
	static {
		// Список предопределенных полей и их соответствующих классов
		// (наименование с пакетом)
		FIELD_WIDGET.put(JEP_DATE_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_DATE_FIELD));
		FIELD_WIDGET.put(JEP_MONEY_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_MONEY_FIELD));
		FIELD_WIDGET.put(JEP_NUMBER_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_NUMBER_FIELD));
		FIELD_WIDGET.put(JEP_INTEGER_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_INTEGER_FIELD));
		FIELD_WIDGET.put(JEP_LONG_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_LONG_FIELD));
		FIELD_WIDGET.put(JEP_TEXT_AREA_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_TEXT_AREA_FIELD));
		FIELD_WIDGET.put(JEP_TEXT_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_TEXT_FIELD));
		FIELD_WIDGET.put(JEP_MASKED_TEXT_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_MASKED_TEXT_FIELD));
		FIELD_WIDGET.put(JEP_TIME_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_TIME_FIELD));
		FIELD_WIDGET.put(JEP_FILE_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, "large.", JEP_FILE_FIELD));
		FIELD_WIDGET.put(JEP_IMAGE_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, "large.", JEP_IMAGE_FIELD));
		FIELD_WIDGET.put(JEP_CHECKBOX_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_CHECKBOX_FIELD));
		FIELD_WIDGET.put(JEP_COMBOBOX_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_COMBOBOX_FIELD));
		FIELD_WIDGET.put(JEP_LIST_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_LIST_FIELD));
		FIELD_WIDGET.put(JEP_TREE_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_TREE_FIELD));
		FIELD_WIDGET.put(JEP_DUAL_LIST_FIELD, JepRiaToolkitUtil.multipleConcat(FIELD_PREFIX, JEP_DUAL_LIST_FIELD));
	}

	/**
	 * Основной метод, который выполняет Task
	 */
	@Override
	public void execute() throws BuildException {
		try {
			JepRiaToolkitUtil.echoMessage("Parsing JepApplication.xml...");

			if (!parseApplicationSettingXML())
				return;
			
			notifyAboutAbsentFields();

			JepRiaToolkitUtil.echoMessage("Create Application Structure for '" + packageName.toLowerCase() + "." + moduleName.toLowerCase() + "' module");
			createApplicationFileStructure();
			JepRiaToolkitUtil.echoMessage("Generate web.xml");
			generateWebXML();
			JepRiaToolkitUtil.echoMessage("Generate application.xml");
			generateApplicationXML();
			JepRiaToolkitUtil.echoMessage("Generate orion-application.xml");
			generateOrionApplicationXML();
			JepRiaToolkitUtil.echoMessage("Create Welcome page!");
			createWelcomePage();
			JepRiaToolkitUtil.echoMessage("Generate xml for GWT-application");
			generateGwtXML();
			JepRiaToolkitUtil.echoMessage("Create Text Files");
			createTextFile();
			JepRiaToolkitUtil.echoMessage("Create File of Overview");
			createOverview();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Client Constant");
			createClientConstant();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Client Factoty");
			createClientFactoryImpl();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Entry Point");
			createEntryPoint();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Detail Form Presenter");
			createDetailFormPresenter();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Detail Form View");
			createDetailFormView();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of List Form Presenter");
			createListFormPresenter();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of List Form View");
			createListFormView();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Module Presenter");
			createModulePresenter();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Service Implementation");
			createServiceImpl();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Server Constant");
			createServerConstant();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of EJB");
			createEJB();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Field Names");
			createFieldNames();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Record Definition");
			createRecordDefinition();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Service");
			createService();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Shared Constant");
			createSharedConstant();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of ToolBar");
			createToolBar();
			JepRiaToolkitUtil.echoMessage("Create Java Classes Of Statusbar");
			createStatusbar();
			JepRiaToolkitUtil.echoMessage("Create client and server side logging");
			createLog4jProperties();
			JepRiaToolkitUtil.echoMessage("Encode Text Resources");
			encodeTextResources();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	/**
	 * Разбор и проверка JepApplication.xml
	 * 
	 * @return флаг успешности/неуспешности парсинга JepApplication.xml
	 * @throws ParserConfigurationException
	 */
	public boolean parseApplicationSettingXML() throws ParserConfigurationException {
		applicationStructureFile = isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile;
		try {
			jepApplicationDoc = JepRiaToolkitUtil.getDOM(applicationStructureFile);

			NodeList nodes = jepApplicationDoc.getElementsByTagName(APPLICATION_TAG_NAME);
			if (JepRiaToolkitUtil.isEmpty(nodes)) {
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! There is no mandatory tag 'application'!"));
				return false;
			}
			Element applicationNode = (Element) nodes.item(0);
			packageName = applicationNode.getAttribute(PROJECT_PACKAGE_ATTRIBUTE);
			if (JepRiaToolkitUtil.isEmpty(packageName)) {
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! There is no mandatory attribute ", PROJECT_PACKAGE_ATTRIBUTE,
						" of tag 'application'!"));
				return false;
			}
			moduleName = applicationNode.getAttribute(APPLICATION_NAME_ATTRIBUTE);
			if (JepRiaToolkitUtil.isEmpty(moduleName)) {
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! There is no mandatory attribute ", APPLICATION_NAME_ATTRIBUTE,
						" of tag 'application'!"));
				return false;
			}
			else if (!moduleName.equals(applicationStructureFile.split(APPLICATION_SETTING_FILE_ENDING)[0])){
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! The attribute ", APPLICATION_NAME_ATTRIBUTE, " '", moduleName,
						"' doesn't match the file name '", this.applicationStructureFile, "'!"));
				return false;
			}
			defaultDataSource = applicationNode.getAttribute(APPLICATION_DATASOURCE_ATTRIBUTE);
			if (JepRiaToolkitUtil.isEmpty(defaultDataSource)) {
				defaultDataSource = DEFAULT_DATASOURCE;
			}

			// инициализация списка форм с их потомками
			forms = getAllModuleNodes();
			for (int index = 0; index < forms.size(); index++) {
				String formName = forms.get(index);
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat("Gather information about module '", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName, "'"));
				
				Element module = getModuleNodeById(formName);
				List<String> moduleRoles = getModuleSecurityRoles(formName);
				// заполнение общего списка ролей, необходимого для генерации
				// web.xml
				for (String moduleRole : moduleRoles) {
					if (!securityRoles.contains(moduleRole))
						securityRoles.add(moduleRole);
				}

				String moduleDataSource = getDataSourceById(formName);
				String modulePackage = getPackageById(formName);

				Module m = new Module(formName, JepRiaToolkitUtil.isEmpty(module.getAttribute(MODULE_NAME_ATTRIBUTE)) ? NO_NAME : module.getAttribute(
						MODULE_NAME_ATTRIBUTE).trim(), JepRiaToolkitUtil.isEmpty(module.getAttribute(MODULE_NAME_EN_ATTRIBUTE)) ? NO_NAME : module
						.getAttribute(MODULE_NAME_EN_ATTRIBUTE).trim(), JepRiaToolkitUtil.isEmpty(moduleDataSource) ? defaultDataSource : moduleDataSource,
						moduleRoles, JepRiaToolkitUtil.isEmpty(modulePackage) ? JepRiaToolkitUtil.multipleConcat(PKG_PREFIX, moduleName.toLowerCase())
								: modulePackage);
				m.setNotRebuild(OFF.equalsIgnoreCase(module.getAttribute(MODULE_BUILD_ATTRIBUTE)));
				m.setTable(getTableById(formName));
				// Инициализация данных о тулбаре
				detailizedModuleForToolBar(m, module);

				List<ModuleField> mfList = new ArrayList<ModuleField>();
				List<Element> fields = getModuleFieldsById(formName);
				if (!JepRiaToolkitUtil.isEmpty(fields))
					for (int i = 0; i < fields.size(); i++) {
						Element field = (Element) fields.get(i);
						String fieldId = JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_ID_ATTRIBUTE)) ? new String() : field
								.getAttribute(FIELD_ID_ATTRIBUTE).trim().toUpperCase();
						ModuleField mf = new ModuleField(formName, fieldId, field.getAttribute(FIELD_TYPE_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_NAME_ATTRIBUTE)) ? NO_NAME : field.getAttribute(FIELD_NAME_ATTRIBUTE)
										.trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_NAME_EN_ATTRIBUTE)) ? JepRiaToolkitUtil.getNameFromID(fieldId) : field
										.getAttribute(FIELD_NAME_EN_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_LIKE_ATTRIBUTE)) ? new String() : field.getAttribute(
										FIELD_LIKE_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_WIDGET_ATTRIBUTE)) ? DEFAULT_WIDGET : field.getAttribute(
										FIELD_WIDGET_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_MAX_LENGTH_ATTRIBUTE)) ? new String() : field.getAttribute(
										FIELD_MAX_LENGTH_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_WIDTH_ATTRIBUTE)) ? new String() : field.getAttribute(
										FIELD_WIDTH_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE)) ? new String() : field.getAttribute(
										FIELD_LABEL_WIDTH_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_HEIGHT_ATTRIBUTE)) ? new String() : field.getAttribute(
										FIELD_HEIGHT_ATTRIBUTE).trim(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase(), 
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE).trim().toUpperCase(), 
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase(),
								JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE)) ? new String() : field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE).trim().toUpperCase());
						
						if (!JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_LIKE_ATTRIBUTE)) && !m.hasLikeFields()) {
							m.setHasLikeFields(true);
						}
						mf.setPrimaryKey(fieldId.equalsIgnoreCase(getPrimaryKeyById(formName)));
						// детализируем поле после парсинга форм: детальной и
						// списочной
						detailizedModuleField(m, mf);
						mfList.add(mf);
					}

				// добавляем соответствие модуля его списку полей
				formFields.put(m, mfList);
			}

			for (int i = 0; i < forms.size(); i++) {
				List<String> nodesWithChildren = getNodesWithChildren(forms.get(i));
				if (!JepRiaToolkitUtil.isEmpty(nodesWithChildren))
					formWithTheirDependencies.add(nodesWithChildren);
			}

			return true;
		} catch (IOException e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", applicationStructureFile,
					" is not found!! Fill and put it in Application Directory!"));
			return false;
		} catch (SAXException e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", applicationStructureFile,
					" is not valid!! Check and correct it!"));
			return false;
		}
	}

	/**
	 * Оповещение о полях, присутствующих на детальной или списочной формах, но
	 * не указанных в секции &lt;record&gt;
	 */
	private void notifyAboutAbsentFields() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = forms.get(i);
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			List<ModuleField> moduleFields = hm.values().iterator().next();
			List<String> absentFields = getListOfAbsentFieldNameByModuleId(formName, moduleFields);
			if (!absentFields.isEmpty()) {
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(WARNING_PREFIX, "Field", (absentFields.size() > 1 ? "s" : ""), " " + absentFields
						+ " ", (absentFields.size() > 1 ? "are" : "is"), " absent in 'record' section for module with ID : '", formName, "'"));
			}
		}
	}

	/**
	 * Создание файловой структуры приложения
	 */
	private void createApplicationFileStructure() {

		JepRiaToolkitUtil.makeDir(getDefinitionProperty(LIB_DIRECTORY_PROPERTY, LIB_DIR_NAME));

		JepRiaToolkitUtil.makeDir(getDefinitionProperty(HTML_DIRECTORY_PROPERTY, WELCOME_PAGE_DIR_NAME));
		
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(RESOURCE_DIRECTORY_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/web")),
				packageName.toLowerCase(), moduleName.toLowerCase() 	
			)
		);
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(MAIN_MODULE_DIRECTORY_PROPERTY,
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/client/ui/main")),
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(ENTRANCE_DIRECTORY_PROPERTY,
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/client/entrance")),
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_DIRECTORY_PROPERTY,
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/shared/text")),
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);

		if (forms.size() == 0) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(WARNING_PREFIX, "Tag-parameter '", MODULE_TAG_NAME,
					"' is desirable. To create structure for forms, fill this parameter in ", applicationStructureFile, "!"));
			return;
		}

		for (int i = 0; i < forms.size(); i++) {
			String formName = ((String) forms.get(i)).toLowerCase();
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_DIRECTORY_PROPERTY,
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/form/detail")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_DIRECTORY_PROPERTY,
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/form/list")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_EJB_DIRECTORY_PROPERTY,
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/server/ejb")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_DIRECTORY_PROPERTY,
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/server/service")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_FIELD_DIRECTORY_PROPERTY,	
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/field")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DIRECTORY_PROPERTY,	
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/record")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_DIRECTORY_PROPERTY,	
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/service")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_DIRECTORY_PROPERTY,	
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/text")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
		}
		
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(CONFIG_HTML_DIRECTORY_PROPERTY, "config/{0}/src/html"),
				DEBUG_BUILD_CONFIG_NAME
			)
		);
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")),
				DEBUG_BUILD_CONFIG_NAME, packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
		
		String deployPropContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + DEBUG_BUILD_CONFIG_NAME + "/deploy.properties", UTF_8);
		JepRiaToolkitUtil.writeToFile(deployPropContent, JepRiaToolkitUtil.multipleConcat("config/"+ DEBUG_BUILD_CONFIG_NAME + "/deploy.properties"), UTF_8, false);

		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(CONFIG_HTML_DIRECTORY_PROPERTY, "config/{0}/src/html"),
				RELEASE_BUILD_CONFIG_NAME
			)
		);
		JepRiaToolkitUtil.makeDir(
			format(
				getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")),
				RELEASE_BUILD_CONFIG_NAME, packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
		deployPropContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + RELEASE_BUILD_CONFIG_NAME + "/deploy.properties", UTF_8);
		JepRiaToolkitUtil.writeToFile(deployPropContent, JepRiaToolkitUtil.multipleConcat("config/"+ RELEASE_BUILD_CONFIG_NAME + "/deploy.properties"), UTF_8, false);

	}

	/**
	 * Генерация дескриптора развертывания web.xml
	 */
	private void generateWebXML() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(WEB_XML_TEMPLATE_PROPERTY, "web.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(WEB_XML_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/web/web.xml")
				), 
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
	}

	/**
	 * Создание application.xml
	 */
	private void generateApplicationXML() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(APPLICATION_XML_TEMPLATE_PROPERTY, "application.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/application.xml")
				), 
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
	}

	/**
	 * Создание orion-application.xml
	 */
	private void generateOrionApplicationXML() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(ORION_APPLICATION_XML_TEMPLATE_PROPERTY, "orion-application.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(ORION_APPLICATION_XML_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/orion-application.xml")
				), 
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
	}

	/**
	 * Создание основного GWT.xml для всего приложения, описывающего его
	 * структуру
	 */
	private void generateMainGwtXML() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{3}.gwt.xml")
				), 
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			prepareData(),
			JepRiaToolkitUtil.multipleConcat(
				"config/", DEBUG_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/", moduleName, ".gwt.xml")
		);
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_PRODUCTION_TEMPLATE_PROPERTY, "mainProduction.gwt.ftl"), 
			prepareData(),
			JepRiaToolkitUtil.multipleConcat(
				"config/", RELEASE_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/", moduleName, ".gwt.xml")
		);
	}

	/**
	 * Генерация всех необходимых xml-документов для данного приложения (включая
	 * основной и дочерние)
	 */
	private void generateGwtXML() {
		generateMainGwtXML();
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			Map<String, Object> data = prepareData();
			data.put(FORM_NAME_TEMPLATE_PARAMETER, formName);
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_GWT_XML_TEMPLATE_PROPERTY, "module.gwt.ftl"), 
				data, 
				format(
					getDefinitionProperty(CLIENT_MODULE_GWT_XML_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/{3}.gwt.xml")
					),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание страницы приветствия в зависимости от флага продукционности
	 * сборки
	 */
	private void createWelcomePage() {
		// JSP
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(APPLICATION_JSP_TEMPLATE_PROPERTY, "applicationJsp.ftl"), 
			prepareData(), 
			format(
				getDefinitionProperty(APPLICATION_JSP_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/{0}.jsp")),
				moduleName
			)
		);
		
		//CSS debug
		String cssTemplateDebugContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + DEBUG_BUILD_CONFIG_NAME + "/src/html/" + CSS_TEMPLATE_NAME, UTF_8);
		JepRiaToolkitUtil.writeToFile(cssTemplateDebugContent,
			format(
				getDefinitionProperty(APPLICATION_CSS_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/{0}.css")
				),
				moduleName
			), UTF_8, false);
		JepRiaToolkitUtil.writeToFile(cssTemplateDebugContent, JepRiaToolkitUtil.multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), UTF_8, false);

		//CSS release
		String cssTemplateReleaseContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + RELEASE_BUILD_CONFIG_NAME + "/src/html/" + CSS_TEMPLATE_NAME, UTF_8);
		JepRiaToolkitUtil.writeToFile(cssTemplateReleaseContent, JepRiaToolkitUtil.multipleConcat("config/", RELEASE_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), UTF_8, false);
	}

	/**
	 * Создание текстовых ресурсов
	 */
	private void createTextFile() {
		Map<String, Object> data = prepareData();
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_TEXT_RESOURCE_TEMPLATE_PROPERTY, "mainText.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_Source.properties")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY, "mainTextEn.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_en.properties")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
		
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			String formName = moduleInfo.getFormName();
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_TEMPLATE_PROPERTY, "clientModuleText.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY, "clientModuleTextEn.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание страницы обзора для приложения
	 */
	private void createOverview() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(OVERVIEW_TEMPLATE_PROPERTY, "overview.ftl"),
			prepareData(),
			format(
				getDefinitionProperty(OVERVIEW_PATH_TEMPLATE_PROPERTY, "src/java/com/technology/{0}/{1}/overview.html"),
				packageName.toLowerCase(), moduleName.toLowerCase()
			)
		);
	}

	/**
	 * Создание классов клиенстких констант
	 */
	private void createClientConstant() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_CONSTANT_TEMPLATE_PROPERTY, "mainModuleConstant.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/{2}ClientConstant.java")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_CONSTANT_TEMPLATE_PROPERTY, "clientModuleConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/{3}ClientConstant.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов клиентских фабрик
	 */
	private void createClientFactoryImpl() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_FACTORY_TEMPLATE_PROPERTY, "clientModuleFactory.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/{3}ClientFactoryImpl.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}

		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_FACTORY_TEMPLATE_PROPERTY, "mainFactory.ftl"),
			data,
			format(
				getDefinitionProperty(MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/{3}ClientFactoryImpl.java")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
	}

	/**
	 * Создание класса точки входа
	 */
	private void createEntryPoint() {
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MODULE_ENTRY_POINT_TEMPLATE_PROPERTY, "moduleEntryPoint.ftl"),
			prepareData(),
			format(
				getDefinitionProperty(MODULE_ENTRY_POINT_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/entrance/{3}EntryPoint.java")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
	}

	/**
	 * Создание классов презентеров детальных форм
	 */
	private void createDetailFormPresenter() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_TEMPLATE_PROPERTY, "clientModuleDetailFormPresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormPresenter.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов представлений детальных форм
	 */
	private void createDetailFormView() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_TEMPLATE_PROPERTY, "clientModuleDetailFormView.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormView.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_TEMPLATE_PROPERTY, "clientModuleDetailFormViewImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormViewImpl.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов презентеров списочных форм
	 */
	private void createListFormPresenter() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild() || !moduleInfo.getHasCustomListFormPresenter()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_LIST_FORM_PRESENTER_TEMPLATE_PROPERTY, "clientModuleListFormPresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/list/{3}ListFormPresenter.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов представлений списочных форм
	 */
	private void createListFormView() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_TEMPLATE_PROPERTY, "clientModuleListFormViewImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/list/{3}ListFormViewImpl.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов презентеров модулей
	 */
	private void createModulePresenter() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild() || JepRiaToolkitUtil.isEmpty(moduleInfo.getToolBarCustomButtonsOnBothForms())) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			// создадим сперва каталог
			JepRiaToolkitUtil.makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_PLAIN_FORM_DIRECTORY_PROPERTY,
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/plain")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName
				)
			);
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_PRESENTER_TEMPLATE_PROPERTY, "clientModulePresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/plain/{3}ModulePresenter.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_PRESENTER_TEMPLATE_PROPERTY, "mainModulePresenter.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/ui/main/{2}MainModulePresenter.java")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
	}

	/**
	 * Создание классов реализаций сервисов приложения
	 */
	private void createServiceImpl() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleServiceImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/{3}ServiceImpl.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			if (moduleInfo.getHasLobFields()){
				JepRiaToolkitUtil.convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_UPLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleUploadServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_UPLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/UploadServiceImpl.java")),
						packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase()
					)
				);
				
				JepRiaToolkitUtil.convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleDownloadServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/DownloadServiceImpl.java")),
						packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase()
					)
				);
			}
			
			if (moduleInfo.getIsExcelAvailable()){
				JepRiaToolkitUtil.convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_EXCEL_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleExcelServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_EXCEL_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/ShowExcelServlet.java")),
						packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase()
					)
				);
			}
		}
	}

	/**
	 * Создание классов серверных констант
	 */
	private void createServerConstant() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_TEMPLATE_PROPERTY, "clientModuleServerConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/{3}ServerConstant.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов EJB приложения
	 */
	private void createEJB() {
		createEjbInterface();
		createLocalEjb();
		createRemoteEjb();
		createEjbClass();
	}

	/**
	 * Создание классов EJB удаленного интерфейса
	 */
	private void createRemoteEjb() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_REMOTE_EJB_TEMPLATE_PROPERTY, "clientModuleRemote.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_REMOTE_EJB_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/ejb/{3}Remote.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов EJB локального интерфейса
	 */
	private void createLocalEjb() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_LOCAL_EJB_TEMPLATE_PROPERTY, "clientModuleLocal.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_LOCAL_EJB_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/ejb/{3}Local.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов реализаций EJB
	 */
	private void createEjbClass() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_EJB_TEMPLATE_PROPERTY, "clientModuleEjb.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_EJB_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/ejb/{3}Bean.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов EJB домашнего интерфейса
	 */
	private void createEjbInterface() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_EJB_INTERFACE_TEMPLATE_PROPERTY, "clientModuleEjbInterface.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_EJB_INTERFACE_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/ejb/{3}.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов, содержащих имена полей модулей
	 */
	private void createFieldNames() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_FIELDS_TEMPLATE_PROPERTY, "clientModuleFields.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}FieldNames.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			if (moduleInfo.getHasOptionField()){
				for (ModuleField field : moduleInfo.getFields()){
					if (field.getIsOptionField()){
						innerData.put(FIELD_TEMPLATE_PARAMETER, field);
						JepRiaToolkitUtil.convertTemplateToFile(
							getDefinitionProperty(CLIENT_MODULE_OPTIONS_TEMPLATE_PROPERTY, "clientModuleOptions.ftl"),
							innerData, 
							format(
								getDefinitionProperty(CLIENT_MODULE_OPTIONS_PATH_TEMPLATE_PROPERTY, 
										JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}Options.java")),
								packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), field.getFieldIdAsParameter()
							)
						);
					}
				}
			}
		}
	}

	/**
	 * Создание классов описания записей
	 */
	private void createRecordDefinition() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_TEMPLATE_PROPERTY, "clientModuleRecordDefinition.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/record/{3}RecordDefinition.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов сервисов модуля
	 */
	private void createService() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_TEMPLATE_PROPERTY, "clientModuleService.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/service/{3}Service.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_ASYNC_TEMPLATE_PROPERTY, "clientModuleServiceAsync.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_ASYNC_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/service/{3}ServiceAsync.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов разделяемых констант (клиентско-серверных)
	 */
	private void createSharedConstant() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		
		JepRiaToolkitUtil.convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY, "mainModuleSharedConstant.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY, 
						JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/{2}Constant.java")),
				packageName.toLowerCase(), moduleName.toLowerCase(), moduleName
			)
		);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY, "clientModuleSharedConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/{3}Constant.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов инструментальной панели
	 */
	private void createToolBar() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			boolean isStandardToolBar = module.isStandardToolBar();
			boolean isToolBarOff = module.isToolBarOff();
			boolean hasCustomButtons = module.hasCustomButtons();
			boolean hasToolBarPresenter = module.hasToolBarPresenter();
			boolean hasToolBarView = module.hasToolBarView();
			if (!isStandardToolBar || isToolBarOff || hasToolBarPresenter || hasToolBarView) {
				// create file structure
				JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName, "/client/ui/toolbar", (hasCustomButtons ? "/images" : "")));
				if (hasCustomButtons) {
					JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/",
							moduleName.toLowerCase(), "/", formName, "/client/ui/eventbus/event"));
				}

				String additionalStringInPresenter = new String();
				String additionalStringInView = new String();
				List<ModuleButton> toolbarButtons = module.getToolBarButtons();

				String customButtonDescription = ""; 
				for (ModuleButton button : module.getToolBarCustomButtons()) {
					customButtonDescription += JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE, "	final static String ", button.getButtonId()
							.toUpperCase(), " = \"", JepRiaToolkitUtil.getFieldIdAsParameter(button.getButtonId(), null), "\";");
				}

				if (!isToolBarOff) {
					for (ModuleButton button : toolbarButtons) {
						boolean isCustomToolBarButton = button.isCustomButton();
						if (!button.isSeparator()) {
							additionalStringInPresenter += JepRiaToolkitUtil.multipleConcat(
									"		bindButton(", END_OF_LINE,
									"			", button.getButtonId(), ", ", END_OF_LINE,
									"			new WorkstateEnum[]{",
									(JepRiaToolkitUtil.isEmpty(button.getWorkStatesAsString()) ? (isCustomToolBarButton ? "" : STANDARD_TOOLBAR.get(
											button.getButtonId()).getWorkStatesAsString()) : button.getWorkStatesAsString()),
									"},", END_OF_LINE,
									"			new ClickHandler() {", END_OF_LINE,
									"				public void onClick(ClickEvent event) {",END_OF_LINE,
									"					",
									(JepRiaToolkitUtil.isEmpty(button.getEvent()) ? (isCustomToolBarButton ? "" : STANDARD_TOOLBAR.get(button.getButtonId()).getEvent()) : !isCustomToolBarButton
											&& button.getEvent().toLowerCase().startsWith("changeworkstate") ? STANDARD_TOOLBAR.get(button.getButtonId()).getEvent() : JepRiaToolkitUtil.multipleConcat(
													(button.getEvent().toLowerCase().startsWith("placecontroller") ? "" : "eventBus."), button.getEvent(), (button.getEvent().endsWith(";") ? "" : ";"))), END_OF_LINE, 
									"				}", END_OF_LINE, 
									"			});", END_OF_LINE,
									WHITE_SPACE, END_OF_LINE);
						} else if (button.isCustomSeparator()) {
							customButtonDescription += JepRiaToolkitUtil.multipleConcat(
									WHITE_SPACE, END_OF_LINE, 
									"	final static String ", button.getButtonId().toUpperCase(), " = \"", JepRiaToolkitUtil.getFieldIdAsParameter(button.getButtonId(), null), "\";");
						}

						additionalStringInView += button.isSeparator() ? JepRiaToolkitUtil.multipleConcat(
								"		addSeparator(", button.getButtonId().toUpperCase(), ");", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat(
								"		addButton(",END_OF_LINE,
								"			", button.getButtonId().toUpperCase(), ", ", END_OF_LINE,
								(JepRiaToolkitUtil.isEmpty(button.getImage()) ? (isCustomToolBarButton ? "" : JepRiaToolkitUtil.isEmpty(STANDARD_TOOLBAR.get(
										button.getButtonId()).getImage()) ? "" : JepRiaToolkitUtil.multipleConcat("			JepImages.",
										STANDARD_TOOLBAR.get(button.getButtonId()).getImage(), "(),", END_OF_LINE)) : JepRiaToolkitUtil.multipleConcat(
										"			", (isCustomToolBarButton ? JepRiaToolkitUtil.initSmall(formName) : "Jep"), "Images.", button.getImage(),
										"(),", END_OF_LINE)),
//								(JepRiaToolkitUtil.isEmpty(button.getImageDisabled()) || !isCustomToolBarButton ? "" : JepRiaToolkitUtil.multipleConcat("			",
//										JepRiaToolkitUtil.initSmall(formName), "Images.", button.getImageDisabled(), "(),", END_OF_LINE)),
								(JepRiaToolkitUtil.isEmpty(button.getText()) ? (isCustomToolBarButton ? JepRiaToolkitUtil
										.multipleConcat("			\"\");", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("			JepTexts.",
										STANDARD_TOOLBAR.get(button.getButtonId()).getText(), "());", END_OF_LINE)) : JepRiaToolkitUtil.multipleConcat(
										"			", (isCustomToolBarButton ? JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), "Text.")
												: "JepTexts."), button.getText(), "());", END_OF_LINE)));
					}
				}

				if (!JepRiaToolkitUtil.isEmpty(customButtonDescription)) {
					customButtonDescription += JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE);
				}

				// toolbar view
				String contentView = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.toolbar;",  END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"public interface ", formName, "ToolBarView extends ToolBarView {", END_OF_LINE,
						WHITE_SPACE,
						customButtonDescription,
						END_OF_LINE, "}");

				if (module.isNotRebuild())
					continue;
				if (!isStandardToolBar || isToolBarOff || hasToolBarView)
					JepRiaToolkitUtil.writeToFile(contentView, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
							"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/toolbar/", formName, "ToolBarView.java"));

				// toolbar view impl
				String contentViewImpl = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.toolbar;",  END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.*;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;", END_OF_LINE,
						(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
							"import static com.technology.", packageModuleFormName, ".client.", formName, "ClientConstant.", JepRiaToolkitUtil.initSmall(formName), "Text;", END_OF_LINE, 
							"import com.google.gwt.core.client.GWT;", END_OF_LINE,
							"import com.technology.", packageModuleFormName, ".client.ui.toolbar.images.", formName, "Images;", END_OF_LINE) : ""),
						"import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;", END_OF_LINE,
						"import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"public class ", formName, "ToolBarViewImpl extends ToolBarViewImpl implements ", formName, "ToolBarView {", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						hasCustomButtons ?
						JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE, "	public static final ",
								formName, "Images ", JepRiaToolkitUtil.initSmall(formName), "Images = GWT.create(", formName, "Images.class);", END_OF_LINE) : "",
						WHITE_SPACE, END_OF_LINE,

						"	public ", formName, "ToolBarViewImpl() {", END_OF_LINE,
						"		super();", END_OF_LINE,
						hasToolBarView && module.getToolBarButtons().size() == 0 ? "" : JepRiaToolkitUtil.multipleConcat(
								"		removeAll();", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE),
						isToolBarOff ? JepRiaToolkitUtil.multipleConcat("		asWidget().setVisible(false);", END_OF_LINE) : additionalStringInView, "	}", END_OF_LINE, "}");

				if (module.isNotRebuild())
					continue;
				if (!isStandardToolBar || isToolBarOff || hasToolBarView)
					JepRiaToolkitUtil.writeToFile(contentViewImpl, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
							"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/toolbar/", formName, "ToolBarViewImpl.java"));

				String customButtonImport = "";
				for (ModuleButton button : module.getToolBarCustomButtons()) {
					customButtonImport += JepRiaToolkitUtil.multipleConcat("import static com.technology.", packageName.toLowerCase(), ".", moduleName
							.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.toolbar.", formName, "ToolBarView.", button.getButtonId()
							.toUpperCase(), ";", END_OF_LINE);
				}
				// toolbar presenter
				String contentPresenter = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.toolbar;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						customButtonImport,
						"import com.google.gwt.place.shared.Place;", END_OF_LINE,
						"import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;", END_OF_LINE,
						"import static com.technology.jep.jepria.client.ui.toolbar.ToolBarView.*;", END_OF_LINE,
						"import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.*;", END_OF_LINE,
						
						"import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;", END_OF_LINE,
						"import com.google.gwt.event.dom.client.ClickEvent;", END_OF_LINE,
						"import com.google.gwt.event.dom.client.ClickHandler;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.WorkstateEnum;", END_OF_LINE, 

						(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat("import com.technology.", packageName.toLowerCase(), ".",
								moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.eventbus.", formName, "EventBus;", END_OF_LINE)
								: JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE)),
						"import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;", END_OF_LINE,
						(isToolBarOff ? "" : JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.client.history.place.*;", END_OF_LINE)),
						"import com.technology.", packageModuleFormName, ".shared.service.", formName, "ServiceAsync;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"public class ", formName, "ToolBarPresenter<V extends ToolBarView, E extends ", (hasCustomButtons ? formName : "Plain"), "EventBus, S extends ", formName, "ServiceAsync, F extends StandardClientFactory<E, S>>", END_OF_LINE,
						"	extends ToolBarPresenter<V, E, S, F> {", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						" 	public ", formName, "ToolBarPresenter(Place place, F clientFactory) {", END_OF_LINE,
						"		super(place, clientFactory);", END_OF_LINE,
						"	}", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						(isToolBarOff || hasToolBarPresenter ? "" : JepRiaToolkitUtil.multipleConcat(
								"	public void bind() {", END_OF_LINE,
								additionalStringInPresenter, "	}", END_OF_LINE)), "}");

				if (module.isNotRebuild())
					continue;
				if (!isStandardToolBar || isToolBarOff || hasToolBarPresenter)
					JepRiaToolkitUtil.writeToFile(contentPresenter, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/",
							packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/toolbar/", formName,
							"ToolBarPresenter.java"));

			}
			// Если есть пользовательские кнопки, создадим необходимые
			// изменения:
			// файл ресурсов, кастомные EventBus, события
			if (hasCustomButtons) {
				String resource = "";
				String importEvent = "";
				String event = "";

				for (ModuleButton button : module.getToolBarCustomButtons()) {
					resource += JepRiaToolkitUtil.multipleConcat(
							WHITE_SPACE, END_OF_LINE,
							"	@Source(\"", button.getImage(), ".png\")", END_OF_LINE,
							"	ImageResource ", button.getImage(), "();", END_OF_LINE
//							(JepRiaToolkitUtil.isEmpty(button.getImageDisabled()) ? "" : JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE,
//									"	@Resource(\"", button.getImageDisabled(), ".png\")", END_OF_LINE, 
//									"	ImageResource ", button.getImageDisabled(), "();", END_OF_LINE))
							);

					importEvent += JepRiaToolkitUtil.multipleConcat(
							"import com.technology.", packageModuleFormName, ".client.ui.eventbus.event.", button.getCustomEvent(), "Event;", END_OF_LINE);

					event += JepRiaToolkitUtil.multipleConcat(
							WHITE_SPACE, END_OF_LINE, 
							"	public void ", button.getEvent(), " { ", END_OF_LINE,
							"		fireEvent(new ", button.getCustomEvent(), "Event());", END_OF_LINE, 
							"	}", END_OF_LINE);

					if (!module.isNotRebuild()) {
						String contentEvent = JepRiaToolkitUtil.multipleConcat(
								"package com.technology.", packageModuleFormName, ".client.ui.eventbus.event;", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE, 
								"import com.google.gwt.event.shared.EventHandler;", END_OF_LINE,
								"import com.technology.jep.jepria.client.ui.eventbus.BusEvent;", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE,
								"public class ", button.getCustomEvent(), "Event extends BusEvent<", button.getCustomEvent(), "Event.Handler> {", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE, 
								"	public interface Handler extends EventHandler {", END_OF_LINE, 
								"		void on", button.getCustomEvent(), "Event(", button.getCustomEvent(), "Event event);", END_OF_LINE, 
								"	}", END_OF_LINE,
								WHITE_SPACE, END_OF_LINE, 
								"	public static final Type<Handler> TYPE = new Type<Handler>();", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE, 
								"	@Override", END_OF_LINE, 
								"	public Type<Handler> getAssociatedType() {", END_OF_LINE, 
								"		return TYPE;", END_OF_LINE, 
								"	}", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE, 
								"	@Override", END_OF_LINE,
								"	protected void dispatch(Handler handler) {", END_OF_LINE, 
								"		handler.on", button.getCustomEvent(), "Event(this);", END_OF_LINE, 
								"	}", END_OF_LINE, 
								"}");

						JepRiaToolkitUtil.writeToFile(contentEvent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/",
								packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/eventbus/event/",
								button.getCustomEvent(), "Event.java"));
					}

				}
				// image bundle
				String contentImages = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.toolbar.images;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"import com.google.gwt.resources.client.ImageResource;", END_OF_LINE,
						"import com.technology.jep.jepria.client.images.JepImages;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"public interface ", formName, "Images extends JepImages {", END_OF_LINE, 
						WHITE_SPACE, 
						resource, 
						WHITE_SPACE, END_OF_LINE,
						"}");

				if (module.isNotRebuild())
					continue;
				JepRiaToolkitUtil.writeToFile(contentImages, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
						"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/toolbar/images/", formName, "Images.java"));

				// ----

				String contentEventBus = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.eventbus;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;", END_OF_LINE, 
						importEvent, 
						WHITE_SPACE, END_OF_LINE,
						"public class ", formName, "EventBus extends PlainEventBus {", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"	public ", formName, "EventBus(PlainClientFactory<?, ?> clientFactory) {", END_OF_LINE, 
						"		super(clientFactory);", END_OF_LINE, 
						"	}", END_OF_LINE,
						event, 
						WHITE_SPACE, END_OF_LINE, 
						"}");

				if (module.isNotRebuild())
					continue;
				JepRiaToolkitUtil.writeToFile(contentEventBus, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
						"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/eventbus/", formName, "EventBus.java"));

			}

		}
	}

	/**
	 * Создание классов статусной панели
	 */
	private void createStatusbar() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild() || !moduleInfo.getIsStatusBarOff()) continue;
			
			String formName = moduleInfo.getFormName();
			
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName, "/client/ui/statusbar"));
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			JepRiaToolkitUtil.convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_STATUSBAR_TEMPLATE_PROPERTY, "clientModuleStatusBar.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_STATUSBAR_PATH_TEMPLATE_PROPERTY, 
							JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/statusbar/{3}StatusBarViewImpl.java")),
					packageName.toLowerCase(), moduleName.toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание файла, хранящего настройки для log4j
	 */
	private void createLog4jProperties() {
		//debug
		String log4jDebugTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + DEBUG_BUILD_CONFIG_NAME + "/src/java/log4j.properties", UTF_8);
		log4jDebugTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(log4jDebugTemplateContent, packageName, moduleName);
		JepRiaToolkitUtil.writeToFile(log4jDebugTemplateContent, JepRiaToolkitUtil.multipleConcat("src/java/log4j.properties"), UTF_8, false);
		JepRiaToolkitUtil.writeToFile(log4jDebugTemplateContent, JepRiaToolkitUtil.multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/src/java/log4j.properties"), UTF_8, false);
		
		//release
		String log4jReleaseTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/config/" + RELEASE_BUILD_CONFIG_NAME + "/src/java/log4j.properties", UTF_8);
		log4jReleaseTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(log4jReleaseTemplateContent, packageName, moduleName);
		JepRiaToolkitUtil.writeToFile(log4jReleaseTemplateContent, JepRiaToolkitUtil.multipleConcat("config/", RELEASE_BUILD_CONFIG_NAME, "/src/java/log4j.properties"), UTF_8, false);
	}

	/**
	 * Конвертация текстовых ресурсов
	 */
	private void encodeTextResources() {
		JepRiaToolkitUtil.runAntTarget("all-text-encode");
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
	 * @param formName
	 *            требуемая форма
	 * @return список имеющихся зависимостей
	 */
	private List<String> getDependencyNodesIfExists(String formName) {
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
	 * Получение списка всех модулей, описанных в файле настроек сборщика
	 * приложения
	 * 
	 * @return список всех описанных модулей
	 */
	private List<String> getAllModuleNodes() {
		NodeList nodes = null;
		List<String> result = new ArrayList<String>();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME));
			nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Element node = (Element) nodes.item(i);
			String moduleId = node.getAttribute(MODULE_ID_ATTRIBUTE);
			if (!JepRiaToolkitUtil.isEmpty(moduleId)) {
				if (!result.contains(moduleId)) // avgrishaev: lets try to skip duplicated...
					result.add(moduleId);
			}
		}

		return result;
	}

	/**
	 * Получение списка модулей одного Scope
	 * @param moduleId
	 *            идентификатор модуля, для которого имеется множество вложенных
	 *            подмодулей
	 * 
	 * @return список модулей
	 */
	private List<String> getNodesWithChildren(String moduleId) {
		NodeList nodes = null;
		List<String> result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", MODULE_TAG_NAME));
			nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		if (nodes.getLength() > 0) {
			result = new ArrayList<String>();
			result.add(moduleId.trim());
			for (int i = 0; i < nodes.getLength(); i++) {
				String modulId = ((Element) nodes.item(i)).getAttribute(MODULE_ID_ATTRIBUTE);
				if (JepRiaToolkitUtil.isEmpty(modulId))
					JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Attribute ", MODULE_ID_ATTRIBUTE,
							" is mandatory! Please fill it."));
				else
					result.add(modulId.trim());
			}
		}

		return result;
	}

	/**
	 * Получение XML-элемента, являющегося GWT-модулем по его ID
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return элемент-модуль
	 */
	private Element getModuleNodeById(String moduleId) {
		Element module = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']"));
			module = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return module;
	}

	/**
	 * Получение списка элементов-полей для модуля
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return список элементов-полей
	 */
	private List<Element> getModuleFieldsById(String moduleId) {
		NodeList fields = null;
		List<Element> result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record/field"));
			fields = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
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
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return значение первичного ключа
	 */
	private String getPrimaryKeyById(String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record[@", MODULE_PRIMARY_KEY_ATTRIBUTE, "]/@", MODULE_PRIMARY_KEY_ATTRIBUTE));
			result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim().toUpperCase() : null;
	}

	/**
	 * Получение наименования таблицы для модуля
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return наименование таблицы
	 */
	private String getTableById(String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record[@", MODULE_TABLE_NAME_ATTRIBUTE, "]/@", MODULE_TABLE_NAME_ATTRIBUTE));
			result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim().toLowerCase() : null;
	}

	/**
	 * Получение списка ролей GWT-модуля
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return список ролей
	 */
	private List<String> getModuleSecurityRoles(String moduleId) {
		NodeList result = null;
		List<String> moduleRoles = new ArrayList<String>();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", MODULE_ROLES_ATTRIBUTE, "/", MODULE_ROLE_ATTRIBUTE));
			result = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);
			for (int i = 0; i < result.getLength(); i++) {
				moduleRoles.add(((Node) result.item(i)).getTextContent().trim());
			}

		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return moduleRoles;
	}

	/**
	 * Получение источника данных для GWT-модуля
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return источник данных
	 */
	private String getDataSourceById(String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", MODULE_DATASOURCE_ATTRIBUTE, "]/@", MODULE_DATASOURCE_ATTRIBUTE));
			result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim() : null;
	}

	/**
	 * Получение наименования пакета для GWT-модуля
	 * @param moduleId
	 *            идентификатор модуля
	 * 
	 * @return наименование пакета
	 */
	private String getPackageById(String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DB_PACKAGE_ATTRIBUTE, "]/@", DB_PACKAGE_ATTRIBUTE));
			result = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim() : null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
	 * @param module
	 *            Элемент-модуль
	 * @param moduleNode
	 *            DOM-элемент модуля
	 */
	private void detailizedModuleForToolBar(Module module, Element moduleNode) {

		module.setToolBarOff(OFF.equalsIgnoreCase(moduleNode.getAttribute(MODULE_TOOLBAR_ATTRIBUTE)));
		module.setStatusbarOff(OFF.equalsIgnoreCase(moduleNode.getAttribute(MODULE_STATUSBAR_ATTRIBUTE)));

		String moduleId = module.getModuleId();
		NodeList nodes = null;
		Element toolbar = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", TOOLBAR_TAG_NAME, "/", BUTTON_TAG_NAME, " | ", "//", MODULE_TAG_NAME, "[normalize-space(@",
					MODULE_ID_ATTRIBUTE, ") = '", moduleId, "']", "/", TOOLBAR_TAG_NAME, "/", SEPARATOR_TAG_NAME));
			nodes = (NodeList) expr.evaluate(jepApplicationDoc, XPathConstants.NODESET);

			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", TOOLBAR_TAG_NAME));
			toolbar = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		if (!JepRiaToolkitUtil.isEmpty(toolbar)) {
			String toolbarPresenter = toolbar.getAttribute(TOOLBAR_PRESENTER_ATTRIBUTE), toolbarView = toolbar.getAttribute(TOOLBAR_VIEW_ATTRIBUTE);
			if (!module.hasToolBarPresenter() && !JepRiaToolkitUtil.isEmpty(toolbarPresenter))
				module.setHasToolBarPresenter(!OFF.equalsIgnoreCase(toolbarPresenter));
			if (!module.hasToolBarView() && !JepRiaToolkitUtil.isEmpty(toolbarView))
				module.setHasToolBarView(!OFF.equalsIgnoreCase(toolbarView));
		}
		if (!JepRiaToolkitUtil.isEmpty(nodes)) {
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element node = (Element) nodes.item(i);
					String buttonId = node.getAttribute(BUTTON_ID_ATTRIBUTE);
					if (EXCEL_BUTTON_ID.equals(buttonId)) {
						module.setExcelAvailable(true);
					}
					String buttonEnableStatesAttribute = node.getAttribute(BUTTON_ENABLE_STATES_ATTRIBUTE);
					buttonEnableStatesAttribute = JepRiaToolkitUtil.isEmpty(buttonEnableStatesAttribute) ? buttonEnableStatesAttribute
							: buttonEnableStatesAttribute.toUpperCase();

					List<WorkstateEnum> workstates = JepRiaToolkitUtil.getWorkStates(buttonEnableStatesAttribute);
					module.getToolBarButtons().add(
							new ModuleButton(buttonId, !JepRiaToolkitUtil.isEmpty(workstates) ? workstates.toArray(new WorkstateEnum[workstates.size()])
									: new WorkstateEnum[] {}, node.getAttribute(BUTTON_IMAGE_ATTRIBUTE), node.getAttribute(BUTTON_EVENT_ATTRIBUTE), node
									.getAttribute(BUTTON_TEXT_ATTRIBUTE), node.getAttribute(BUTTON_NAME_ATTRIBUTE), node
									.getAttribute(BUTTON_NAME_EN_ATTRIBUTE)).setSeparator(!nodes.item(i).getNodeName()
									.equalsIgnoreCase(BUTTON_TAG_NAME)));
				}
			}
		}
	}

	/**
	 * Детализация элемента-поля GWT-модуля
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleField(Module module, ModuleField recordField) {
		try {
			detailizedModuleFieldAsDetailed(module, recordField);
			detailizedModuleFieldAsListed(module, recordField);
			detailizedModuleFieldAsParameters(recordField);
			detailizedPrefixModuleFieldParameters(module);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Является ли поле с указанным ID рекорд дефинишном
	 * @param moduleId
	 *            Идентификатор модуля
	 * @param fieldId
	 *            Идентификатор поля
	 * 
	 * @return флаг, указывающий является ли поле рекорд дефинишном
	 */
	private boolean isRecordField(String moduleId, String fieldId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/record/", FIELD_TAG_NAME, "[translate(normalize-space(@", FIELD_ID_ATTRIBUTE, "),'",
					ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			return !JepRiaToolkitUtil.isEmpty((Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE));
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return false;
	}

	/**
	 * Получение ссылки на узел детальной формы по указанному идентификатору
	 * модуля
	 * @param moduleId
	 *            Идентификатор модуля
	 * 
	 * @return XML-узел в JepApplication.xml
	 */
	private Element getFormDetailElementByModuleId(String moduleId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", DETAIL_FORM_TAG_NAME));
			return (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как представителя списочной формы
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsDetailed(Module module, ModuleField recordField) {
		Element field = null;
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		Element formDetailElement = getFormDetailElementByModuleId(moduleId);
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", DETAIL_FORM_TAG_NAME, "/", FIELD_TAG_NAME, "[translate(normalize-space(@",
					FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			field = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		if (!JepRiaToolkitUtil.isEmpty(formDetailElement) && JepRiaToolkitUtil.isEmpty(module.getFieldLabelWidth())) {
			module.setFieldLabelWidth(formDetailElement.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE));
		}
		if (!JepRiaToolkitUtil.isEmpty(field)) {
			recordField.setDetailFormField(true);
			String fieldWidget = JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_WIDGET_ATTRIBUTE)) ? null : field.getAttribute(FIELD_WIDGET_ATTRIBUTE)
					.trim();
			if (!JepRiaToolkitUtil.isEmpty(fieldWidget)) {
				recordField.setFieldWidget(fieldWidget);
			}
			String name = new String();
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_NAME_ATTRIBUTE))) {
				recordField.setFieldDetailFormName(name.trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_NAME_EN_ATTRIBUTE))) {
				recordField.setFieldDetailFormNameEn(name.trim());
			}
			if (JepRiaToolkitUtil.isEmpty(recordField.getFieldType())) {
				recordField.setFieldType(JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_TYPE_ATTRIBUTE)) ? null : field.getAttribute(
						FIELD_TYPE_ATTRIBUTE).trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_MAX_LENGTH_ATTRIBUTE))) {
				recordField.setFieldMaxLength(name.trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_WIDTH_ATTRIBUTE))) {
				recordField.setFieldWidth(name.trim());
			}
			String labelWidth = module.getFieldLabelWidth();
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_LABEL_WIDTH_ATTRIBUTE))) {
				recordField.setLabelWidth(name.trim());
			} else if (JepRiaToolkitUtil.isEmpty(recordField.getLabelWidth()) && !JepRiaToolkitUtil.isEmpty(labelWidth)) {
				recordField.setLabelWidth(labelWidth);
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_HEIGHT_ATTRIBUTE))) {
				recordField.setFieldHeight(name.trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_VISIBLE_WORKSTATES_ATTRIBUTE))) {
				recordField.setVisibleWorkStates(name.trim().toUpperCase());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_MANDATORY_WORKSTATES_ATTRIBUTE))) {
				recordField.setMandatoryWorkStates(name.trim().toUpperCase());
			}
			
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE))) {
				recordField.setEditableWorkStates(name.trim().toUpperCase());
			} else if (field.hasAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE) 
					&& JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_EDITABLE_WORKSTATES_ATTRIBUTE)))
			{
				recordField.setIsEditable(false);
			}
				
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_ENABLE_WORKSTATES_ATTRIBUTE))) {
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
	 * Получение ссылки на узел списочной формы по указанному идентификатору
	 * модуля
	 * @param moduleId
	 *            Идентификатор модуля
	 * 
	 * @return XML-узел в JepApplication.xml
	 */
	private Element getFormListElementByModuleId(String moduleId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", LIST_FORM_TAG_NAME));
			return (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как представителя детальной формы
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsListed(Module module, ModuleField recordField) {
		Element field = null;
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		Element formList = getFormListElementByModuleId(moduleId);
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", LIST_FORM_TAG_NAME, "/", FIELD_TAG_NAME, "[translate(normalize-space(@",
					FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			field = (Element) expr.evaluate(jepApplicationDoc, XPathConstants.NODE);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		if (!JepRiaToolkitUtil.isEmpty(formList)) {
			String groupFieldName = formList.getAttribute(LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE);
			recordField.setGroupFormField(recordField.getFieldId().equalsIgnoreCase(groupFieldName));

			if (module.isDNDOff())
				module.setDNDOff(JepRiaToolkitUtil.isEmpty(formList.getAttribute(LIST_FORM_DND_NAME_ATTRIBUTE))
						|| OFF.equalsIgnoreCase(formList.getAttribute(LIST_FORM_DND_NAME_ATTRIBUTE)));

			if (!module.isDblClickOff())
				module.setDblClickOff(OFF.equalsIgnoreCase(formList.getAttribute(DBL_CLICK_NAME_ATTRIBUTE)));

			String presenterAttribute = formList.getAttribute(LIST_FORM_PRESENTER_ATTRIBUTE);
			if (!module.hasListFormPresenter() && !JepRiaToolkitUtil.isEmpty(presenterAttribute))
				module.setHasListFormPresenter(!OFF.equalsIgnoreCase(presenterAttribute));
		}
		if (!JepRiaToolkitUtil.isEmpty(field)) {
			recordField.setListFormField(true);
			String name = new String();
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_NAME_ATTRIBUTE))) {
				recordField.setFieldListFormName(name.trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_NAME_EN_ATTRIBUTE))) {
				recordField.setFieldListFormNameEn(name.trim());
			}
			if (JepRiaToolkitUtil.isEmpty(recordField.getFieldType())) {
				recordField.setFieldType(JepRiaToolkitUtil.isEmpty(field.getAttribute(FIELD_TYPE_ATTRIBUTE)) ? null : field.getAttribute(
						FIELD_TYPE_ATTRIBUTE).trim());
			}
			if (!JepRiaToolkitUtil.isEmpty(name = field.getAttribute(FIELD_WIDTH_ATTRIBUTE))) {
				recordField.setColumnWidth(name.trim());
			}
		}

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

	/**
	 * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsParameters(ModuleField recordField) {
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil
					.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId, "']", "/",
							DATABASE_TAG_NAME, "/find[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String findParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			List<String> parameters = Arrays.asList(findParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));

			recordField.setFindParameter(!JepRiaToolkitUtil.isEmpty(findParameters) && parameters.contains(fieldId));

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", DATABASE_TAG_NAME, "/create[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String createParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			parameters = Arrays.asList(createParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
			recordField.setCreateParameter(!JepRiaToolkitUtil.isEmpty(createParameters) && parameters.contains(fieldId));

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", DATABASE_TAG_NAME, "/update[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String updateParameters = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			parameters = Arrays.asList(updateParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
			recordField.setUpdateParameter(!JepRiaToolkitUtil.isEmpty(updateParameters) && parameters.contains(fieldId));

			recordField.setDeleteParameter(recordField.getIsPrimaryKey());
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * 
	 * @param recordField
	 */
	private void detailizedPrefixModuleFieldParameters(Module module) {
		String defaultPrefix = null;
		String moduleId = module.getModuleId();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			defaultPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			defaultPrefix = !JepRiaToolkitUtil.isEmpty(defaultPrefix) ? defaultPrefix.trim() : null;
			module.setDefaultParameterPrefix(defaultPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/create[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String createParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			createParameterPrefix = !JepRiaToolkitUtil.isEmpty(createParameterPrefix) ? createParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(createParameterPrefix))
				module.setCreateParameterPrefix(createParameterPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/update[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String updateParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			updateParameterPrefix = !JepRiaToolkitUtil.isEmpty(updateParameterPrefix) ? updateParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(updateParameterPrefix))
				module.setUpdateParameterPrefix(updateParameterPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/find[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String findParameterPrefix = (String) expr.evaluate(jepApplicationDoc, XPathConstants.STRING);
			findParameterPrefix = !JepRiaToolkitUtil.isEmpty(findParameterPrefix) ? findParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(findParameterPrefix))
				module.setFindParameterPrefix(findParameterPrefix);
		} catch (Exception e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * Получение хэшмэпа модуля и списка его полей по идентификатору
	 * 
	 * @param moduleId
	 *            идентификатор модуля
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

	/**
	 * Получение списка идентификаторов полей, отсутствующих в списке
	 * перечисления record'ов, но указанных среди полей списочной или детальной
	 * форм
	 * 
	 * @param moduleId
	 *            Идентификатор модуля
	 * @param mfList
	 *            Имеющийся список полей данного модуля
	 * @return список идентификаторов отсутствующих полей
	 */
	private List<String> getListOfAbsentFieldNameByModuleId(String moduleId, List<ModuleField> mfList) {
		List<String> result = new ArrayList<String>();
		Element formDetailElement = getFormDetailElementByModuleId(moduleId);
		Element formListElement = getFormListElementByModuleId(moduleId);

		if (!JepRiaToolkitUtil.isEmpty(formDetailElement)) {
			NodeList formDetailNodes = formDetailElement.getChildNodes();
			for (int i = 0; i < formDetailNodes.getLength(); i++) {
				if (formDetailNodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element formDetailNode = (Element) formDetailNodes.item(i);
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

		if (!JepRiaToolkitUtil.isEmpty(formListElement)) {
			NodeList formListNodes = formListElement.getChildNodes();
			for (int i = 0; i < formListNodes.getLength(); i++) {
				if (formListNodes.item(i).getNodeType() == ELEMENT_NODE) {
					Element formListNode = (Element) formListNodes.item(i);
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

	public String[] getCustomEventInfo(Module module, String formName, FORM form) {
		String importEvent = "";
		String addEventHandler = "";
		String eventDescription = "";
		String importEventHandler = "";

		for (ModuleButton button : module.getToolBarCustomButtonsForForm(form)) {
			addEventHandler += JepRiaToolkitUtil.multipleConcat(
					"		eventBus.addHandler(", button.getCustomEvent(), "Event.TYPE, this);", END_OF_LINE);

			eventDescription += JepRiaToolkitUtil.multipleConcat(
					"	public void on", button.getCustomEvent(), "Event(", button.getCustomEvent(), "Event event) {", END_OF_LINE, 
					"		//TODO: your business logic;", END_OF_LINE, 
					"	}", END_OF_LINE);

			importEvent += JepRiaToolkitUtil.multipleConcat(
					"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.eventbus.event.", button.getCustomEvent(), "Event;", END_OF_LINE);

			importEventHandler += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(importEventHandler) ? "" : ", "), button.getCustomEvent(),
					"Event.Handler");
		}

		if (!JepRiaToolkitUtil.isEmpty(importEvent)) {
			importEvent += JepRiaToolkitUtil.multipleConcat(
					"import com.google.gwt.user.client.ui.AcceptsOneWidget;", END_OF_LINE,
					"import com.google.gwt.event.shared.EventBus;", END_OF_LINE);
		}

		return new String[] { importEvent, importEventHandler, addEventHandler, eventDescription };
	}

	// сетеры для соответствующих атрибутов Task
	public void setApplicationStructureFile(String applicationStructureFile) {
		this.applicationStructureFile = applicationStructureFile;
	}

	private Map<String, Object> resultData = null; 
	
	private Map<String, Object> prepareData(){
		if (resultData == null){
			resultData = new HashMap<String, Object>();
			resultData.put(MODULE_NAME_TEMPLATE_PARAMETER, moduleName);
			resultData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, packageName);
			resultData.put(SECURITY_ROLES_TEMPLATE_PARAMETER, securityRoles);
			List<ModuleInfo> mods = new ArrayList<ModuleInfo>(forms.size());
			boolean hasTextFile = false;
			boolean hasBinaryFile = false;
			for (int i = 0; i < forms.size(); i++) {
				String formName = (String) forms.get(i);
				Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
				Module module = hm.keySet().iterator().next();
				
				ModuleInfo modInfo = new ModuleInfo();
				modInfo.setFormName(formName);
				modInfo.setFormTitle(module.getModuleName());
				modInfo.setFormTitleEn(module.getModuleNameEn());
				modInfo.setFieldLabelWidth(module.getFieldLabelWidth());
				modInfo.setDataSource(module.getModuleDataSource());
				modInfo.setPrimaryKey(getPrimaryKeyById(formName));
				modInfo.setTable(module.getTable());
				modInfo.setDbPackage(module.getDbPackageName());
				modInfo.setIsExcelAvailable(module.isExcelAvailable());
				modInfo.setNotRebuild(module.isNotRebuild());
				modInfo.setDefaultParameterPrefix(module.getDefaultParameterPrefix());
				modInfo.setCreateParameterPrefix(module.getCreateParameterPrefix());
				modInfo.setFindParameterPrefix(module.getFindParameterPrefix());
				modInfo.setUpdateParameterPrefix(module.getUpdateParameterPrefix());
				String mainFormIfExist = getMainFormNameIfExist(formName);
				modInfo.setMainFormName(mainFormIfExist);
				if (!JepRiaToolkitUtil.isEmpty(mainFormIfExist)) {
					String mainFormParentKey = getPrimaryKeyById(mainFormIfExist);
					mainFormParentKey = JepRiaToolkitUtil.isEmpty(mainFormParentKey) ? JepRiaToolkitUtil.multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX) : mainFormParentKey;
					modInfo.setMainFormParentKey(mainFormParentKey);
				}
				boolean isJepToolBar = module.isStandardToolBar() && !module.isToolBarOff();
				modInfo.setIsJepToolBarPresenter(isJepToolBar && !module.hasToolBarPresenter());
				modInfo.setIsJepToolBarView(isJepToolBar && !module.hasToolBarView());
				modInfo.setIsDblClickOff(module.isDblClickOff());
				modInfo.setIsToolBarOff(module.isToolBarOff());
				modInfo.setIsStatusBarOff(module.isStatusBarOff());
				modInfo.setHasLikeField(module.hasLikeFields());
				modInfo.setScopeModuleIds(getDependencyNodesIfExists(formName));
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
		}
		return resultData;
	}
}