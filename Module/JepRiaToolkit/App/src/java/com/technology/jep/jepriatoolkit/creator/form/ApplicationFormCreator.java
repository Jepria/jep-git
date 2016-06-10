package com.technology.jep.jepriatoolkit.creator.form;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_NAME_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_PROJECT_PACKAGE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INFO_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PARENT_MODULE_NAME_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.YES;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TRUE_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.buildAndDeploy;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertToXml;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.createApplicationStructure;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.createBlankModule;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getApplicationDefinitionFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Element;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.Modules;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;

public class ApplicationFormCreator extends Task {
	
	// Атрибуты таска
	private String applicationName;
	private String moduleName, parentModuleName;
	private String applicationStructureFile;
	private String skipBuildAndDeploy;
	// Тип операции, производимой в данном таске
	private OperationType type;
	
	/**
	 * Выполняемые действия данной командой Ant
	 */
	private enum OperationType {
		/**
		 * Создаем новый ApplicationDefinition.xml с учетом переданных параметров -DAPPLICATION_NAME и -DMODULE_NAME
		 * Для данной команды параметр APPLICATION_NAME обязателен, MODULE_NAME - опционален.
		 */
		CREATE,
		/**
		 * Добавляем к существующему ApplicationDefinition.xml новый модуль с учетом переданных параметров -DMODULE_NAME и -DPARENT_MODULE_NAME
		 * Для данной команды параметр MODULE_NAME обязателен, PARENT_MODULE_NAME - опционален.
		 * Также для удобства имеется параметр -DAPPLICATION_STRUCTURE_FILE_PATH для возможности явного указания файла структуры приложения, 
		 * если он отличен от умолчательного.
		 */
		ADD
	}
	
