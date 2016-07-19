package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_DATASOURCE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ID_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_BUILD_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLE_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_STATUSBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TOOLBAR_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_EN_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PKG_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.RECORD_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.ArrayList;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.creator.module.adapter.BooleanAdapter;
import com.technology.jep.jepriatoolkit.log.Logger;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

// Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"moduleRoles", "db", "record", "forms", "toolbar", "childModules", "isNotRebuild", "isStatusbarOff", "isToolBarOff", "moduleNameEn", "moduleName", "moduleId"})
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
	private Boolean hasToolBarPresenter = null;
	@XmlTransient
	private Boolean hasToolBarView = null;
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
	@XmlAttribute(name=MODULE_BUILD_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean isNotRebuild = false;
	@XmlElement(name=RECORD_TAG_NAME)
	private Record record;
	@XmlElement(name=FORMS_TAG_NAME)
	private Forms forms;
	@XmlElement(name=MODULE_TAG_NAME)
	private List<Module> childModules;
	
	@SuppressWarnings("unused")
	private Module(){}
	
	public Module(String moduleId) {
		setModuleId(moduleId);
	}
	
	public Module(String moduleId, String moduleName, String moduleNameEn, Db db, List<String> moduleRoleNames){
		this(moduleId);
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
	public Boolean hasToolBarPresenter() {
		return hasToolBarPresenter;
	}
	public void setHasToolBarPresenter(Boolean hasToolBarPresenter) {
		this.hasToolBarPresenter = hasToolBarPresenter;
	}
	public Boolean hasToolBarView() {
		return hasToolBarView;
	}
	public void setHasToolBarView(Boolean hasToolBarView) {
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
	@XmlElement(name=TOOLBAR_TAG_NAME)
	public ToolBar getToolbar() {
		if (!toolbarButtons.isEmpty() || hasToolBarPresenter != null || hasToolBarView != null) {
			ToolBar toolBar = new ToolBar();
			toolBar.setButtons(toolbarButtons);
			toolBar.setPresenter(hasToolBarPresenter);
			toolBar.setView(hasToolBarView);
			return toolBar;
		}
		return null;
	}
	public List<Module> getChildModules() {
		return childModules;
	}
	public void setChildModules(List<Module> childModules) {
		this.childModules = childModules;
	}
	public void uptodate(Module newModule){
		List<ModuleField> fields = newModule.record != null ? newModule.record.getFields() : new ArrayList<ModuleField>();
		for (ModuleField field : fields){
			boolean exists = false;
			String fieldId = field.getFieldId();
			List<ModuleField> recordFields = record.getFields();
			for (ModuleField originField : recordFields){
				exists = fieldId.equalsIgnoreCase(originField.getFieldId()); 
				if (exists){
					break;
				}
			}
			if (!exists){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("The record field '", fieldId, "' was added to application structure!"));
				recordFields.add(field);
			}
		}
		fields = newModule.forms != null && newModule.forms.getListForm() != null ? newModule.forms.getListForm().getFields() : new ArrayList<ModuleField>();
		for (ModuleField field : fields){
			boolean exists = false;
			String fieldId = field.getFieldId();
			List<ModuleField> listFields = forms != null && forms.getListForm() != null ? forms.getListForm().getFields() : new ArrayList<ModuleField>();
			for (ModuleField originField : listFields){
				exists = fieldId.equalsIgnoreCase(originField.getFieldId()); 
				if (exists){
					break;
				}
			}
			if (!exists){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("The list form field '", fieldId, "' was added to application structure!"));
				listFields.add(field);
			}
		}
		fields = newModule.forms != null && newModule.forms.getDetailForm() != null ? newModule.forms.getDetailForm().getFields() : new ArrayList<ModuleField>();
		for (ModuleField field : fields){
			boolean exists = false;
			String fieldId = field.getFieldId();
			List<ModuleField> detailFields = forms != null && forms.getDetailForm() != null ? forms.getDetailForm().getFields() : new ArrayList<ModuleField>();
			for (ModuleField originField : detailFields){
				exists = fieldId.equalsIgnoreCase(originField.getFieldId()); 
				if (exists){
					break;
				}
			}
			if (!exists){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("The detail form field '", fieldId, "' was added to application structure!"));
				detailFields.add(field);
			}
		}
		
		List<ModuleField> currentFields = record == null ? new ArrayList<ModuleField>() : record.getFields();
		ModuleForm currentForm = newModule.getRecord();
		// check if all fields in xml presents in source code or maybe were deleted
		for (ModuleField originmodulefield : currentFields){
			String originModuleFieldId = originmodulefield.getFieldId();
			if (currentForm != null && !currentForm.contains(originModuleFieldId)){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("Pay attention that record field '", originModuleFieldId, "' has no source code! If you need, you can remove it manually!"));
			}
		}
		
		currentFields = forms != null && forms.getDetailForm() != null ? forms.getDetailForm().getFields() : new ArrayList<ModuleField>();
		currentForm = newModule != null && newModule.getForms() != null ? newModule.getForms().getDetailForm() : null;
		// check if all fields in xml presents in source code or maybe were deleted
		for (ModuleField originmodulefield : currentFields){
			String originModuleFieldId = originmodulefield.getFieldId();
			if (currentForm != null && !currentForm.contains(originModuleFieldId)){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("Pay attention that detail form field '", originModuleFieldId, "' has no source code! If you need, you can remove it manually!"));
			}
		}
		
		currentFields = forms != null && forms.getListForm() != null ? forms.getListForm().getFields() : new ArrayList<ModuleField>();
		currentForm = newModule != null && newModule.getForms() != null ? newModule.getForms().getListForm() : null;
		// check if all fields in xml presents in source code or maybe were deleted
		for (ModuleField originmodulefield : currentFields){
			String originModuleFieldId = originmodulefield.getFieldId();
			if (currentForm != null && !currentForm.contains(originModuleFieldId)){
				Logger.appendMessageToForm(newModule.getModuleId(), multipleConcat("Pay attention that list form field '", originModuleFieldId, "' has no source code! If you need, you can remove it manually!"));
			}
		}
	}
	
	/**
	 * Создание модуля с пустой списочной и детальной формами
	 * 
	 * @param moduleId			идентификатор модуля
	 * @param applicationName	наименование приложения
	 * @return пустой модуль
	 */
	public static Module createBlankModule(String moduleId, String applicationName){
		Module module = new Module(moduleId);
		module.setModuleName(NO_NAME);
		module.setModuleNameEn(NO_NAME);
		Db db = new Db(multipleConcat(PKG_PREFIX, applicationName.toLowerCase()), DEFAULT_DATASOURCE);
		module.setDb(db);
		return module;
	}
	
	@XmlElementDecl(name = JepRiaToolkitConstant.MODULE_TAG_NAME)
    public static JAXBElement<Module> createModule(String moduleName, String applicationName) {
        return new JAXBElement<Module>(new QName(null, JepRiaToolkitConstant.MODULE_TAG_NAME), Module.class, null, createBlankModule(moduleName, applicationName));
    }
}
