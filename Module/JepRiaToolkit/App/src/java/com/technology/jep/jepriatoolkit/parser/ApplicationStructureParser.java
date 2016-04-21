package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getMethods;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
import com.technology.jep.jepriatoolkit.creator.module.ListForm;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.Record;

public class ApplicationStructureParser extends Task implements JepRiaToolkitConstant {
	
	@Override
	public void execute() throws BuildException {
		List<String> names = extractFileNamesByPattern(getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml"));
		String projectPackage = names.get(0);
		String applicationXmlPath = convertPatternInRealPath(getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml"));
		String applicationName = getApplicationName(applicationXmlPath);
		echoMessage(multipleConcat("Generate ", applicationName, "Definition.xml..."));
		
		Application structure = new Application();
		structure.setName(applicationName);
		structure.setDefaultDatasource(DEFAULT_DATASOURCE);
		structure.setProjectPackage(projectPackage);
		String mainGwtXmlPropertyPath = convertPatternInRealPath(getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
			multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{3}.gwt.xml")
		));
		List<String> moduleNames = getModuleNames(mainGwtXmlPropertyPath);
		List<Module> modules = new ArrayList<Module>(moduleNames.size());
		for(String moduleId : moduleNames){
			String clientModuleDaoPath = convertPatternInRealPath(getDefinitionProperty(CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY, 
							multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/{2}/server/dao/{3}Dao.java")));
			
			String packageName = null;
			StringBuilder findParameters = new StringBuilder();
			StringBuilder createParameters = new StringBuilder();
			StringBuilder updateParameters = new StringBuilder();
			for (MethodDeclaration method : getMethods(clientModuleDaoPath)){
				String methodName = method.getName();
				if ("find".equalsIgnoreCase(methodName) || 
						"create".equalsIgnoreCase(methodName) ||
							"update".equalsIgnoreCase(methodName)){
					
					Pattern p = Pattern.compile("get\\((.*?)\\)");
					String methodBody = method.getBody().toString();
					Matcher m = p.matcher(methodBody);
					while(m.find()){
						String value = m.group(1);
						if ("find".equalsIgnoreCase(methodName))
							findParameters.append(findParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
						else if ("create".equalsIgnoreCase(methodName))
							createParameters.append(createParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
						else if ("update".equalsIgnoreCase(methodName))
							updateParameters.append(updateParameters.toString().isEmpty() ? "" : SEPARATOR.concat(WHITE_SPACE)).append(value);
					}
					
					p = Pattern.compile(":=\\s+(.*?)\\.find");
					m = p.matcher(methodBody);
					if (m.find()){
						packageName = m.group(1);
					}
				}
			}
			echoMessage("-----");
			echoMessage(packageName);
			echoMessage("-----");
			echoMessage("find" + findParameters.toString());
			echoMessage("-----");
			echoMessage("create" + createParameters.toString());
			echoMessage("-----");
			echoMessage("update" + updateParameters.toString());
			
			Db db = new Db(packageName, DEFAULT_DATASOURCE);
			if (!findParameters.toString().isEmpty())
				db.setFind(new FunctionParameters(findParameters.toString()));
			if (!createParameters.toString().isEmpty())
				db.setCreate(new FunctionParameters(createParameters.toString()));
			if (!updateParameters.toString().isEmpty())
				db.setUpdate(new FunctionParameters(updateParameters.toString()));
			
			Module mod = new Module(moduleId, "Test", "Test", db, Arrays.asList("TestRoles"));
			
			Record rec = new Record("REPORT_TASK_ID");
			rec.setTable("test_table");
			ModuleField field = new ModuleField("Test", "Test", "Integer", "Test", "Test", null, "JepNumberField", null, null, null, null, "CREATE, VIEW_LIST", "CREATE", "CREATE", "CREATE");
			rec.setFields(Arrays.asList(field));
			
			mod.setRecord(rec);
			
			List<ModuleField> fields = new ArrayList<ModuleField>();
			DetailForm detailForm = new DetailForm();
			detailForm.setPresenterBody(Arrays.toString(getMethods("D:/Project/JEPGit/Module/JepRiaShowcase/App/src/java/com/technology/jep/jepriashowcase/allshopgoods/client/ui/form/detail/AllShopGoodsDetailFormPresenter.java").toArray()));
			mod.setForms(detailForm, new ListForm());
			
			modules.add(mod);
		}
		
		structure.setModules(modules);
		
		convertToXml(structure, multipleConcat(applicationName, "Definition2.xml"));
		
		echoMessage("ProjectPackage: " + projectPackage);
		echoMessage("Application XML Path: " + applicationXmlPath);
		echoMessage("ApplicationName: " + applicationName);
		echoMessage("----");
		names = extractFileNamesByPattern(getDefinitionProperty(null, "src/resources/com/technology/{0}/{1}/{2}/web.xml"));
		echoMessage(Arrays.toString(names.toArray()));
		echoMessage("----");
		names = extractFileNamesByPattern(getDefinitionProperty(null, "src/java/com/technology/{0}/{1}/{2}/{3}.gwt.xml"));
		echoMessage(Arrays.toString(names.toArray()));
		echoMessage("----");
		names = extractFileNamesByPattern(getDefinitionProperty(null, "src/java/com/technology/{0}/{1}/{2}/{3}"));
		echoMessage(Arrays.toString(names.toArray()));
		echoMessage("----");
		names = extractFileNamesByPattern(getDefinitionProperty(null, "src/java/com/technology/{0}/{1}/main/shared/text/{2}Text_Source.properties"));
		echoMessage(Arrays.toString(names.toArray()));
		echoMessage("----");
		names = extractFileNamesByPattern(getDefinitionProperty(null, "src/java/{0}/{1}/{2}/{3}/main/shared/text/{4}Text_Source.{5}"));
		echoMessage(Arrays.toString(names.toArray()));
		echoMessage("----");
		echoMessage(convertPatternInRealPath(getDefinitionProperty(null, "src/java/{0}/{1}/{2}/{3}/main/shared/text/{4}Text_Source.{5}")));
		echoMessage("----");
		
		
	}
}
