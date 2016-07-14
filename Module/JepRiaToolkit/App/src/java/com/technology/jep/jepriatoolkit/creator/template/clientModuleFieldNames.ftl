package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field;
 
import com.technology.jep.jepria.shared.field.JepRecordFieldNames;
 
public class ${form.formName}FieldNames extends JepRecordFieldNames {
  <#assign fieldDeclaration = "" separator = ";">
  <#list form.fields as field><#t>
  <#if !fieldDeclaration?contains(separator + field.fieldId + separator)>
  <#assign fieldDeclaration = fieldDeclaration + separator + field.fieldId + separator>
  public static final String ${field.fieldId?upper_case} = "${field.fieldId}";
  </#if>
  </#list>
  <#if form.mainFormParentKey??>
  <#if !fieldDeclaration?contains(separator+ form.mainFormParentKey + separator)>
  <#assign fieldDeclaration = fieldDeclaration + separator + form.mainFormParentKey + separator>
  public static final String ${form.mainFormParentKey?upper_case} = "${form.mainFormParentKey?lower_case}";
  </#if>
  </#if>
  <#if form.hasLobFields>
  <#assign MIME_TYPE_FIELD_ID = "MIME_TYPE" EXTENSION_FIELD_ID = "EXTENSION">
  <#if !fieldDeclaration?contains(separator + MIME_TYPE_FIELD_ID + separator)>
  <#assign fieldDeclaration = fieldDeclaration + separator + MIME_TYPE_FIELD_ID + separator>
  public static final String ${MIME_TYPE_FIELD_ID} = "${MIME_TYPE_FIELD_ID}";
  </#if>
  <#if !fieldDeclaration?contains(separator + EXTENSION_FIELD_ID + separator)>
  <#assign fieldDeclaration = fieldDeclaration + separator + EXTENSION_FIELD_ID + separator>
  public static final String ${EXTENSION_FIELD_ID} = "${EXTENSION_FIELD_ID}";
  </#if>
  </#if>
}
