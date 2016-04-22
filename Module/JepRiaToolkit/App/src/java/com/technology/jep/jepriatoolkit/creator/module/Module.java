package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_STATUSBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TOOLBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.RECORD_TAG_NAME;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.ArrayList;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.technology.jep.jepriatoolkit.creator.module.adapter.BooleanAdapter;

// Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"moduleRoles", "db", "record", "forms", "isStatusbarOff", "isToolBarOff", "moduleNameEn", "moduleName", "moduleId"})
@XmlRootElement(name=MODULE_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Module {
	@XmlTransient
	private String table;
	@XmlAttribute(name=ID_ATTRIBUTE, required = true)
	private String moduleId;
	@XmlAttribute(name=NAME_ATTRIBUTE)
	private String moduleName;
	@XmlAttribute(name=NAME_EN_ATTRIBUTE)
	private String moduleNameEn;
	@XmlElement(name=MODULE_ROLES_ATTRIBUTE)
	private Roles moduleRoles;
	@XmlElement(name=DATABASE_TAG_NAME)
	private Db db;
	@XmlTransient
	private String defaultParameterPrefix;
	@XmlTransient
	private String findParameterPrefix;
	@XmlTransient
	private String createParameterPrefix;
	@XmlTransient
	private String updateParameterPrefix;
	@XmlTransient
	private String fieldLabelWidth;
	@XmlTransient
	private List<ModuleButton> toolbarButtons = new ArrayList<ModuleButton>();
	@XmlTransient
	private List<ModuleButton> customToolBarButtons = null;
	@XmlTransient
	private boolean hasLikeFields = false;
	@XmlTransient
	private boolean hasListFormPresenter = false;
	@XmlTransient
	private boolean hasToolBarPresenter = false;
	@XmlTransient
	private boolean hasToolBarView = false;
	@XmlAttribute(name=MODULE_TOOLBAR_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean isToolBarOff = false;
	@XmlAttribute(name=MODULE_STATUSBAR_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean isStatusbarOff = false;
	@XmlTransient
	private boolean isDNDOff = true;
	@XmlTransient
	private boolean isDblClickOff = false;
	@XmlTransient
	private boolean isExcelAvailable = false;
	@XmlTransient
	private boolean isNotRebuild = false;
	@XmlElement(name=RECORD_TAG_NAME)
	private Record record;
	@XmlElement(name=FORMS_TAG_NAME)
	private Forms forms;
	
	@SuppressWarnings("unused")
	private Module(){}
	
	public Module(String moduleId, String moduleName, String moduleNameEn, Db db, List<String> moduleRoleNames){
		setModuleId(moduleId);
		setModuleName(moduleName);
		setModuleNameEn(moduleNameEn);
		setDb(db);
		setModuleRoleNames(moduleRoleNames);		
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
		if (isEmpty(customToolBarButtons)){		
			customToolBarButtons = new ArrayList<ModuleButton>();
			if (!isEmpty(toolbarButtons))
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
			echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return detailFormButtons;
	}
	public static List<ModuleButton> getToolBarCustomButtonsForForm(List<ModuleButton> customButtons, FORM form) {
		if (isEmpty(customButtons)) return customButtons;
		
		List<ModuleButton> detailFormButtons = new ArrayList<ModuleButton>();
		try {
			for (ModuleButton customButton : customButtons){
				if (form.equals(customButton.getPlacedForm())){
					detailFormButtons.add(customButton);
				}
			}
		}
		catch(UnsupportedDataTypeException e){
			echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
		}
		return detailFormButtons;
	}
	public void setToolBarButtons(List<ModuleButton> toolbarButtons) {
		this.toolbarButtons = toolbarButtons;
	}
	public List<String> getModuleRoleNames() {
		return moduleRoles == null ? null : moduleRoles.getRoles();
	}
	public void setModuleRoleNames(List<String> moduleRoleNames) {
		this.moduleRoles = null;
		if (!isEmpty(moduleRoleNames)){
			this.moduleRoles = new Roles(moduleRoleNames);
		}
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
	public String getModuleSecurityRolesAsString(){
		String moduleRoleNames = new String();
		
		if (moduleRoles != null)
			for (String role : moduleRoles.getRoles()){
				moduleRoleNames += multipleConcat(isEmpty(moduleRoleNames) ? "" : ", ", role);
			}
		
		return moduleRoleNames;
	}	
	public boolean isStandardToolBar(){
		return toolbarButtons.size() == 0;
	}
	public String getFindParameterPrefix() {
		return !isEmpty(findParameterPrefix) ? findParameterPrefix : defaultParameterPrefix;
	}
	public void setFindParameterPrefix(String findParameterPrefix) {
		this.findParameterPrefix = findParameterPrefix;
	}
	public String getCreateParameterPrefix() {
		return !isEmpty(createParameterPrefix) ? createParameterPrefix : defaultParameterPrefix;
	}
	public void setCreateParameterPrefix(String createParameterPrefix) {
		this.createParameterPrefix = createParameterPrefix;
	}
	public String getUpdateParameterPrefix() {
		return !isEmpty(updateParameterPrefix) ? updateParameterPrefix : defaultParameterPrefix;
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
		if (!isEmpty(fieldLabelWidth)) {
			this.fieldLabelWidth = fieldLabelWidth;
		}
	}
	public Db getDb() {
		return db;
	}
	public void setDb(Db db) {
		this.db = db;
	}
	public Record getRecord() {
		return record;
	}
	public void setRecord(Record record) {
		this.record = record;
	}
	public Forms getForms(){
		return forms;
	}
	public void setForms(DetailForm detailForm, ListForm listForm){
		this.forms = new Forms(detailForm, listForm);
	}
}
