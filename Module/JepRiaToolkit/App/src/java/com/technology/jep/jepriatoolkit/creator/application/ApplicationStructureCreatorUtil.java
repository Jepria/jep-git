package com.technology.jep.jepriatoolkit.creator.application;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.HAS_BINARY_FILE_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.HAS_TEXT_FILE_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.IDENTIFICATOR_SUFFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_NAME_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PACKAGE_NAME_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SECURITY_ROLES_TEMPLATE_PARAMETER;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.DetailForm;
import com.technology.jep.jepriatoolkit.creator.module.Forms;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.creator.module.ModuleInfo;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class ApplicationStructureCreatorUtil {
	
	private static Configuration cfg = null;
	
	public static Configuration getTemplateConfiguration(){
		if (cfg == null) {
			// Create your Configuration instance, and specify if up to what FreeMarker
			// version (here 2.3.22) do you want to apply the fixes that are not 100%
			// backward-compatible. See the Configuration JavaDoc for details.
			cfg = new Configuration(Configuration.VERSION_2_3_23);
	
			// Specify the source where the template files come from. Here I set a
			// plain directory for it, but non-file-system sources are possible too:
			cfg.setClassForTemplateLoading(ApplicationStructureCreatorUtil.class, "/com/technology/jep/jepriatoolkit/creator/template");
			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			cfg.setDefaultEncoding(UTF_8);
			//charset of the output
			cfg.setOutputEncoding(UTF_8);
			//default locale
		    cfg.setLocale(Locale.US);
			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		}
		return cfg;
	}
	
	public static void convertTemplateToFile(String templateFile, Map<String, Object> dataToMap, String resultFile){
		try {
			Template webXmlTemplate = getTemplateConfiguration().getTemplate(templateFile);
			Writer bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(
					new FileOutputStream(
						resultFile
					), 
					UTF_8
				)
			);
			try {
				webXmlTemplate.process(dataToMap, bufferedWriter);
			}
			finally {
				bufferedWriter.close();
			}
			
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Подготовка данных для мэппинга на подготовленные шаблоны 
	 * 
	 * @return данные для мэпирования
	 */
	public static final Map<String, Object> prepareData(ApplicationSettingParser applicationParser){
		Map<String, Object> resultData = new HashMap<String, Object>();
		Application application = applicationParser.getApplication();
		List<String> forms = applicationParser.getForms();
		resultData.put(MODULE_NAME_TEMPLATE_PARAMETER, application.getName());
		resultData.put(PACKAGE_NAME_TEMPLATE_PARAMETER, application.getProjectPackage());
		resultData.put(SECURITY_ROLES_TEMPLATE_PARAMETER, applicationParser.getRoles());
		List<ModuleInfo> mods = new ArrayList<ModuleInfo>(forms.size());
		boolean hasTextFile = false;
		boolean hasBinaryFile = false;
		for (int i = 0; i < forms.size(); i++) {
			String formName = (String) forms.get(i);
			Map<Module, List<ModuleField>> hm = applicationParser.getModuleWithFieldsById(formName);
			Module module = hm.keySet().iterator().next();
			
			ModuleInfo modInfo = new ModuleInfo();
			modInfo.setFormName(formName);
			modInfo.setFormTitle(module.getModuleName());
			modInfo.setFormTitleEn(module.getModuleNameEn());
			modInfo.setFieldLabelWidth(module.getFieldLabelWidth());
			modInfo.setDataSource(module.getDb().getDatasource());
			modInfo.setPrimaryKey(applicationParser.getPrimaryKeyById(formName));
			modInfo.setTable(module.getTable());
			modInfo.setDbPackage(module.getDb().getPackageName());
			modInfo.setIsExcelAvailable(module.isExcelAvailable());
			modInfo.setNotRebuild(module.isNotRebuild());
			modInfo.setDefaultParameterPrefix(module.getDefaultParameterPrefix());
			modInfo.setCreateParameterPrefix(module.getCreateParameterPrefix());
			modInfo.setFindParameterPrefix(module.getFindParameterPrefix());
			modInfo.setUpdateParameterPrefix(module.getUpdateParameterPrefix());
			Forms fms = module.getForms();
			if (!isEmpty(fms)) {
				DetailForm detailForm = fms.getDetailForm();
				if(!isEmpty(detailForm)) {
					modInfo.setPresenterBody(detailForm.getPresenterBody());
				}
			}
			String mainFormIfExist = applicationParser.getMainFormNameIfExist(formName);
			modInfo.setMainFormName(mainFormIfExist);
			if (!isEmpty(mainFormIfExist)) {
				String mainFormParentKey = applicationParser.getPrimaryKeyById(mainFormIfExist);
				mainFormParentKey = isEmpty(mainFormParentKey) ? multipleConcat(mainFormIfExist, IDENTIFICATOR_SUFFIX) : mainFormParentKey;
				modInfo.setMainFormParentKey(mainFormParentKey);
			}
			boolean isJepToolBar = module.isStandardToolBar() && !module.isToolBarOff();
			modInfo.setIsJepToolBarPresenter(isJepToolBar && !Boolean.TRUE.equals(module.hasToolBarPresenter()));
			modInfo.setIsJepToolBarView(isJepToolBar && !Boolean.TRUE.equals(module.hasToolBarView()));
			modInfo.setIsDblClickOff(module.isDblClickOff());
			modInfo.setStandardToolBar(module.isStandardToolBar());
			modInfo.setIsToolBarOff(module.isToolBarOff());
			modInfo.setIsStatusBarOff(module.isStatusBarOff());
			modInfo.setHasToolBarView(module.hasToolBarView());
			modInfo.setHasToolBarPresenter(module.hasToolBarPresenter());
			modInfo.setHasLikeField(module.hasLikeFields());
			modInfo.setScopeModuleIds(applicationParser.getDependencyNodesIfExists(formName));
			modInfo.setToolBarButtons(module.getToolBarButtons());
			modInfo.setToolBarCustomButtons(module.getToolBarCustomButtons());
			modInfo.setModuleRoleNames(module.getModuleRoleNames());
			
			List<ModuleField> moduleFields = hm.values().iterator().next();
			boolean hasLobFields = false;
			boolean hasOptionField = false;
			for (ModuleField moduleField : moduleFields) {
				if (moduleField.isCLOB())
					hasTextFile = true;
				if (moduleField.isBLOB())
					hasBinaryFile = true;
				if (moduleField.getIsLOB())
					hasLobFields = true;	
				if (moduleField.getIsOptionField()){
					hasOptionField = true;
				}
			}
			modInfo.setHasLobFields(hasLobFields);
			modInfo.setHasOptionField(hasOptionField);
			modInfo.setFields(moduleFields);
			mods.add(modInfo);
		}
		resultData.put(HAS_TEXT_FILE_TEMPLATE_PARAMETER, hasTextFile);
		resultData.put(HAS_BINARY_FILE_TEMPLATE_PARAMETER, hasBinaryFile);
		resultData.put(FORMS_TEMPLATE_PARAMETER, mods);
		return resultData;
	}
}
