package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;

import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;

public class ApplicationStructureParser extends Task implements JepRiaToolkitConstant {
	
	@Override
	public void execute() throws BuildException {
		echoMessage("Generate JepApplication.xml...");
		List<String> names = extractFileNamesByPattern(getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml"));
		String projectPackage = names.get(0);
		String applicationXmlPath = convertPatternInRealPath(getDefinitionProperty(APPLICATION_XML_PATH_TEMPLATE_PROPERTY, "src/resources/com/technology/{0}/{1}/application.xml"));
		String applicationName = getApplicationName(applicationXmlPath);
		
		Application structure = new Application();
		structure.setName(applicationName);
		structure.setDefaultDatasource(DEFAULT_DATASOURCE);
		structure.setProjectPackage(projectPackage);
		
		Module mod = new Module("Test", "Test", "Test", DEFAULT_DATASOURCE, Arrays.asList("TestRoles"), "TestPackage");
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
