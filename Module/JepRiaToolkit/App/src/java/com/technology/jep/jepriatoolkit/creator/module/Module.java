package com.technology.jep.jepriatoolkit.creator.module;

import java.util.ArrayList;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;

import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class Module implements JepRiaToolkitConstant {

	private String table;
	private String moduleId;
	private String moduleName;
	private String moduleNameEn;
	private String moduleDataSource;
	private String dbPackageName;
	private String defaultParameterPrefix;
	private String findParameterPrefix;
	private String createParameterPrefix;
	private String updateParameterPrefix;
	private String fieldLabelWidth;
	private List<String> moduleRoleNames;
	private List<ModuleButton> toolbarButtons = new ArrayList<ModuleButton>();
	private List<ModuleButton> customToolBarButtons = null;
	private boolean hasLikeFields = false;
	private boolean hasListFormPresenter = false;
	private boolean hasToolBarPresenter = false;
	private boolean hasToolBarView = false;
	private boolean isToolBarOff = false;
	private boolean isStatusbarOff = false;
	private boolean isDNDOff = true;
	private boolean isDblClickOff = false;
	private boolean isExcelAvailable = false;
	private boolean isNotRebuild = false;
	
	public Module(String moduleId, String moduleName, String moduleNameEn, String moduleDataSource, List<String> moduleRoleNames, String dbPackage){
		setModuleId(moduleId);
		setModuleName(moduleName);
		setModuleNameEn(moduleNameEn);
		setModuleDataSource(moduleDataSource);
		setModuleRoleNames(moduleRoleNames);
		setDbPackageName(dbPackage);
	}	
	
	public boolean isStatusBarOff() {
		return isStatusbarOff;
	}
	public void setStatusbarOff(boolean isStatusbarOff) {
		this.isStatusbarOff = isStatusbarOff;
	}
	public boolean isToolBarOff() {
		return isToolBarOff;
	}
	public void setToolBarOff(boolean isToolBarOff) {
		this.isToolBarOff = isToolBarOff;
	}
	public List<ModuleButton> getToolBarButtons() {
		return toolbarButtons;
	}	
	public List<ModuleButton> getToolBarCustomButtons() {
		if (JepRiaToolkitUtil.isEmpty(customToolBarButtons)){		
			customToolBarButtons = new ArrayList<ModuleButton>();
			if (!JepRiaToolkitUtil.isEmpty(toolbarButtons))
				for (ModuleButton button : toolbarButtons)
					if (button.getIsCustomButton())
						customToolBarButtons.add(button);			
		}		
		return customToolBarButtons;
	}
	public List<ModuleButton> getToolBarCustomButtonsForForm(FORM form) {
		List<ModuleButton> customButtons = getToolBarCustomButtons(),
		   				detailFormButtons = new ArrayList<ModuleButton>();
		try {
			for (ModuleButton customButton : customButtons){
				if (form.equals(customButton.getPlacedForm())){
					detailFormButtons.add(customButton);
				}
			}
		}
		catch(UnsupportedDataTypeException e){
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return detailFormButtons;
	}
	public static List<ModuleButton> getToolBarCustomButtonsForForm(List<ModuleButton> customButtons, FORM form) {
		if (JepRiaToolkitUtil.isEmpty(customButtons)) return customButtons;
		
		List<ModuleButton> detailFormButtons = new ArrayList<ModuleButton>();
		try {
			for (ModuleButton customButton : customButtons){
				if (form.equals(customButton.getPlacedForm())){
					detailFormButtons.add(customButton);
				}
			}
		}
		catch(UnsupportedDataTypeException e){
			JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return detailFormButtons;
	}
	public void setToolBarButtons(List<ModuleButton> toolbarButtons) {
		this.toolbarButtons = toolbarButtons;
	}
	public String getDbPackageName() {
		return dbPackageName;
	}
	public void setDbPackageName(String dbPackageName) {
		this.dbPackageName = dbPackageName;
	}
	public List<String> getModuleRoleNames() {
		return moduleRoleNames;
	}	
	public void setModuleRoleNames(List<String> moduleRoleNames) {
		this.moduleRoleNames = moduleRoleNames;
	}
	public boolean hasLikeFields() {
		return hasLikeFields;
	}
	public void setHasLikeFields(boolean hasLikeFields) {
		this.hasLikeFields = hasLikeFields;
	}
	public boolean hasListFormPresenter() {
		return hasListFormPresenter;
	}
	public void setHasListFormPresenter(boolean hasListFormPresenter) {
		this.hasListFormPresenter = hasListFormPresenter;
	}
	public boolean hasToolBarPresenter() {
		return hasToolBarPresenter;
	}
	public void setHasToolBarPresenter(boolean hasToolBarPresenter) {
		this.hasToolBarPresenter = hasToolBarPresenter;
	}
	public boolean hasToolBarView() {
		return hasToolBarView;
	}
	public void setHasToolBarView(boolean hasToolBarView) {
		this.hasToolBarView = hasToolBarView;
	}
	public boolean isDNDOff() {
		return isDNDOff;
	}
	public void setDNDOff(boolean isDNDOff) {
		this.isDNDOff = isDNDOff;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleNameEn() {
		return moduleNameEn;
	}
	public void setModuleNameEn(String moduleNameEn) {
		this.moduleNameEn = moduleNameEn;
	}
	public String getModuleDataSource() {
		return moduleDataSource;
	}
	public void setModuleDataSource(String moduleDataSource) {
		this.moduleDataSource = moduleDataSource;
	}	
	public String getModuleSecurityRolesAsString(){
		String moduleRoles = new String();
		
		for (String role : moduleRoleNames){
			moduleRoles += JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.isEmpty(moduleRoles) ? "" : ", ", role);
		}
		
		return moduleRoles;
	}	
	public boolean isStandardToolBar(){
		return toolbarButtons.size() == 0;
	}
	public String getFindParameterPrefix() {
		return !JepRiaToolkitUtil.isEmpty(findParameterPrefix) ? findParameterPrefix : defaultParameterPrefix;
	}
	public void setFindParameterPrefix(String findParameterPrefix) {
		this.findParameterPrefix = findParameterPrefix;
	}
	public String getCreateParameterPrefix() {
		return !JepRiaToolkitUtil.isEmpty(createParameterPrefix) ? createParameterPrefix : defaultParameterPrefix;
	}
	public void setCreateParameterPrefix(String createParameterPrefix) {
		this.createParameterPrefix = createParameterPrefix;
	}
	public String getUpdateParameterPrefix() {
		return !JepRiaToolkitUtil.isEmpty(updateParameterPrefix) ? updateParameterPrefix : defaultParameterPrefix;
	}
	public void setUpdateParameterPrefix(String updateParameterPrefix) {
		this.updateParameterPrefix = updateParameterPrefix;
	}
	public String getDefaultParameterPrefix() {
		return defaultParameterPrefix;
	}
	public void setDefaultParameterPrefix(String defaultParameterPrefix) {
		this.defaultParameterPrefix = defaultParameterPrefix;
	}
	public boolean isDblClickOff() {
		return isDblClickOff;
	}
	public void setDblClickOff(boolean isDblClickOff) {
		this.isDblClickOff = isDblClickOff;
	}
	public boolean isExcelAvailable() {
		return isExcelAvailable;
	}
	public void setExcelAvailable(boolean isExcelAvailable) {
		this.isExcelAvailable = isExcelAvailable;
	}
	public boolean isNotRebuild() {
		return isNotRebuild;
	}
	public void setNotRebuild(boolean isNotRebuild) {
		this.isNotRebuild = isNotRebuild;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public boolean hasCustomButtons(){
		return getToolBarCustomButtons().size() > 0;
	}
	public String getFieldLabelWidth() {
		return fieldLabelWidth;
	}
	public void setFieldLabelWidth(String fieldLabelWidth) {
		if (!JepRiaToolkitUtil.isEmpty(fieldLabelWidth)) {
			this.fieldLabelWidth = fieldLabelWidth;
		}
	}
}
