package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_APPLICATION_XML;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OUTPUT_LOG_FILE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.XML_EXTENSION;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.YES;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getApplicationBySourceCode;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.copyFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.log.Logger;

public class ApplicationStructureParser extends Task {
	
	private String jepRiaVersion;
	
	@Override
	public void execute() throws BuildException {
		// Получаем информацию о модуле по исходным кодам
		Application structure = getApplicationBySourceCode(jepRiaVersion);
		if (structure == null) return;
		
		String fileName = structure.getApplicationFileName();
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
				ApplicationStructureParserUtil.log(multipleConcat("Content from '", JEP_APPLICATION_XML, "' was copied to file '", fileName, "'"), null);
				copyFile(JEP_APPLICATION_XML, fileName);
			}
			catch(IOException e){
				ApplicationStructureParserUtil.log(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()), null);
				return;
			}
		}
		echoMessage(multipleConcat("Generate ", normalizePath(fileName), "..."));
		
		if (new File(fileName).exists()){
			try {
				ApplicationSettingParser applicationParser = ApplicationSettingParser.getInstance(normalizePath(fileName), true);
				Application application = applicationParser.getApplication();
				application.uptodate(structure);
				structure = application;
			} catch (ParserConfigurationException e) {
				ApplicationStructureParserUtil.log(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()), null);
			}
		}
		convertToXml(structure, fileName);
		Logger.printMessages(OUTPUT_LOG_FILE);
	}
	
	public void setJepRiaVersion(String jepRiaVersion) {
		this.jepRiaVersion = jepRiaVersion;
	}
}
