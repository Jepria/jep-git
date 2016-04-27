package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {/*"fields", */"table", "primaryKey"})
@XmlRootElement(name=RECORD_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Record extends ModuleForm {

	@XmlAttribute(name=MODULE_PRIMARY_KEY_ATTRIBUTE)
	private String primaryKey;
	
	@XmlAttribute(name=MODULE_TABLE_NAME_ATTRIBUTE)
	private String table;
	
//	@XmlElement(name = FIELD_TAG_NAME)
//	private List<ModuleField> fields = null;
	
	@SuppressWarnings("unused")
	private Record(){}
	
	public Record(List<ModuleField> recordFields) {
		super(recordFields);
	}
	
//	public Record(String[] primaryKeyAndTableName){
//		int arrayLength = primaryKeyAndTableName.length;
//		if(arrayLength >= 1){
//			setPrimaryKey(primaryKeyAndTableName[0]);
//			if (arrayLength == 2){
//				setTable(primaryKeyAndTableName[1]);
//			}
//		}
//	}
	public void setPrimaryKeyAndTableName(String[] primaryKeyAndTableName){
		int arrayLength = primaryKeyAndTableName.length;
		if(arrayLength >= 1){
			setPrimaryKey(primaryKeyAndTableName[0]);
			if (arrayLength == 2){
				setTable(primaryKeyAndTableName[1]);
			}
		}
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
//	public List<ModuleField> getFields() {
//		return fields;
//	}
//
//	public void setFields(List<ModuleField> fields) {
//		this.fields = fields;
//	}
//	
//	public boolean contains(String fieldId){
//		for (ModuleField field : fields){
//			if (fieldId.equalsIgnoreCase(field.getFieldId())){
//				return true;
//			}
//		}
//		return false;
//	}
}
