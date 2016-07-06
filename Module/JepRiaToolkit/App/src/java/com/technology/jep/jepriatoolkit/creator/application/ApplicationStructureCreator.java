package com.technology.jep.jepriatoolkit.creator.application;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;	
import static com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil.convertTemplateToFile;
import static com.technology.jep.jepriatoolkit.creator.application.ApplicationStructureCreatorUtil.prepareData;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.encodeTextResources;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.makeDir;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.readFromJar;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.replacePackageModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.writeToFile;
import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

@SuppressWarnings("unchecked")
public class ApplicationStructureCreator extends Task {
	// Атрибут таска
	public String applicationStructureFile;
	// объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения	
	private Application application;
	private List<String> forms = new ArrayList<String>();
	// Данные для шаблонизатора
	private Map<String, Object> resultData = null;
	// Парсер настроек приложения
	private ApplicationSettingParser applicationParser = null;

	/**
	 * Основной метод, который выполняет Task
	 */
	@Override
	public void execute() throws BuildException {
		try {
			// Разбор и проверка applicationStructureFile (по умолчанию, <ApplicationName>Definition.xml)
			applicationParser = ApplicationSettingParser.getInstance(isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile);
			application = applicationParser.getApplication();
			forms = applicationParser.getForms();
			resultData = prepareData(applicationParser);
			
			applicationParser.notifyAboutAbsentFields();

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
					"' is desirable. To create structure for forms, fill this parameter in ", normalizePath(applicationStructureFile), "!"));
		}
		else {
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
		}
		
		makeDir(
			format(
				getDefinitionProperty(CONFIG_HTML_DIRECTORY_PROPERTY, "config/{0}/src/html"),
				DEBUG_BUILD_CONFIG_NAME
			)
		);
		makeDir(
			format(
				multipleConcat(getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")), "/client"),
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
				PRODUCTION_BUILD_CONFIG_NAME
			)
		);
		makeDir(
			format(
				multipleConcat(getDefinitionProperty(CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY, 
					multipleConcat("config/{0}/", PREFIX_DESTINATION_SOURCE_CODE, "{1}/{2}/main/")), "/client"),
				PRODUCTION_BUILD_CONFIG_NAME, application.getProjectPackage().toLowerCase(), application.getName().toLowerCase()
			)
		);
		deployPropContent = 
			readFromJar(
					format(
						getDefinitionProperty(DEPLOY_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY, "/templates/config/{0}/deploy.properties"),
						PRODUCTION_BUILD_CONFIG_NAME
					), UTF_8);
		writeToFile(deployPropContent, 
				format(
					getDefinitionProperty(DEPLOY_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/deploy.properties"),
					PRODUCTION_BUILD_CONFIG_NAME
				), UTF_8, false);
	}

	/**
	 * Генерация дескриптора развертывания web.xml
	 */
	private void generateWebXML() {
		convertTemplateToFile(
			getDefinitionProperty(WEB_XML_TEMPLATE_PROPERTY, "web.ftl"), 
			resultData,
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
			resultData,
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
			resultData,
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
		
		String mainGwtXmlPath = format(
			getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{2}.gwt.xml")
			), 
			application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
		);
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			resultData,
			mainGwtXmlPath
		);
		
		String mainDebugGwtXmlPath = multipleConcat(
			"config/", DEBUG_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
			application.getProjectPackage().toLowerCase(),  "/", application.getName().toLowerCase(),
			"/main/", application.getName(), ".gwt.xml");
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY, "mainDebug.gwt.ftl"), 
			resultData,
			mainDebugGwtXmlPath
		);
		
		String mainProductionGwtXmlPath = multipleConcat(
			"config/", PRODUCTION_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
			application.getProjectPackage().toLowerCase(),  "/", application.getName().toLowerCase(),
			"/main/", application.getName(), ".gwt.xml");
		convertTemplateToFile(
			getDefinitionProperty(MAIN_GWT_XML_PRODUCTION_TEMPLATE_PROPERTY, "mainProduction.gwt.ftl"), 
			resultData,
			mainProductionGwtXmlPath
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
			Map<String, Object> data = resultData;
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
			resultData, 
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
		String cssTemplateReleaseContent = readFromJar(multipleConcat("/templates/config/", PRODUCTION_BUILD_CONFIG_NAME, "/src/html/", CSS_TEMPLATE_NAME), UTF_8);
		writeToFile(cssTemplateReleaseContent, multipleConcat("config/", PRODUCTION_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", application.getName(), ".css"), UTF_8, false);
	}

	/**
	 * Создание текстовых ресурсов
	 */
	private void createTextFile() {
		Map<String, Object> data = resultData;
		
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
			resultData,
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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

		String mainClientFactoryPath = format(
			getDefinitionProperty(MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
					multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/{3}ClientFactoryImpl.java")),
			application.getProjectPackage().toLowerCase(), application.getName().toLowerCase(), application.getName()
		);
		convertTemplateToFile(
			getDefinitionProperty(MAIN_MODULE_FACTORY_TEMPLATE_PROPERTY, "mainFactory.ftl"),
			data,
			mainClientFactoryPath
		);
		
		try {
			JepRiaToolkitUtil.copyFile(mainClientFactoryPath, multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/", mainClientFactoryPath));
			JepRiaToolkitUtil.copyFile(mainClientFactoryPath, multipleConcat("config/", PRODUCTION_BUILD_CONFIG_NAME, "/", mainClientFactoryPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Создание класса точки входа
	 */
	private void createEntryPoint() {
		convertTemplateToFile(
			getDefinitionProperty(MODULE_ENTRY_POINT_TEMPLATE_PROPERTY, "moduleEntryPoint.ftl"),
			resultData,
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
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
		Map<String, Object> data = resultData;
		List<ModuleInfo> moduleInfos = (List<ModuleInfo>) data.get(FORMS_TEMPLATE_PARAMETER);
		for (ModuleInfo moduleInfo : moduleInfos) {
			if (moduleInfo.isNotRebuild()) continue;
			
			boolean isStandardToolBar = moduleInfo.getIsStandardToolBar();
			boolean isToolBarOff = moduleInfo.getIsToolBarOff();
			List<ModuleButton> toolBarCustomButtons = moduleInfo.getToolBarCustomButtons();
			boolean hasCustomButtons = toolBarCustomButtons.size() > 0;
			boolean hasToolBarPresenter = Boolean.TRUE.equals(moduleInfo.getHasToolBarPresenter());
			boolean hasToolBarView = Boolean.TRUE.equals(moduleInfo.getHasToolBarView());
			
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
		Map<String, Object> data = resultData;
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
				PRODUCTION_BUILD_CONFIG_NAME
			), UTF_8);
		log4jReleaseTemplateContent = replacePackageModuleNames(log4jReleaseTemplateContent, application.getProjectPackage(), application.getName());
		writeToFile(log4jReleaseTemplateContent, 
			format(
				getDefinitionProperty(LOG4J_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY, "config/{0}/src/java/log4j.properties"),
				PRODUCTION_BUILD_CONFIG_NAME
			), UTF_8, false);
	}

	// сетеры для соответствующих атрибутов Task
	public void setApplicationStructureFile(String applicationStructureFile) {
		this.applicationStructureFile = applicationStructureFile;
	}
}