	/**
	 * Основной метод, который выполняет Task
	 */
	@Override
	public void execute() throws BuildException {
		try {
			// Тип производимой операции определяется по переданным параметрам:
			// Если значение applicationName определено, значит будет выполнено создание новой структуры,
			// В противном случае - добавляем модуль к имеющейся структуре
			type = isEmpty(applicationName) ? OperationType.ADD : OperationType.CREATE ;
			Application application;
			String fileName;
			switch(type) {
				case ADD : {
					// проверка обязательности наименования модуля
					if (isNotInitializedParameter(moduleName)){
						echoMessage(multipleConcat(ERROR_PREFIX,
								"Application definition XML wouldn't modify! Please specify the attribute '-D", MODULE_NAME_TASK_ATTRIBUTE, "' in command line to add new module!"));
						return;
					}
					// определяем файл структуры приложения
					fileName = isEmptyOrNotInitializedParameter(applicationStructureFile) ? getApplicationDefinitionFile() : applicationStructureFile;
					ApplicationSettingParser applicationParser = ApplicationSettingParser.getInstance(fileName);
					application = applicationParser.getApplication();
					Element module = applicationParser.getModuleNodeByIdIgnoringCase(moduleName);
					// если такой модуль еще не определен в имеющейся структуре
					if (module == null){
						Element parentModule = null;
						// проверяем родительский на существование
						if (!isEmptyOrNotInitializedParameter(parentModuleName)){
							parentModule = applicationParser.getModuleNodeByIdIgnoringCase(parentModuleName);
							if (parentModule == null){
								echoMessage(multipleConcat(ERROR_PREFIX,
										"Application definition XML wouldn't modify! The parent module '", parentModuleName, "' not found! Please specify correct value for the attribute '-D", PARENT_MODULE_NAME_TASK_ATTRIBUTE, "'"));
								return;
							}
						}
						echoMessage(multipleConcat("Modify ", normalizePath(fileName), "..."));
						
						// Ссылка на список дочерних узлов: если родительский узел не определен как параметр, то добавляем в корень структуры,
						// иначе найдем дочерние узлы искомого модуля
						List<Module> childModules = application.getModules().getModules();
						
						// проставим всем модуля признак не пересборки
						initNotRebuildProperty(childModules);
						
						if (parentModule != null){
							// составим путь для поиска родительского модуля
							List<String> pathToParentModule = new ArrayList<String>();
							
							String parentMName = parentModuleName;
							pathToParentModule.add(parentMName);
							// ищем все родительские узлы указанного родителя (вероятно он находится не на первом уровне вложения)
							while ((parentMName = applicationParser.getMainFormNameIfExist(parentMName)) != null){
								pathToParentModule.add(parentMName);
							}
							// изменяем порядок следования родительских узлов для возможности обхода модулей в прямом направлении
							Collections.reverse(pathToParentModule);
							for (String parentName : pathToParentModule){
								List<Module> childrenModules = childModules;
								for (Module childModule : childrenModules){
									if (parentName.equalsIgnoreCase(childModule.getModuleId())){
										// если дочерних узлов еще не добавлено, то создадим таковые и добавлять будем к ним
										if (isEmpty(childModule.getChildModules())) {
											childModule.setChildModules(new ArrayList<Module>());
										}
										childModules = childModule.getChildModules();
										break;
									}
								}
							}
						}
						
						childModules.add(createBlankModule(moduleName, application.getName()));
					}
					else {
						echoMessage(multipleConcat(ERROR_PREFIX,
								"Application definition XML wouldn't modify! The module '", module.getAttribute(MODULE_ID_ATTRIBUTE), "' has already existed! Please specify correct value for the attribute '-D", MODULE_NAME_TASK_ATTRIBUTE, "'"));
						return;
					}
					break;
				}
				case CREATE : 
				default : {
					if (isNotInitializedParameter(applicationName) || isNotInitializedParameter(moduleName)){
						echoMessage(multipleConcat(ERROR_PREFIX, "Application definition XML wouldn't generate! Please define the mandatory attributes '-D", APPLICATION_NAME_TASK_ATTRIBUTE, "' and '-D", MODULE_NAME_TASK_ATTRIBUTE, "' in command line to do it!"));
						return;
					}
					fileName = multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, applicationName, APPLICATION_SETTING_FILE_ENDING);
					// Уточним стоит ли перезаписывать файл, если таковой уже существует
					if (new File(fileName).exists()){
						echoMessage(multipleConcat(WARNING_PREFIX, "The file '", normalizePath(applicationName), "' has already existed!\n", 
								WARNING_PREFIX, "Do you want to replace this file (", YES, " / ", NO, ")? Please type your answer: "));
						Scanner scanner = new Scanner(System.in);
						try {
							while (scanner.hasNext()){
								String line = scanner.next();
								if (NO.equalsIgnoreCase(line)){
									return;
								}
								else {
									break;
								}
							}
						}
						finally {
							scanner.close();	
						}
					}
					
					// Новая заготовка для приложения
					String applicationDataSource = DEFAULT_DATASOURCE;
					application = new Application();
					application.setName(applicationName);
					application.setDefaultDatasource(applicationDataSource);
					application.setProjectPackage(DEFAULT_PROJECT_PACKAGE);
					
					if (isEmptyOrNotInitializedParameter(moduleName)) {
						echoMessage(multipleConcat(INFO_PREFIX,
								"To define application with empty form you should specify the optional attribute '-D", MODULE_NAME_TASK_ATTRIBUTE, "' in command line."));	
					}
					else {
						// Новая заготовка для пустого модуля, если такой параметр определен
						application.setModules(new Modules(createBlankModule(moduleName, applicationName)));
					}
					
					echoMessage(multipleConcat("Generate ", normalizePath(fileName), "..."));
					
					break;
				}
			}
			
			convertToXml(application, fileName);
			createApplicationStructure(fileName);
			if (!TRUE_TASK_ATTRIBUTE.equals(skipBuildAndDeploy)) {
				buildAndDeploy();
			}
		}
		catch(Exception e){
			throw new BuildException(e);
		}
	}
	
	private void initNotRebuildProperty(List<Module> childModules) {
		for (Module mod : childModules){
			mod.setNotRebuild(true);
			if (!isEmpty(mod.getChildModules())){
				initNotRebuildProperty(mod.getChildModules());
			}
		}
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setModuleName(String formName) {
		this.moduleName = formName;
	}
	
	public void setParentModuleName(String formName) {
		this.parentModuleName = formName;
	}

	public void setApplicationStructureFile(String applicationStructureFile) {
		this.applicationStructureFile = applicationStructureFile;
	}
	
	public void setSkipBuildAndDeploy(String skipBuildAndDeploy){
		this.skipBuildAndDeploy = skipBuildAndDeploy;
	}
}
