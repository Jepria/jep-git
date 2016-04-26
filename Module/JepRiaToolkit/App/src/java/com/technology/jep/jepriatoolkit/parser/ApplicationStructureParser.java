package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclaration;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
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
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.Statement;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleButton;
import com.technology.jep.jepriatoolkit.creator.module.ModuleDeclaration;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.creator.module.Record;
import com.technology.jep.jepriatoolkit.creator.module.ToolBar;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureParser extends Task implements JepRiaToolkitConstant {
	
	private static final String JDBC_JNDI_PREFIX = "jdbc/";
	private static final String UPDATE_DAO_FUNCTION = "update";
	private static final String CREATE_DAO_FUNCTION = "create";
	private static final String FIND_DAO_FUNCTION = "find";
	private static final String DATA_SOURCE_JNDI_NAME_CONSTANT_NAME = "DATA_SOURCE_JNDI_NAME";
	private static final String JEP_APPLICATION_XML = "JepApplication.xml";
	private static final String YES = "y";
	private static final String NO = "n";
	private static final String XML_EXTENSION = ".xml";

	@Override
	public void execute() throws BuildException {
		// Obtain info about application from configuration file application.xml
		String relativeApplicationXmlPath = getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml");
		List<String> names = extractFileNamesByPattern(relativeApplicationXmlPath);
		String projectPackage = names.get(0);
		String applicationXmlPath = convertPatternInRealPath(relativeApplicationXmlPath);
		String applicationName = getApplicationName(applicationXmlPath);
		String fileName = multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, applicationName, APPLICATION_SETTING_FILE_ENDING);
		if (new File(fileName).exists()){
			echoMessage(multipleConcat(WARNING_PREFIX, "The file '", normalizePath(fileName), "' has already existed!\n", 
					WARNING_PREFIX, "Do you want to replace this file (", YES, " / ", NO, ") or just choose new file name to save the application structure? Please type your answer: "));
			Scanner scanner = new Scanner(System.in);
			try {
				while (scanner.hasNext()){
					String line = scanner.next();
					if (NO.equalsIgnoreCase(line)){
						return;
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
				echoMessage(multipleConcat("Content from '", JEP_APPLICATION_XML, "' was copied to file '", fileName, "'"));
				copyFile(JEP_APPLICATION_XML, fileName);
			}
			catch(IOException e){
				echoMessage(e.getLocalizedMessage());
				return;
			}
		}
		
		echoMessage(multipleConcat("Generate ", normalizePath(fileName), "..."));
		
		String applicationDataSource = DEFAULT_DATASOURCE;
		Application structure = new Application();
		structure.setName(applicationName);
		structure.setDefaultDatasource(applicationDataSource);
		structure.setProjectPackage(projectPackage);
		// The information about modules will be given from main.gwt.xml
		String mainGwtXmlPropertyPath = convertPatternInRealPath(getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
			multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{2}.gwt.xml")
		));
		List<String> moduleNames = getModuleNames(mainGwtXmlPropertyPath);
		List<Module> modules = new ArrayList<Module>(moduleNames.size());
		
		String mainModuleResourcePath = convertPatternInRealPath(getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_Source.properties")));
		
		String mainModuleResourceEnPath = convertPatternInRealPath(getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_en.properties")));
		
		ResourceBundle mainModuleResourceBundle = getResourceByPath(mainModuleResourcePath);
		ResourceBundle mainModuleResourceBundleEn = getResourceByPath(mainModuleResourceEnPath);
		
		for(String moduleId : moduleNames){
			String clientModuleDaoPath = convertPatternInRealPath(
					replacePathWithModuleId(
						getDefinitionProperty(CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY, 
								multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}Dao.java")
						), moduleId
					));
			String packageName = null;
			String moduleDataSource = "";
			StringBuilder findParameters = new StringBuilder();
			StringBuilder createParameters = new StringBuilder();
			StringBuilder updateParameters = new StringBuilder();
			ModuleDeclaration declaration = getModuleDeclaration(clientModuleDaoPath);
			for (MethodDeclaration method : declaration.getMethods()){
				String methodName = method.getName();
				if (FIND_DAO_FUNCTION.equalsIgnoreCase(methodName) || 
						CREATE_DAO_FUNCTION.equalsIgnoreCase(methodName) ||
							UPDATE_DAO_FUNCTION.equalsIgnoreCase(methodName)){
					
					Pattern p = Pattern.compile("get\\((.*?)\\)");
					String methodBody = method.getBody().toString();
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
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/{3}ServerConstant.java"))
					, moduleId));
			declaration = getModuleDeclaration(clientModuleServerConstantPath);
			for (FieldDeclaration field : declaration.getFields()){
				VariableDeclarator var = field.getVariables().iterator().next();
				String fieldName = var.getId().getName();
				if (DATA_SOURCE_JNDI_NAME_CONSTANT_NAME.equalsIgnoreCase(fieldName)){
					moduleDataSource = extractStringFromQuotes(var.getInit().toString()).split(JDBC_JNDI_PREFIX)[1];
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
			Module mod = new Module(moduleId, mainModuleResourceBundle.getString(submoduleResourceKey), mainModuleResourceBundleEn.getString(submoduleResourceKey), db, Arrays.asList(getRoles(moduleId).split("\\s*,\\s*")));
			Record rec = new Record(getPrimaryKeyAndTableName(moduleId));
			
			String clientModuleFieldNamesPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/field/{3}FieldNames.java")), moduleId
					));
			ModuleDeclaration module = getModuleDeclaration(clientModuleFieldNamesPath);
			List<ModuleField> recordFields = new ArrayList<ModuleField>();
			List<ModuleField> detailFormFields = new ArrayList<ModuleField>();
			List<ModuleField> listFormFields = new ArrayList<ModuleField>();
			for (FieldDeclaration fieldDeclaration : module.getFields()){
				String fieldId = extractStringFromQuotes(fieldDeclaration.getVariables().iterator().next().getInit().toString()).toUpperCase();
				ModuleField field = new ModuleField(moduleId, fieldId, JepTypeEnum.STRING.name());
				recordFields.add(field);
				ModuleField detailFormField = prepareDetailFormModuleField(moduleId, field);
				if (detailFormField != null){
					field.setFieldType(detailFormField.getFieldType());
					detailFormField.setFieldType(null);
					detailFormFields.add(detailFormField);
				}
				ModuleField listFormField = prepareListFormModuleField(moduleId, field);
				if (listFormField != null) {
					listFormField.setFieldType(null);
					listFormFields.add(listFormField);
				}
			}
			rec.setFields(recordFields);
			mod.setRecord(rec);
			
			String clientModuleDetailPresenterPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormPresenter.java")), moduleId
					));
			ModuleDeclaration clientModuleDetailPresenter = getModuleDeclaration(clientModuleDetailPresenterPath);
			String presenterBody = multipleConcat(Arrays.toString(clientModuleDetailPresenter.getFields().toArray()),
					END_OF_LINE, Arrays.toString(clientModuleDetailPresenter.getMethods().toArray()));
			DetailForm detailForm = new DetailForm(detailFormFields);
			detailForm.setPresenterBody(presenterBody);
			
			ListForm listForm = new ListForm(listFormFields);
			mod.setForms(detailForm, listForm);
			
			String clientModuleToolBarViewPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarViewImpl.java")), moduleId
					));
			ModuleDeclaration clientModuleToolBarView = getModuleDeclaration(clientModuleToolBarViewPath);
			ToolBar toolBar = new ToolBar();
			ConstructorDeclaration constructor = clientModuleToolBarView.getConstructors().iterator().next();
			List<ModuleButton> buttons = new ArrayList<ModuleButton>();
			for (Statement st : constructor.getBlock().getStmts()){
				String line = st.toString();
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
			
			String clientModuleToolBarPresenterPath = convertPatternInRealPath(
					replacePathWithModuleId(
							getDefinitionProperty(CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY, 
									multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/client/ui/toolbar/{3}ToolBarPresenter.java")), moduleId
					));
			ModuleDeclaration clientModuleToolBarPresenter = getModuleDeclaration(clientModuleToolBarPresenterPath);
			for (MethodDeclaration md : clientModuleToolBarPresenter.getMethods()){
				if ("bind".equalsIgnoreCase(md.getName())){
					for (ModuleButton button : buttons){
						if (button.getIsCustomButton()) {
							Pattern p = Pattern.compile(multipleConcat("bindButton\\s*\\(\\s*", button.getButtonId(), "\\s*,\\s*new\\s*WorkstateEnum\\s*\\[\\s*\\]\\s*\\{(.*?)\\}\\s*,\\s*new\\s*ClickHandler\\s*\\(\\s*\\)\\s*\\{\\s*public\\s*void\\s*onClick\\s*\\(\\s*ClickEvent\\s*event\\s*\\)\\s*\\{\\s*(.*?)\\s*\\}\\s*\\}\\s*\\)"));
							Matcher m = p.matcher(md.toString());
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
			
			toolBar.setButtons(buttons);
			mod.setToolbar(toolBar);
			
			modules.add(mod);
		}
		
		structure.setModules(new Modules(modules));
		
		convertToXml(structure, fileName);
	}

	public String[] getPrimaryKeyAndTableName(String moduleId) {
		ModuleDeclaration clientModuleDeclaration = getClientModuleRecordDefinition(moduleId);
		String superClassInConstructorBody = clientModuleDeclaration.getConstructors().iterator().next().getBlock().getStmts().iterator().next().toString();
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
	
	public static ModuleField prepareDetailFormModuleField(String moduleId, ModuleField field){
		String clientModuleDetailFormViewImplPath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "src/java/com/technology/{0}/{1}/{2}/client/ui/form/detail/{3}DetailFormViewImpl.java")
				), moduleId));
		String fieldId = field.getFieldId();
		ModuleDeclaration clientModuleDetailFormDeclaration = getModuleDeclaration(clientModuleDetailFormViewImplPath);
		ConstructorDeclaration clientModuleDetailFormConstructor = clientModuleDetailFormDeclaration.getConstructors().iterator().next();
		
		Pattern p = Pattern.compile(multipleConcat("fields\\s*\\.\\s*put\\s*\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*", JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\s*\\);"));
		Matcher m = p.matcher(clientModuleDetailFormConstructor.getBlock().toString());
		if (!m.find()){
			return null;
		}
		
		ModuleField result = new ModuleField(field);
		ModuleDeclaration clientModuleDeclaration = getClientModuleRecordDefinition(moduleId);
		
		for (MethodDeclaration method : clientModuleDeclaration.getMethods()){
			if ("buildtypemap".equalsIgnoreCase(method.getName())){
				p = Pattern.compile(multipleConcat("put\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*(.*?)\\)"));
				m = p.matcher(method.getBody().toString());
				if (m.find()){
					result.setFieldType(m.group(1));
				}
			}
			else if ("buildlikemap".equalsIgnoreCase(method.getName())){
				p = Pattern.compile(multipleConcat("put\\(\\s*", fieldId.toUpperCase(), "\\s*,\\s*(.*?)\\)"));
				m = p.matcher(method.getBody().toString());
				if (m.find()){
					result.setFieldLike(m.group(1));
				}
			}
		}
		
		String clientModuleResourcePath = convertPatternInRealPath(
					replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties")
					), moduleId));
		
		String clientModuleResourceEnPath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties")
				), moduleId));
		
		ResourceBundle clientModuleResourceBundle = getResourceByPath(clientModuleResourcePath);
		ResourceBundle clientModuleResourceBundleEn = getResourceByPath(clientModuleResourceEnPath);
		
		String fieldDetailFormName = clientModuleResourceBundle.getString(multipleConcat(initSmall(moduleId), ".detail.", fieldId.toLowerCase()));
		result.setFieldDetailFormName(NO_NAME.equalsIgnoreCase(fieldDetailFormName) ? null : fieldDetailFormName);
		
		String fieldDetailFormNameEn = clientModuleResourceBundleEn.getString(multipleConcat(initSmall(moduleId), ".detail.", fieldId.toLowerCase()));
		result.setFieldDetailFormNameEn(NO_NAME.equalsIgnoreCase(fieldDetailFormNameEn) ? null : fieldDetailFormNameEn);
		
		p = Pattern.compile(multipleConcat("(.*?)\\s*", JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\s*=\\s*new\\s*(.*?)\\("));
		m = p.matcher(clientModuleDetailFormConstructor.getBlock().toString());
		if (m.find()){
			result.setFieldWidget(m.group(3));
		}
		p = Pattern.compile(multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\.setFieldWidth\\((.*?)\\);"));
		m = p.matcher(clientModuleDetailFormConstructor.getBlock().toString());
		if (m.find()){
			result.setFieldWidth(m.group(2));
		}
		p = Pattern.compile(multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null), "(.*?)\\.setLabelWidth\\((.*?)\\);"));
		m = p.matcher(clientModuleDetailFormConstructor.getBlock().toString());
		if (m.find()){
			result.setLabelWidth(m.group(2));
		}
		p = Pattern.compile(multipleConcat("setFieldMaxLength\\(", fieldId.toUpperCase(),",\\s*(.*?)\\);"));
		m = p.matcher(clientModuleDetailFormConstructor.getBlock().toString());
		if (m.find()){
			result.setFieldMaxLength(m.group(1));
		}
		
		return result;
	}
	
	public static ModuleField prepareListFormModuleField(String moduleId, ModuleField field){
		
		String clientModuleListFormViewImplPath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "src/java/com/technology/{0}/{1}/{2}/client/ui/form/list/{3}ListFormViewImpl.java")
				), moduleId));
		
		ModuleDeclaration clientModuleListFormDeclaration = getModuleDeclaration(clientModuleListFormViewImplPath);
		
		String columnConfiguration = null;
		for (MethodDeclaration method : clientModuleListFormDeclaration.getMethods()){
			if ("getColumnConfigurations".equalsIgnoreCase(method.getName())){
				columnConfiguration = method.toString();
				break;
			}
		}
		String fieldId = field.getFieldId();
		ModuleField result = new ModuleField(field);
		Pattern p = Pattern.compile(multipleConcat("new\\s*JepColumn\\s*\\(\\s*", fieldId.toUpperCase(),",\\s*", initSmall(moduleId),"Text\\.", initSmall(moduleId), "_list_", fieldId.toLowerCase(), "\\s*\\(\\s*\\)\\s*,\\s*(.*?)\\s*,"));
		Matcher m = p.matcher(columnConfiguration);
		if (m.find()){
			String columnWidth = m.group(1);
			result.setColumnWidth(DEFAULT_FIELD_WIDTH.equalsIgnoreCase(columnWidth) ? null : columnWidth);
		}
		else { 
			return null;
		}
		
		String clientModuleResourcePath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_Source.properties")
				), moduleId));
		
		String clientModuleResourceEnPath = convertPatternInRealPath(
				replacePathWithModuleId(getDefinitionProperty(CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
						multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/text/{3}Text_en.properties")
				), moduleId));
		
		ResourceBundle clientModuleResourceBundle = getResourceByPath(clientModuleResourcePath);
		ResourceBundle clientModuleResourceBundleEn = getResourceByPath(clientModuleResourceEnPath);
		
		String fieldListFormName = clientModuleResourceBundle.getString(multipleConcat(initSmall(moduleId), ".list.", fieldId.toLowerCase()));
		result.setFieldListFormName(NO_NAME.equalsIgnoreCase(fieldListFormName) ? null : fieldListFormName);
		
		String fieldListFormNameEn = clientModuleResourceBundleEn.getString(multipleConcat(initSmall(moduleId), ".list.", fieldId.toLowerCase()));
		result.setFieldListFormNameEn(NO_NAME.equalsIgnoreCase(fieldListFormNameEn) ? null : fieldListFormNameEn);
		
		return result;
	}
	
	private static ModuleDeclaration getClientModuleRecordDefinition(String moduleId){
		String clientModuleRecordDefinitionPath = convertPatternInRealPath(
				replacePathWithModuleId(
					getDefinitionProperty(CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/shared/record/{3}RecordDefinition.java")
					),
				moduleId));
		ModuleDeclaration clientModuleDeclaration = getModuleDeclaration(clientModuleRecordDefinitionPath);
		return clientModuleDeclaration;
	}
	
	private static String replacePathWithModuleId(String path, String moduleId){
		return format(path, "{0}", "{1}", moduleId.toLowerCase(), "{2}");
	}
	
	public static String getRoles(String moduleId){
		String mainModulePresenterFilePath = convertPatternInRealPath(getDefinitionProperty(MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/client/ui/main/{2}MainModulePresenter.java")));
		
		Pattern p = Pattern.compile(multipleConcat(moduleId.toUpperCase(), "_MODULE_ID,\\s*\"(.*?)\""));
		String methodBody;
		try {
			methodBody = readFromFile(mainModulePresenterFilePath, UTF_8);
		} catch (FileNotFoundException e) {
			echoMessage(e.getLocalizedMessage());
			return "";
		}
		Matcher m = p.matcher(methodBody);
		if(m.find()){
			return m.group(1);
		}
		return "";
	}
}
