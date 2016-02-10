package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.creator.module.Module.getToolBarCustomButtonsForForm;

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ModuleInfo {

	private String formName;
	private String formTitle;
	private String formTitleEn;
	private String fieldLabelWidth;
	private String dataSource;
	private String primaryKey;
	private String mainFormName;
	private String mainFormParentKey;
	private String table;
	private String dbPackage;
	private String defaultParameterPrefix;
	private String findParameterPrefix;
	private String createParameterPrefix;
	private String updateParameterPrefix;
	private boolean hasLobFields;
	private boolean isExcelAvailable;
	private boolean isNotRebuild;
	private boolean isJepToolBarPresenter;
	private boolean isJepToolBarView;
	private boolean isDblClickOff;
	private boolean isToolBarOff;
	private boolean isStatusBarOff;
	private boolean hasOptionField;
	private boolean hasLikeField;
	private List<ModuleField> fields = new ArrayList<ModuleField>(); 
	private List<ModuleButton> toolBarCustomButtons = new ArrayList<ModuleButton>();
	private List<ModuleButton> toolBarButtons = new ArrayList<ModuleButton>();
	private List<String> scopeModuleIds = new ArrayList<String>();
	private List<String> moduleRoleNames  = new ArrayList<String>();
	
	public ModuleInfo() {}
	
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getFormTitleEn() {
		return formTitleEn;
	}
	public void setFormTitleEn(String formTitleEn) {
		this.formTitleEn = formTitleEn;
	}
	public String getFieldLabelWidth() {
		return fieldLabelWidth;
	}
	public void setFieldLabelWidth(String fieldLabelWidth) {
		this.fieldLabelWidth = fieldLabelWidth;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getMainFormName() {
		return mainFormName;
	}
	public void setMainFormName(String mainFormName) {
		this.mainFormName = mainFormName;
	}
	public String getMainFormParentKey() {
		return mainFormParentKey;
	}
	public void setMainFormParentKey(String mainFormParentKey) {
		this.mainFormParentKey = mainFormParentKey;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getDbPackage() {
		return dbPackage;
	}
	public void setDbPackage(String dbPackage) {
		this.dbPackage = dbPackage;
	}
	public String getDefaultParameterPrefix() {
		return defaultParameterPrefix;
	}
	public void setDefaultParameterPrefix(String defaultParameterPrefix) {
		this.defaultParameterPrefix = defaultParameterPrefix;
	}
	public String getFindParameterPrefix() {
		return findParameterPrefix;
	}
	public void setFindParameterPrefix(String findParameterPrefix) {
		this.findParameterPrefix = findParameterPrefix;
	}
	public String getCreateParameterPrefix() {
		return createParameterPrefix;
	}
	public void setCreateParameterPrefix(String createParameterPrefix) {
		this.createParameterPrefix = createParameterPrefix;
	}
	public String getUpdateParameterPrefix() {
		return updateParameterPrefix;
	}
	public void setUpdateParameterPrefix(String updateParameterPrefix) {
		this.updateParameterPrefix = updateParameterPrefix;
	}
	public boolean getHasLobFields() {
		return hasLobFields;
	}
	public void setHasLobFields(boolean hasLobFields) {
		this.hasLobFields = hasLobFields;
	}
	public boolean getIsExcelAvailable() {
		return isExcelAvailable;
	}
	public void setIsExcelAvailable(boolean isExcelAvailable) {
		this.isExcelAvailable = isExcelAvailable;
	}	
	public boolean getIsJepToolBarPresenter() {
		return isJepToolBarPresenter;
	}
	public void setIsJepToolBarPresenter(boolean isJepToolBarPresenter) {
		this.isJepToolBarPresenter = isJepToolBarPresenter;
	}
	public boolean getIsJepToolBarView() {
		return isJepToolBarView;
	}
	public void setIsJepToolBarView(boolean isJepToolBarView) {
		this.isJepToolBarView = isJepToolBarView;
	}
	public boolean getHasCustomListFormPresenter() {
		return isDblClickOff || getToolBarCustomButtonsOnListForm().size() > 0;
	}
	public boolean getIsDblClickOff() {
		return isDblClickOff;
	}
	public void setIsDblClickOff(boolean isDblClickOff) {
		this.isDblClickOff = isDblClickOff;
	}
	public boolean getIsToolBarOff() {
		return isToolBarOff;
	}
	public void setIsToolBarOff(boolean isToolBarOff) {
		this.isToolBarOff = isToolBarOff;
	}
	public boolean getIsStatusBarOff() {
		return isStatusBarOff;
	}
	public void setIsStatusBarOff(boolean isStatusBarOff) {
		this.isStatusBarOff = isStatusBarOff;
	}
	public boolean getHasOptionField() {
		return hasOptionField;
	}
	public void setHasOptionField(boolean hasOptionField) {
		this.hasOptionField = hasOptionField;
	}
	public boolean getHasLikeField() {
		return hasLikeField;
	}
	public void setHasLikeField(boolean hasLikeField) {
		this.hasLikeField = hasLikeField;
	}
	public boolean getIsMain() {
		return scopeModuleIds.size() > 0;
	}
	public boolean getIsDependent(){
		return !JepRiaToolkitUtil.isEmpty(mainFormName);
	}
	public List<ModuleField> getFields(){
		return fields;
	}
	public void setFields(List<ModuleField> fields){
		this.fields = fields;
	}
	public List<ModuleField> getOptionFields(){
		return JepRiaToolkitUtil.getOptionField(fields);
	}
	public List<ModuleField> getSortDetailFormFields(){
		return JepRiaToolkitUtil.sortFields(fields, true);
	}
	public List<ModuleField> getSortListFormFields(){
		return JepRiaToolkitUtil.sortFields(fields, false);
	}
	public List<ModuleButton> getToolBarButtons() {
		return toolBarButtons;
	}
	public void setToolBarButtons(List<ModuleButton> toolbarButtons) {
		if (!JepRiaToolkitUtil.isEmpty(toolbarButtons)){
			this.toolBarButtons = toolbarButtons;
		}
	}
	public List<ModuleButton> getToolBarCustomButtons() {
		return toolBarCustomButtons;
	}
	public void setToolBarCustomButtons(List<ModuleButton> toolBarCustomButtons) {
		this.toolBarCustomButtons = toolBarCustomButtons;
	}
	public List<ModuleButton> getToolBarCustomButtonsOnDetailForm() {
		return getToolBarCustomButtonsForForm(toolBarCustomButtons, FORM.DETAIL_FORM);
	}
	public List<ModuleButton> getToolBarCustomButtonsOnListForm() {
		return getToolBarCustomButtonsForForm(toolBarCustomButtons, FORM.LIST_FORM);
	}
	public List<ModuleButton> getToolBarCustomButtonsOnBothForms() {
		return getToolBarCustomButtonsForForm(toolBarCustomButtons, FORM.FORM_CONTAINER);
	}
	public List<String> getScopeModuleIds() {
		return scopeModuleIds;
	}
	public void setScopeModuleIds(List<String> scopeModuleIds) {
		this.scopeModuleIds = scopeModuleIds;
	}
	public boolean isNotRebuild() {
		return isNotRebuild;
	}
	public void setNotRebuild(boolean isNotRebuild) {
		this.isNotRebuild = isNotRebuild;
	}
	public List<String> getModuleRoleNames() {
		return moduleRoleNames;
	}
	public void setModuleRoleNames(List<String> moduleRoleNames) {
		this.moduleRoleNames = moduleRoleNames;
	}
}
