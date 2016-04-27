package com.technology.jep.jepriatoolkit.creator;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;
import static com.technology.jep.jepriatoolkit.creator.ApplicationStructureCreatorUtil.convertTemplateToFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.detailizedModuleField;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.detailizedModuleForToolBar;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getAllModuleNodes;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDOM;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDataSourceById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getFormDetailElementByModuleId;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getListOfAbsentFieldNameByModuleId;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleFieldsById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleNodeById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleSecurityRoles;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getNameFromID;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getNodesWithChildren;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getPackageById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getPrimaryKeyById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getTableById;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.makeDir;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.readFromJar;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.replacePackageModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.runAntTarget;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.writeToFile;
import static java.text.MessageFormat.format;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FieldType;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.creator.module.Record;

@SuppressWarnings("unchecked")
public class ApplicationStructureCreator extends Task {

	// Атрибут таска
	public static String applicationStructureFile;
	// объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения	
	public static Application application;
	public static List<String> forms = new ArrayList<String>();
	private static List<List<String>> formWithTheirDependencies = new ArrayList<List<String>>();
	private static List<String> securityRoles = new ArrayList<String>();
	// Соответствие формы списку полей, где указано наименование поля и его тип
	private static Map<Module, List<ModuleField>> formFields = new HashMap<Module, List<ModuleField>>();
	// Ссылка на <ApplicationName>Definition.xml
	private static Document jepApplicationDoc = null;
	// Данные для шаблонизатора
	private Map<String, Object> resultData = null; 

