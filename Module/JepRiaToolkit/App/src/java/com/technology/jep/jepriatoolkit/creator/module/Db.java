package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"find", "create", "update", "datasource", "packageName", "prefix"})
@XmlRootElement(name = DATABASE_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Db {

	@XmlAttribute(name = DB_PACKAGE_ATTRIBUTE)
	private String packageName;
	@XmlAttribute(name = MODULE_DATASOURCE_ATTRIBUTE)
	private String datasource;
	@XmlAttribute(name = DATABASE_PREFIX_ATTRIBUTE_NAME)
	private String prefix;
	@XmlElement
	private FunctionParameters find;
	@XmlElement
	private FunctionParameters create;
	@XmlElement
	private FunctionParameters update;
	
	public Db(){}
	
	public Db(String pack, String ds){
		setPackageName(pack);
		setDatasource(ds);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public FunctionParameters getFind() {
		return find;
	}

	public void setFind(FunctionParameters find) {
		this.find = find;
	}

	public FunctionParameters getCreate() {
		return create;
	}

	public void setCreate(FunctionParameters create) {
		this.create = create;
	}

	public FunctionParameters getUpdate() {
		return update;
	}

	public void setUpdate(FunctionParameters update) {
		this.update = update;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
