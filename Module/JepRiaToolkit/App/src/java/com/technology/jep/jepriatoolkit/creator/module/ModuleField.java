package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepria.shared.field.JepTypeEnum.BIGDECIMAL;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BINARY_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BOOLEAN;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.CLOB;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE_TIME;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TEXT_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TIME;
import static com.technology.jep.jepriatoolkit.creator.ApplicationStructureCreator.FIELD_WIDGET;

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ModuleField implements JepRiaToolkitConstant {
	
	private String moduleId;
	private String fieldId;
	private String fieldType;
	private String fieldListFormName;
	private String fieldListFormNameEn;
	private String fieldDetailFormName;
	private String fieldDetailFormNameEn;
	private String fieldLike;
	private String fieldWidget;
	private String fieldMaxLength;
	private String fieldWidth;
	private String columnWidth;
	private String fieldHeight;
	private String labelWidth;
	private Integer listFormIndex;
	private Integer detailFormIndex;
	private boolean isEditable = true;
	private boolean isPrimaryKey = false;
	private boolean isListFormField = false;
	private boolean isDetailFormField = false;	
	private boolean isFindParameter = false;	
	private boolean isCreateParameter = false;	
	private boolean isUpdateParameter = false;
	private boolean isDeleteParameter = false;
	private boolean isGroupFormField = false;	
	private List<WorkstateEnum> visibleWorkStates;
	private List<WorkstateEnum> mandatoryWorkStates;
	private List<WorkstateEnum> editableWorkStates;
	private List<WorkstateEnum> enableWorkStates;
		
	public ModuleField(String moduleId, String fieldId, String fieldType, String fieldName, String fieldNameEn, String fieldLike, String fieldWidget, String fieldMaxLength, String fieldWidth, String labelWidth, String fieldHeight, String visibleWorkstates, String mandatoryWorkstates, String editableWorkstates, String enableWorkstates){
		setModuleId(moduleId);
		setFieldId(fieldId);
		setFieldType(fieldType);
		setFieldListFormName(fieldName);
		setFieldListFormNameEn(fieldNameEn);
		setFieldDetailFormName(fieldName);
		setFieldDetailFormNameEn(fieldNameEn);
		setFieldLike(fieldLike);
		setFieldWidget(fieldWidget);
		setFieldMaxLength(fieldMaxLength);
		setFieldWidth(fieldWidth);
		setColumnWidth(fieldWidth);
		setLabelWidth(labelWidth);
		setFieldHeight(fieldHeight);
		setVisibleWorkStates(visibleWorkstates);
		setMandatoryWorkStates(mandatoryWorkstates);
		setEditableWorkStates(editableWorkstates);
		setEnableWorkStates(enableWorkstates);
	}	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldIdAsParameter() {
		return JepRiaToolkitUtil.initCap(JepRiaToolkitUtil.getFieldIdAsParameter(
				JepRiaToolkitUtil.subtractFieldSuffix(fieldId), null));
	}
	public String getFieldName() {
		return JepRiaToolkitUtil.multipleConcat(JepRiaToolkitUtil.getFieldIdAsParameter(fieldId, null),
				fieldWidget.substring(JEP_FIELD_PREFIX.length()));
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldListFormName() {
		return fieldListFormName;
	}
	public void setFieldListFormName(String fieldListFormName) {
		this.fieldListFormName = fieldListFormName;
	}
	public String getFieldListFormNameEn() {
		return fieldListFormNameEn;
	}
	public void setFieldListFormNameEn(String fieldListFormNameEn) {
		this.fieldListFormNameEn = fieldListFormNameEn;
	}
	public String getFieldDetailFormName() {
		return fieldDetailFormName;
	}
	public void setFieldDetailFormName(String fieldDetailFormName) {
		this.fieldDetailFormName = fieldDetailFormName;
	}
	public String getFieldDetailFormNameEn() {
		return fieldDetailFormNameEn;
	}
	public void setFieldDetailFormNameEn(String fieldDetailFormNameEn) {
		this.fieldDetailFormNameEn = fieldDetailFormNameEn;
	}
	public String getFieldLike() {
		return fieldLike;
	}
	public void setFieldLike(String fieldLike) {
		this.fieldLike = fieldLike;
	}
	public String getFieldWidget() {
		return fieldWidget;
	}
	public void setFieldWidget(String fieldWidget) {
		this.fieldWidget = fieldWidget;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isListFormField() {
		return isListFormField;
	}
	public void setListFormField(boolean isListFormField) {
		this.isListFormField = isListFormField;
	}
	public boolean getIsDetailFormField() {
		return isDetailFormField;
	}
	public void setDetailFormField(boolean isDetailFormField) {
		this.isDetailFormField = isDetailFormField;
	}
	public boolean isFindParameter() {
		return isFindParameter;
	}
	public void setFindParameter(boolean isFindParameter) {
		this.isFindParameter = isFindParameter;
	}
	public boolean isCreateParameter() {
		return isCreateParameter;
	}
	public void setCreateParameter(boolean isCreateParameter) {
		this.isCreateParameter = isCreateParameter;
	}
	public boolean isUpdateParameter() {
		return isUpdateParameter;
	}
	public void setUpdateParameter(boolean isUpdateParameter) {
		this.isUpdateParameter = isUpdateParameter;
	}
	public boolean isDeleteParameter() {
		return isDeleteParameter;
	}
	public void setDeleteParameter(boolean isDeleteParameter) {
		this.isDeleteParameter = isDeleteParameter;
	}
	public List<WorkstateEnum> getVisibleWorkStates() {
		return visibleWorkStates;
	}	
	public void setVisibleWorkStates(String workstates) {
		visibleWorkStates = JepRiaToolkitUtil.getWorkStates(workstates);
	}
	public List<WorkstateEnum> getMandatoryWorkStates() {
		return mandatoryWorkStates;
	}	
	public void setMandatoryWorkStates(String workstates) {
		mandatoryWorkStates = JepRiaToolkitUtil.getWorkStates(workstates);
	}	
	public List<WorkstateEnum> getEditableWorkStates() {
		return editableWorkStates;
	}
	public void setEditableWorkStates(String workstates) {
		List<WorkstateEnum> editWorkStates = JepRiaToolkitUtil.getWorkStates(workstates);
		if (!JepRiaToolkitUtil.isEmpty(editWorkStates)){
			editableWorkStates = new ArrayList<WorkstateEnum>();
			for (WorkstateEnum workstate : editWorkStates){
				if (WorkstateEnum.isEditableState(workstate)){
					editableWorkStates.add(workstate);
				}
			}
		} 
	}	
	public List<WorkstateEnum> getEnableWorkStates() {
		return enableWorkStates;
	}
	public void setEnableWorkStates(String workstates) {
		List<WorkstateEnum> enabWorkStates = JepRiaToolkitUtil.getWorkStates(workstates);
		if (!JepRiaToolkitUtil.isEmpty(enabWorkStates)){
			enableWorkStates = new ArrayList<WorkstateEnum>();
			for (WorkstateEnum workstate : enabWorkStates){
				if (JepRiaToolkitUtil.isEditFormState(workstate)){
					enableWorkStates.add(workstate);
				}
			}
		}
	}
	public boolean isGroupFormField() {
		return isGroupFormField;
	}
	public void setGroupFormField(boolean isGroupFormField) {
		this.isGroupFormField = isGroupFormField;
	}
	public String getFieldMaxLength() {
		return fieldMaxLength;
	}
	public void setFieldMaxLength(String fieldMaxLength) {
		if (!JepRiaToolkitUtil.isEmpty(fieldMaxLength)) {
			this.fieldMaxLength = fieldMaxLength;
		}
	}
	public String getFieldWidth() {
		return fieldWidth;
	}
	public void setFieldWidth(String fieldWidth) {
		if (!JepRiaToolkitUtil.isEmpty(fieldWidth)) {
			this.fieldWidth = fieldWidth;
		}
	}
	public String getColumnWidth() {
		return JepRiaToolkitUtil.isEmpty(columnWidth) ? Integer.toString(150) : columnWidth;
	}
	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}
	public String getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(String labelWidth) {
		if (!JepRiaToolkitUtil.isEmpty(labelWidth)) {
			this.labelWidth = labelWidth;
		}
	}
	public boolean isCLOB(){
		return (JepRiaToolkitUtil.isEmpty(fieldType) ? 
					(JepRiaToolkitUtil.isEmpty(fieldWidget) ? false :
						JEP_FILE_FIELD.equalsIgnoreCase(fieldWidget)) : 
							(TEXT_FILE.name().equalsIgnoreCase(fieldType) ||
									CLOB.name().equalsIgnoreCase(fieldType)));
	}
	public boolean isBLOB(){
		return (JepRiaToolkitUtil.isEmpty(fieldType) ? 
					(JepRiaToolkitUtil.isEmpty(fieldWidget) ? false : 
						(JEP_FILE_FIELD.equalsIgnoreCase(fieldWidget) ||
							JEP_IMAGE_FIELD.equalsIgnoreCase(fieldWidget))) :
					BINARY_FILE.name().equalsIgnoreCase(fieldType));
	}
	public boolean isLOB(){
		return isCLOB() || isBLOB();
	}
	public boolean getIsComboBoxField(){
		return JEP_COMBOBOX_FIELD.equalsIgnoreCase(fieldWidget);
	}
	public boolean getIsOptionField(){
		return getIsComboBoxField()
				|| JEP_LIST_FIELD.equalsIgnoreCase(fieldWidget)
				|| JEP_DUAL_LIST_FIELD.equalsIgnoreCase(fieldWidget)
				|| JEP_TREE_FIELD.equalsIgnoreCase(fieldWidget);
	}
	public String getFieldHeight() {
		return fieldHeight;
	}
	public void setFieldHeight(String fieldHeight) {
		this.fieldHeight = fieldHeight;
	}
	public Integer getListFormIndex() {
		return listFormIndex;
	}
	public void setListFormIndex(Integer listFormIndex) {
		this.listFormIndex = listFormIndex;
	}
	public Integer getDetailFormIndex() {
		return detailFormIndex;
	}
	public void setDetailFormIndex(Integer detailFormIndex) {
		this.detailFormIndex = detailFormIndex;
	}
	public Integer getFormIndex(boolean isDetailForm){
		return isDetailForm ? 
				(getIsDetailFormField() ? getDetailFormIndex() : null) : 
				(isListFormField() ? getListFormIndex() : null);
	}
	public boolean getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	private static final String orWithDoubleWhiteSpace = JepRiaToolkitUtil.multipleConcat(WHITE_SPACE, OR, WHITE_SPACE);
	public String getVisibility(){
		if (JepRiaToolkitUtil.isEmpty(visibleWorkStates)) return null;
		String visibleWorkstateOr = new String();
		for (WorkstateEnum state : visibleWorkStates) {
			visibleWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(visibleWorkstateOr) ? "" : orWithDoubleWhiteSpace),
					JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
		}
		return JepRiaToolkitUtil.multipleConcat("fields.setFieldVisible(", fieldId, ", ", visibleWorkstateOr, ");", END_OF_LINE);
	}
	public String getMandatory(){
		if (JepRiaToolkitUtil.isEmpty(mandatoryWorkStates)) return null;
		String mandatoryWorkstateOr = new String();
		for (WorkstateEnum state : mandatoryWorkStates) {
			mandatoryWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(mandatoryWorkstateOr) ? "" : orWithDoubleWhiteSpace),
					JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
		}
		return JepRiaToolkitUtil.multipleConcat("fields.setFieldAllowBlank(", fieldId, ", !",
				(mandatoryWorkstateOr.contains(orWithDoubleWhiteSpace) ? "(" : ""), mandatoryWorkstateOr,
				(mandatoryWorkstateOr.contains(orWithDoubleWhiteSpace) ? ")" : ""), ");", END_OF_LINE);
	}
	public String getEditable(){
		if (getIsDetailFormField() && !getIsEditable()) {
			return JepRiaToolkitUtil.multipleConcat("fields.setFieldEditable(", fieldId, ", false);", END_OF_LINE);
		}
		else {
			if (JepRiaToolkitUtil.isEmpty(editableWorkStates)) return null;
			String editableWorkstateOr = new String();
			for (WorkstateEnum state : editableWorkStates){
				editableWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(editableWorkstateOr) ? "" : orWithDoubleWhiteSpace),
						JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
			}
			return JepRiaToolkitUtil.multipleConcat("fields.setFieldEditable(", fieldId, ", ", editableWorkstateOr, ");", END_OF_LINE);
		}
	}
	public String getEnabled(){
		if (JepRiaToolkitUtil.isEmpty(enableWorkStates)) return null;
		String enableWorkstateOr = new String();
		if (!JepRiaToolkitUtil.isEmpty(enableWorkStates)) {
			for (WorkstateEnum state : enableWorkStates) {
				enableWorkstateOr += JepRiaToolkitUtil.multipleConcat((JepRiaToolkitUtil.isEmpty(enableWorkstateOr) ? "" : orWithDoubleWhiteSpace),
						JepRiaToolkitUtil.getWorkStateAsString(state), ".equals(workstate)");
			}
			return JepRiaToolkitUtil.multipleConcat("fields.setFieldEnabled(", fieldId, ", ", enableWorkstateOr, ");",  END_OF_LINE);
		}
		return null;
	}
	public String getHeight(){
		if (!JepRiaToolkitUtil.isEmpty(fieldHeight)) {
			return JepRiaToolkitUtil.multipleConcat("fields.setFieldHeight(", fieldId, ", ", fieldHeight, ");", END_OF_LINE);
		}
		return null;
	}
	public String getImportString(){
		return FIELD_WIDGET.get(fieldWidget);
	}
	public boolean getIsBigDecimalNumberField(){
		return JEP_NUMBER_FIELD.equalsIgnoreCase(fieldWidget) && BIGDECIMAL.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsMaskedTextField(){
		return JEP_MASKED_TEXT_FIELD.equalsIgnoreCase(fieldWidget);
	}
	public boolean getIsIntegerType(){
		return INTEGER.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsBigDecimalType(){
		return BIGDECIMAL.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsBooleanType(){
		return BOOLEAN.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsDateType(){
		return DATE.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsTimeType(){
		return TIME.name().equalsIgnoreCase(fieldType);
	}
	public boolean getIsDateTimeType(){
		return DATE_TIME.name().equalsIgnoreCase(fieldType);
	}
	public String getDisplayValueForComboBox(){
		return JepRiaToolkitUtil.getDisplayValueForComboBox(fieldId);
	}
}