	/**
	 * Основной метод, который выполняет Task
	 */
	@Override
	public void execute() throws BuildException {
		try {
			if (!parseApplicationSettingXML()) return;
			
			notifyAboutAbsentFields();

			echoMessage(multipleConcat("Create Application Structure for '", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase(), "' module"));
			createApplicationFileStructure();
			echoMessage("Generate web.xml");
			generateWebXML();
			echoMessage("Generate application.xml");
			generateApplicationXML();
			echoMessage("Generate orion-application.xml");
			generateOrionApplicationXML();
			echoMessage("Create Welcome page!");
			createWelcomePage();
			echoMessage("Generate xml for GWT-application");
			generateGwtXML();
			echoMessage("Create Text Files");
			createTextFile();
			echoMessage("Create File of Overview");
			createOverview();
			echoMessage("Create Java Classes Of Client Constant");
			createClientConstant();
			echoMessage("Create Java Classes Of Client Factoty");
			createClientFactoryImpl();
			echoMessage("Create Java Classes Of Entry Point");
			createEntryPoint();
			echoMessage("Create Java Classes Of Detail Form Presenter");
			createDetailFormPresenter();
			echoMessage("Create Java Classes Of Detail Form View");
			createDetailFormView();
			echoMessage("Create Java Classes Of List Form Presenter");
			createListFormPresenter();
			echoMessage("Create Java Classes Of List Form View");
			createListFormView();
			echoMessage("Create Java Classes Of Module Presenter");
			createModulePresenter();
			echoMessage("Create Java Classes Of Service Implementation");
			createServiceImpl();
			echoMessage("Create Java Classes Of Server Constant");
			createServerConstant();
			echoMessage("Create Java Classes Of DAO");
			createDao();
			echoMessage("Create Java Classes Of Field Names");
			createFieldNames();
			echoMessage("Create Java Classes Of Record Definition");
			createRecordDefinition();
			echoMessage("Create Java Classes Of Service");
			createService();
			echoMessage("Create Java Classes Of Shared Constant");
			createSharedConstant();
			echoMessage("Create Java Classes Of ToolBar");
			createToolBar();
			echoMessage("Create Java Classes Of Statusbar");
			createStatusbar();
			echoMessage("Create client and server side logging");
			createLog4jProperties();
			echoMessage("Encode Text Resources");
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
	public static boolean parseApplicationSettingXML() throws ParserConfigurationException {
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
				
				Element module = getModuleNodeById(jepApplicationDoc, formName);
				List<String> moduleRoles = getModuleSecurityRoles(jepApplicationDoc, formName);
				// заполнение общего списка ролей, необходимого для генерации web.xml
				for (String moduleRole : moduleRoles) {
					if (!securityRoles.contains(moduleRole))
						securityRoles.add(moduleRole);
				}

				String moduleDataSource = getDataSourceById(jepApplicationDoc, formName);
				String modulePackage = getPackageById(jepApplicationDoc, formName);

				Db db = new Db(isEmpty(modulePackage) ? multipleConcat(PKG_PREFIX, application.getName().toLowerCase())
						: modulePackage, isEmpty(moduleDataSource) ? defaultDataSource : moduleDataSource);
				Module m = new Module(formName, isEmpty(module.getAttribute(MODULE_NAME_ATTRIBUTE)) ? NO_NAME : module.getAttribute(
						MODULE_NAME_ATTRIBUTE).trim(), isEmpty(module.getAttribute(MODULE_NAME_EN_ATTRIBUTE)) ? NO_NAME : module
						.getAttribute(MODULE_NAME_EN_ATTRIBUTE).trim(), db, moduleRoles);
				m.setNotRebuild(OFF.equalsIgnoreCase(module.getAttribute(MODULE_BUILD_ATTRIBUTE)));
				m.setTable(getTableById(jepApplicationDoc, formName));
				// Инициализация данных о тулбаре
				detailizedModuleForToolBar(jepApplicationDoc, m, module);

				List<ModuleField> mfList = new ArrayList<ModuleField>();
				List<Element> fields = getModuleFieldsById(jepApplicationDoc, formName);
				if (!isEmpty(fields)){
					String primaryKey = getPrimaryKeyById(jepApplicationDoc, formName);
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
						detailizedModuleField(jepApplicationDoc, m, mf);
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
					Element formDetailElement = getFormDetailElementByModuleId(jepApplicationDoc, m.getModuleId());
					if (formDetailElement != null){
						NodeList presenterBodyNodes = formDetailElement.getElementsByTagName(PRESENTER_BOBY_TAG_NAME);
						if (presenterBodyNodes != null && presenterBodyNodes.getLength() > 0){
							detailForm.setPresenterBody(presenterBodyNodes.item(0).getTextContent());
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
				List<String> nodesWithChildren = getNodesWithChildren(jepApplicationDoc, forms.get(i));
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
	 * Оповещение о полях, присутствующих на детальной или списочной формах, но
	 * не указанных в секции &lt;record&gt;
	 */
	private void notifyAboutAbsentFields() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = forms.get(i);
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			List<ModuleField> moduleFields = hm.values().iterator().next();
			List<String> absentFields = getListOfAbsentFieldNameByModuleId(jepApplicationDoc, formName, moduleFields);
			if (!absentFields.isEmpty()) {
				echoMessage(multipleConcat(WARNING_PREFIX, "Field", (absentFields.size() > 1 ? "s" : ""), WHITE_SPACE, Arrays.toString(absentFields.toArray()), WHITE_SPACE, 
						(absentFields.size() > 1 ? "are" : "is"), " absent in 'record' section for module with ID : '", formName, "'"));
			}
		}
	}

	/**
	 * Создание файловой структуры приложения
	 */
	private void createApplicationFileStructure() {

		makeDir(getDefinitionProperty(LIB_DIRECTORY_PROPERTY, LIB_DIR_NAME));

		makeDir(getDefinitionProperty(HTML_DIRECTORY_PROPERTY, WELCOME_PAGE_DIR_NAME));
		
		makeDir(
			format(
				getDefinitionProperty(RESOURCE_DIRECTORY_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/web")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase() 	
			)
		);
		makeDir(
			format(
				getDefinitionProperty(MAIN_MODULE_DIRECTORY_PROPERTY,
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/client/ui/main")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
		makeDir(
			format(
				getDefinitionProperty(ENTRANCE_DIRECTORY_PROPERTY,
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/client/entrance")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
		makeDir(
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_DIRECTORY_PROPERTY,
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/shared/text")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);

		if (forms.size() == 0) {
			echoMessage(multipleConcat(WARNING_PREFIX, "Tag-parameter '", MODULE_TAG_NAME,
					"' is desirable. To create structure for forms, fill this parameter in ", applicationStructureFile, "!"));
			return;
		}

		for (int i = 0; i < forms.size(); i++) {
			String formName = ((String) forms.get(i)).toLowerCase();
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_DIRECTORY_PROPERTY,
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/form/detail")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_DIRECTORY_PROPERTY,
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/form/list")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_DAO_DIRECTORY_PROPERTY,
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/server/dao")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_DIRECTORY_PROPERTY,
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/server/service")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_FIELD_DIRECTORY_PROPERTY,	
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/field")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DIRECTORY_PROPERTY,	
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/record")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_DIRECTORY_PROPERTY,	
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/service")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_DIRECTORY_PROPERTY,	
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/shared/text")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
		}
		
		makeDir(
			format(
				getDefinitionProperty(CONFIG_HTML_DIRECTORY_PROPERTY, "config/{0}/src/html"),
				DEBUG_BUILD_CONFIG_NAME
			)
		);
		makeDir(
			format(
				getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")),
				DEBUG_BUILD_CONFIG_NAME, application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
		
		String deployPropContent = readFromJar(
				format(
					getDefinitionProperty(DEPLOY_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY, "/templates/config/{0}/deploy.properties"),
					DEBUG_BUILD_CONFIG_NAME
				), UTF_8);
		writeToFile(deployPropContent, 
				format(
					getDefinitionProperty(DEPLOY_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/deploy.properties"),
					DEBUG_BUILD_CONFIG_NAME
				), UTF_8, false);

		makeDir(
			format(
				getDefinitionProperty(CONFIG_HTML_DIRECTORY_PROPERTY, "config/{0}/src/html"),
				RELEASE_BUILD_CONFIG_NAME
			)
		);
		makeDir(
			format(
				getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")),
				RELEASE_BUILD_CONFIG_NAME, application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
		deployPropContent = 
			readFromJar(
					format(
						getDefinitionProperty(DEPLOY_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY, "/templates/config/{0}/deploy.properties"),
						RELEASE_BUILD_CONFIG_NAME
					), UTF_8);
		writeToFile(deployPropContent, 
				format(
					getDefinitionProperty(DEPLOY_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/deploy.properties"),
					RELEASE_BUILD_CONFIG_NAME
				), UTF_8, false);

	}

	/**
	 * Генерация дескриптора развертывания web.xml
	 */
	private void generateWebXML() {
		convertTemplateToFile(
			getDefinitionProperty(WEB_XML_TEMPLATE_PROPERTY, "web.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(WEB_XML_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/web/web.xml")
				), 
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
	}

	/**
	 * Создание application.xml
	 */
	private void generateApplicationXML() {
		convertTemplateToFile(
			getDefinitionProperty(APPLICATION_XML_TEMPLATE_PROPERTY, "application.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/application.xml")
				), 
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
	}

	/**
	 * Создание orion-application.xml
	 */
	private void generateOrionApplicationXML() {
		convertTemplateToFile(
			getDefinitionProperty(ORION_APPLICATION_XML_TEMPLATE_PROPERTY, "orion-application.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(ORION_APPLICATION_XML_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_RESOURCE, "{0}/{1}/orion-application.xml")
				), 
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
	}

	/**
	 * Создание основного GWT.xml для всего приложения, описывающего его
	 * структуру
	 */
	private void generateMainGwtXML() {
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			prepareData(),
			format(
				getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{2}.gwt.xml")
				), 
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			prepareData(),
			multipleConcat(
				"config/", DEBUG_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				application.getProjectPackage().toLowerCase(),  "/", application.getName().toLowerCase(),
				"/main/", application.getName(), ".gwt.xml")
		);
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_PRODUCTION_TEMPLATE_PROPERTY, "mainProduction.gwt.ftl"), 
			prepareData(),
			multipleConcat(
				"config/", RELEASE_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				application.getProjectPackage().toLowerCase(),  "/", application.getName().toLowerCase(),
				"/main/", application.getName(), ".gwt.xml")
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
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_GWT_XML_TEMPLATE_PROPERTY, "module.gwt.ftl"), 
				data, 
				format(
					getDefinitionProperty(CLIENT_MODULE_GWT_XML_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/{3}.gwt.xml")
					),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
		convertTemplateToFile(
			getDefinitionProperty(APPLICATION_JSP_TEMPLATE_PROPERTY, "applicationJsp.ftl"), 
			prepareData(), 
			format(
				getDefinitionProperty(APPLICATION_JSP_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(WELCOME_PAGE_DIR_NAME, "/{0}.jsp")),
				application.getName()
			)
		);
		
		//CSS debug
		String cssTemplateDebugContent = readFromJar(multipleConcat("/templates/config/", DEBUG_BUILD_CONFIG_NAME, "/src/html/", CSS_TEMPLATE_NAME), UTF_8);
		writeToFile(cssTemplateDebugContent,
			format(
				getDefinitionProperty(APPLICATION_CSS_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(WELCOME_PAGE_DIR_NAME, "/{0}.css")
				),
				application.getName()
			), UTF_8, false);
		writeToFile(cssTemplateDebugContent, multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", application.getName(), ".css"), UTF_8, false);

		//CSS release
		String cssTemplateReleaseContent = readFromJar(multipleConcat("/templates/config/", RELEASE_BUILD_CONFIG_NAME, "/src/html/", CSS_TEMPLATE_NAME), UTF_8);
		writeToFile(cssTemplateReleaseContent, multipleConcat("config/", RELEASE_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", application.getName(), ".css"), UTF_8, false);
	}

	/**
	 * Создание текстовых ресурсов
	 */
	private void createTextFile() {
		Map<String, Object> data = prepareData();
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_TEXT_RESOURCE_TEMPLATE_PROPERTY, "mainText.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_Source.properties")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY, "mainTextEn.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_en.properties")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
		
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			String formName = moduleInfo.getFormName();
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_TEMPLATE_PROPERTY, "clientModuleText.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY, "clientModuleTextEn.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание страницы обзора для приложения
	 */
	private void createOverview() {
		convertTemplateToFile(
			getDefinitionProperty(OVERVIEW_TEMPLATE_PROPERTY, "overview.ftl"),
			prepareData(),
			format(
				getDefinitionProperty(OVERVIEW_PATH_TEMPLATE_PROPERTY, "src/java/com/technology/{0}/{1}/overview.html"),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
	}

	/**
	 * Создание классов клиенстких констант
	 */
	private void createClientConstant() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_CONSTANT_TEMPLATE_PROPERTY, "mainModuleConstant.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/{2}ClientConstant.java")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_CONSTANT_TEMPLATE_PROPERTY, "clientModuleConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/{3}ClientConstant.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_FACTORY_TEMPLATE_PROPERTY, "clientModuleFactory.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/{3}ClientFactoryImpl.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}

		convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_FACTORY_TEMPLATE_PROPERTY, "mainFactory.ftl"),
			data,
			format(
				getDefinitionProperty(MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/{3}ClientFactoryImpl.java")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
	}

	/**
	 * Создание класса точки входа
	 */
	private void createEntryPoint() {
		convertTemplateToFile(
			getDefinitionProperty(MODULE_ENTRY_POINT_TEMPLATE_PROPERTY, "moduleEntryPoint.ftl"),
			prepareData(),
			format(
				getDefinitionProperty(MODULE_ENTRY_POINT_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/entrance/{3}EntryPoint.java")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_TEMPLATE_PROPERTY, "clientModuleDetailFormPresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormPresenter.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_TEMPLATE_PROPERTY, "clientModuleDetailFormView.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormView.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_TEMPLATE_PROPERTY, "clientModuleDetailFormViewImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormViewImpl.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_LIST_FORM_PRESENTER_TEMPLATE_PROPERTY, "clientModuleListFormPresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/list/{3}ListFormPresenter.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_TEMPLATE_PROPERTY, "clientModuleListFormViewImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/list/{3}ListFormViewImpl.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			if (moduleInfo.isNotRebuild() || isEmpty(moduleInfo.getToolBarCustomButtonsOnBothForms())) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			// создадим сперва каталог
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_PLAIN_FORM_DIRECTORY_PROPERTY,
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/plain")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_PRESENTER_TEMPLATE_PROPERTY, "clientModulePresenter.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/plain/{3}ModulePresenter.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_PRESENTER_TEMPLATE_PROPERTY, "mainModulePresenter.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/ui/main/{2}MainModulePresenter.java")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleServiceImpl.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/{3}ServiceImpl.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			if (moduleInfo.getHasLobFields()){
				convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_UPLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleUploadServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_UPLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/UploadServiceImpl.java")),
						application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase()
					)
				);
				
				convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleDownloadServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/DownloadServiceImpl.java")),
						application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase()
					)
				);
			}
			
			if (moduleInfo.getIsExcelAvailable()){
				convertTemplateToFile(
					getDefinitionProperty(CLIENT_MODULE_EXCEL_SERVICE_IMPL_TEMPLATE_PROPERTY, "clientModuleExcelServiceImpl.ftl"),
					innerData, 
					format(
						getDefinitionProperty(CLIENT_MODULE_EXCEL_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/service/ShowExcelServlet.java")),
						application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase()
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_TEMPLATE_PROPERTY, "clientModuleServerConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/{3}ServerConstant.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов DAO приложения
	 */
	private void createDao() {
		createDaoInterface();
		createDaoClass();
	}

	/**
	 * Создание классов реализаций DAO
	 */
	private void createDaoClass() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DAO_TEMPLATE_PROPERTY, "clientModuleDao.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}Dao.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов DAO домашнего интерфейса
	 */
	private void createDaoInterface() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_DAO_INTERFACE_TEMPLATE_PROPERTY, "clientModuleDaoInterface.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_DAO_INTERFACE_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_FIELDS_TEMPLATE_PROPERTY, "clientModuleFields.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}FieldNames.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			if (moduleInfo.getHasOptionField()){
				for (ModuleField field : moduleInfo.getFields()){
					if (field.getIsOptionField()){
						innerData.put(FIELD_TEMPLATE_PARAMETER, field);
						convertTemplateToFile(
							getDefinitionProperty(CLIENT_MODULE_OPTIONS_TEMPLATE_PROPERTY, "clientModuleOptions.ftl"),
							innerData, 
							format(
								getDefinitionProperty(CLIENT_MODULE_OPTIONS_PATH_TEMPLATE_PROPERTY, 
										multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}Options.java")),
								application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), field.getFieldIdAsParameter()
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_TEMPLATE_PROPERTY, "clientModuleRecordDefinition.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/record/{3}RecordDefinition.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_TEMPLATE_PROPERTY, "clientModuleService.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/service/{3}Service.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SERVICE_ASYNC_TEMPLATE_PROPERTY, "clientModuleServiceAsync.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SERVICE_ASYNC_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/service/{3}ServiceAsync.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
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
		
		convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY, "mainModuleSharedConstant.ftl"),
			data, 
			format(
				getDefinitionProperty(MAIN_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/{2}Constant.java")),
				application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
			)
		);
		
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			String formName = moduleInfo.getFormName();
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY, "clientModuleSharedConstant.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/{3}Constant.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание классов инструментальной панели
	 */
	private void createToolBar() {
		Map<String, Object> data = prepareData();
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			boolean isStandardToolBar = moduleInfo.getIsStandardToolBar();
			boolean isToolBarOff = moduleInfo.getIsToolBarOff();
			List<ModuleButton> toolBarCustomButtons = moduleInfo.getToolBarCustomButtons();
			boolean hasCustomButtons = toolBarCustomButtons.size() > 0;
			boolean hasToolBarPresenter = moduleInfo.getHasToolBarPresenter();
			boolean hasToolBarView = moduleInfo.getHasToolBarView();
			
			if (!isStandardToolBar || isToolBarOff || hasToolBarPresenter || hasToolBarView) {
				String formName = moduleInfo.getFormName();
				// create file structure
				makeDir(
					format(
						getDefinitionProperty(CLIENT_MODULE_TOOLBAR_DIRECTORY_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/toolbar/{3}")),
						application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName, (hasCustomButtons ? "/images" : "")
					)
				);
				if (hasCustomButtons) {
					makeDir(
						format(
							getDefinitionProperty(CLIENT_MODULE_EVENTBUS_DIRECTORY_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/eventbus/event")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
						)
					);
				}
				
				Map<String, Object> innerData = new HashMap<String, Object>();
				innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
				innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
				innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
				
				if (!isStandardToolBar || isToolBarOff || hasToolBarView){
					convertTemplateToFile(
						getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_TEMPLATE_PROPERTY, "clientModuleToolBarView.ftl"),
						innerData, 
						format(
							getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarView.java")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
						)
					);
					
					convertTemplateToFile(
						getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_IMPL_TEMPLATE_PROPERTY, "clientModuleToolBarViewImpl.ftl"),
						innerData, 
						format(
							getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarViewImpl.java")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
						)
					);
				}
				if (!isStandardToolBar || isToolBarOff || hasToolBarPresenter) {
					convertTemplateToFile(
						getDefinitionProperty(CLIENT_MODULE_TOOLBAR_PRESENTER_TEMPLATE_PROPERTY, "clientModuleToolBarView.ftl"),
						innerData, 
						format(
							getDefinitionProperty(CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarPresenter.java")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
						)
					);
				}
				
				if (hasCustomButtons) {
					convertTemplateToFile(
						getDefinitionProperty(CLIENT_MODULE_IMAGES_TEMPLATE_PROPERTY, "clientModuleToolBarImages.ftl"),
						innerData, 
						format(
							getDefinitionProperty(CLIENT_MODULE_IMAGES_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/images/{3}Images.java")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
						)
					);
					convertTemplateToFile(
						getDefinitionProperty(CLIENT_MODULE_EVENTBUS_TEMPLATE_PROPERTY, "clientModuleEventBus.ftl"),
						innerData, 
						format(
							getDefinitionProperty(CLIENT_MODULE_EVENTBUS_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/eventbus/{3}EventBus.java")),
							application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
						)
					);
					for (ModuleButton button : toolBarCustomButtons) {
						innerData.put(BUTTON_TEMPLATE_PARAMETER, button);
						convertTemplateToFile(
							getDefinitionProperty(CLIENT_MODULE_EVENT_TEMPLATE_PROPERTY, "clientModuleEvent.ftl"),
							innerData, 
							format(
								getDefinitionProperty(CLIENT_MODULE_EVENT_PATH_TEMPLATE_PROPERTY, 
										multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/eventbus/event/{3}Event.java")),
								application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), button.getCustomEvent()
							)
						);
					}
				}
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
			
			makeDir(
				format(
					getDefinitionProperty(CLIENT_MODULE_STATUSBAR_DIRECTORY_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/{2}/client/ui/statusbar")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName
				)
			);
			
			Map<String, Object> innerData = new HashMap<String, Object>();
			innerData.put(FORM_TEMPLATE_PARAMETER, moduleInfo);
			innerData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, data.get(PACKAGE_NAME_TEMPLATE_PARAMETER));
			innerData.put(MODULE_NAME_TEMPLATE_PARAMETER, data.get(MODULE_NAME_TEMPLATE_PARAMETER));
			
			convertTemplateToFile(
				getDefinitionProperty(CLIENT_MODULE_STATUSBAR_TEMPLATE_PROPERTY, "clientModuleStatusBar.ftl"),
				innerData, 
				format(
					getDefinitionProperty(CLIENT_MODULE_STATUSBAR_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/statusbar/{3}StatusBarViewImpl.java")),
					application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), formName.toLowerCase(), formName
				)
			);
		}
	}

	/**
	 * Создание файла, хранящего настройки для log4j
	 */
	private void createLog4jProperties() {
		//debug
		String log4jDebugTemplateContent = readFromJar(
			format(
				getDefinitionProperty(LOG4J_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY, "/templates/config/{0}/src/java/log4j.properties"),
				DEBUG_BUILD_CONFIG_NAME
			), UTF_8);
		log4jDebugTemplateContent = replacePackageModuleNames(log4jDebugTemplateContent, application.getProjectPackage(), application.getName());
		writeToFile(log4jDebugTemplateContent, 
				getDefinitionProperty(LOG4J_PROPERTIES_CODE_DESTINATION_PATH_TEMPLATE_PROPERTY, "src/java/log4j.properties")
				, UTF_8, false);
		writeToFile(log4jDebugTemplateContent, 
			format(
				getDefinitionProperty(LOG4J_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/src/java/log4j.properties"),
				DEBUG_BUILD_CONFIG_NAME
			), UTF_8, false);
		
		//release
		String log4jReleaseTemplateContent = readFromJar(
			format(
				getDefinitionProperty(LOG4J_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY, "/templates/config/{0}/src/java/log4j.properties"),
				RELEASE_BUILD_CONFIG_NAME
			), UTF_8);
		log4jReleaseTemplateContent = replacePackageModuleNames(log4jReleaseTemplateContent, application.getProjectPackage(), application.getName());
		writeToFile(log4jReleaseTemplateContent, 
			format(
				getDefinitionProperty(LOG4J_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/src/java/log4j.properties"),
				RELEASE_BUILD_CONFIG_NAME
			), UTF_8, false);
	}

	/**
	 * Конвертация текстовых ресурсов
	 */
	private void encodeTextResources() {
		runAntTarget("all-text-encode");
	}

	/**
	 * Функция, определяющая является ли форма зависимой. Если да, то от какой
	 * 
	 * @param formName наименование проверяемой на зависимость формы
	 * @return наименование главной формы
	 */
	public static String getMainFormNameIfExist(String formName) {
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
	 * Получение хэшмэпа модуля и списка его полей по идентификатору
	 * 
	 * @param moduleId идентификатор модуля
	 * @return хэшмэп модуля и списка полей
	 */
	public static Map<Module, List<ModuleField>> getModuleWithFieldsById(String moduleId) {
		Map<Module, List<ModuleField>> hm = new HashMap<Module, List<ModuleField>>();

		for (Module module : formFields.keySet()) {
			if (module.getModuleId().equalsIgnoreCase(moduleId)) {
				hm.put(module, formFields.get(module));
				break;
			}
		}
		return hm;
	}

	// сетеры для соответствующих атрибутов Task
	public void setApplicationStructureFile(String applicationStructureFile) {
		ApplicationStructureCreator.applicationStructureFile = applicationStructureFile;
	}

	/**
	 * Подготовка данных для мэппинга на подготовленные шаблоны 
	 * 
	 * @return данные для мэпирования
	 */
	private Map<String, Object> prepareData(){
		if (resultData == null){
			resultData = new HashMap<String, Object>();
			resultData.put(MODULE_NAME_TEMPLATE_PARAMETER, application.getName());
			resultData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, application.getProjectPackage());
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
				modInfo.setDataSource(module.getDb().getDatasource());
				modInfo.setPrimaryKey(getPrimaryKeyById(jepApplicationDoc, formName));
				modInfo.setTable(module.getTable());
				modInfo.setDbPackage(module.getDb().getPackageName());
				modInfo.setIsExcelAvailable(module.isExcelAvailable());
				modInfo.setNotRebuild(module.isNotRebuild());
				modInfo.setDefaultParameterPrefix(module.getDefaultParameterPrefix());
				modInfo.setCreateParameterPrefix(module.getCreateParameterPrefix());
				modInfo.setFindParameterPrefix(module.getFindParameterPrefix());
				modInfo.setUpdateParameterPrefix(module.getUpdateParameterPrefix());
				String mainFormIfExist = getMainFormNameIfExist(formName);
				modInfo.setMainFormName(mainFormIfExist);
				if (!isEmpty(mainFormIfExist)) {
					String mainFormParentKey = getPrimaryKeyById(jepApplicationDoc, mainFormIfExist);
					mainFormParentKey = isEmpty(mainFormParentKey) ? multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX) : mainFormParentKey;
					modInfo.setMainFormParentKey(mainFormParentKey);
				}
				boolean isJepToolBar = module.isStandardToolBar() && !module.isToolBarOff();
				modInfo.setIsJepToolBarPresenter(isJepToolBar && !module.hasToolBarPresenter());
				modInfo.setIsJepToolBarView(isJepToolBar && !module.hasToolBarView());
				modInfo.setIsDblClickOff(module.isDblClickOff());
				modInfo.setStandardToolBar(module.isStandardToolBar());
				modInfo.setIsToolBarOff(module.isToolBarOff());
				modInfo.setIsStatusBarOff(module.isStatusBarOff());
				modInfo.setHasToolBarView(module.hasToolBarView());
				modInfo.setHasToolBarPresenter(module.hasToolBarPresenter());
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