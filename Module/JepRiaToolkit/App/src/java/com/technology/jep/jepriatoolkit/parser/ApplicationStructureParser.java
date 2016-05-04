package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_SOURCE_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPathSupressException;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.copyFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.extractFileNamesByPattern;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getModuleNames;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getResourceByPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.ApplicationDefinition;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.log.Logger;
import com.technology.jep.jepriatoolkit.parser.thread.ModuleParser;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ApplicationStructureParser extends Task {
	
	private static final String OUTPUT_LOG_FILE = "createStucture.log";
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
		int moduleCount = moduleNames.size();
		List<Module> modules = new ArrayList<Module>(moduleCount);
		
		String mainModuleResourcePath = convertPatternInRealPathSupressException(getDefinitionProperty(MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_Source.properties"), resource));
		
		String mainModuleResourceEnPath = convertPatternInRealPathSupressException(getDefinitionProperty(MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY, 
				multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "/{0}/{1}/main/shared/text/{2}Text_en.properties"), resource));
		
		ResourceBundle mainModuleResourceBundle = getResourceByPath(mainModuleResourcePath);
		ResourceBundle mainModuleResourceBundleEn = getResourceByPath(mainModuleResourceEnPath);
		
		ExecutorService service = Executors.newFixedThreadPool(moduleCount);
		
		CompletionService<Module> completionService = new ExecutorCompletionService<Module>(service);
		
		for(String moduleId : moduleNames){
			completionService.submit(new ModuleParser(structure, moduleId, resource, mainModuleResourceBundle, mainModuleResourceBundleEn));
		}
		//now retrieve the futures after computation (auto wait for it)
		int received = 0;
		while (received < moduleCount) {
			Future<Module> resultFuture = null;
			try {
				resultFuture = completionService.take();
				modules.add(resultFuture.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			received++;
		}
		// important: shutdown your ExecutorService
		service.shutdown();
		structure.setModules(new Modules(modules));
		
		if (new File(fileName).exists()){
			try {
				ApplicationSettingParser applicationParser = ApplicationSettingParser.getInstance(normalizePath(fileName), true);
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

	public void setJepRiaVersion(String jepRiaVersion) {
		this.jepRiaVersion = jepRiaVersion;
	}
	
	private void log(String message, String moduleId){
		echoMessage(message);
		Logger.appendMessageToTheEndOfForm(moduleId, message);
	}
}
