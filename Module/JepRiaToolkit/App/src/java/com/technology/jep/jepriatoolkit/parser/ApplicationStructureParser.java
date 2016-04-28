package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_XML_PATH_TEMPLATE_PROPERTY;
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
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_FIELD_WIDTH;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PKG_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_SOURCE_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclaration;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclarationSuppressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPathSupressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.copyFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractStringFromQuotes;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getResourceByPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.initSmall;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.readFromFile;
import static java.text.MessageFormat.format;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.Statement;
import com.technology.jep.jepriatoolkit.ApplicationDefinition;
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
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.creator.module.Record;
import com.technology.jep.jepriatoolkit.log.Logger;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureParser extends Task {
	
	private static final String OUTPUT_LOG_FILE = "createStucture.log";
	private static final String JDBC_JNDI_PREFIX = "jdbc/";
	private static final String UPDATE_DAO_FUNCTION = "update";
	private static final String CREATE_DAO_FUNCTION = "create";
	private static final String FIND_DAO_FUNCTION = "find";
	private static final String DATA_SOURCE_JNDI_NAME_CONSTANT_NAME = "DATA_SOURCE_JNDI_NAME";
	private static final String JEP_APPLICATION_XML = "JepApplication.xml";
	private static final String YES = "y";
	private static final String NO = "n";
	private static final String XML_EXTENSION = ".xml";

	private String jepRiaVersion;
	
	@Override
	public void execute() throws BuildException {
		
		ResourceBundle resource = ApplicationDefinition.LAST.getResource();
		
		if (!isEmptyOrNotInitializedParameter(jepRiaVersion)){
			Pattern p = Pattern.compile("Tag/(\\d+)\\.(\\d+)\\.(\\d+)");
			Matcher m = p.matcher(jepRiaVersion);
			if (m.find()){
				resource = ApplicationDefinition.valueOf(multipleConcat("JEPRIA_", m.group(1))).getResource();
			}
			else {
				log(multipleConcat(ERROR_PREFIX, "The Version is '", jepRiaVersion, "' isn't supported!"), null);
				return;
			}
		}
		// Obtain info about application from configuration file application.xml
		String relativeApplicationXmlPath = getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml", resource);
		String applicationName = getApplicationName(convertPatternInRealPath(relativeApplicationXmlPath));
		String fileName = getApplicationFileName(applicationName);
		if (JepRiaToolkitUtil.isEmpty(fileName)) return;
		
		echoMessage(multipleConcat("Generate ", normalizePath(fileName), "..."));
		
		String applicationDataSource = DEFAULT_DATASOURCE;
		Application structure = new Application();
		structure.setName(applicationName);
		structure.setDefaultDatasource(applicationDataSource);
		structure.setProjectPackage(extractFileNamesByPattern(relativeApplicationXmlPath).iterator().next());
		// The information about modules will be given from main.gwt.xml
		String mainGwtXmlPropertyPath = convertPatternInRealPath(getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
			multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{2}.gwt.xml"), resource
		));
		List<String> moduleNames = getModuleNames(mainGwtXmlPropertyPath);
		structure.setModuleIds(moduleNames);
		List<Module> modules = new ArrayList<Module>(moduleNames.size());
		
		String mainModuleResourcePath = convertPatternInRealPath(getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_Source.properties"), resource));
		
		String mainModuleResourceEnPath = convertPatternInRealPath(getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_en.properties"), resource));
		
		ResourceBundle mainModuleResourceBundle = getResourceByPath(mainModuleResourcePath);
		ResourceBundle mainModuleResourceBundleEn = getResourceByPath(mainModuleResourceEnPath);
		
		for(String moduleId : moduleNames){
			String clientModuleDaoPath = convertPatternInRealPath(
					replacePathWithModuleId(
						getDefinitionProperty(CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}Dao.java"), resource
						), moduleId
					));
			String packageName = null;
			String moduleDataSource = "";
			StringBuilder findParameters = new StringBuilder();
			StringBuilder createParameters = new StringBuilder();
			StringBuilder updateParameters = new StringBuilder();
			ModuleDeclaration declaration;
			try {
				declaration = getModuleDeclaration(clientModuleDaoPath);
			}
			catch(FileNotFoundException e){
				log(multipleConcat(ERROR_PREFIX, "File '", clientModuleDaoPath, "' is not found!"), moduleId);
				continue;
			}
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
			
			String clientModuleServerConstantPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/{3}ServerConstant.java"), resource)
					, moduleId));
			try {
				declaration = getModuleDeclaration(clientModuleServerConstantPath);
			}
			catch(FileNotFoundException e){
				log(multipleConcat(ERROR_PREFIX, "File '", clientModuleServerConstantPath, "' is not found!"), moduleId);
				continue;
			}
			for (FieldDeclaration field : declaration.getFields()){
				VariableDeclarator var = field.getVariables().iterator().next();
				String fieldName = var.getId().getName();
				if (DATA_SOURCE_JNDI_NAME_CONSTANT_NAME.equalsIgnoreCase(fieldName)){
					moduleDataSource = extractStringFromQuotes(var.getInit().toStringWithoutComments()).split(JDBC_JNDI_PREFIX)[1];
					break;
				}
			}
			
			Db db = new Db();
			if (!JepRiaToolkitUtil.isEmpty(packageName) && !packageName.equalsIgnoreCase(multipleConcat(PKG_PREFIX, applicationName))){
				db.setPackageName(packageName);
			}
			if (!JepRiaToolkitUtil.isEmpty(moduleDataSource) && !applicationDataSource.equalsIgnoreCase(moduleDataSource)){
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
			Module mod = new Module(moduleId, moduleText, moduleTextEn, db, Arrays.asList(getRoles(moduleId, resource).split("\\s*,\\s*")));
			String clientModuleFieldNamesPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}FieldNames.java"), resource), moduleId
					));
			try {
				declaration = getModuleDeclaration(clientModuleFieldNamesPath);
			}
			catch(FileNotFoundException e){
				log(multipleConcat(ERROR_PREFIX, "File '", clientModuleFieldNamesPath, "' is not found!"), moduleId);
				continue;
			}
			List<ModuleField> recordFields = new ArrayList<ModuleField>();
			List<ModuleField> detailFormFields = new ArrayList<ModuleField>();
			List<ModuleField> listFormFields = new ArrayList<ModuleField>();
			if (declaration != null) {
				for (FieldDeclaration fieldDeclaration : declaration.getFields()){
					String fieldId = extractStringFromQuotes(fieldDeclaration.getVariables().iterator().next().getInit().toStringWithoutComments()).toUpperCase();
					ModuleField field = new ModuleField(moduleId, fieldId);
					prepareDetailFormModuleField(moduleId, field, resource);
					prepareListFormModuleField(moduleId, field, resource);
					recordFields.add(new ModuleField(field, FieldType.RECORD));
					if (field.getIsDetailFormField())
						detailFormFields.add(new ModuleField(field, FieldType.FORM_DETAIL));
					if (field.getIsListFormField())
						listFormFields.add(new ModuleField(field, FieldType.FORM_LIST));
				}
			}
			Record rec = new Record(recordFields);
			try {
				rec.setPrimaryKeyAndTableName(getPrimaryKeyAndTableName(moduleId, resource));
			}
			catch(FileNotFoundException e){
				log(multipleConcat(ERROR_PREFIX, "File '", getClientModuleRecordDefinitionPath(moduleId, resource), "' is not found!"), moduleId);
				continue;		
			}
			mod.setRecord(rec);
			
			String clientModuleDetailPresenterPath = convertPatternInRealPathSupressException(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormPresenter.java"), resource), moduleId
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
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarViewImpl.java"), resource), moduleId
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
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarPresenter.java"), resource), moduleId
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
			modules.add(mod);
		}
		structure.setModules(new Modules(modules));
		
		if (new File(fileName).exists()){
			try {
				ApplicationSettingParser applicationParser = ApplicationSettingParser.getInstance(normalizePath(fileName));
				Application application = applicationParser.getApplication();
				application.uptodate(structure);
				structure = application;
			} catch (ParserConfigurationException e) {
				log(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()), null);
			}
		}
		convertToXml(structure, fileName);
		Logger.printMessages(OUTPUT_LOG_FILE);
	}

	public String getApplicationFileName(String applicationName) {
		String fileName = multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, applicationName, APPLICATION_SETTING_FILE_ENDING);
		
		if (new File(fileName).exists()){
			echoMessage(multipleConcat(WARNING_PREFIX, "The file '", normalizePath(fileName), "' has already existed!\n", 
					WARNING_PREFIX, "Do you want to replace this file (", YES, " / ", NO, ") or just choose new file name to save the application structure? Please type your answer: "));
			Scanner scanner = new Scanner(System.in);
			try {
				while (scanner.hasNext()){
					String line = scanner.next();
					if (NO.equalsIgnoreCase(line)){
						return null;
					}
					else {
						if (YES.equalsIgnoreCase(line)){
							break;
						}
						else {
							fileName = multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, line);
							if (!fileName.endsWith(XML_EXTENSION)){
								fileName = multipleConcat(fileName, XML_EXTENSION);
							}
							break;
						}
					}
				}
			}
			finally {
				scanner.close();	
			}
		}
		else if (new File(multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, JEP_APPLICATION_XML)).exists()){
			try {
				log(multipleConcat("Content from '", JEP_APPLICATION_XML, "' was copied to file '", fileName, "'"), null);
				copyFile(JEP_APPLICATION_XML, fileName);
			}
			catch(IOException e){
				log(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()), null);
				return null;
			}
		}
		return fileName;
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
		
		String clientModuleResourcePath = convertPatternInRealPath(
					replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties"), resource
					), moduleId));
		
		String clientModuleResourceEnPath = convertPatternInRealPath(
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
		String clientModuleResourcePath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties"), resource
				), moduleId));
		
		String clientModuleResourceEnPath = convertPatternInRealPath(
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
	
	private static ModuleDeclaration getClientModuleRecordDefinition(String moduleId, ResourceBundle resource) throws FileNotFoundException {
		ModuleDeclaration clientModuleDeclaration = getModuleDeclaration(getClientModuleRecordDefinitionPath(moduleId, resource));
		return clientModuleDeclaration;
	}

	public static String getClientModuleRecordDefinitionPath(String moduleId, ResourceBundle resource) {
		return convertPatternInRealPath(
				replacePathWithModuleId(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/record/{3}RecordDefinition.java"), resource
					),
				moduleId));
	}
	
	private static String replacePathWithModuleId(String path, String moduleId){
		return format(path, "{0}", "{1}", moduleId.toLowerCase(), "{2}");
	}
	
	public String getRoles(String moduleId, ResourceBundle resource){
		String mainModulePresenterFilePath = convertPatternInRealPath(getDefinitionProperty(MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
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

	public void setJepRiaVersion(String jepRiaVersion) {
		this.jepRiaVersion = jepRiaVersion;
	}
	
	private void log(String message, String moduleId){
		echoMessage(message);
		Logger.appendMessageToTheEndOfForm(moduleId, message);
	}
}
