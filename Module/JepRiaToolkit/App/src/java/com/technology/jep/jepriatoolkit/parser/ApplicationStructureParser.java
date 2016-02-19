package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Db;
import com.technology.jep.jepriatoolkit.creator.module.FunctionParameters;
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
		
		Db db = new Db("TestPackage", DEFAULT_DATASOURCE);
		db.setFind(new FunctionParameters("INTERVIEW_BEGIN_DATE_FROM, INTERVIEW_BEGIN_DATE_TO, DEPARTMENT_ID, POSITION_ID, EMPLOYEE_ID, CATEGORY_ID, IS_CLOSE"));
		db.setCreate(new FunctionParameters("INTERVIEW_BEGIN_DATE_FROM, INTERVIEW_END_DATE_TO, DEPARTMENT_ID, POSITION_ID, EMPLOYEE_ID, CATEGORY_ID, IS_CLOSE"));
		Module mod = new Module("Test", "Test", "Test", db, Arrays.asList("TestRoles"));
		
		Record rec = new Record("REPORT_TASK_ID");
		rec.setTable("test_table");
		ModuleField field = new ModuleField("Test", "Test", "Integer", "Test", "Test", null, "JepNumberField", null, null, null, null, "CREATE, VIEW_LIST", "CREATE", "CREATE", "CREATE");
		rec.setFields(Arrays.asList(field));
		
		mod.setRecord(rec);
		structure.setModules(Arrays.asList(mod));
		
		
		
		convertToXml(structure, "JepApplication.xml");
		
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
		echoMessage("--------");
		echoMessage(convertPatternInRealPath(getDefinitionProperty(null, "src/java/{0}/{1}/{2}/{3}/main/shared/text/{4}Text_Source.{5}")));
	}
}
