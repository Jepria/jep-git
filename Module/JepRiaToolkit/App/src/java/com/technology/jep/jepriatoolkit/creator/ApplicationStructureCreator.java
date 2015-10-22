package com.technology.jep.jepriatoolkit.creator;

import static com.technology.jep.jepria.shared.field.JepTypeEnum.BIGDECIMAL;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BOOLEAN;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE_TIME;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.STRING;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TIME;
import static com.technology.jep.jepriatoolkit.creator.module.ModuleButton.STANDARD_TOOLBAR;
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
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureCreator extends Task implements JepRiaToolkitConstant {

	public String moduleName, packageName, riaVersion, commonHome, defaultDataSource;

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
//			JepRiaToolkitUtil.echoMessage("Convert Application in Debug Mode");
//			convertApplicationInDebugMode();
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

		try {
			jepApplicationDoc = JepRiaToolkitUtil.getDOM(APPLICATION_SETTING_XML);

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
						"Application setting XML is not correct! There is no mandatory attribute ", APPLICATION_NAME_ATTRIBUTE,
						" of tag 'application'!"));
				return false;
			}
			moduleName = applicationNode.getAttribute(APPLICATION_NAME_ATTRIBUTE);
			if (JepRiaToolkitUtil.isEmpty(moduleName)) {
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
						"Application setting XML is not correct! There is no mandatory attribute ", PROJECT_PACKAGE_ATTRIBUTE,
						" of tag 'application'!"));
				return false;
			}
			defaultDataSource = applicationNode.getAttribute(APPLICATION_DATASOURCE_ATTRIBUTE);
			if (JepRiaToolkitUtil.isEmpty(defaultDataSource)) {
				defaultDataSource = DEFAULT_DATASOURCE;
			}

			// инициализация списка форм с их потомками
			forms = getAllModuleNodes(jepApplicationDoc);
			for (int index = 0; index < forms.size(); index++) {
				String formName = forms.get(index);
				JepRiaToolkitUtil.echoMessage("  . " + formName);
				
				Element module = getModuleNodeById(jepApplicationDoc, formName);
				List<String> moduleRoles = getModuleSecurityRoles(jepApplicationDoc, formName);
				// заполнение общего списка ролей, необходимого для генерации
				// web.xml
				for (String moduleRole : moduleRoles) {
					if (!securityRoles.contains(moduleRole))
						securityRoles.add(moduleRole);
				}

				String moduleDataSource = getDataSourceById(jepApplicationDoc, formName);
				String modulePackage = getPackageById(jepApplicationDoc, formName);

				Module m = new Module(formName, JepRiaToolkitUtil.isEmpty(module.getAttribute(MODULE_NAME_ATTRIBUTE)) ? NO_NAME : module.getAttribute(
						MODULE_NAME_ATTRIBUTE).trim(), JepRiaToolkitUtil.isEmpty(module.getAttribute(MODULE_NAME_EN_ATTRIBUTE)) ? NO_NAME : module
						.getAttribute(MODULE_NAME_EN_ATTRIBUTE).trim(), JepRiaToolkitUtil.isEmpty(moduleDataSource) ? defaultDataSource : moduleDataSource,
						moduleRoles, JepRiaToolkitUtil.isEmpty(modulePackage) ? JepRiaToolkitUtil.multipleConcat(PKG_PREFIX, moduleName.toLowerCase())
								: modulePackage);
				m.setNotRebuild(OFF.equalsIgnoreCase(module.getAttribute(MODULE_BUILD_ATTRIBUTE)));
				m.setTable(getTableById(jepApplicationDoc, formName));
				// Инициализация данных о тулбаре
				detailizedModuleForToolBar(jepApplicationDoc, m, module);

				List<ModuleField> mfList = new ArrayList<ModuleField>();
				List<Element> fields = getModuleFieldsById(jepApplicationDoc, formName);
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
						mf.setPrimaryKey(fieldId.equalsIgnoreCase(getPrimaryKeyById(jepApplicationDoc, formName)));
						// детализируем поле после парсинга форм: детальной и
						// списочной
						detailizedModuleField(jepApplicationDoc, m, mf);
						mfList.add(mf);
					}

				// добавляем соответствие модуля его списку полей
				formFields.put(m, mfList);
			}

			for (int i = 0; i < forms.size(); i++) {
				List<String> nodesWithChildren = getNodesWithChildren(jepApplicationDoc, forms.get(i));
				if (!JepRiaToolkitUtil.isEmpty(nodesWithChildren))
					formWithTheirDependencies.add(nodesWithChildren);
			}

			return true;
		} catch (IOException e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", APPLICATION_SETTING_XML,
					" is not found!! Fill and put it in Application Directory!"));
			return false;
		} catch (SAXException e) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", APPLICATION_SETTING_XML,
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

		JepRiaToolkitUtil.makeDir(LIB_DIR_NAME);

		JepRiaToolkitUtil.makeDir(WELCOME_PAGE_DIR_NAME);
		JepRiaToolkitUtil
				.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/web"));
		JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
				"/main/client/ui/main"));
		JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
				"/main/client/entrance"));
		JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
				"/main/shared/text"));

		if (forms.size() == 0) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(WARNING_PREFIX, "Tag-parameter '", MODULE_TAG_NAME,
					"' is desirable. To create structure for forms, fill this parameter in ", APPLICATION_SETTING_XML, "!"));
			return;
		}

		for (int i = 0; i < forms.size(); i++) {
			String formName = ((String) forms.get(i)).toLowerCase();
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/client/ui/form/detail"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/client/ui/form/list"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/server/ejb"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/server/service"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/shared/field"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/shared/record"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/shared/service"));
			JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName.toLowerCase(),
					"/", formName, "/shared/text"));
		}
		
		JepRiaToolkitUtil.makeDir("config/" + DEBUG_BUILD_CONFIG_NAME + "/src/html");
		JepRiaToolkitUtil.makeDir(
			JepRiaToolkitUtil.multipleConcat(
				"config/", DEBUG_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/")
			);
		//deploy.properties
		String deployPropContent = JepRiaToolkitUtil.readFromJar("/templates/config/debug/deploy.properties", UTF_8);
		JepRiaToolkitUtil.writeToFile(deployPropContent, JepRiaToolkitUtil.multipleConcat("config/"+ DEBUG_BUILD_CONFIG_NAME + "/deploy.properties"), UTF_8, false);

		JepRiaToolkitUtil.makeDir("config/"+ RELEASE_BUILD_CONFIG_NAME + "/src/html");
		JepRiaToolkitUtil.makeDir(
			JepRiaToolkitUtil.multipleConcat(
				"config/", RELEASE_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/")
			);
		deployPropContent = JepRiaToolkitUtil.readFromJar("/templates/config/release/deploy.properties", UTF_8);
		JepRiaToolkitUtil.writeToFile(deployPropContent, JepRiaToolkitUtil.multipleConcat("config/"+ RELEASE_BUILD_CONFIG_NAME + "/deploy.properties"), UTF_8, false);

	}

	/**
	 * Генерация дескриптора развертывания web.xml
	 */
	private void generateWebXML() {
		try {
			Document doc = JepRiaToolkitUtil.createDOM();
			// root
			Element webAppElement = doc.createElement("web-app");
			// servlet
			createServletNode(doc, webAppElement, LOGGER_SERVLET_NAME, LOGGER_SERVLET_CLASS, JepRiaToolkitUtil.multipleConcat("/", moduleName, "/gwt-log"));
			createServletNode(doc, webAppElement, MAIN_SERVICE_SERVLET_NAME, MAIN_SERVICE_SERVLET_CLASS,
					JepRiaToolkitUtil.multipleConcat("/", moduleName, "/MainService"));
			boolean isAlreadyExcelServletAvailable = false;
			boolean isAlreadyTextFileAvailable = false;
			boolean isAlreadyBinaryFileAvailable = false;
			for (int i = 0; i < forms.size(); i++) {
				String formName = (String) forms.get(i);
				Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
				Module module = hm.keySet().iterator().next();
				List<ModuleField> moduleFields = hm.values().iterator().next();

				createServletNode(
					doc,
					webAppElement,
					JepRiaToolkitUtil.multipleConcat(formName, "Servlet"),
					JepRiaToolkitUtil.multipleConcat("com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".",
						formName.toLowerCase(), ".server.service.", formName, "ServiceImpl"),
					JepRiaToolkitUtil.multipleConcat("/", moduleName, "/", formName, "Service"));
				createEjbNode(doc, webAppElement, formName);

				boolean hasTextFile = false;
				boolean hasBinaryFile = false;
				for (ModuleField moduleField : moduleFields) {
					if (moduleField.isCLOB())
						hasTextFile = true;
					if (moduleField.isBLOB())
						hasBinaryFile = true;
					if (moduleField.isLOB()) {
						createServletNode(
								doc,
								webAppElement,
								"uploadServlet",
								JepRiaToolkitUtil.multipleConcat("com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".",
										formName.toLowerCase(), ".server.service.UploadServiceImpl"), JepRiaToolkitUtil.multipleConcat("/", moduleName, "/upload"));
						createServletNode(
								doc,
								webAppElement,
								"downloadServlet",
								JepRiaToolkitUtil.multipleConcat("com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".",
										formName.toLowerCase(), ".server.service.DownloadServiceImpl"),
								JepRiaToolkitUtil.multipleConcat("/", moduleName, "/download"));
					}
				}
				if (module.isExcelAvailable() && !isAlreadyExcelServletAvailable) {
					createServletNode(
							doc,
							webAppElement,
							"ShowExcelServlet",
							JepRiaToolkitUtil.multipleConcat("com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".",
									formName.toLowerCase(), ".server.service.ShowExcelServlet"), JepRiaToolkitUtil.multipleConcat("/", moduleName, "/showExcel"));
					isAlreadyExcelServletAvailable = true;
				}
				if (hasTextFile && !isAlreadyTextFileAvailable) {
					createEjbNode(doc, webAppElement, formName, "TextFileUpload", "com.technology.jep.jepria.server.upload.clob.TextFileUploadLocal");
					createEjbNode(doc, webAppElement, formName, "TextFileDownload",
							"com.technology.jep.jepria.server.download.clob.TextFileDownloadLocal");
					isAlreadyTextFileAvailable = true;
				}
				if (hasBinaryFile && !isAlreadyBinaryFileAvailable) {
					createEjbNode(doc, webAppElement, formName, "BinaryFileUpload",
							"com.technology.jep.jepria.server.upload.blob.BinaryFileUploadLocal");
					createEjbNode(doc, webAppElement, formName, "BinaryFileDownload",
							"com.technology.jep.jepria.server.download.blob.BinaryFileDownloadLocal");
					isAlreadyBinaryFileAvailable = true;
				}
			}
			// version servlet and ejb
			createServletNode(doc, webAppElement, VERSION_SERVLET_NAME, VERSION_SERVLET_CLASS, "/versionServlet");
			createEjbNode(doc, webAppElement, null, "Version", "com.technology.jep.jepria.server.version.ejb.JepVersionLocal");

			// welcomeFileList
			Element welcomeFileListElement = doc.createElement("welcome-file-list");
			Element welcomeFileElement = doc.createElement("welcome-file");
			welcomeFileElement.setTextContent(JepRiaToolkitUtil.multipleConcat(moduleName, ".jsp"));
			welcomeFileListElement.appendChild(welcomeFileElement);
			webAppElement.appendChild(welcomeFileListElement);
			// security
			createSecurityNode(doc, webAppElement);

			doc.appendChild(webAppElement);

			JepRiaToolkitUtil
					.prettyPrint(doc, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, packageName.toLowerCase(), "/",
							moduleName.toLowerCase(), "/web/web.xml"));

		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * Создание записи о сервлете в дескрипторе развертывания
	 * 
	 * @param doc
	 *            ссылка на xml-документ
	 * @param rootNode
	 *            ссылка на корневой узел
	 * @param servletName
	 *            наименование сервлета
	 * @param servletClass
	 *            ссылка на класс, реализующий данный сервлет
	 * @param servletUrlPattern
	 *            url на сервлет
	 */
	private void createServletNode(Document doc, Element rootNode, String servletName, String servletClass, String servletUrlPattern) {
		Element servletElement = doc.createElement("servlet");
		Element servletNameElement = doc.createElement("servlet-name");
		servletNameElement.setTextContent(servletName);
		Element servletClassElement = doc.createElement("servlet-class");
		servletClassElement.setTextContent(servletClass);

		servletElement.appendChild(servletNameElement);
		servletElement.appendChild(servletClassElement);
		rootNode.appendChild(servletElement);

		Element servletElementMapping = doc.createElement("servlet-mapping");
		servletNameElement = doc.createElement("servlet-name");
		servletNameElement.setTextContent(servletName);
		Element urlPatternElement = doc.createElement("url-pattern");
		urlPatternElement.setTextContent(servletUrlPattern);

		servletElementMapping.appendChild(servletNameElement);
		servletElementMapping.appendChild(urlPatternElement);
		rootNode.appendChild(servletElementMapping);
	}

	/**
	 * Создание бина в дескрипторе развертывания
	 * 
	 * @param doc
	 *            ссылка на xml-документ
	 * @param rootNode
	 *            ссылка на корневой узел
	 * @param formName
	 *            наименование формы
	 * @param beanNameAndPackage
	 *            массив с названием бина и наименованием пакета
	 */
	private void createEjbNode(Document doc, Element rootNode, String formName, String... beanNameAndPackage) {
		Element ejbLocalRefElement = doc.createElement("ejb-local-ref");
		Element ejbRefNameElement = doc.createElement("ejb-ref-name");
		boolean isAnotherBeanType = beanNameAndPackage.length == 2;
		ejbRefNameElement.setTextContent(JepRiaToolkitUtil.multipleConcat("ejb/", (isAnotherBeanType ? beanNameAndPackage[0] : formName), "Bean"));
		Element ejbRefTypeElement = doc.createElement("ejb-ref-type");
		ejbRefTypeElement.setTextContent("Session");
		Element localElement = doc.createElement("local");
		localElement.setTextContent(isAnotherBeanType ? beanNameAndPackage[1] : JepRiaToolkitUtil.multipleConcat(
				"com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".server.ejb.", formName, "Local"));

		ejbLocalRefElement.appendChild(ejbRefNameElement);
		ejbLocalRefElement.appendChild(ejbRefTypeElement);
		ejbLocalRefElement.appendChild(localElement);
		rootNode.appendChild(ejbLocalRefElement);
	}

	/**
	 * Создание записи с настройками безопасности в дескрипторе развертывания
	 * 
	 * @param doc
	 *            ссылка на xml-документ
	 * @param rootNode
	 *            ссылка на корневой узел
	 */
	private void createSecurityNode(Document doc, Element rootNode) {
		if (securityRoles.size() == 0) {
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(WARNING_PREFIX,
					"Tag-parameter 'roles' is desirable. To generate web.xml correctly, fill this parameter in ", APPLICATION_SETTING_XML, "!"));
			return;
		}

		Element securityConstraintElement = doc.createElement("security-constraint");
		Element webResourceCollectionElement = doc.createElement("web-resource-collection");

		Element webResourceNameElement = doc.createElement("web-resource-name");
		webResourceNameElement.setTextContent("The application entry point");

		Element urlPatternElement = doc.createElement("url-pattern");
		//urlPatternElement.setTextContent(JepRiaToolkitUtil.multipleConcat("/", moduleName, ".jsp"));
		urlPatternElement.setTextContent(JepRiaToolkitUtil.multipleConcat("/*"));

		Element httpMethodElement = doc.createElement("http-method");
		httpMethodElement.setTextContent("*");

		webResourceCollectionElement.appendChild(webResourceNameElement);
		webResourceCollectionElement.appendChild(urlPatternElement);
		webResourceCollectionElement.appendChild(httpMethodElement);

		securityConstraintElement.appendChild(webResourceCollectionElement);

		Element authConstraintElement = doc.createElement("auth-constraint");

		for (int i = 0; i < securityRoles.size(); i++) {
			String securityRole = (String) securityRoles.get(i);
			Element securityRoleElement = doc.createElement("security-role");
			Element roleNameElement = doc.createElement("role-name");
			roleNameElement.setTextContent(securityRole); 
			//roleNameElement.setTextContent("{{PUBLIC}}"); 
			securityRoleElement.appendChild(roleNameElement);
			rootNode.appendChild(securityRoleElement);

			Element roleName2Element = doc.createElement("role-name");
			roleName2Element.setTextContent(securityRole);

			authConstraintElement.appendChild(roleName2Element);
		}

		securityConstraintElement.appendChild(authConstraintElement);

		rootNode.appendChild(securityConstraintElement);

		// public access for version servlet
		securityConstraintElement = doc.createElement("security-constraint");
		webResourceCollectionElement = doc.createElement("web-resource-collection");

		webResourceNameElement = doc.createElement("web-resource-name");
		webResourceNameElement.setTextContent("Version Servlet entry point");

		urlPatternElement = doc.createElement("url-pattern");
		urlPatternElement.setTextContent("/versionServlet");

		Element httpMethodElement1 = doc.createElement("http-method");
		httpMethodElement1.setTextContent("GET");

		Element httpMethodElement2 = doc.createElement("http-method");
		httpMethodElement2.setTextContent("POST");

		webResourceCollectionElement.appendChild(webResourceNameElement);
		webResourceCollectionElement.appendChild(urlPatternElement);
		webResourceCollectionElement.appendChild(httpMethodElement1);
		webResourceCollectionElement.appendChild(httpMethodElement2);

		securityConstraintElement.appendChild(webResourceCollectionElement);

		rootNode.appendChild(securityConstraintElement);
	}

	/**
	 * Создание application.xml
	 */
	private void generateApplicationXML() {
		try {
			Document doc = JepRiaToolkitUtil.createDOM();
			// root
			Element applicationElement = doc.createElement("application");

			Element displayNameElement = doc.createElement("display-name");
			displayNameElement.setTextContent(moduleName);

			Element moduleElement = doc.createElement("module");
			Element webElement = doc.createElement("web");
			Element webUriElement = doc.createElement("web-uri");
			webUriElement.setTextContent(JepRiaToolkitUtil.multipleConcat(moduleName.toLowerCase(), ".war"));
			Element contextRootElement = doc.createElement("context-root");
			contextRootElement.setTextContent(moduleName);
			webElement.appendChild(webUriElement);
			webElement.appendChild(contextRootElement);
			moduleElement.appendChild(webElement);

			Element module2Element = doc.createElement("module");
			Element ejbElement = doc.createElement("ejb");
			ejbElement.setTextContent(JepRiaToolkitUtil.multipleConcat(moduleName.toLowerCase(), "-ejb.jar"));
			module2Element.appendChild(ejbElement);

			Element module3Element = doc.createElement("module");
			Element ejbElement2 = doc.createElement("ejb");
			ejbElement2.setTextContent("jepria-ejb.jar");
			module3Element.appendChild(ejbElement2);

			applicationElement.appendChild(displayNameElement);
			applicationElement.appendChild(moduleElement);
			applicationElement.appendChild(module2Element);
			applicationElement.appendChild(module3Element);

			doc.appendChild(applicationElement);

			JepRiaToolkitUtil.prettyPrint(doc, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/application.xml"));

		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * Создание orion-application.xml
	 */
	private void generateOrionApplicationXML() {
		try {
			Document doc = JepRiaToolkitUtil.createDOM();
			// root
			Element rootElement = doc.createElement("orion-application");
			rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "http://xmlns.oracle.com/oracleas/schema/orion-application-10_0.xsd");
			rootElement.setAttribute("deployment-version", "10.1.3.1.0");
			rootElement.setAttribute("component-classification", "external");
			rootElement.setAttribute("schema-major-version", "10");
			rootElement.setAttribute("schema-minor-version", "0");

			Element ejbModuleElement = doc.createElement("ejb-module");
			ejbModuleElement.setAttribute("remote", "false");
			ejbModuleElement.setAttribute("path", JepRiaToolkitUtil.multipleConcat("lib/", moduleName.toLowerCase(), "-ejb.jar"));

			Element webModuleElement = doc.createElement("web-module");
			webModuleElement.setAttribute("id", moduleName.toLowerCase());
			webModuleElement.setAttribute("path", JepRiaToolkitUtil.multipleConcat(moduleName.toLowerCase(), ".war"));

			Element libraryElement = doc.createElement("library");
			libraryElement.setAttribute("path", LIB_DIR_NAME);

			Element library2Element = doc.createElement("library");
			library2Element.setAttribute("path", JepRiaToolkitUtil.multipleConcat(moduleName.toLowerCase(), "/WEB-INF/", LIB_DIR_NAME));

			Element logElement = doc.createElement("log");
			Element fileElement = doc.createElement("file");
			fileElement.setAttribute("path", "application.log");
			logElement.appendChild(fileElement);

			Element jaznElement = doc.createElement("jazn");
			jaznElement.setAttribute("provider", "XML");
			Element propertyElement = doc.createElement("property");
			propertyElement.setAttribute("name", "role.mapping.dynamic");
			propertyElement.setAttribute("value", "true");

			Element property2Element = doc.createElement("property");
			property2Element.setAttribute("name", "custom.loginmodule.provider");
			property2Element.setAttribute("value", "true");
			property2Element.setAttribute("path", JepRiaToolkitUtil.multipleConcat(moduleName.toLowerCase(), "/WEB-INF/", LIB_DIR_NAME));

			Element jaznWebAppElement = doc.createElement("jazn-web-app");
			jaznWebAppElement.setAttribute("auth-method", "CUSTOM_AUTH");
			jaznElement.appendChild(propertyElement);
			jaznElement.appendChild(property2Element);
			jaznElement.appendChild(jaznWebAppElement);

			Element jaznLoginConfigElement = doc.createElement("jazn-loginconfig");
			Element applicationElement = doc.createElement("application");
			Element nameElement = doc.createElement("name");
			nameElement.setTextContent(moduleName.toLowerCase());
			Element loginModulesElement = doc.createElement("login-modules");
			Element loginModuleElement = doc.createElement("login-module");
			Element classElement = doc.createElement("class");
			classElement.setTextContent("com.technology.jep.jepcommon.security.JepLoginModule");
			Element controlFlagElement = doc.createElement("control-flag");
			controlFlagElement.setTextContent("required");

			loginModuleElement.appendChild(classElement);
			loginModuleElement.appendChild(controlFlagElement);

			Node node = jaznLoginConfigElement.appendChild(applicationElement);
			node.appendChild(nameElement);
			loginModulesElement.appendChild(loginModuleElement);

			node.appendChild(loginModulesElement);

			rootElement.appendChild(ejbModuleElement);
			rootElement.appendChild(webModuleElement);
			rootElement.appendChild(libraryElement);
			rootElement.appendChild(library2Element);
			rootElement.appendChild(logElement);
			rootElement.appendChild(jaznElement);
			rootElement.appendChild(jaznLoginConfigElement);

			doc.appendChild(rootElement);

			JepRiaToolkitUtil.prettyPrint(doc, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_RESOURCE, packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/orion-application.xml"));

		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * Создание основного GWT.xml для всего приложения, описывающего его
	 * структуру
	 */
	private void generateMainGwtXML() {

		// debug
		String mainGwtTemplateContent = JepRiaToolkitUtil.readFromJar(
			"/templates/config/debug/src/java/com/technology/jep/jepriashowcase/main/template.gwt.xml", 
			UTF_8);
		mainGwtTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(mainGwtTemplateContent, packageName, moduleName);
		
		StringBuilder inheritsModules = new StringBuilder();
		StringBuilder entryPoint = new StringBuilder();

		//getting left spacer
		String spacerString = "";
		int spacerEnd = mainGwtTemplateContent.indexOf("<%InheritsModules%>"), spacerStart = spacerEnd;
		
		while (!spacerString.contains(END_OF_LINE) && spacerStart > 0)
			spacerString = mainGwtTemplateContent.substring(--spacerStart, spacerEnd);

		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			inheritsModules.append("<inherits name=\"com.technology.");
			inheritsModules.append(packageName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(moduleName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(formName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(formName);
			inheritsModules.append("\" />");
			if (i < forms.size() - 1)
				inheritsModules.append(spacerString);
		}
		
		mainGwtTemplateContent = mainGwtTemplateContent.replace("<%InheritsModules%>", inheritsModules.toString());

		entryPoint.append("<entry-point class=\"com.technology.");
		entryPoint.append(packageName.toLowerCase());
		entryPoint.append(".");
		entryPoint.append(moduleName.toLowerCase());
		entryPoint.append(".main.client.entrance.");
		entryPoint.append(moduleName);
		entryPoint.append("EntryPoint\" />");

		mainGwtTemplateContent = mainGwtTemplateContent.replace("<%EntryPoint%>", entryPoint.toString());

		JepRiaToolkitUtil.writeToFile(mainGwtTemplateContent, 
			JepRiaToolkitUtil.multipleConcat(
				PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/", moduleName, ".gwt.xml"), UTF_8);

		JepRiaToolkitUtil.writeToFile(mainGwtTemplateContent, 
				JepRiaToolkitUtil.multipleConcat(
					"config/", DEBUG_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
					packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
					"/main/", moduleName, ".gwt.xml"), UTF_8);

		//release
		mainGwtTemplateContent = JepRiaToolkitUtil.readFromJar(
			"/templates/config/release/src/java/com/technology/jep/jepriashowcase/main/template.gwt.xml", 
			UTF_8);
		mainGwtTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(mainGwtTemplateContent, packageName, moduleName);
		
		inheritsModules = new StringBuilder();
		entryPoint = new StringBuilder();

		//getting left spacer
		spacerString = "";
		spacerEnd = mainGwtTemplateContent.indexOf("<%InheritsModules%>"); 
		spacerStart = spacerEnd;
		
		while (!spacerString.contains(END_OF_LINE) && spacerStart > 0)
			spacerString = mainGwtTemplateContent.substring(--spacerStart, spacerEnd);

		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			inheritsModules.append("<inherits name=\"com.technology.");
			inheritsModules.append(packageName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(moduleName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(formName.toLowerCase());
			inheritsModules.append(".");
			inheritsModules.append(formName);
			inheritsModules.append("\" />");
			if (i < forms.size() - 1)
				inheritsModules.append(spacerString);
		}
		
		mainGwtTemplateContent = mainGwtTemplateContent.replace("<%InheritsModules%>", inheritsModules.toString());

		entryPoint.append("<entry-point class=\"com.technology.");
		entryPoint.append(packageName.toLowerCase());
		entryPoint.append(".");
		entryPoint.append(moduleName.toLowerCase());
		entryPoint.append(".main.client.entrance.");
		entryPoint.append(moduleName);
		entryPoint.append("EntryPoint\" />");

		mainGwtTemplateContent = mainGwtTemplateContent.replace("<%EntryPoint%>", entryPoint.toString());

		JepRiaToolkitUtil.writeToFile(mainGwtTemplateContent, 
			JepRiaToolkitUtil.multipleConcat(
				"config/", RELEASE_BUILD_CONFIG_NAME, "/", PREFIX_DESTINATION_SOURCE_CODE,
				packageName.toLowerCase(),  "/", moduleName.toLowerCase(),
				"/main/", moduleName, ".gwt.xml"), UTF_8);

	}

	/**
	 * Генерация всех необходимых xml-документов для данного приложения (включая
	 * основной и дочерние)
	 */
	private void generateGwtXML() {
		generateMainGwtXML();
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			try {
				Document doc = JepRiaToolkitUtil.createDOM();
				// root
				Element moduleElement = doc.createElement("module");
				moduleElement.setAttribute("rename-to", formName);

				Element inheritsElement = doc.createElement("inherits");
				inheritsElement
						.setAttribute("name", JepRiaToolkitUtil.multipleConcat("com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(),
								".main.", moduleName));
				moduleElement.appendChild(inheritsElement);

				Element sourceElement = doc.createElement("source");
				sourceElement.setAttribute("path", "client");
				moduleElement.appendChild(sourceElement);

				sourceElement = doc.createElement("source");
				sourceElement.setAttribute("path", "shared");
				moduleElement.appendChild(sourceElement);

				doc.appendChild(moduleElement);

				JepRiaToolkitUtil.prettyPrint(doc, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/", formName, ".gwt.xml"));

			} catch (Exception e) {
				// e.printStackTrace();
				JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
			}
		}
	}

	/**
	 * Создание страницы приветствия
	 */
	private void createWelcomePage() {
		// By default, create page as for Test Build
		createWelcomePage(false);
	}

	/**
	 * Создание страницы приветствия в зависимости от флага продукционности
	 * сборки
	 * 
	 * @param isProductionBuild
	 *            флаг продукционности сборки
	 * @throws IOException 
	 */
	public void createWelcomePage(boolean isProductionBuild) {
		// JSP
		String jspTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/src/html/" + JSP_TEMPLATE_NAME, UTF_8);
		jspTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(jspTemplateContent, packageName, moduleName);
		JepRiaToolkitUtil.writeToFile(jspTemplateContent, JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/", moduleName, ".jsp"), "UTF-8");
		
		//CSS debug
		String cssTemplateDebugContent = JepRiaToolkitUtil.readFromJar("/templates/config/debug/src/html/" + CSS_TEMPLATE_NAME, UTF_8);
		JepRiaToolkitUtil.writeToFile(cssTemplateDebugContent, JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), "UTF-8", false);
		JepRiaToolkitUtil.writeToFile(cssTemplateDebugContent, JepRiaToolkitUtil.multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), "UTF-8", false);

		//CSS release
		String cssTemplateReleaseContent = JepRiaToolkitUtil.readFromJar("/templates/config/release/src/html/" + CSS_TEMPLATE_NAME, UTF_8);
		JepRiaToolkitUtil.writeToFile(cssTemplateReleaseContent, JepRiaToolkitUtil.multipleConcat("config/", RELEASE_BUILD_CONFIG_NAME, "/", WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), "UTF-8", false);
	}

	/**
	 * Создание текстовых ресурсов
	 */
	private void createTextFile() {
		String mainContent = JepRiaToolkitUtil.multipleConcat("module.title=", moduleName);
		String mainContentEn = mainContent;
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);

			Module mod = hm.keySet().iterator().next();
			String formTitl = mod.getModuleName();
			formTitl = JepRiaToolkitUtil.isEmpty(formTitl) ? formName : formTitl;
			String formTitlEn = mod.getModuleNameEn();
			formTitlEn = JepRiaToolkitUtil.isEmpty(formTitlEn) ? formName : formTitlEn;
			mainContent += JepRiaToolkitUtil.multipleConcat(END_OF_LINE, "submodule.", formName.toLowerCase(), ".title=", formTitl);
			mainContentEn += JepRiaToolkitUtil.multipleConcat(END_OF_LINE, "submodule.", formName.toLowerCase(), ".title=", formTitlEn);
			String content = JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".title=", formTitl, END_OF_LINE, WHITE_SPACE, END_OF_LINE);
			String contentEn = JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".title=", formTitlEn, END_OF_LINE, WHITE_SPACE, END_OF_LINE);

			List<ModuleField> fFields = hm.get(mod);
			for (int j = 0; j < fFields.size(); j++) {
				ModuleField formField = fFields.get(j);
				String field = formField.getFieldId().toLowerCase();

				if (formField.isListFormField()) {
					content += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".list.", field, "=", formField.getFieldListFormName(), END_OF_LINE);
					contentEn += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".list.", field, "=", formField.getFieldListFormNameEn(), END_OF_LINE);
				}
				if (formField.isDetailFormField()) {
					content += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".detail.", field, "=", formField.getFieldDetailFormName(), END_OF_LINE);
					contentEn += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".detail.", field, "=", formField.getFieldDetailFormNameEn(), END_OF_LINE);
				}

			}

			for (ModuleButton button : mod.getToolBarCustomButtons()) {
				String text = button.getText();
				if (JepRiaToolkitUtil.isEmpty(text))
					text = button.getButtonId().toLowerCase();

				content += JepRiaToolkitUtil.multipleConcat(text, "=", JepRiaToolkitUtil.isEmpty(button.getName()) ? text : button.getName(), END_OF_LINE);
				contentEn += JepRiaToolkitUtil.multipleConcat(text, "=", JepRiaToolkitUtil.isEmpty(button.getNameEn()) ? text : button.getNameEn(), END_OF_LINE);
			}

			content += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".detail.row_count=Количество записей", END_OF_LINE);
			contentEn += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.initSmall(formName), ".detail.row_count=Row Count", END_OF_LINE);

			Module module = hm.keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/text/", formName, "Text_Source.properties"));
			// выйдет из цикла раньше
			JepRiaToolkitUtil.writeToFile(contentEn, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/text/", formName, "Text_en.properties"));
		}

		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/main/shared/text/", moduleName, "Text_Source.properties"));
		JepRiaToolkitUtil.writeToFile(mainContentEn, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/main/shared/text/", moduleName, "Text_en.properties"));
	}

	/**
	 * Создание страницы обзора для приложения
	 */
	private void createOverview() {
		String content = JepRiaToolkitUtil
				.multipleConcat(
						"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">",
						END_OF_LINE,
						"<html>",
						END_OF_LINE,
						"	<body bgcolor=\"white\">",
						END_OF_LINE,
						"		Пакет содержит application часть модуля ",
						moduleName, //!!!!
						".<br/>",
						END_OF_LINE,
						"		<h2>Содержание</h2>",
						END_OF_LINE,
						"		<ul>",
						END_OF_LINE,
						"			<li><a href=\"#about\">Описание</a>",
						END_OF_LINE,
						"			<li><a href=\"#deploy\">Установка</a>",
						END_OF_LINE,
						"			<li><a href=\"#error\">Ошибки</a>",
						END_OF_LINE,
						"			<li><a href=\"#todo\">Доработки</a>",
						END_OF_LINE,
						"		</ul>",
						END_OF_LINE,
						"		<h3><a name=\"about\">Описание</a></h3>",
						END_OF_LINE,
						"		Данный модуль содержит функционал.",
						END_OF_LINE,
						"		<h3><a name=\"deploy\">Установка</a></h3>",
						END_OF_LINE,
						"		Установка производится из директории <i>App</i> модуля командой:<br/>",
						END_OF_LINE,
						"		<i>ant deploy -DDEPLOYMENT_PATH=&lt;DEPLOYMENT_PATH&gt; -DLOGIN=&lt;LOGIN&gt; -DPASSWORD=&lt;PASSWORD&gt; -DOC4J_HOME=&lt;OC4J_HOME&gt; -DLOAD_OPERATORID=&lt;LOAD_OPERATORID&gt;&nbsp;</i>, где<br/>",
						END_OF_LINE,
						"		<ul>",
						END_OF_LINE,
						"			<li>DEPLOYMENT_PATH - адрес установки модуля вида <i>[opmn://]host:opmnPort[/iASInstanceName/oc4jInstanceName]</i>",
						END_OF_LINE,
						"			<li>PORT - параметр нужен в случае, если http-порт на сервере отличен от 80го (по-умолчанию значение 80)",
						END_OF_LINE,
						"			<li>LOGIN - логин пользователя, под которым происходит установка модуля",
						END_OF_LINE,
						"			<li>PASSWORD - пароль пользователя, под которым происходит установка модуля",
						END_OF_LINE,
						"			<li>OC4J_HOME - директория установки JDeveloper",
						END_OF_LINE,
						"			<li>LOAD_OPERATORID - логин/пароль учетной записи в системе RFInfo, от чьего имени производится установка",
						END_OF_LINE,
						"		</ul>",
						END_OF_LINE,
						"		Пример:",
						END_OF_LINE,
						"		<pre>ant deploy -DDEPLOYMENT_PATH=opmn://10.90.7.148:6003/OracleAS_1.srvapp7.d.t/OC4J_4 -DPORT=8888 -DLOGIN=LOGIN -DPASSWORD=PASSWORD -DOC4J_HOME=D:\\Oracle\\jdev101330 -DLOAD_OPERATORID=nagornyys/123<br/></pre>",
						END_OF_LINE, "		<h3><a name=\"error\">Ошибки</a></h3>", END_OF_LINE, "		<br/>", END_OF_LINE,
						"		<h3><a name=\"todo\">Доработки</a></h3>", END_OF_LINE, "		<br/>", END_OF_LINE, "	</body>", END_OF_LINE, "</html>",
						END_OF_LINE);
		
		//String jspTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/", src/html/" + JSP_TEMPLATE_NAME, UTF_8);
		
		JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
				moduleName.toLowerCase(), "/overview.html"));
	}

	/**
	 * Создание классов клиенстких констант
	 */
	private void createClientConstant() {
		String mainContent = JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"import com.google.gwt.core.client.GWT;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.shared.text.", moduleName, "Text;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.jep.jepria.shared.JepRiaConstant;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"public class ", moduleName, "ClientConstant extends JepRiaConstant {", END_OF_LINE
			);
		
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			List<String> scopeModuleIds = getDependencyNodesIfExists(formName);
			boolean isMainForm = scopeModuleIds.size() > 0;

			mainContent += JepRiaToolkitUtil.multipleConcat("	public static final String ", formName.toUpperCase(), "_MODULE_ID = \"", formName, "\";", END_OF_LINE);

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					(isMainForm ? JepRiaToolkitUtil.multipleConcat("import static com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.", moduleName, "ClientConstant.*;", END_OF_LINE) : ""),
					"import com.google.gwt.core.client.GWT;", END_OF_LINE, 
					"import com.technology.", packageModuleFormName, ".shared.", formName, "Constant;", END_OF_LINE, 
					"import com.technology.", packageModuleFormName, ".shared.text.", formName, "Text;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "ClientConstant extends ", formName, "Constant {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE
				);

			if (isMainForm) {
				content += JepRiaToolkitUtil.multipleConcat("	public static String[] scopeModuleIds = {", formName.toUpperCase(), "_MODULE_ID");
				for (int index = 0; index < scopeModuleIds.size(); index++) {
					content += JepRiaToolkitUtil.multipleConcat(", ", scopeModuleIds.get(index).toUpperCase(), "_MODULE_ID");
				}
				content += JepRiaToolkitUtil.multipleConcat("}; ", END_OF_LINE, WHITE_SPACE, END_OF_LINE);
			}

			content += JepRiaToolkitUtil.multipleConcat("	public static ", formName, "Text ", JepRiaToolkitUtil.initSmall(formName), "Text = (", formName, "Text) GWT.create(", formName, "Text.class);", END_OF_LINE, "}" );
			
			Module module = getModuleWithFieldsById(formName).keySet().iterator().next();
			if (module.isNotRebuild())
				continue;

			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/", formName, "ClientConstant.java"));
		}

		mainContent += JepRiaToolkitUtil.multipleConcat(
				END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"	public static ", moduleName, "Text ", JepRiaToolkitUtil.initSmall(moduleName), "Text = (", moduleName, "Text) GWT.create(", moduleName, "Text.class);", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE
			);
		
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			
			mainContent += JepRiaToolkitUtil.multipleConcat("	public static final String URL_", formName.toUpperCase(), "_MODULE = \"", "/", moduleName, "/", moduleName, ".jsp?em=", formName, "&es=sh\";", END_OF_LINE);
		}
		
		mainContent += "}";
		
		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/main/client/", moduleName, "ClientConstant.java"));

	}

	/**
	 * Создание классов клиентских фабрик
	 */
	private void createClientFactoryImpl() {
		String importFormPresenters = new String();
		String clientFactories = new String();
		String moduleIds = new String();
		String moduleIdsText = new String();

		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			Module module = getModuleWithFieldsById(formName).keySet().iterator().next();
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());

			importFormPresenters += JepRiaToolkitUtil.multipleConcat(
					"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.", formName, "ClientFactoryImpl;", END_OF_LINE
				);
			clientFactories += JepRiaToolkitUtil.multipleConcat(
					"		", (JepRiaToolkitUtil.isEmpty(clientFactories) ? "" : "else "), "if(", formName.toUpperCase(), "_MODULE_ID.equals(moduleId)) {", END_OF_LINE, 
					"			GWT.runAsync(new LoadPlainClientFactory(callback) {", END_OF_LINE,
					"				public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {", END_OF_LINE, 
					"					Log.trace(", moduleName, "ClientFactoryImpl.this.getClass() + \".getPlainClientFactory: moduleId = \" + ", formName.toUpperCase(), "_MODULE_ID);", END_OF_LINE,
					"					return ", formName, "ClientFactoryImpl.getInstance();", END_OF_LINE, 
					"				}", END_OF_LINE, 
					"			});", END_OF_LINE, 
					"		}", END_OF_LINE
					);

			boolean isJepToolBar = module.isStandardToolBar() && !module.isToolBarOff();
			boolean hasDependency = false;// dependencies.size() > 0;
			boolean hasCustomButtons = module.hasCustomButtons();
			boolean isDblClickOff = module.isDblClickOff();
			boolean isListFormPresenter = true; //module.hasListFormPresenter();
			boolean existToolBarPresenter = module.hasToolBarPresenter();
			boolean existToolBarView = module.hasToolBarView();

			moduleIds += JepRiaToolkitUtil.multipleConcat((i == 0 ? "			" : "			, "), formName.toUpperCase(), "_MODULE_ID", END_OF_LINE);
			moduleIdsText += JepRiaToolkitUtil.multipleConcat((i == 0 ? "			" : "			, "), JepRiaToolkitUtil.initSmall(moduleName), "Text.submodule_", formName.toLowerCase(), "_title()", END_OF_LINE);

			List<ModuleButton> listFormCustomButtons = module.getToolBarCustomButtonsForForm(FORM.LIST_FORM);

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".client;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"import static com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.", moduleName, "ClientConstant.", formName.toUpperCase(), "_MODULE_ID;", END_OF_LINE,
					"import com.google.gwt.core.client.GWT;", END_OF_LINE,
					"import com.google.gwt.place.shared.Place;", END_OF_LINE,
					"import com.google.gwt.user.client.ui.IsWidget;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE,
					"import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;", END_OF_LINE,
					(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
							"import com.technology.", packageModuleFormName, ".client.ui.eventbus.", formName, "EventBus;", END_OF_LINE
							)
							: ""),
					"import com.technology.jep.jepria.client.ui.JepPresenter;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.form.list.ListFormView;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;", END_OF_LINE,
					"import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;", END_OF_LINE,
					(isJepToolBar && !existToolBarPresenter ? JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;", END_OF_LINE) 
							: JepRiaToolkitUtil.multipleConcat(
									"import com.technology.", (hasCustomButtons ? packageModuleFormName : "jep.jepria"), ".client.ui.toolbar.", (hasCustomButtons ? formName : ""), "ToolBarPresenter;", END_OF_LINE)),
									JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;", END_OF_LINE),
									JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;", END_OF_LINE),
					(isJepToolBar && !existToolBarView ? "" : JepRiaToolkitUtil.multipleConcat("import com.technology.", packageName.toLowerCase(),
							".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.toolbar.", formName, "ToolBarViewImpl;", END_OF_LINE)),
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".client.ui.form.detail.", formName, "DetailFormPresenter;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".client.ui.form.detail.", formName, "DetailFormViewImpl;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".client.ui.form.list.", formName, "ListFormViewImpl;", END_OF_LINE,
					(isDblClickOff || isListFormPresenter || listFormCustomButtons.size() > 0 ? JepRiaToolkitUtil.multipleConcat(
						"import com.technology.", packageModuleFormName, ".client.ui.form.list.", formName, "ListFormPresenter;")
						: "import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;"), END_OF_LINE,
					hasDependency || hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
						"import com.technology.", packageModuleFormName, ".client.ui.plain.", formName, "ModulePresenter;", END_OF_LINE) 
						: "",
					"import com.technology.", packageModuleFormName, ".shared.service.", formName, "Service;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".shared.service.", formName, "ServiceAsync;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".shared.record.", formName, "RecordDefinition;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"public class ", formName, "ClientFactoryImpl<E extends ", (hasCustomButtons ? formName : "Plain"), "EventBus, S extends ", formName, "ServiceAsync>", END_OF_LINE,
					"	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	private static final IsWidget ", JepRiaToolkitUtil.initSmall(formName), "DetailFormView = new ", formName, "DetailFormViewImpl();", END_OF_LINE,
					!isJepToolBar || existToolBarView ? JepRiaToolkitUtil.multipleConcat(
							"	private static final IsWidget ", JepRiaToolkitUtil.initSmall(formName), "ToolBarView = new ", formName, "ToolBarViewImpl();", END_OF_LINE) 
							: "",
					"	private static final IsWidget ", JepRiaToolkitUtil.initSmall(formName), "ListFormView = new ", formName, "ListFormViewImpl();", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public ", formName, "ClientFactoryImpl() {", END_OF_LINE,
					"		super(", formName, "RecordDefinition.instance);", END_OF_LINE,
					"		initActivityMappers(this);", END_OF_LINE,
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {", END_OF_LINE,
					"		if(instance == null) {", END_OF_LINE,
					"			instance = GWT.create(", formName, "ClientFactoryImpl.class);", END_OF_LINE,
					"		}", END_OF_LINE,
					"		return instance;", END_OF_LINE,
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"public JepPresenter createPlainModulePresenter(Place place) {", END_OF_LINE,
					
					"	return new ", hasCustomButtons ? formName : "Standard", "ModulePresenter(", formName.toUpperCase(), "_MODULE_ID, place, this);", END_OF_LINE,
					"}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public JepPresenter createDetailFormPresenter(Place place) {", END_OF_LINE,
					"		return new ", formName, "DetailFormPresenter(place, this);", END_OF_LINE,
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public JepPresenter createListFormPresenter(Place place) {", END_OF_LINE,
					"		return new ", formName, "ListFormPresenter(place, this);", END_OF_LINE,
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					!isJepToolBar || existToolBarView ? JepRiaToolkitUtil.multipleConcat(
						"	public JepPresenter createToolBarPresenter(Place place) {", END_OF_LINE,
						"		return new ", (hasCustomButtons ? formName : ""), "ToolBarPresenter(place, this);", END_OF_LINE,
						"	}", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE)
						: "",
					!isJepToolBar || existToolBarView ? JepRiaToolkitUtil.multipleConcat(
							"	public IsWidget getToolBarView() {", END_OF_LINE, 
							"		return ", JepRiaToolkitUtil.initSmall(formName), "ToolBarView;", END_OF_LINE, 
							"	}", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE) : "",
					"	public IsWidget getDetailFormView() {", END_OF_LINE,
					"		return ", JepRiaToolkitUtil.initSmall(formName), "DetailFormView;", END_OF_LINE,
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public IsWidget getListFormView() {", END_OF_LINE,
					"		return ", JepRiaToolkitUtil.initSmall(formName), "ListFormView;", END_OF_LINE,
					"	}", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	public S getService() {", END_OF_LINE,
					"		if(dataService == null) {", END_OF_LINE,
					"			dataService = (S) GWT.create(", formName, "Service.class);", END_OF_LINE,
					"		}", END_OF_LINE,
					"		return dataService;", END_OF_LINE,
					"	}", END_OF_LINE,
					hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
					"	public E getEventBus() {", END_OF_LINE,
					"		if(eventBus == null) {", END_OF_LINE,
					"			eventBus = new " + formName + "EventBus(this);", END_OF_LINE,
					"		}", END_OF_LINE,
					"		return (E) eventBus;", END_OF_LINE,
					"	}", END_OF_LINE) : "",
					"}"
				);

			if (module.isNotRebuild())
				continue;

			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/", formName, "ClientFactoryImpl.java"));
		}

		String mainContent = JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"import static com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.", moduleName, "ClientConstant.*;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"import com.allen_sauer.gwt.log.client.Log;", END_OF_LINE,
				"import com.google.gwt.activity.shared.Activity;", END_OF_LINE,
				"import com.google.gwt.core.client.GWT;", END_OF_LINE, 
				"import com.google.gwt.place.shared.Place;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				importFormPresenters,
				"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.ui.main.", moduleName, "MainModulePresenter;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainView;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainClientFactory;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainModulePresenter;", END_OF_LINE,
				"import com.technology.jep.jepria.client.async.LoadAsyncCallback;", END_OF_LINE,
				"import com.technology.jep.jepria.client.async.LoadPlainClientFactory;", END_OF_LINE, 
				"import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE,
				"import com.technology.jep.jepria.shared.service.JepMainServiceAsync;", END_OF_LINE, 
				"import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"public class ", moduleName, "ClientFactoryImpl<E extends MainEventBus, S extends JepMainServiceAsync>", END_OF_LINE,
				"	extends MainClientFactoryImpl<E, S>", END_OF_LINE, 
				"		implements MainClientFactory<E, S> {", END_OF_LINE,
				"	public static MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {", END_OF_LINE, 
				"		if(instance == null) {", END_OF_LINE,
				"			instance = GWT.create(", moduleName, "ClientFactoryImpl.class);", END_OF_LINE, 
				"		}", END_OF_LINE,
				"		return instance;", END_OF_LINE, 
				"	}", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"	private ", moduleName, "ClientFactoryImpl() {", END_OF_LINE, 
				"		super(new String[]{", END_OF_LINE, 
				moduleIds, "		},new String[]{", END_OF_LINE, 
				moduleIdsText, "		});", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"		initActivityMappers(this);", END_OF_LINE,
				"	}", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"	@SuppressWarnings(\"unchecked\")", END_OF_LINE,
				"	@Override", END_OF_LINE,
				"	public Activity createMainModulePresenter() {", END_OF_LINE,
				"		return new ", moduleName, "MainModulePresenter(this);", END_OF_LINE,
				"	}", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"	public void getPlainClientFactory(String moduleId, final LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {", END_OF_LINE, 
				clientFactories, "	}", END_OF_LINE, 
				"}");

		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
				moduleName.toLowerCase(), "/main/client/", moduleName, "ClientFactoryImpl.java"));
	}

	/**
	 * Создание класса точки входа
	 */
	private void createEntryPoint() {
		String mainContent = JepRiaToolkitUtil.multipleConcat(
			"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(),".main.client.entrance;", END_OF_LINE, 
			WHITE_SPACE, END_OF_LINE, 
			"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.", moduleName, "ClientFactoryImpl;", END_OF_LINE,
			"import com.technology.jep.jepria.client.entrance.JepEntryPoint;", END_OF_LINE, 
			WHITE_SPACE, END_OF_LINE,
			"public class ", moduleName, "EntryPoint extends JepEntryPoint {", END_OF_LINE, 
			WHITE_SPACE, END_OF_LINE, 
			"	", moduleName, "EntryPoint() {", END_OF_LINE, 
			"		super(", moduleName, "ClientFactoryImpl.getInstance());", END_OF_LINE,
			"	}", END_OF_LINE, 
			"}");
		
		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
				moduleName.toLowerCase(), "/main/client/entrance/", moduleName, "EntryPoint.java"));
	}

	/**
	 * Создание классов презентеров детальных форм
	 */
	private void createDetailFormPresenter() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			boolean isMainForm = isMainForm(formName);
			boolean isDependentForm = !JepRiaToolkitUtil.isEmpty(isDependentForm(formName));
			boolean hasOptionField = false;
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			List<ModuleField> moduleFields = hm.values().iterator().next();
			String fieldVisibility = new String();
			String fieldMandatory = new String();
			String fieldEditable = new String();
			String fieldEnable = new String();
			String fieldsHeight = new String();
			String services = new String();
			String fillOptions = new String();
			List<String> subtractFieldIds = new ArrayList<String>();

			String[] infoAboutCustomEvent = getCustomEventInfo(module, formName, FORM.DETAIL_FORM);

			for (ModuleField moduleField : moduleFields) {
				List<WorkstateEnum> visibleWorkstates = moduleField.getVisibleWorkStates();
				List<WorkstateEnum> mandatoryWorkstates = moduleField.getMandatoryWorkStates();
				List<WorkstateEnum> editableWorkstates = moduleField.getEditableWorkStates();
				List<WorkstateEnum> enableWorkstates = moduleField.getEnableWorkStates();
				String visibleWorkstateOr = new String();
				String mandatoryWorkstateOr = new String();
				String editableWorkstateOr = new String();
				String enableWorkstateOr = new String();
				String height = moduleField.getFieldHeight();
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());

				if (isOptionField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
							JepRiaToolkitUtil.subtractFieldSuffix(moduleField.getFieldId()), null));
					subtractFieldIds.add(subtractFieldId);

					if (JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())) {
						services += JepRiaToolkitUtil.multipleConcat(
								"		fields.addFieldListener(", moduleField.getFieldId(), ", JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {", END_OF_LINE, 
								"			@Override", END_OF_LINE,
								"			public void handleEvent(final JepEvent event) {", END_OF_LINE, 
								"				service.get", subtractFieldId, "(new FirstTimeUseAsyncCallback<List<JepOption>>(event) {", END_OF_LINE,
								"					public void onSuccessLoad(List<JepOption> result){", END_OF_LINE, 
								"						fields.setFieldOptions(", moduleField.getFieldId(), ", result);", END_OF_LINE, 
								"					}", END_OF_LINE, 
								"				});", END_OF_LINE, 
								"			}", END_OF_LINE, 
								"		});", END_OF_LINE);
					} else if (JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
								|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
								|| JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
							) {
						services += JepRiaToolkitUtil.multipleConcat(
								"		service.get", subtractFieldId, "(new JepAsyncCallback<List<JepOption>>() {", END_OF_LINE, 
								"			public void onSuccess(List<JepOption> result){", END_OF_LINE, 
								"				fields.setFieldOptions(", moduleField.getFieldId(), ", result);", END_OF_LINE, 
								"			}", END_OF_LINE, 
								"		});", END_OF_LINE
							);
					}
				}

				if (!JepRiaToolkitUtil.isEmpty(height)) {
					fieldsHeight += JepRiaToolkitUtil.multipleConcat("		fields.setFieldHeight(", moduleField.getFieldId(), ", ", height, ");", END_OF_LINE);
				}
				String orWithDoubleWhiteSpace = JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, OR, WHITE_SPACE);
				if (!JepRiaToolkitUtil.isEmpty(visibleWorkstates)) {
					for (WorkstateEnum state : visibleWorkstates) {
						visibleWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(visibleWorkstateOr) ? "" : orWithDoubleWhiteSpace),
								JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
					}
					fieldVisibility += JepRiaToolkitUtil.multipleConcat("		fields.setFieldVisible(", moduleField.getFieldId(), ", ", visibleWorkstateOr, ");", END_OF_LINE);
				}
				
				if (!JepRiaToolkitUtil.isEmpty(mandatoryWorkstates)) {
					for (WorkstateEnum state : mandatoryWorkstates) {
						mandatoryWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(mandatoryWorkstateOr) ? "" : orWithDoubleWhiteSpace),
								JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
					}
					fieldMandatory += JepRiaToolkitUtil.multipleConcat("		fields.setFieldAllowBlank(", moduleField.getFieldId(), ", !",
							(mandatoryWorkstateOr.contains(orWithDoubleWhiteSpace) ? "(" : ""), mandatoryWorkstateOr,
							(mandatoryWorkstateOr.contains(orWithDoubleWhiteSpace) ? ")" : ""), ");", END_OF_LINE);
				}
				
				
				if (moduleField.isDetailFormField() && !moduleField.isEditable()) {
					fieldEditable += JepRiaToolkitUtil.multipleConcat("		fields.setFieldEditable(", moduleField.getFieldId(), ", false);", END_OF_LINE);
				} else if (!JepRiaToolkitUtil.isEmpty(editableWorkstates)) {
					for (WorkstateEnum state : editableWorkstates) {
						editableWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(editableWorkstateOr) ? "" : orWithDoubleWhiteSpace),
								JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
					}
					fieldEditable += JepRiaToolkitUtil.multipleConcat("		fields.setFieldEditable(", moduleField.getFieldId(), ", ", editableWorkstateOr, ");", END_OF_LINE);
				} 
				
				if (!JepRiaToolkitUtil.isEmpty(enableWorkstates)) {
					for (WorkstateEnum state : enableWorkstates) {
						enableWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(enableWorkstateOr) ? "" : orWithDoubleWhiteSpace),
								JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
					}
					fieldEnable += JepRiaToolkitUtil.multipleConcat("		fields.setFieldEnabled(", moduleField.getFieldId(), ", ", enableWorkstateOr, ");",  END_OF_LINE);
				}

				if (isOptionField)
					hasOptionField = true;

			}

			boolean isFieldVisible = !JepRiaToolkitUtil.isEmpty(fieldVisibility), isFieldMandatory = !JepRiaToolkitUtil.isEmpty(fieldMandatory), isFieldEditable = !JepRiaToolkitUtil
					.isEmpty(fieldEditable), isFieldEnable = !JepRiaToolkitUtil.isEmpty(fieldEnable), isFieldHeight = !JepRiaToolkitUtil.isEmpty(fieldsHeight), hasCustomButtons = module
					.hasCustomButtons();

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".client.ui.form.detail;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE,
					(!isDependentForm ? JepRiaToolkitUtil.multipleConcat(
							"import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;", END_OF_LINE) : ""),
					"import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;", END_OF_LINE,
					(isMainForm ? JepRiaToolkitUtil.multipleConcat("import static com.technology.", packageModuleFormName, ".client.", formName, "ClientConstant.*;", END_OF_LINE) : ""),
					WHITE_SPACE, END_OF_LINE,
					"import com.google.gwt.place.shared.Place;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;", END_OF_LINE,
					infoAboutCustomEvent[0],
					(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat("import com.technology.", packageModuleFormName.toLowerCase(), ".client.ui.eventbus.", formName, "EventBus;", END_OF_LINE)
							: JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE)),
					"import com.technology.jep.jepria.client.ui.WorkstateEnum;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;", END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(services) ? "" : JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.client.async.JepAsyncCallback;", END_OF_LINE,
							"import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;", END_OF_LINE,
							"import com.technology.jep.jepria.client.widget.event.JepListener;", END_OF_LINE,
							"import com.technology.jep.jepria.client.widget.event.JepEvent;", END_OF_LINE,
							"import com.technology.jep.jepria.client.widget.event.JepEventType;", END_OF_LINE)),
					(hasOptionField ? JepRiaToolkitUtil.multipleConcat("import java.util.List;", END_OF_LINE,
							"import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE) : ""));

			content += JepRiaToolkitUtil.multipleConcat(
					"import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".shared.service.", formName, "ServiceAsync;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "DetailFormPresenter<E extends ", (hasCustomButtons ? formName : "Plain"), "EventBus, S extends ", formName, "ServiceAsync> ", END_OF_LINE,
					"		extends DetailFormPresenter<", formName, "DetailFormView, E, S, StandardClientFactory<E, S>> ",
					(JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[1]) ? "" : JepRiaToolkitUtil.multipleConcat(END_OF_LINE, "			implements ",
							infoAboutCustomEvent[1])),
					"{ ", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(services) ? "" : JepRiaToolkitUtil.multipleConcat("	private S service = clientFactory.getService();", END_OF_LINE,
							WHITE_SPACE, END_OF_LINE)),
					"	public ", formName, "DetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {", END_OF_LINE,
					"		super(",
					(isMainForm ? "scopeModuleIds, " : ""),
					"place, clientFactory);", END_OF_LINE,
					"	}", END_OF_LINE,
					JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[2]) ? "" : JepRiaToolkitUtil.multipleConcat("	@Override", END_OF_LINE,
							"	public void start(AcceptsOneWidget container, EventBus eventBus) {", END_OF_LINE,
							"		super.start(container, eventBus);", END_OF_LINE, WHITE_SPACE, END_OF_LINE, infoAboutCustomEvent[2], "	}", END_OF_LINE,
							WHITE_SPACE, END_OF_LINE),
					WHITE_SPACE, END_OF_LINE,
					"	", (JepRiaToolkitUtil.isEmpty(services) ? "/* " : ""), "public void bind() {", END_OF_LINE,
					"		super.bind();", END_OF_LINE,
					"		// TODO Здесь размещается код связывания presenter-а и view ", END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(services) ? "" : services),
					"	}", END_OF_LINE,
					"	", (JepRiaToolkitUtil.isEmpty(services) ? "*/ " : ""), END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	protected void adjustToWorkstate(WorkstateEnum workstate) {", END_OF_LINE,
					(!isFieldVisible ? "" : JepRiaToolkitUtil.multipleConcat(fieldVisibility, WHITE_SPACE, END_OF_LINE)),
					(!isFieldMandatory ? "" : JepRiaToolkitUtil.multipleConcat(fieldMandatory, WHITE_SPACE, END_OF_LINE)),
					(!isFieldEditable ? "" : JepRiaToolkitUtil.multipleConcat(fieldEditable, WHITE_SPACE, END_OF_LINE)),
					(!isFieldEnable ? "" : JepRiaToolkitUtil.multipleConcat(fieldEnable, WHITE_SPACE, END_OF_LINE)),
					(!isFieldHeight ? "" : JepRiaToolkitUtil.multipleConcat(fieldsHeight, WHITE_SPACE, END_OF_LINE)),
					!isDependentForm ? JepRiaToolkitUtil.multipleConcat("		fields.setFieldVisible(MAX_ROW_COUNT, SEARCH.equals(workstate));", END_OF_LINE,
							"		fields.setFieldAllowBlank(MAX_ROW_COUNT, !SEARCH.equals(workstate));", END_OF_LINE,
							"		fields.setFieldValue(MAX_ROW_COUNT, 25);", END_OF_LINE) : "", "	}", END_OF_LINE, WHITE_SPACE, END_OF_LINE

					, (JepRiaToolkitUtil.isEmpty(fillOptions) ? "" : JepRiaToolkitUtil.multipleConcat(fillOptions, WHITE_SPACE, END_OF_LINE))

					,
					JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[3]) ? "" : JepRiaToolkitUtil.multipleConcat(infoAboutCustomEvent[3], WHITE_SPACE, END_OF_LINE)

					, "}");

			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/form/detail/", formName, "DetailFormPresenter.java"));
		}
	}

	/**
	 * Создание классов представлений детальных форм
	 */
	private void createDetailFormView() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			boolean isDependentForm = !JepRiaToolkitUtil.isEmpty(isDependentForm(formName));
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			List<ModuleField> moduleFields = hm.values().iterator().next();

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".client.ui.form.detail;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE, 
					"import static com.technology.", packageModuleFormName, ".client.", formName, "ClientConstant.", JepRiaToolkitUtil.initSmall(formName), "Text;", END_OF_LINE
					);
			if (!isDependentForm) {
				content += JepRiaToolkitUtil.multipleConcat("import ", FIELD_PREFIX, "JepIntegerField;", END_OF_LINE,
						"import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;", END_OF_LINE);
			}

			content += JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE);
					
			String initializationFieldVariableString = new String();
			String addFieldToContainerString = new String();
			String putFieldToManagerString = new String();
			String fieldsMaxLength = new String();
			boolean hasNumberFieldWithBigDecimalType = false;
			for (ModuleField moduleField : JepRiaToolkitUtil.sortFields(moduleFields, true)) {
				if (moduleField.isDetailFormField()) {
					if (!JepRiaToolkitUtil.isEmpty(moduleField.getFieldMaxLength())) {
						fieldsMaxLength += JepRiaToolkitUtil.multipleConcat("		setFieldMaxLength(", moduleField.getFieldId(), ", ",
								moduleField.getFieldMaxLength(), ");", END_OF_LINE);
					}
					String fieldWidget = moduleField.getFieldWidget();

					boolean isBigDecimalNumberField = false; 
					boolean isMaskedTextField = JEP_MASKED_TEXT_FIELD.equalsIgnoreCase(fieldWidget); 
					if (!hasNumberFieldWithBigDecimalType) {
						hasNumberFieldWithBigDecimalType = isBigDecimalNumberField;
					}
					if (!JepRiaToolkitUtil.isEmpty(FIELD_WIDGET.get(fieldWidget))) {
						String importString = JepRiaToolkitUtil.multipleConcat("import ", FIELD_WIDGET.get(fieldWidget), ";", END_OF_LINE);

						if (!content.contains(importString))
							content += importString;
					}

					boolean hasCustomWidth = !JepRiaToolkitUtil.isEmpty(moduleField.getFieldWidth());
					boolean hasLabelWidth = !JepRiaToolkitUtil.isEmpty(moduleField.getLabelWidth());
					String fieldName = JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(moduleField.getFieldId(), null),
							fieldWidget.substring(JEP_FIELD_PREFIX.length()));

					initializationFieldVariableString += JepRiaToolkitUtil.multipleConcat(
							"		", fieldWidget, " ", fieldName, " = new ", fieldWidget, 
							"(", JepRiaToolkitUtil.initSmall(formName), "Text.", JepRiaToolkitUtil.initSmall(formName), "_detail_", moduleField.getFieldId().toLowerCase(), "()", 
							(isBigDecimalNumberField ? ", BigDecimal.class" : ""), 
							(isMaskedTextField ? ", \"cccccccccc\"" : ""),
							");", END_OF_LINE,
							
							(hasCustomWidth ? JepRiaToolkitUtil.multipleConcat("		", fieldName, ".setFieldWidth(", moduleField.getFieldWidth(), ");", END_OF_LINE) : ""),
							(hasLabelWidth ? JepRiaToolkitUtil.multipleConcat("		", fieldName, ".setLabelWidth(", moduleField.getLabelWidth(), ");", END_OF_LINE) : ""));

					addFieldToContainerString += JepRiaToolkitUtil.multipleConcat("		panel.add(", fieldName, ");", END_OF_LINE);
					putFieldToManagerString += JepRiaToolkitUtil.multipleConcat("		fields.put(", moduleField.getFieldId().toUpperCase(), ", ", fieldName, ");", END_OF_LINE);
				}
			}
			content += JepRiaToolkitUtil.multipleConcat(
					"import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;", END_OF_LINE,
					"import com.google.gwt.dom.client.Style.Unit;", END_OF_LINE,
					"import com.google.gwt.user.client.ui.ScrollPanel;", END_OF_LINE,
					"import com.google.gwt.user.client.ui.VerticalPanel;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					!JepRiaToolkitUtil.isEmpty(fieldsMaxLength) ? JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;", END_OF_LINE,
							"import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;", END_OF_LINE) : "",
					hasNumberFieldWithBigDecimalType ? JepRiaToolkitUtil.multipleConcat(
							"import java.math.BigDecimal;", END_OF_LINE,
							"import ", FIELD_PREFIX, "JepNumberField;", END_OF_LINE) : "",
					"import com.technology.jep.jepria.client.widget.field.FieldManager;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"public class ", formName, "DetailFormViewImpl", END_OF_LINE,
					"	extends DetailFormViewImpl ", END_OF_LINE,
					"	implements ", formName, "DetailFormView {	", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public ", formName, "DetailFormViewImpl() {", END_OF_LINE,
					"		super(new FieldManager());", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		ScrollPanel scrollPanel = new ScrollPanel();", END_OF_LINE,
					"		scrollPanel.setSize(\"100%\", \"100%\");", END_OF_LINE,
					"		VerticalPanel panel = new VerticalPanel();", END_OF_LINE,
					"		panel.getElement().getStyle().setMarginTop(5, Unit.PX);", END_OF_LINE,
					"		scrollPanel.add(panel);", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					initializationFieldVariableString,
					(!isDependentForm ? JepRiaToolkitUtil.multipleConcat(
							"		JepIntegerField maxRowCountField = new JepIntegerField(", JepRiaToolkitUtil.initSmall(formName), "Text.", JepRiaToolkitUtil.initSmall(formName), "_detail_row_count());", END_OF_LINE,
							"		maxRowCountField.setMaxLength(4);", 		END_OF_LINE,
							"		maxRowCountField.setFieldWidth(55);", 		END_OF_LINE,
							!JepRiaToolkitUtil.isEmpty(module.getFieldLabelWidth()) ? JepRiaToolkitUtil.multipleConcat(
									"		maxRowCountField.setLabelWidth(", module.getFieldLabelWidth(), ");", END_OF_LINE) : "", 
									WHITE_SPACE, END_OF_LINE) : ""),
					addFieldToContainerString,
					(!isDependentForm ? JepRiaToolkitUtil.multipleConcat("		panel.add(maxRowCountField);", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE) : ""),
					"		setWidget(scrollPanel);", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					putFieldToManagerString,
					(!isDependentForm ? JepRiaToolkitUtil.multipleConcat("		fields.put(MAX_ROW_COUNT, maxRowCountField);", END_OF_LINE) : ""),
					JepRiaToolkitUtil.isEmpty(fieldsMaxLength) ? "" : JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE, fieldsMaxLength),
					"	}", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					!JepRiaToolkitUtil.isEmpty(fieldsMaxLength) ? JepRiaToolkitUtil.multipleConcat(
							"	public void setFieldMaxLength(String fieldId, Integer maxLength) {", END_OF_LINE,
							"		JepMultiStateField field = fields.get(fieldId);", END_OF_LINE, 
							"		if(field instanceof JepTextField) {", 		END_OF_LINE, 
							"			((JepTextField)field).setMaxLength(maxLength);", END_OF_LINE, 
							"		}", END_OF_LINE, 
							"	}", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE) : "", "}"
				);

			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/form/detail/", formName, "DetailFormViewImpl.java"));
			
			//DetailFormView interface
			JepRiaToolkitUtil.writeToFile(JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageModuleFormName, ".client.ui.form.detail;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"public interface ", formName, "DetailFormView extends DetailFormView {", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"}", END_OF_LINE
				)
				, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/form/detail/", formName, "DetailFormView.java"));

		}
	}

	/**
	 * Создание классов презентеров списочных форм
	 */
	private void createListFormPresenter() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			boolean isDblClickOff = module.isDblClickOff();
			boolean isHasPresenter = true; //module.hasListFormPresenter();
			boolean hasCustomButtons = module.hasCustomButtons();
			List<ModuleButton> formListButtons = module.getToolBarCustomButtonsForForm(FORM.LIST_FORM);

			String[] infoAboutCustomEvent = getCustomEventInfo(module, formName, FORM.LIST_FORM);

			if (isDblClickOff || isHasPresenter || formListButtons.size() > 0) {
				String content = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".client.ui.form.list;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
//						"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE,
						"import com.google.gwt.place.shared.Place;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.jep.jepria.client.widget.event.JepListener;", END_OF_LINE,
						"import com.technology.jep.jepria.client.widget.event.JepEvent;", END_OF_LINE,
						"import com.technology.jep.jepria.client.widget.event.JepEventType;", END_OF_LINE,
						(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
								"import com.technology.", packageModuleFormName, ".client.ui.eventbus.", formName, "EventBus;", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat(
								"import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;", END_OF_LINE)),
						"import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;", END_OF_LINE,
						"import com.technology.", packageModuleFormName, ".shared.service.", formName, "ServiceAsync;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;", END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.form.list.ListFormView;", END_OF_LINE,
						infoAboutCustomEvent[0],
						WHITE_SPACE, END_OF_LINE,
						"public class ", formName, "ListFormPresenter<V extends ListFormView, E extends ", (hasCustomButtons ? formName : "Plain"), "EventBus, S extends ", formName, "ServiceAsync, F extends StandardClientFactory<E, S>> ", END_OF_LINE,
						"	extends ListFormPresenter<V, E, S, F> ",
						(JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[1]) ? "" : JepRiaToolkitUtil.multipleConcat(END_OF_LINE, 
								"		implements ",
								infoAboutCustomEvent[1])),
						"{ ", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"	public ", formName, "ListFormPresenter(Place place, F clientFactory) {", END_OF_LINE,
						"		super(place, clientFactory);", END_OF_LINE,
						"	}", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						
						JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[2]) ? "" : JepRiaToolkitUtil.multipleConcat(
								"	@Override", END_OF_LINE,
								"	public void start(AcceptsOneWidget container, EventBus eventBus) {", END_OF_LINE,
								"		super.start(container, eventBus);", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE, 
								infoAboutCustomEvent[2], 
								"	}", END_OF_LINE, 
								WHITE_SPACE, END_OF_LINE),
								
						isDblClickOff ? JepRiaToolkitUtil.multipleConcat(
								"	@Override", END_OF_LINE,
								"	public void rowDoubleClick(JepEvent event) {}", END_OF_LINE,
								WHITE_SPACE, END_OF_LINE)
								: "",
						JepRiaToolkitUtil.isEmpty(infoAboutCustomEvent[3]) ? "" : JepRiaToolkitUtil.multipleConcat(infoAboutCustomEvent[3], WHITE_SPACE,
								END_OF_LINE), "}");

				if (module.isNotRebuild())
					continue;
				JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/form/list/", formName, "ListFormPresenter.java"));
			}
		}
	}

	/**
	 * Создание классов представлений списочных форм
	 */
	private void createListFormView() {
		final int COLUMN_WIDTH = 150;
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			List<ModuleField> moduleFields = hm.values().iterator().next();
			String columnConfiguration = new String();
			String classField = new String();
			String columnConfigMethod = new String();
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".client.ui.form.list;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE
					);
			if (moduleFields.size() > 0) {
				content += JepRiaToolkitUtil.multipleConcat(
						"import static com.technology.", packageModuleFormName, ".client.", formName, "ClientConstant.", JepRiaToolkitUtil.initSmall(formName), "Text;", END_OF_LINE
						);
			}

			for (ModuleField moduleField : JepRiaToolkitUtil.sortFields(moduleFields, false)) {
				boolean isIntegerType = INTEGER.name().equalsIgnoreCase(moduleField.getFieldType());
				boolean isBigDecimalType = BIGDECIMAL.name().equalsIgnoreCase(moduleField.getFieldType());
				boolean isBooleanType = BOOLEAN.name().equalsIgnoreCase(moduleField.getFieldType());
				boolean isDateType = DATE.name().equalsIgnoreCase(moduleField.getFieldType());
				boolean isTimeType = TIME.name().equalsIgnoreCase(moduleField.getFieldType());
				boolean isDateTimeType = DATE_TIME.name().equalsIgnoreCase(moduleField.getFieldType());

				if (isIntegerType || isBigDecimalType) {
					String importString = JepRiaToolkitUtil.multipleConcat(
							"import com.google.gwt.cell.client.NumberCell;", END_OF_LINE,
							"import com.google.gwt.i18n.client.NumberFormat;", END_OF_LINE);
					content += JepRiaToolkitUtil.appendIfHasNot(content, importString);
					
					String defaultNumberFormatter = 
						isBigDecimalType ? 
							JepRiaToolkitUtil.multipleConcat("	private static NumberFormat defaultDecimalFormatter = NumberFormat.getFormat(\"#0.00\");", END_OF_LINE) :
							JepRiaToolkitUtil.multipleConcat("	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat(\"#\");", END_OF_LINE); 
					
					classField += JepRiaToolkitUtil.appendIfHasNot(classField, defaultNumberFormatter);
				}
				
				if (isBooleanType) {
					String importString = JepRiaToolkitUtil.multipleConcat("import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;", END_OF_LINE, 
							"import com.technology.jep.jepria.shared.util.JepRiaUtil;", END_OF_LINE,
							"import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;", END_OF_LINE);
					content += JepRiaToolkitUtil.appendIfHasNot(content, importString);

				} else {
					if (isDateType || isTimeType || isDateTimeType) {
						String importString = JepRiaToolkitUtil.multipleConcat("import com.google.gwt.i18n.client.DateTimeFormat;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);
						importString = JepRiaToolkitUtil.multipleConcat("import com.google.gwt.cell.client.DateCell;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);

					}

					if (isDateType) {
						String importString = JepRiaToolkitUtil.multipleConcat(
								"import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);

						String defaultDateFormatter = JepRiaToolkitUtil.multipleConcat(
							"	private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);", END_OF_LINE);
						classField += JepRiaToolkitUtil.appendIfHasNot(classField, defaultDateFormatter);
					}

					if (isTimeType) {
						String importString = JepRiaToolkitUtil.multipleConcat(
								"import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);
						
						String defaultTimeFormatter = JepRiaToolkitUtil.multipleConcat(
							"	private static DateTimeFormat defaultTimeFormatter = DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT);", END_OF_LINE);
						classField += JepRiaToolkitUtil.appendIfHasNot(classField, defaultTimeFormatter);
					}

					if (isDateTimeType) {
						String importString = JepRiaToolkitUtil.multipleConcat(
								"import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);
						
						importString = JepRiaToolkitUtil.multipleConcat(
								"import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;", END_OF_LINE);
						content += JepRiaToolkitUtil.appendIfHasNot(content, importString);

						String defaultDateTimeFormatter = JepRiaToolkitUtil .multipleConcat(
							"	private static DateTimeFormat defaultDateTimeFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + \" \" + DEFAULT_TIME_FORMAT);", END_OF_LINE);
						classField += JepRiaToolkitUtil.appendIfHasNot(classField, defaultDateTimeFormatter);
					}
				}

				if (moduleField.isListFormField()) {
					String columnConfig = JepRiaToolkitUtil.multipleConcat("new JepColumn(", moduleField.getFieldId().toUpperCase(), ", ",
							JepRiaToolkitUtil.initSmall(formName), "Text.", JepRiaToolkitUtil.initSmall(formName), "_list_", moduleField.getFieldId()
									.toLowerCase(), "(), ", (JepRiaToolkitUtil.isEmpty(moduleField.getColumnWidth()) ? Integer.toString(COLUMN_WIDTH)
									: moduleField.getColumnWidth()));
					if (isDateType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new DateCell(defaultDateFormatter)"
							);
					}
					
					if (isTimeType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new DateCell(defaultTimeFormatter)"
							);
					}
					
					if (isDateTimeType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new DateCell(defaultDateTimeFormatter)"
							);
					}
					
					if (isBooleanType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new JepCheckBoxCell()"
							);
					}
					
					if (isIntegerType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new NumberCell(defaultNumberFormatter)"
							);
					}
					
					if (isBigDecimalType) {
						columnConfig += JepRiaToolkitUtil.multipleConcat(
							", new NumberCell(defaultDecimalFormatter)"
							);
					}
					
					columnConfig += ")";
					

					columnConfiguration += JepRiaToolkitUtil.multipleConcat("		columns.add(", columnConfig, ");", END_OF_LINE);
				}
			}
			content += JepRiaToolkitUtil.multipleConcat(
					"import com.google.gwt.user.client.ui.HeaderPanel;", END_OF_LINE,
					"import com.technology.jep.jepria.shared.record.JepRecord;", END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;", END_OF_LINE, 
					"import java.util.List;", END_OF_LINE,
					"import com.technology.jep.jepria.client.widget.list.JepColumn;", END_OF_LINE,
					"import com.technology.jep.jepria.client.widget.list.JepGrid;", END_OF_LINE,
					"import com.technology.jep.jepria.client.widget.list.GridManager;", END_OF_LINE,
					"import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;", END_OF_LINE, 
					"import java.util.ArrayList;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "ListFormViewImpl extends ListFormViewImpl<GridManager> {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"	public ", formName, "ListFormViewImpl() {", END_OF_LINE, 
					"		super(new GridManager());", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		HeaderPanel gridPanel = new HeaderPanel();", END_OF_LINE,
					"		setWidget(gridPanel);", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		gridPanel.setHeight(\"100%\");", END_OF_LINE,
					"		gridPanel.setWidth(\"100%\");", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getGridId(), getColumnConfigurations(), true);", END_OF_LINE,
					"		PagingStandardBar pagingBar = new PagingStandardBar(25);", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		gridPanel.setContentWidget(grid);", END_OF_LINE,
					"		gridPanel.setFooterWidget(pagingBar);", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"		list.setWidget(grid);", END_OF_LINE, 
					"		list.setPagingToolBar(pagingBar);", END_OF_LINE, 
					"	}", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					classField, 
					WHITE_SPACE, END_OF_LINE, 
					"	private static List<JepColumn> getColumnConfigurations() {", END_OF_LINE,
					"		final List<JepColumn> columns = new ArrayList<JepColumn>();", END_OF_LINE, 
					columnConfiguration,
					"		return columns;", END_OF_LINE, 
					"	}", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	private String getGridId() {", END_OF_LINE,
					"		return this.getClass().toString().replace(\"class \", \"\");", END_OF_LINE,
					"	}", END_OF_LINE,
					"}");

			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/form/list/", formName, "ListFormViewImpl.java"));
		}
	}

	/**
	 * Создание классов презентеров модулей
	 */
	private void createModulePresenter() {

		String moduleIds = new String();
		String moduleIdsText = new String();
		String addModuleProtection = new String();

		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);

			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());

			boolean hasCustomButtons = module.hasCustomButtons();
			
			String content = new String();
			
			if (hasCustomButtons) {
			
				String importEvent = "";
				String addEventHandler = "";
				String eventDescription = "";
				String importEventHandler = "";
				for (ModuleButton button : module.getToolBarCustomButtons()){
					
					addEventHandler += JepRiaToolkitUtil.multipleConcat(
							"		eventBus.addHandler(", button.getCustomEvent(), "Event.TYPE, this);", END_OF_LINE);
					 
					eventDescription +=
					JepRiaToolkitUtil.multipleConcat(
						"	@Override", END_OF_LINE
						, "	public void on", button.getCustomEvent(), "Event(", button.getCustomEvent(), "Event event) {", END_OF_LINE
						, "		// TODO Реализовать обработчик события ", button.getCustomEvent(), "Event", END_OF_LINE 
						, "		messageBox.alert(\"on", button.getCustomEvent(), "Event\");", END_OF_LINE
						, "	}", END_OF_LINE
					);
				 
					importEvent += JepRiaToolkitUtil.multipleConcat(
						"import com.technology.", packageModuleFormName ,".client.ui.eventbus.event.", button.getCustomEvent(), "Event;", END_OF_LINE);
				
					importEventHandler +=
					JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(importEventHandler)
					? "" : ", ") , button.getCustomEvent(), "Event.Handler");
				}
			
				content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName , ".client.ui.plain;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import static com.technology.", packageName.toLowerCase(),".",moduleName.toLowerCase(),".main.client.", moduleName, "ClientConstant.", formName.toUpperCase(), "_MODULE_ID;", END_OF_LINE, 
					"import com.google.gwt.core.client.GWT;", END_OF_LINE);
			
				 content += JepRiaToolkitUtil.multipleConcat(
				 "import com.google.gwt.place.shared.Place;", END_OF_LINE, 
				 "import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.", formName, "ClientFactoryImpl;", END_OF_LINE, 
				 "import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".shared.service.", formName, "ServiceAsync;", END_OF_LINE,
				 "import com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.eventbus.", formName, "EventBus;", END_OF_LINE, 
				 "import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;", END_OF_LINE,
				 "import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;", END_OF_LINE,
				 "import com.technology.jep.jepria.client.ui.plain.StandardModuleView;", END_OF_LINE,
				 "import com.technology.jep.jepria.client.util.JepClientUtil;", END_OF_LINE,
				 (hasCustomButtons ?
					JepRiaToolkitUtil.multipleConcat(importEvent, 
						  "import com.google.gwt.user.client.ui.AcceptsOneWidget;", END_OF_LINE, 
						  "import com.google.gwt.event.shared.EventBus;", END_OF_LINE)
					: "")
				 );
			
				content += JepRiaToolkitUtil.multipleConcat(
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "ModulePresenter<V extends StandardModuleView, E extends ", formName, "EventBus, S extends ", formName,"ServiceAsync, F extends StandardClientFactory<E, S>>", END_OF_LINE,
					"		extends StandardModulePresenter<V, E, S, F> ", END_OF_LINE, 
					(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
							"		implements ", importEventHandler) : ""),
					 " {", END_OF_LINE, 
					 WHITE_SPACE, END_OF_LINE,
					 "	public ",formName,"ModulePresenter(String moduleId, Place place, F clientFactory) {", END_OF_LINE, 
					 "		super(", formName.toUpperCase(),"_MODULE_ID, place, clientFactory);", END_OF_LINE, 
					 "	}", END_OF_LINE, 
					 WHITE_SPACE, END_OF_LINE
					);

				content += JepRiaToolkitUtil.multipleConcat(
					(hasCustomButtons ? JepRiaToolkitUtil.multipleConcat(
						WHITE_SPACE, END_OF_LINE, 
						"	@Override", END_OF_LINE,
						"	public void start(AcceptsOneWidget container, EventBus eventBus) {", END_OF_LINE, 
						"		super.start(container, eventBus);", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE
						, addEventHandler, "	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						eventDescription, 
						WHITE_SPACE, END_OF_LINE) : "")
						, "}");
			 }
			moduleIds += JepRiaToolkitUtil.multipleConcat((i == 0 ? "			" : "			, "), formName.toUpperCase(), "_MODULE_ID", END_OF_LINE);
			moduleIdsText += JepRiaToolkitUtil.multipleConcat((i == 0 ? "			" : "			, "), JepRiaToolkitUtil.initSmall(moduleName), "Text.submodule_", formName.toLowerCase(), "_title()", END_OF_LINE);
			addModuleProtection += JepRiaToolkitUtil.multipleConcat("		addModuleProtection(", formName.toUpperCase(), "_MODULE_ID, \"", module.getModuleSecurityRolesAsString(), "\", CHECK_ROLES_BY_OR);", END_OF_LINE);

			 if (module.isNotRebuild()) continue;
			
			if (hasCustomButtons) {
				JepRiaToolkitUtil.makeDir(
					JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/", moduleName
						.toLowerCase(), "/", formName, "/client/ui/plain"));

				JepRiaToolkitUtil
						.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName
								.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/plain/", formName, "ModulePresenter.java"));
				continue;
			}

		}

		String mainContent = JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.ui.main;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"import static com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.client.", moduleName, "ClientConstant.*;", END_OF_LINE,
				"import static com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_OR;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"import java.util.HashSet;", END_OF_LINE, 
				"import java.util.Set;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"import com.google.gwt.place.shared.Place;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainView;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainClientFactory;", END_OF_LINE,
				"import com.technology.jep.jepria.client.ui.main.MainModulePresenter;", END_OF_LINE,
				"import com.technology.jep.jepria.shared.service.JepMainServiceAsync;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"public class ", moduleName, "MainModulePresenter<E extends MainEventBus, S extends JepMainServiceAsync>", END_OF_LINE,
				"			extends MainModulePresenter<MainView, E, S, MainClientFactory<E, S>> {", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"	public ", moduleName, "MainModulePresenter(MainClientFactory<E, S> clientFactory) {", END_OF_LINE
				);

		mainContent += JepRiaToolkitUtil.multipleConcat(
				"		super(clientFactory);", END_OF_LINE, 
				addModuleProtection, END_OF_LINE, 
				"	}", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE, 
				"}"
				);

		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
				moduleName.toLowerCase(), "/main/client/ui/main/", moduleName, "MainModulePresenter.java"));
	}

	/**
	 * Создание классов реализаций сервисов приложения
	 */
	private void createServiceImpl() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			List<ModuleField> moduleFields = hm.values().iterator().next();
			String getters = new String();
			boolean hasLOBField = false;
			for (ModuleField moduleField : moduleFields) {
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());
				if (isOptionField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
							JepRiaToolkitUtil.subtractFieldSuffix(moduleField.getFieldId()), null));
					getters += JepRiaToolkitUtil.multipleConcat(
							WHITE_SPACE, END_OF_LINE, 
							"	public List<JepOption> get", subtractFieldId, "() throws ApplicationException {", END_OF_LINE, 
							"		List<JepOption> result = null;", END_OF_LINE, 
							"		try {", END_OF_LINE,
							"			", formName, " ", formName.toLowerCase(), " = (", formName, ") JepServerUtil.ejbLookup(ejbName);", END_OF_LINE, 
							"			result = ", formName.toLowerCase(), ".get", subtractFieldId, "();", END_OF_LINE, 
							"		} catch (Throwable th) {", END_OF_LINE,
							"			throw new ApplicationException(th.getLocalizedMessage(), th);", END_OF_LINE, 
							"		}", END_OF_LINE, 
							"		return result;", END_OF_LINE,
							"	}", END_OF_LINE);
				}
				if (moduleField.isLOB())
					hasLOBField = true;
			}
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".server.service;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".shared.service.", formName, "Service;", END_OF_LINE,
					"import com.technology.jep.jepria.server.service.JepDataServiceServlet;", END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(getters) ? "" : JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.server.util.JepServerUtil;", END_OF_LINE, 
							"import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE,
							"import com.technology.jep.jepria.shared.exceptions.ApplicationException;", END_OF_LINE,
							"import com.technology.", packageModuleFormName, ".server.ejb.", formName, ";", END_OF_LINE, 
							"import java.util.List;", END_OF_LINE
							)),
					"import com.technology.", packageModuleFormName, ".shared.record.", formName, "RecordDefinition;", END_OF_LINE,
					"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.BEAN_JNDI_NAME;", END_OF_LINE,
					hasLOBField ? (JepRiaToolkitUtil.multipleConcat(
							"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.DATA_SOURCE_JNDI_NAME;", END_OF_LINE, 
							"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.RESOURCE_BUNDLE_NAME;", END_OF_LINE)) : "",
					"import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"@RemoteServiceRelativePath(\"", formName, "Service\")", END_OF_LINE, 
					"public class ", formName, "ServiceImpl extends JepDataServiceServlet implements ", formName, "Service  {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	private static final long serialVersionUID = 1L;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"	public ", formName, "ServiceImpl() {", END_OF_LINE, 
					"		super(", formName, "RecordDefinition.instance, BEAN_JNDI_NAME", (hasLOBField ? ", DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME" : ""), ");", END_OF_LINE, 
					"	}", END_OF_LINE, 
					(JepRiaToolkitUtil.isEmpty(getters) ? "" : getters), "}");

			Module module = hm.keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/service/", formName, "ServiceImpl.java"));

			if (hasLOBField) {
				content = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".server.service;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.DATA_SOURCE_JNDI_NAME;", END_OF_LINE, 
						"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.RESOURCE_BUNDLE_NAME;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.", packageModuleFormName, ".shared.record.", formName, "RecordDefinition;", END_OF_LINE,
						"import com.technology.jep.jepria.server.upload.JepUploadServlet;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"public class UploadServiceImpl extends JepUploadServlet {", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"	public UploadServiceImpl() {", END_OF_LINE, 
						"		super(", formName, "RecordDefinition.instance,", END_OF_LINE,
						"			DATA_SOURCE_JNDI_NAME, ", END_OF_LINE, 
						"			RESOURCE_BUNDLE_NAME); ", END_OF_LINE, 
						"	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, "}");
				JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/service/UploadServiceImpl.java"));

				content = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".server.service;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.DATA_SOURCE_JNDI_NAME;", END_OF_LINE, 
						"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.RESOURCE_BUNDLE_NAME;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.", packageModuleFormName, ".shared.record.", formName, "RecordDefinition;", END_OF_LINE,
						"import com.technology.jep.jepria.server.download.JepDownloadServlet;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"public class DownloadServiceImpl extends JepDownloadServlet {", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"	public DownloadServiceImpl() {", END_OF_LINE, 
						"		super(", formName, "RecordDefinition.instance,", END_OF_LINE,
						"			DATA_SOURCE_JNDI_NAME, ", END_OF_LINE, 
						"			RESOURCE_BUNDLE_NAME); ", END_OF_LINE, 
						"	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, "}");
				JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/service/DownloadServiceImpl.java"));
			}

			if (module.isExcelAvailable()) {
				content = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageModuleFormName, ".server.service;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"import com.technology.", packageModuleFormName, ".shared.record.", formName, "RecordDefinition;", END_OF_LINE, 
						"import com.technology.jep.jepria.server.download.excel.ExcelServlet;", END_OF_LINE,
						WHITE_SPACE, END_OF_LINE, "public class ShowExcelServlet extends ExcelServlet {", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"	public ShowExcelServlet() {", END_OF_LINE, 
						"		super(", formName, "RecordDefinition.instance); ", END_OF_LINE, 
						"	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"}"
					);
				JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/service/ShowExcelServlet.java"));
			}
		}
	}

	/**
	 * Создание классов серверных констант
	 */
	private void createServerConstant() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".server;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.server.JepRiaServerConstant;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "ServerConstant extends JepRiaServerConstant {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	public static final String BEAN_JNDI_NAME = \"", formName, "Bean\";", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"	public static final String RESOURCE_BUNDLE_NAME = \"com.technology.", packageModuleFormName, ".shared.text.", formName, "Text\";", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE);

			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			String dataSource = module.getModuleDataSource();
			content += JepRiaToolkitUtil.multipleConcat(
					"	public static final String DATA_SOURCE_JNDI_NAME = \"jdbc/", dataSource, "\";", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE, "}");

			if (module.isNotRebuild())
				continue;
			
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/", formName, "ServerConstant.java"));
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
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".server.ejb;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"import javax.ejb.Remote;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".server.ejb.", formName, ";", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"@Remote", END_OF_LINE, 
					"public interface ", formName, "Remote extends ", formName, " {", END_OF_LINE, "}");

			Module module = getModuleWithFieldsById(formName).keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/ejb/", formName, "Remote.java"));
		}
	}

	/**
	 * Создание классов EJB локального интерфейса
	 */
	private void createLocalEjb() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".server.ejb;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"import javax.ejb.Local;", END_OF_LINE,
					"import com.technology.", packageModuleFormName, ".server.ejb.", formName, ";", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"@Local", END_OF_LINE, 
					"public interface ", formName, "Local extends ", formName, " {", END_OF_LINE, 
					"}"
				);

			Module module = getModuleWithFieldsById(formName).keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/ejb/", formName, "Local.java"));
		}
	}

	/**
	 * Создание классов реализаций EJB
	 */
	private void createEjbClass() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			String primaryKey = getPrimaryKeyById(jepApplicationDoc, formName);

			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			String dbPackage = module.getDbPackageName();

			String findResultSetMapper = new String();
			String findParameters = new String();
			String findParametersAsList = new String();
			String createParameters = new String();
			String createParametersAsList = new String();
			String updateParameters = new String();
			String updateParametersAsList = new String();
			String deleteParameters = new String();
			String deleteParametersAsList = new String();
			String getters = new String();

			List<ModuleField> moduleFields = hm.values().iterator().next();
			List<String> subtractFieldIds = new ArrayList<String>();
			List<ModuleField> optionFields = JepRiaToolkitUtil.getOptionField(moduleFields);

			boolean hasLOBField = false;
			boolean hasOptionField = false;
			boolean hasFileNameField = false;

			List<ModuleButton> buttons = module.getToolBarButtons();
			boolean hasViewDetailsWS = false;
			for (ModuleButton button : buttons) {
				if (VIEW_DETAILS_BUTTON_ID.equalsIgnoreCase(button.getButtonId())) {
					hasViewDetailsWS = true;
					break;
				}
			}
			JepTypeEnum keyType = JepTypeEnum.STRING;
			for (ModuleField moduleField : moduleFields) {
				if (moduleField.isPrimaryKey()) {
					keyType = JepRiaToolkitUtil.getFieldTypeAsEnum(moduleField.getFieldType());
				}
				if (FILE_NAME_FIELD_ID.equalsIgnoreCase(moduleField.getFieldId())) {
					hasFileNameField = true;
				}
			}

			String jepRiaResourceBundleImport = new String();

			for (ModuleField moduleField : moduleFields) {
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());
				boolean isOptionListField = 
						JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget()) ||
						JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget()) ||
						JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());
				if (isOptionField)
					hasOptionField = true;
				boolean isKeyOption = false;
				if (!isOptionField) {
					for (ModuleField optionField : optionFields) {
						if (moduleField.getFieldId().equalsIgnoreCase(JepRiaToolkitUtil.getDisplayValueForComboBox(optionField.getFieldId())))
							isKeyOption = moduleField.isListFormField()
									|| (!JepRiaToolkitUtil.isEmpty(moduleField.getVisibleWorkStates()) ? moduleField.getVisibleWorkStates().contains(
											WorkstateEnum.VIEW_DETAILS) : moduleField.isDetailFormField() && hasViewDetailsWS);
					}
				}

				if (moduleField.isListFormField()
						|| moduleField.isPrimaryKey()
						|| (!JepRiaToolkitUtil.isEmpty(moduleField.getVisibleWorkStates()) ? moduleField.getVisibleWorkStates().contains(
								WorkstateEnum.VIEW_DETAILS) : moduleField.isDetailFormField() && hasViewDetailsWS) || isKeyOption) {
					String fieldId = moduleField.getFieldId().toUpperCase();
					JepTypeEnum fieldType = JepRiaToolkitUtil.getFieldTypeAsEnum(moduleField.getFieldType());
					String resultSet = "null";
					switch (fieldType) {
					case STRING:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getString(", fieldId, ")");
						break;
					case INTEGER:
						resultSet = JepRiaToolkitUtil.multipleConcat("getInteger(rs, ", fieldId, ")");
						break;
					case FLOAT:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getFloat(", fieldId, ")");
						break;
					case DOUBLE:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getDouble(", fieldId, ")");
						break;
					case BIGDECIMAL:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getBigDecimal(", fieldId, ")");
						break;
					case BOOLEAN:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getBoolean(", fieldId, ")");
						break;
					case DATE:
					case DATE_TIME:
						resultSet = JepRiaToolkitUtil.multipleConcat("getDate(rs, ", fieldId, ")");
						break;
					case TIME:
						resultSet = JepRiaToolkitUtil.multipleConcat("getTimestamp(rs, ", fieldId, ")");
						break;
					case OPTION:
						String optionNameFieldId = fieldId.endsWith("CODE") ? fieldId.replace("CODE", "NAME") : fieldId.replace("ID", "NAME"); 
						resultSet = JepRiaToolkitUtil.multipleConcat("new JepOption(rs.getString(", optionNameFieldId, "), rs.getString(", fieldId, "))");
						break;
					case BINARY_FILE:
					case TEXT_FILE:
					case CLOB:
						hasLOBField = true;
						resultSet = JepRiaToolkitUtil.multipleConcat(
								"new JepFileReference(",
								(hasFileNameField ? JepRiaToolkitUtil.multipleConcat("rs.getString(", FILE_NAME_FIELD_ID, ")") : "null"),
								", ",
								(keyType.equals(JepTypeEnum.INTEGER) ? JepRiaToolkitUtil.multipleConcat("getInteger(rs, ", primaryKey, ")") : JepRiaToolkitUtil
										.multipleConcat("rs.getString(", primaryKey, ")")), ", rs.getString(", EXTENSION_FIELD_ID,
								"), rs.getString(", MIME_TYPE_FIELD_ID, "))");
						break;
					default:
						resultSet = JepRiaToolkitUtil.multipleConcat("rs.getString(", fieldId, ")");
						break;
					}
					findResultSetMapper += JepRiaToolkitUtil.multipleConcat("						record.set(", fieldId, ", ", resultSet, ");", END_OF_LINE);
				}
				
				String fieldIdAsParameter = null;
				if (moduleField.isFindParameter()) {
					fieldIdAsParameter = JepRiaToolkitUtil.getFieldIdAsParameter(moduleField.getFieldId(), module.getFindParameterPrefix());
					findParameters += JepRiaToolkitUtil.multipleConcat("				  	+ \"", (JepRiaToolkitUtil.isEmpty(findParameters) ? "" : ", "),
							fieldIdAsParameter, " => ? \" ", END_OF_LINE);
					findParametersAsList += JepRiaToolkitUtil.multipleConcat(
						"				, ", 
						(isOptionField ? "JepOption.<String>getValue(" : ""),
						(isOptionListField ? "JepOption.getOptionValuesAsString((List<JepOption>)" : ""),
						"templateRecord.get(",
						moduleField.getFieldId().toUpperCase(), ")", 
						(isOptionField || isOptionListField ? ")" : ""), END_OF_LINE);
				}
				
				if (moduleField.isCreateParameter()) {
					fieldIdAsParameter = JepRiaToolkitUtil.getFieldIdAsParameter(moduleField.getFieldId(), module.getCreateParameterPrefix());
					createParameters += JepRiaToolkitUtil.multipleConcat("				  	+ \"", (JepRiaToolkitUtil.isEmpty(createParameters) ? "" : ", "),
							fieldIdAsParameter, " => ? \" ", END_OF_LINE);
					createParametersAsList += JepRiaToolkitUtil.multipleConcat("				, ", 
						(isOptionField ? "JepOption.<String>getValue(" : ""), 
						(isOptionListField ? "JepOption.getOptionValuesAsString((List<JepOption>)" : ""),
						"record.get(", moduleField.getFieldId().toUpperCase(), ")", 
						(isOptionField || isOptionListField ? ")" : ""), END_OF_LINE);
				}
				
				if (moduleField.isUpdateParameter()) {
					fieldIdAsParameter = JepRiaToolkitUtil.getFieldIdAsParameter(moduleField.getFieldId(), module.getUpdateParameterPrefix());
					updateParameters += JepRiaToolkitUtil.multipleConcat("				  	+ \"", (JepRiaToolkitUtil.isEmpty(updateParameters) ? "" : ", "),
							fieldIdAsParameter, " => ? \" ", END_OF_LINE);
					updateParametersAsList += JepRiaToolkitUtil.multipleConcat("				, ", 
						(isOptionField ? "JepOption.<String>getValue(" : ""), 
						(isOptionListField ? "JepOption.getOptionValuesAsString((List<JepOption>)" : ""),
						"record.get(", moduleField.getFieldId().toUpperCase(), ")", 
						(isOptionField || isOptionListField ? ")" : ""), END_OF_LINE);
				}
				
				if (moduleField.isDeleteParameter()) {
					fieldIdAsParameter = JepRiaToolkitUtil.getFieldIdAsParameter(moduleField.getFieldId(), module.getDefaultParameterPrefix());
					deleteParameters += JepRiaToolkitUtil.multipleConcat("				  	+ \"", (JepRiaToolkitUtil.isEmpty(deleteParameters) ? "" : ", "),
							fieldIdAsParameter, " => ? \" ", END_OF_LINE);
					deleteParametersAsList += JepRiaToolkitUtil.multipleConcat("				, record.get(", moduleField.getFieldId().toUpperCase(), ") ",
							END_OF_LINE);
				}

				if (isOptionField || isOptionListField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
							JepRiaToolkitUtil.subtractFieldSuffix(moduleField.getFieldId()), null));
					subtractFieldIds.add(subtractFieldId);
					boolean isBooleanType = BOOLEAN.name().equalsIgnoreCase(moduleField.getFieldType());
					if (isBooleanType && JepRiaToolkitUtil.isEmpty(jepRiaResourceBundleImport)) {
						jepRiaResourceBundleImport = JepRiaToolkitUtil.multipleConcat(
								"import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;", END_OF_LINE,
								"import java.util.ResourceBundle;", END_OF_LINE, 
								"import java.util.ArrayList;", END_OF_LINE);
					}
					getters += JepRiaToolkitUtil.multipleConcat(
						WHITE_SPACE, END_OF_LINE,
						"	public List<JepOption> get", subtractFieldId, "() throws ApplicationException {", END_OF_LINE,
						isBooleanType ? JepRiaToolkitUtil.multipleConcat(
							"		ResourceBundle resource = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);", END_OF_LINE,
							"		List<JepOption> result = new ArrayList<JepOption>();", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE,
							"		JepOption option = new JepOption();", END_OF_LINE, 
							"		option.set(", subtractFieldId, "Options.", moduleField.getFieldId(), ", 0);", END_OF_LINE, 
							"		option.set(", subtractFieldId, "Options.", JepRiaToolkitUtil.getDisplayValueForComboBox(moduleField.getFieldId()), ", resource.getString(\"no\"));", END_OF_LINE,
							"		result.add(option);", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE, 
							"		option = new JepOption();", END_OF_LINE,
							"		option.set(", subtractFieldId, "Options.", moduleField.getFieldId(), ", 1);", END_OF_LINE, 
							"		option.set(", subtractFieldId, "Options.", JepRiaToolkitUtil.getDisplayValueForComboBox(moduleField.getFieldId()), ", resource.getString(\"yes\"));", END_OF_LINE, 
							"		result.add(option);", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE,
							"		return result;", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("		String sqlQuery = ", END_OF_LINE,
							"			\" begin \" ", END_OF_LINE, 
							"			+ \" ? := ", dbPackage, ".get", subtractFieldId, ";\" ", END_OF_LINE,
							"			+ \" end;\";", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE, 
							"		return super.getOptions(", END_OF_LINE,
							"				sqlQuery,", END_OF_LINE, 
							"				new ResultSetMapper<JepOption>() {", END_OF_LINE,
							"					public void map(ResultSet rs, JepOption dto) throws SQLException {", END_OF_LINE, 
							"						dto.setValue(", (JepTypeEnum.INTEGER.equals(JepRiaToolkitUtil.getFieldTypeAsEnum(moduleField.getFieldType())) ? 
									"getInteger(rs, "
									: "rs.getString("), subtractFieldId, "Options.", moduleField.getFieldId(), "));", END_OF_LINE,
							"						dto.setName(rs.getString(", subtractFieldId, "Options.", JepRiaToolkitUtil.getDisplayValueForComboBox(moduleField.getFieldId()), "));", END_OF_LINE, 
							"					}", END_OF_LINE,
							"				}", END_OF_LINE, 
							"		);", END_OF_LINE), 
							"	}", END_OF_LINE);
				}
			}

			String mainFormIfExist = isDependentForm(formName);
			if (!JepRiaToolkitUtil.isEmpty(mainFormIfExist)) {
				String dependencyPrimaryKey = getPrimaryKeyById(jepApplicationDoc, mainFormIfExist);
				dependencyPrimaryKey = JepRiaToolkitUtil.isEmpty(dependencyPrimaryKey) ? (JepRiaToolkitUtil.multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX))
						: dependencyPrimaryKey;

				// определим: имеется ли идентификатор первичного ключа уже в
				// списке записей модуля
				boolean hasField = false;
				for (ModuleField field : moduleFields) {
					if (field.getFieldId().equalsIgnoreCase(dependencyPrimaryKey)) {
						hasField = true;
						break;
					}
				}

				if (!hasField) {
					String fieldIdAsParameter = JepRiaToolkitUtil
							.getFieldIdAsParameter(dependencyPrimaryKey.toUpperCase(), module.getFindParameterPrefix());
					findParameters += JepRiaToolkitUtil.multipleConcat("				  	// + \"", (JepRiaToolkitUtil.isEmpty(findParameters) ? "" : ", "),
							fieldIdAsParameter, " => ? \" ", END_OF_LINE);
					findParametersAsList += JepRiaToolkitUtil.multipleConcat("				//, templateRecord.get(", dependencyPrimaryKey.toUpperCase(), ") ",
							END_OF_LINE);
				}
			}

			String content = JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageModuleFormName, ".server.ejb;", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.DATA_SOURCE_JNDI_NAME;", END_OF_LINE,
				"import static com.technology.", packageModuleFormName, ".server.", formName, "ServerConstant.RESOURCE_BUNDLE_NAME;", END_OF_LINE,
				"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE,
				"import javax.ejb.Local;", END_OF_LINE,
				"import javax.ejb.Remote;", END_OF_LINE,
				"import javax.ejb.Stateless;", END_OF_LINE,
				"import oracle.j2ee.ejb.StatelessDeployment;", END_OF_LINE,
				"import com.technology.jep.jepria.server.ejb.JepDataBean;", END_OF_LINE,
				"import com.technology.jep.jepria.shared.exceptions.ApplicationException;", END_OF_LINE,
				"import com.technology.jep.jepria.shared.record.JepRecord;", END_OF_LINE,
				"import com.technology.jep.jepria.shared.util.Mutable;", END_OF_LINE,
				"import com.technology.", packageModuleFormName, ".server.ejb.", formName, ";", END_OF_LINE,
				"import com.technology.jep.jepria.server.dao.ResultSetMapper;", END_OF_LINE,
				(hasLOBField ? JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.shared.record.lob.JepFileReference;", END_OF_LINE) : ""),
				(hasOptionField ? JepRiaToolkitUtil.multipleConcat("import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE) : ""), jepRiaResourceBundleImport);

			for (String subtractFieldId : subtractFieldIds)
				content += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.multipleConcat(
						"import com.technology.", packageModuleFormName, ".shared.field.", subtractFieldId, "Options;", END_OF_LINE));

			content += JepRiaToolkitUtil.multipleConcat(
				"import java.sql.ResultSet;", END_OF_LINE, 
				"import java.sql.SQLException;", END_OF_LINE,
				"import java.util.List;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"@Local( { ", formName, "Local.class })", END_OF_LINE,
				"@Remote( { ", formName, "Remote.class })", END_OF_LINE, 
				"@StatelessDeployment", END_OF_LINE, 
				"@Stateless", END_OF_LINE,
				"public class ", formName, "Bean extends JepDataBean implements ", formName, " {", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"	public ", formName, "Bean() {", END_OF_LINE, 
				"		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);", END_OF_LINE, 
				"	}", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE);

			content += JepRiaToolkitUtil.multipleConcat(
				"	public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {", END_OF_LINE,
				(!JepRiaToolkitUtil.isEmpty(findParameters) && !JepRiaToolkitUtil.isEmpty(findParametersAsList) ? JepRiaToolkitUtil.multipleConcat(
						"		String sqlQuery = ", END_OF_LINE, 
						"			\"begin  \" ", END_OF_LINE, 
						"			  +	\"? := ", dbPackage, ".find", formName, "(\" ", END_OF_LINE, 
						findParameters, "					+ \", maxRowCount => ? \" ", END_OF_LINE,
						"					+ \", operatorId => ? \" ", END_OF_LINE, 
						"			  + \");\"", END_OF_LINE, 
						"		 + \" end;\";", END_OF_LINE,
						"		return super.find(sqlQuery,", END_OF_LINE, 
						"				new ResultSetMapper<JepRecord>() {", END_OF_LINE,
						"					public void map(ResultSet rs, JepRecord record) throws SQLException {", END_OF_LINE, 
						findResultSetMapper,
						"					}", END_OF_LINE, 
						"				}", END_OF_LINE, 
						findParametersAsList, "				, maxRowCount ", END_OF_LINE,
						"				, operatorId);", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("		throw new UnsupportedOperationException();", END_OF_LINE)),
				"	}", END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {", END_OF_LINE,
				(!JepRiaToolkitUtil.isEmpty(deleteParameters) && !JepRiaToolkitUtil.isEmpty(deleteParametersAsList) ? JepRiaToolkitUtil.multipleConcat(
						"		String sqlQuery = ", END_OF_LINE,
						"			\"begin \" ", END_OF_LINE,
						"			  + \"", dbPackage, ".delete", formName, "(\" ", END_OF_LINE,
						deleteParameters,
						"					+ \", operatorId => ? \" ", END_OF_LINE,
						"			  + \");\"", END_OF_LINE,
						"		  + \"end;\";", END_OF_LINE,
						"		super.delete(sqlQuery ", END_OF_LINE,
						deleteParametersAsList, 
						"				, operatorId);", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("		throw new UnsupportedOperationException();", END_OF_LINE)),
				"	}", 
				END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"	public void update(JepRecord record, Integer operatorId) throws ApplicationException {", END_OF_LINE,
				(!JepRiaToolkitUtil.isEmpty(updateParameters) && !JepRiaToolkitUtil.isEmpty(updateParametersAsList) ? JepRiaToolkitUtil.multipleConcat(
						"		String sqlQuery = ", END_OF_LINE, 
						"			\"begin \" ", END_OF_LINE, 
						"			+	\"", dbPackage, ".update", formName, "(\" ", END_OF_LINE, 
						updateParameters, "					+ \", operatorId => ? \" ", END_OF_LINE, 
						"			+ \");\"", END_OF_LINE, 
						"		 + \"end;\";", END_OF_LINE, 
						"		super.update(sqlQuery ", END_OF_LINE, 
						updateParametersAsList,
						"				, operatorId);", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("		throw new UnsupportedOperationException();", END_OF_LINE)),
				"	}",
				END_OF_LINE,
				WHITE_SPACE, END_OF_LINE,
				"	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {", END_OF_LINE,
				(!JepRiaToolkitUtil.isEmpty(createParameters) && !JepRiaToolkitUtil.isEmpty(createParametersAsList) ? JepRiaToolkitUtil.multipleConcat(
						"		String sqlQuery = ", END_OF_LINE, 
						"			\"begin \" ", END_OF_LINE, 
						"			  + \"? := ", dbPackage, ".create", formName, "(\" ", END_OF_LINE, 
						createParameters, "					+ \", operatorId => ? \" ", END_OF_LINE, 
						"			  + \");\"", END_OF_LINE, 
						"			+ \"end;\";", END_OF_LINE, 
						"		return super.create(sqlQuery, ", END_OF_LINE,
						"				Integer.class ", END_OF_LINE, 
						createParametersAsList, 
						"				, operatorId);", END_OF_LINE) : JepRiaToolkitUtil.multipleConcat("		throw new UnsupportedOperationException();", END_OF_LINE)), 
						"	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						(JepRiaToolkitUtil.isEmpty(getters) ? "" : getters), 
						"}"
					);

			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/ejb/", formName, "Bean.java"));
		}
	}

	/**
	 * Создание классов EJB домашнего интерфейса
	 */
	private void createEjbInterface() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			List<ModuleField> moduleFields = hm.values().iterator().next();
			String getters = new String();
			for (ModuleField moduleField : moduleFields) {
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());
				if (isOptionField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
							JepRiaToolkitUtil.subtractFieldSuffix(moduleField.getFieldId()), null));
					getters += JepRiaToolkitUtil.multipleConcat("	List<JepOption> get", subtractFieldId, "() throws ApplicationException;", END_OF_LINE);
				}
			}

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".server.ejb;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(getters) ? "" : JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE,
							"import com.technology.jep.jepria.shared.exceptions.ApplicationException;", END_OF_LINE, 
							"import java.util.List;", END_OF_LINE)), 
					"import com.technology.jep.jepria.server.ejb.JepDataStandard;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"public interface ", formName, " extends JepDataStandard {", END_OF_LINE, 
					JepRiaToolkitUtil.isEmpty(getters) ? "" : getters, "}");

			Module module = hm.keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/server/ejb/", formName, ".java"));
		}
	}

	/**
	 * Создание классов, содержащих имена полей модулей
	 */
	private void createFieldNames() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			boolean hasLOBField = false, hasMimeType = false, hasExtension = false;

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".shared.field;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.shared.field.JepRecordFieldNames;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "FieldNames extends JepRecordFieldNames {", END_OF_LINE);
			String mainFormIfExist = isDependentForm(formName);
			if (!JepRiaToolkitUtil.isEmpty(mainFormIfExist)) {
				String mainFormParentKey = getPrimaryKeyById(jepApplicationDoc, mainFormIfExist);
				mainFormParentKey = JepRiaToolkitUtil.isEmpty(mainFormParentKey) ? JepRiaToolkitUtil.multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX)
						: mainFormParentKey;
				// TODO: обработать составной ключ!
				String[] mainFormParentKeyFields = mainFormParentKey.split(",");
				for (String mainFormParentKeyField : mainFormParentKeyFields) {
					content += JepRiaToolkitUtil.multipleConcat("	public static final String ", mainFormParentKeyField.trim().toUpperCase(), " = \"",
							mainFormParentKeyField.trim().toLowerCase(), "\";", END_OF_LINE);
				}
			}
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);

			List<ModuleField> fFields = hm.values().iterator().next();
			for (int j = 0; j < fFields.size(); j++) {
				ModuleField formFieldType = fFields.get(j);
				if (formFieldType.isLOB() && !hasLOBField)
					hasLOBField = true;
				if (MIME_TYPE_FIELD_ID.equalsIgnoreCase(formFieldType.getFieldId()) && !hasMimeType)
					hasMimeType = true;
				if (EXTENSION_FIELD_ID.equalsIgnoreCase(formFieldType.getFieldId()) && !hasExtension)
					hasExtension = true;

				String field = formFieldType.getFieldId().toUpperCase();
				String fieldDescription = JepRiaToolkitUtil.multipleConcat("	public static final String ", field, " = \"", field.toLowerCase(), "\";",
						END_OF_LINE);
				content += content.contains(fieldDescription) ? "" : fieldDescription;

				// create file structure
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(formFieldType.getFieldWidget())
						|| JEP_LIST_FIELD.equalsIgnoreCase(formFieldType.getFieldWidget())
						|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(formFieldType.getFieldWidget())
						|| JEP_TREE_FIELD.equalsIgnoreCase(formFieldType.getFieldWidget());
				if (isOptionField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(JepRiaToolkitUtil.subtractFieldSuffix(field), null));
					String fieldContent = JepRiaToolkitUtil.multipleConcat(
							"package com.technology.", packageModuleFormName, ".shared.field;", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE,
							"import com.technology.jep.jepria.shared.field.JepRecordFieldNames;", END_OF_LINE, 
							WHITE_SPACE, END_OF_LINE,
							"public class ", subtractFieldId, "Options extends JepRecordFieldNames {", END_OF_LINE, 
							"	public static final String ", field, " = \"", field.toLowerCase(), "\";", END_OF_LINE, 
							"	public static final String ", JepRiaToolkitUtil.getDisplayValueForComboBox(field), " = \"", JepRiaToolkitUtil.getDisplayValueForComboBox(field).toLowerCase(), "\";", END_OF_LINE, "}");

					Module module = hm.keySet().iterator().next();
					if (module.isNotRebuild())
						continue;
					JepRiaToolkitUtil.writeToFile(fieldContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
							"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/field/", subtractFieldId, "Options.java"));
				}
			}

			if (hasLOBField) {
				if (!hasMimeType) {
					content += JepRiaToolkitUtil.multipleConcat("	public static final String ", MIME_TYPE_FIELD_ID, " = \"", MIME_TYPE_FIELD_ID.toLowerCase(), "\";", END_OF_LINE);
				}
				if (!hasExtension) {
					content += JepRiaToolkitUtil.multipleConcat("	public static final String ", EXTENSION_FIELD_ID, " = \"", EXTENSION_FIELD_ID.toLowerCase(), "\";", END_OF_LINE);
				}
			}
			content += "}";

			Module module = hm.keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/field/", formName, "FieldNames.java"));
		}
	}

	/**
	 * Создание классов описания записей
	 */
	private void createRecordDefinition() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module currentModule = hm.keySet().iterator().next();
			List<ModuleField> fFields = hm.values().iterator().next();
			String lobField = new String();
			for (ModuleField moduleField : fFields) {
				if (moduleField.isLOB()) {
					lobField = moduleField.getFieldId();
					break;
				}
			}
			boolean hasLOBField = !JepRiaToolkitUtil.isEmpty(lobField);

			String primaryKey = getPrimaryKeyById(jepApplicationDoc, formName);

			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".shared.record;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"import static com.technology.", packageModuleFormName, ".shared.field.", formName, "FieldNames.*;", END_OF_LINE, 
					"import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;", END_OF_LINE,
					"import com.technology.jep.jepria.shared.field.JepTypeEnum;", END_OF_LINE);
			if (currentModule.hasLikeFields()) {
				content += JepRiaToolkitUtil.multipleConcat(
						"import static com.technology.jep.jepria.shared.field.JepLikeEnum.*;", END_OF_LINE,
						"import com.technology.jep.jepria.shared.field.JepLikeEnum;", END_OF_LINE);
			}
			content += JepRiaToolkitUtil.multipleConcat(
					"import com.technology.jep.jepria.shared.record", (hasLOBField ? ".lob" : ""), ".Jep", (hasLOBField ? "Lob" : ""), "RecordDefinition;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import java.util.HashMap;", END_OF_LINE,
					"import java.util.Map;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"public class ", formName, "RecordDefinition extends Jep", (hasLOBField ? "Lob" : ""), "RecordDefinition {", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	private static final long serialVersionUID = 1L;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	public static final ", formName, "RecordDefinition instance = new ", formName, "RecordDefinition();", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"	private ", formName, "RecordDefinition() {", END_OF_LINE,
					"		super(buildTypeMap()", END_OF_LINE,
					(JepRiaToolkitUtil.multipleConcat("			, new String[]{", 
						(JepRiaToolkitUtil.isEmpty(primaryKey) ? "" : primaryKey),
						"}", END_OF_LINE)),
					(hasLOBField ? JepRiaToolkitUtil.multipleConcat("			, \"", (JepRiaToolkitUtil.isEmpty(currentModule.getTable()) ? "table_name"
							: currentModule.getTable()), "\"", (JepRiaToolkitUtil.isEmpty(currentModule.getTable()) ? "//TODO: rename table!" : ""), "",
							END_OF_LINE, 
							"			, buildFieldMap()") : ""), (hasLOBField ? "" : "		"), ");", 
					END_OF_LINE);

			if (currentModule.hasLikeFields()) {
				content += JepRiaToolkitUtil.multipleConcat(
						"		super.setLikeMap(buildLikeMap());", END_OF_LINE);
			}
			content += JepRiaToolkitUtil.multipleConcat(
					"	}", END_OF_LINE, WHITE_SPACE, END_OF_LINE,
					"	private static Map<String, JepTypeEnum> buildTypeMap() {", END_OF_LINE,
					"		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();", END_OF_LINE);
			String likeContent = new String();
			boolean hasExtension = false;
			boolean hasMimeType = false;
			for (int j = 0; j < fFields.size(); j++) {
				ModuleField formFieldType = fFields.get(j);
				String field = formFieldType.getFieldId().toUpperCase();
				if (MIME_TYPE_FIELD_ID.equalsIgnoreCase(field) && !hasMimeType)
					hasMimeType = true;
				if (EXTENSION_FIELD_ID.equalsIgnoreCase(field) && !hasExtension)
					hasExtension = true;
				String fieldType = JepRiaToolkitUtil.isEmpty(formFieldType.getFieldType()) ? JepRiaToolkitUtil.getAppropriateFieldType(formFieldType
						.getFieldWidget()) : formFieldType.getFieldType().toUpperCase();
				String fieldLike = null;
				if (!JepRiaToolkitUtil.isEmpty(fieldLike = formFieldType.getFieldLike())) {
					likeContent += JepRiaToolkitUtil.multipleConcat("		likeMap.put(", field, ", ", fieldLike.toUpperCase(), ");", END_OF_LINE);
				}
				content += JepRiaToolkitUtil.multipleConcat("		typeMap.put(", field, ", ", fieldType, ");", END_OF_LINE);
			}

			if (hasLOBField) {
				if (!hasExtension) {
					content += JepRiaToolkitUtil.multipleConcat("		typeMap.put(", EXTENSION_FIELD_ID, ", ", STRING.name(), ");", END_OF_LINE);
				}
				if (!hasMimeType) {
					content += JepRiaToolkitUtil.multipleConcat("		typeMap.put(", MIME_TYPE_FIELD_ID, ", ", STRING.name(), ");", END_OF_LINE);
				}
			}

			content += JepRiaToolkitUtil.multipleConcat(
					"		return typeMap;", END_OF_LINE, 
					"	}", END_OF_LINE);
			if (currentModule.hasLikeFields()) {
				content += JepRiaToolkitUtil.multipleConcat(
						WHITE_SPACE, END_OF_LINE, 
						"	private static Map<String, JepLikeEnum> buildLikeMap() {", END_OF_LINE, 
						"		Map<String, JepLikeEnum> likeMap = new HashMap<String, JepLikeEnum>();", END_OF_LINE, 
						likeContent,
						"		return likeMap;", END_OF_LINE, 
						"	}", END_OF_LINE);
			}

			if (hasLOBField) {
				content += JepRiaToolkitUtil.multipleConcat(
						WHITE_SPACE, END_OF_LINE, 
						"	private static Map<String, String> buildFieldMap() {", END_OF_LINE,
						"		Map<String, String> fieldMap = new HashMap<String, String>();", END_OF_LINE, 
						(JepRiaToolkitUtil.isEmpty(primaryKey) ? "" :
							JepRiaToolkitUtil.multipleConcat(
								"		fieldMap.put(", primaryKey, ", ", primaryKey, ");", END_OF_LINE )), 
						
						"		fieldMap.put(", lobField, ", ", lobField, ");", END_OF_LINE, 
						"		return fieldMap;", END_OF_LINE, 
						"	}", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE);
			}

			content += "}";

			if (currentModule.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/record/", formName, "RecordDefinition.java"));
		}
	}

	/**
	 * Создание классов сервисов модуля
	 */
	private void createService() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			List<ModuleField> moduleFields = hm.values().iterator().next();
			String gettersForService = new String();
			String gettersForAsyncService = new String();
			for (ModuleField moduleField : moduleFields) {
				boolean isOptionField = JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(moduleField.getFieldWidget())
						|| JEP_TREE_FIELD.equalsIgnoreCase(moduleField.getFieldWidget());
				if (isOptionField) {
					String subtractFieldId = JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
							JepRiaToolkitUtil.subtractFieldSuffix(moduleField.getFieldId()), null));
					gettersForService += JepRiaToolkitUtil.multipleConcat(
							"	List<JepOption> get", subtractFieldId, "() throws ApplicationException;", END_OF_LINE);
					gettersForAsyncService += JepRiaToolkitUtil.multipleConcat(
							"	void get", subtractFieldId, "(AsyncCallback<List<JepOption>> callback);", END_OF_LINE);
				}
			}
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".shared.service;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					"import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;", END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(gettersForService) ? "" : JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE,
							"import com.technology.jep.jepria.shared.exceptions.ApplicationException;", END_OF_LINE, 
							"import java.util.List;", END_OF_LINE)), 
					"import com.technology.jep.jepria.shared.service.data.JepDataService;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"@RemoteServiceRelativePath(\"", formName, "Service\")", END_OF_LINE, 
					"public interface ", formName, "Service extends JepDataService {", END_OF_LINE, 
					JepRiaToolkitUtil.isEmpty(gettersForService) ? "" : gettersForService, "}");

			Module module = hm.keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/service/", formName, "Service.java"));

			content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".shared.service;", END_OF_LINE,
					WHITE_SPACE, END_OF_LINE,
					(JepRiaToolkitUtil.isEmpty(gettersForAsyncService) ? "" : JepRiaToolkitUtil.multipleConcat(
							"import com.technology.jep.jepria.shared.field.option.JepOption;", END_OF_LINE,
							"import com.google.gwt.user.client.rpc.AsyncCallback;", END_OF_LINE, "import java.util.List;", END_OF_LINE)),
					"import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"public interface ", formName, "ServiceAsync extends JepDataServiceAsync {", END_OF_LINE, JepRiaToolkitUtil
							.isEmpty(gettersForAsyncService) ? "" : gettersForAsyncService, "}");
			// выйдет из цикла раньше
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
					moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/service/", formName, "ServiceAsync.java"));
		}
	}

	/**
	 * Создание классов разделяемых констант (клиентско-серверных)
	 */
	private void createSharedConstant() {
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			String packageModuleFormName = JepRiaToolkitUtil.multipleConcat(packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase());
			String content = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageModuleFormName, ".shared;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.shared.JepRiaConstant;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, 
					"public class ", formName, "Constant extends JepRiaConstant  {", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE, "}");

			Module module = getModuleWithFieldsById(formName).keySet().iterator().next();
			if (module.isNotRebuild())
				continue;
			
			JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(
					PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/shared/", formName, "Constant.java"));
		}
		
		String mainContent = JepRiaToolkitUtil.multipleConcat(
				"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".main.shared;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE,
				"import com.technology.jep.jepria.shared.JepRiaConstant;", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"public class ", moduleName, "Constant extends JepRiaConstant  {", END_OF_LINE, 
				WHITE_SPACE, END_OF_LINE, 
				"}");

		JepRiaToolkitUtil.writeToFile(mainContent, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
				moduleName.toLowerCase(), "/main/shared/", moduleName, "Constant.java"));
		
		
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
								(JepRiaToolkitUtil.isEmpty(button.getImageDisabled()) || !isCustomToolBarButton ? "" : JepRiaToolkitUtil.multipleConcat("			",
										JepRiaToolkitUtil.initSmall(formName), "Images.", button.getImageDisabled(), "(),", END_OF_LINE)),
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
							"	ImageResource ", button.getImage(), "();", END_OF_LINE,
							(JepRiaToolkitUtil.isEmpty(button.getImageDisabled()) ? "" : JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, END_OF_LINE,
									"	@Resource(\"", button.getImageDisabled(), ".png\")", END_OF_LINE, 
									"	ImageResource ", button.getImageDisabled(), "();", END_OF_LINE)));

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
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			Map<Module, List<ModuleField>> hm = getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			boolean isStatusbarOff = module.isStatusbarOff();
			if (isStatusbarOff) {
				// create file structure
				JepRiaToolkitUtil.makeDir(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName, "/client/ui/statusbar"));
				// statusbar view
				String contentView = JepRiaToolkitUtil.multipleConcat(
						"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.statusbar;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"import com.technology.jep.jepria.client.ui.statusbar.StatusBarView;", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"public class ", formName, "StatusbarView extends StatusBarView {", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE, 
						"	public ", formName, "StatusbarView() {", END_OF_LINE, 
						"		super();", END_OF_LINE, 
						WHITE_SPACE, END_OF_LINE,
						"		asWidget().setVisible(false);", END_OF_LINE, 
						"	}", END_OF_LINE, "}");

				if (module.isNotRebuild())
					continue;
				JepRiaToolkitUtil.writeToFile(contentView, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(), "/",
						moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/statusbar/", formName, "StatusbarView.java"));

				// statusbar presenter
				String contentPresenter = JepRiaToolkitUtil.multipleConcat(
					"package com.technology.", packageName.toLowerCase(), ".", moduleName.toLowerCase(), ".", formName.toLowerCase(), ".client.ui.statusbar;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"import com.technology.jep.jepria.client.ui.statusbar.JepStatusBarPresenter;", END_OF_LINE, 
					WHITE_SPACE, END_OF_LINE,
					"public class ", formName, "StatusbarPresenter extends JepStatusBarPresenter {", END_OF_LINE, 
					"}");

				JepRiaToolkitUtil.writeToFile(contentPresenter, JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/", packageName.toLowerCase(),
					"/", moduleName.toLowerCase(), "/", formName.toLowerCase(), "/client/ui/statusbar/", formName, "StatusbarPresenter.java"));

			}
		}
	}

	/**
	 * Создание файла, хранящего настройки для log4j
	 */
	private void createLog4jProperties() {
		//debug
		String log4jDebugTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/config/debug/src/java/log4j.properties", UTF_8);
		log4jDebugTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(log4jDebugTemplateContent, packageName, moduleName);
		JepRiaToolkitUtil.writeToFile(log4jDebugTemplateContent, JepRiaToolkitUtil.multipleConcat("src/java/log4j.properties"), "UTF-8", false);
		JepRiaToolkitUtil.writeToFile(log4jDebugTemplateContent, JepRiaToolkitUtil.multipleConcat("config/", DEBUG_BUILD_CONFIG_NAME, "/src/java/log4j.properties"), "UTF-8", false);
		
		//release
		String log4jReleaseTemplateContent = JepRiaToolkitUtil.readFromJar("/templates/config/release/src/java/log4j.properties", UTF_8);
		log4jReleaseTemplateContent = JepRiaToolkitUtil.replacePackageModuleNames(log4jReleaseTemplateContent, packageName, moduleName);
		JepRiaToolkitUtil.writeToFile(log4jReleaseTemplateContent, JepRiaToolkitUtil.multipleConcat("config/", RELEASE_BUILD_CONFIG_NAME, "/src/java/log4j.properties"), "UTF-8", false);
	}

	/**
	 * Конвертация текстовых ресурсов
	 */
	private void encodeTextResources() {
		JepRiaToolkitUtil.runAntTarget("all-text-encode");
	}

	/**
	 * Перевод приложения (его настроек) в режим дебага
	 */
	private void convertApplicationInDebugMode() {
		JepRiaToolkitUtil.runAntTarget("build-config");
	}

	/**
	 * Функция, определяющая является ли форма зависимой. Если да, то от какой
	 * 
	 * @param formName
	 *            наименование проверяемой на зависимость формы
	 * @return наименование главной формы
	 */
	public String isDependentForm(String formName) {
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
	 * Функция определения является ли форма главной
	 * 
	 * @param formName
	 *            наименование проверяемой на главность формы
	 * @return признак главности
	 */
	private boolean isMainForm(String formName) {
		return getDependencyNodesIfExists(formName).size() > 0;
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
	 * @param doc
	 *            документ
	 * @return список всех описанных модулей
	 */
	private List<String> getAllModuleNodes(Document doc) {
		NodeList nodes = null;
		List<String> result = new ArrayList<String>();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME));
			nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (Exception e) {
			// e.printStackTrace();
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
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля, для которого имеется множество вложенных
	 *            подмодулей
	 * @return список модулей
	 */
	private List<String> getNodesWithChildren(Document doc, String moduleId) {
		NodeList nodes = null;
		List<String> result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", MODULE_TAG_NAME));
			nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (Exception e) {
			// e.printStackTrace();
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
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return элемент-модуль
	 */
	private Element getModuleNodeById(Document doc, String moduleId) {
		Element module = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']"));
			module = (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return module;
	}

	/**
	 * Получение списка элементов-полей для модуля
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return список элементов-полей
	 */
	private List<Element> getModuleFieldsById(Document doc, String moduleId) {
		NodeList fields = null;
		List<Element> result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record/field"));
			fields = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (Exception e) {
			// e.printStackTrace();
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
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return значение первичного ключа
	 */
	private String getPrimaryKeyById(Document doc, String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record[@", MODULE_PRIMARY_KEY_ATTRIBUTE, "]/@", MODULE_PRIMARY_KEY_ATTRIBUTE));
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim().toUpperCase() : null;
	}

	/**
	 * Получение наименования таблицы для модуля
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return наименование таблицы
	 */
	private String getTableById(Document doc, String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/record[@", MODULE_TABLE_NAME_ATTRIBUTE, "]/@", MODULE_TABLE_NAME_ATTRIBUTE));
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim().toLowerCase() : null;
	}

	/**
	 * Получение списка ролей GWT-модуля
	 * 
	 * @param doc
	 *            документ-файл настроек
	 * @param moduleId
	 *            идентификатор модуля
	 * @return список ролей
	 */
	private List<String> getModuleSecurityRoles(Document doc, String moduleId) {
		NodeList result = null;
		List<String> moduleRoles = new ArrayList<String>();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", MODULE_ROLES_ATTRIBUTE, "/", MODULE_ROLE_ATTRIBUTE));
			result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < result.getLength(); i++) {
				moduleRoles.add(((Node) result.item(i)).getTextContent().trim());
			}

		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return moduleRoles;
	}

	/**
	 * Получение источника данных для GWT-модуля
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return источник данных
	 */
	private String getDataSourceById(Document doc, String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", MODULE_DATASOURCE_ATTRIBUTE, "]/@", MODULE_DATASOURCE_ATTRIBUTE));
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim() : null;
	}

	/**
	 * Получение наименования пакета для GWT-модуля
	 * 
	 * @param doc
	 *            документ
	 * @param moduleId
	 *            идентификатор модуля
	 * @return наименование пакета
	 */
	private String getPackageById(Document doc, String moduleId) {
		String result = null;
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DB_PACKAGE_ATTRIBUTE, "]/@", DB_PACKAGE_ATTRIBUTE));
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return !JepRiaToolkitUtil.isEmpty(result) ? result.trim() : null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
	 * 
	 * @param doc
	 *            Документ
	 * @param module
	 *            Элемент-модуль
	 * @param moduleNode
	 *            DOM-элемент модуля
	 */
	private void detailizedModuleForToolBar(Document doc, Module module, Element moduleNode) {

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
			nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", TOOLBAR_TAG_NAME));
			toolbar = (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
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
									: new WorkstateEnum[] {}, node.getAttribute(BUTTON_IMAGE_ATTRIBUTE), node
									.getAttribute(BUTTON_IMAGE_DISABLED_ATTRIBUTE), node.getAttribute(BUTTON_EVENT_ATTRIBUTE), node
									.getAttribute(BUTTON_TEXT_ATTRIBUTE), node.getAttribute(BUTTON_NAME_ATTRIBUTE), node
									.getAttribute(BUTTON_NAME_EN_ATTRIBUTE)).setSeparator(!nodes.item(i).getNodeName()
									.equalsIgnoreCase(BUTTON_TAG_NAME)));
				}
			}
		}
	}

	/**
	 * Детализация элемента-поля GWT-модуля
	 * 
	 * @param doc
	 *            Документ
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleField(Document doc, Module module, ModuleField recordField) {
		try {
			detailizedModuleFieldAsDetailed(doc, module, recordField);
			detailizedModuleFieldAsListed(doc, module, recordField);
			detailizedModuleFieldAsParameters(doc, recordField);
			detailizedPrefixModuleFieldParameters(doc, module);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Является ли поле с указанным ID рекорд дефинишном
	 * 
	 * @param doc
	 *            Ссылка на JepApplication.xml
	 * @param moduleId
	 *            Идентификатор модуля
	 * @param fieldId
	 *            Идентификатор поля
	 * @return флаг, указывающий является ли поле рекорд дефинишном
	 */
	private boolean isRecordField(Document doc, String moduleId, String fieldId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/record/", FIELD_TAG_NAME, "[translate(normalize-space(@", FIELD_ID_ATTRIBUTE, "),'",
					ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			return !JepRiaToolkitUtil.isEmpty((Element) expr.evaluate(doc, XPathConstants.NODE));
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return false;
	}

	/**
	 * Получение ссылки на узел детальной формы по указанному идентификатору
	 * модуля
	 * 
	 * @param doc
	 *            Ссылка на JepApplication.xml
	 * @param moduleId
	 *            Идентификатор модуля
	 * @return XML-узел в JepApplication.xml
	 */
	private Element getFormDetailElementByModuleId(Document doc, String moduleId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", DETAIL_FORM_TAG_NAME));
			return (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как представителя списочной формы
	 * 
	 * @param doc
	 *            Документ
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsDetailed(Document doc, Module module, ModuleField recordField) {
		Element field = null;
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		Element formDetailElement = getFormDetailElementByModuleId(doc, moduleId);
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", DETAIL_FORM_TAG_NAME, "/", FIELD_TAG_NAME, "[translate(normalize-space(@",
					FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			field = (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
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
				recordField.setEditable(false);
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
					} else if (isRecordField(doc, moduleId, nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
						counter++;
					}
				}
			}
		}
	}

	/**
	 * Получение ссылки на узел списочной формы по указанному идентификатору
	 * модуля
	 * 
	 * @param doc
	 *            Ссылка на JepApplication.xml
	 * @param moduleId
	 *            Идентификатор модуля
	 * @return XML-узел в JepApplication.xml
	 */
	private Element getFormListElementByModuleId(Document doc, String moduleId) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", LIST_FORM_TAG_NAME));
			return (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return null;
	}

	/**
	 * Детализация элемента-поля GWT-модуля как представителя детальной формы
	 * 
	 * @param doc
	 *            Документ
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsListed(Document doc, Module module, ModuleField recordField) {
		Element field = null;
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		Element formList = getFormListElementByModuleId(doc, moduleId);
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']", "/", FORMS_TAG_NAME, "/", LIST_FORM_TAG_NAME, "/", FIELD_TAG_NAME, "[translate(normalize-space(@",
					FIELD_ID_ATTRIBUTE, "),'", ALPHABET_LOWER_CASE, "','", ALPHABET_UPPER_CASE, "') = '", fieldId.toUpperCase(), "']"));
			field = (Element) expr.evaluate(doc, XPathConstants.NODE);
		} catch (Exception e) {
			// e.printStackTrace();
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
				} else if (isRecordField(doc, moduleId, nodeEl.getAttribute(FIELD_ID_ATTRIBUTE))) {
					counter++;
				}
			}
		}
	}

	/**
	 * Детализация элемента-поля GWT-модуля как параметра для EJB-методов
	 * 
	 * @param doc
	 *            Документ
	 * @param recordField
	 *            Элемент-поле
	 */
	private void detailizedModuleFieldAsParameters(Document doc, ModuleField recordField) {
		String fieldId = recordField.getFieldId();
		String moduleId = recordField.getModuleId();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil
					.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId, "']", "/",
							DATABASE_TAG_NAME, "/find[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String findParameters = (String) expr.evaluate(doc, XPathConstants.STRING);
			List<String> parameters = Arrays.asList(findParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));

			recordField.setFindParameter(!JepRiaToolkitUtil.isEmpty(findParameters) && parameters.contains(fieldId));

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", DATABASE_TAG_NAME, "/create[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String createParameters = (String) expr.evaluate(doc, XPathConstants.STRING);
			parameters = Arrays.asList(createParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
			recordField.setCreateParameter(!JepRiaToolkitUtil.isEmpty(createParameters) && parameters.contains(fieldId));

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']", "/", DATABASE_TAG_NAME, "/update[@", DATABASE_PARAMETERS_TAG_NAME, "]", "/@", DATABASE_PARAMETERS_TAG_NAME));
			String updateParameters = (String) expr.evaluate(doc, XPathConstants.STRING);
			parameters = Arrays.asList(updateParameters.replaceAll(WHITE_SPACE, "").toUpperCase().split(SEPARATOR));
			recordField.setUpdateParameter(!JepRiaToolkitUtil.isEmpty(updateParameters) && parameters.contains(fieldId));

			recordField.setDeleteParameter(recordField.isPrimaryKey());
		} catch (Exception e) {
			// e.printStackTrace();
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
	}

	/**
	 * 
	 * @param doc
	 * @param recordField
	 */
	private void detailizedPrefixModuleFieldParameters(Document doc, Module module) {
		String defaultPrefix = null;
		String moduleId = module.getModuleId();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE,
					") = '", moduleId, "']/", DATABASE_TAG_NAME, "[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			defaultPrefix = (String) expr.evaluate(doc, XPathConstants.STRING);
			defaultPrefix = !JepRiaToolkitUtil.isEmpty(defaultPrefix) ? defaultPrefix.trim() : null;
			module.setDefaultParameterPrefix(defaultPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/create[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String createParameterPrefix = (String) expr.evaluate(doc, XPathConstants.STRING);
			createParameterPrefix = !JepRiaToolkitUtil.isEmpty(createParameterPrefix) ? createParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(createParameterPrefix))
				module.setCreateParameterPrefix(createParameterPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/update[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String updateParameterPrefix = (String) expr.evaluate(doc, XPathConstants.STRING);
			updateParameterPrefix = !JepRiaToolkitUtil.isEmpty(updateParameterPrefix) ? updateParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(updateParameterPrefix))
				module.setUpdateParameterPrefix(updateParameterPrefix);

			xpath.reset();
			expr = xpath.compile(JepRiaToolkitUtil.multipleConcat("//", MODULE_TAG_NAME, "[normalize-space(@", MODULE_ID_ATTRIBUTE, ") = '", moduleId,
					"']/", DATABASE_TAG_NAME, "/find[@", DATABASE_PREFIX_ATTRIBUTE_NAME, "]/@", DATABASE_PREFIX_ATTRIBUTE_NAME));
			String findParameterPrefix = (String) expr.evaluate(doc, XPathConstants.STRING);
			findParameterPrefix = !JepRiaToolkitUtil.isEmpty(findParameterPrefix) ? findParameterPrefix.trim() : defaultPrefix;
			if (!JepRiaToolkitUtil.isEmpty(findParameterPrefix))
				module.setFindParameterPrefix(findParameterPrefix);
		} catch (Exception e) {
			// e.printStackTrace();
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
		Element formDetailElement = getFormDetailElementByModuleId(jepApplicationDoc, moduleId);
		Element formListElement = getFormListElementByModuleId(jepApplicationDoc, moduleId);

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
	public void setRiaVersion(String riaVersion) {
		this.riaVersion = riaVersion;
	}

	public void setCommonHome(String commonHome) {
		this.commonHome = commonHome;
	}
	
	/**
	 * Настройка видимости сообщения о тестовой сборке 
	 */
	public void switchTestBuildMessage(String moduleName, boolean isProductionBuild) {
		/*
		String cssTemplateContent = "";

		try {
			cssTemplateContent = JepRiaToolkitUtil.readFromFile(JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), "UTF-8");
		} catch (FileNotFoundException e) {
			JepRiaToolkitUtil.echoMessage("[ERROR] " + e.getLocalizedMessage());
		}
		
		if (isProductionBuild) {
			if (cssTemplateContent.contains(TEST_BUILD_MESSAGE_STYLE_VISIBLE)) 
				cssTemplateContent = cssTemplateContent.replace(TEST_BUILD_MESSAGE_STYLE_VISIBLE, TEST_BUILD_MESSAGE_STYLE_HIDDEN);
			else
				if (!cssTemplateContent.contains(TEST_BUILD_MESSAGE_STYLE_HIDDEN)) 
					cssTemplateContent += END_OF_LINE + TEST_BUILD_MESSAGE_STYLE_HIDDEN;
		} else {
			if (cssTemplateContent.contains(TEST_BUILD_MESSAGE_STYLE_HIDDEN)) 
				cssTemplateContent = cssTemplateContent.replace(TEST_BUILD_MESSAGE_STYLE_HIDDEN, TEST_BUILD_MESSAGE_STYLE_VISIBLE);
			else
				if (!cssTemplateContent.contains(TEST_BUILD_MESSAGE_STYLE_VISIBLE)) 
					cssTemplateContent += END_OF_LINE + TEST_BUILD_MESSAGE_STYLE_VISIBLE;
		}
		
		JepRiaToolkitUtil.writeToFile(cssTemplateContent, JepRiaToolkitUtil.multipleConcat(WELCOME_PAGE_DIR_NAME, "/", moduleName, ".css"), "UTF-8");
		*/
	}

}