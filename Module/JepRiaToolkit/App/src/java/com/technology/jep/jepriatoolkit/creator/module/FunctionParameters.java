package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_PARAMETERS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DATABASE_PREFIX_ATTRIBUTE_NAME;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"parameters", "prefix"})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FunctionParameters {

  @XmlAttribute(name = DATABASE_PARAMETERS_TAG_NAME, required = true)
  private String parameters;
  
  @XmlAttribute(name = DATABASE_PREFIX_ATTRIBUTE_NAME)
  private String prefix;
  
  @SuppressWarnings("unused")
  private FunctionParameters(){}
  
  public FunctionParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
}
