package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao;
 
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;

import com.technology.jep.jepria.server.dao.JepDao;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName};
import com.technology.jep.jepria.server.dao.ResultSetMapper;
<#if form.hasOptionField>
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if>
<#assign hasBooleanType=false hasFileNameField=false hasViewDetailsWS=false hasMainParentKey=false>
<#list form.toolBarButtons as button>
<#if button.buttonId == "VIEW_DETAILS_BUTTON_ID">
<#assign hasViewDetailsWS=true>
</#if>
</#list>
<#list form.fields as field><#t>
<#if field.isOptionField>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${field.fieldIdAsParameter}Options;
</#if>
<#if !hasBooleanType && field.isBooleanType>
<#assign hasBooleanType=true>
import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;
import java.util.ResourceBundle;
import java.util.ArrayList;
</#if>
<#if field.fieldId == "FILE_NAME">
<#assign hasFileNameField=true>
</#if>
<#if form.mainFormParentKey?? && field.fieldId == form.mainFormParentKey>
<#assign hasMainParentKey=true>
</#if>
</#list>
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ${form.formName}Dao extends JepDao implements ${form.formName} {

  @Override
  public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin  " 
        +  "? := ${form.dbPackage}.find${form.formName}(" 
            <#assign first = true hasParentKey = hasMainParentKey>
            <#list form.fields as field><#t>
          <#if field.isFindParameter>
          + "<#if first><#assign first = false><#else>, </#if>${field.getFieldIdWithPrefix(form.findParameterPrefix)} => ?"
          </#if>
          <#if form.mainFormParentKey?? && !hasParentKey>
          <#assign hasParentKey = true>
          // + "<#if first><#assign first = false><#else>, </#if>${field.getFieldIdWithPrefix(form.mainFormParentKey, form.findParameterPrefix)} => ?"
          </#if>
          </#list> 
          + ", maxRowCount => ? " 
          + ", operatorId => ? " 
        + ");"
     + " end;";
    return super.find(sqlQuery,
        new ResultSetMapper<JepRecord>() {
          public void map(ResultSet rs, JepRecord record) throws SQLException {
            <#list form.fields as field><#t>
            <#if field.isListFormField || field.isPrimaryKey || field.getEnabledOnViewDetails(hasViewDetailsWS) || field.getIsKeyOption(form.optionFields, hasViewDetailsWS)>
            record.set(${field.fieldId?upper_case}, ${field.getResultSet(hasFileNameField, form.primaryKey)});
            </#if>
            </#list>
          }
        }
        <#list form.fields as field><#t>
        <#if field.isFindParameter>
        , ${field.getParameter(true)}
        </#if>
        </#list> 
        <#if form.mainFormParentKey?? && !hasMainParentKey>
        // , templateRecord.get(${form.mainFormParentKey?upper_case})
        </#if>
        , maxRowCount 
        , operatorId);
  }

  @Override
  public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "${form.dbPackage}.delete${form.formName}(" 
            <#assign first = true>
            <#list form.fields as field><#t>
          <#if field.isDeleteParameter>
          + "<#if first><#assign first = false><#else>, </#if>${field.getFieldIdWithPrefix(form.defaultParameterPrefix)} => ?"
          </#if>
          </#list>  
          + ", operatorId => ? " 
        + ");"
      + "end;";
    super.delete(sqlQuery 
        <#list form.fields as field><#t>
        <#if field.isDeleteParameter>
        , ${field.getParameter(false)}
        </#if>
        </#list>
        , operatorId);
  }

  @Override
  public void update(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
      +  "${form.dbPackage}.update${form.formName}(" 
            <#assign first = true>
            <#list form.fields as field><#t>
          <#if field.isUpdateParameter>
          + "<#if first><#assign first = false><#else>, </#if>${field.getFieldIdWithPrefix(form.updateParameterPrefix)} => ?"
          </#if>
          </#list>   
          + ", operatorId => ? " 
      + ");"
     + "end;";
    super.update(sqlQuery 
        <#list form.fields as field><#t>
        <#if field.isUpdateParameter>
        , ${field.getParameter(false)}
        </#if>
        </#list>
        , operatorId);
  }

  @Override
  public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "? := ${form.dbPackage}.create${form.formName}(" 
            <#assign first = true>
            <#list form.fields as field><#t>
          <#if field.isCreateParameter>
          + "<#if first><#assign first = false><#else>, </#if>${field.getFieldIdWithPrefix(form.createParameterPrefix)} => ?"
          </#if>
          </#list>    
          + ", operatorId => ? " 
        + ");"
      + "end;";
    return super.create(sqlQuery,
        Integer.class 
        <#list form.fields as field><#t>
        <#if field.isCreateParameter>
        , ${field.getParameter(false)}
        </#if>
        </#list>
        , operatorId);
  }
   <#list form.fields as field><#t>
  <#if field.isOptionField>
  
  public List<JepOption> get${field.fieldIdAsParameter}() throws ApplicationException {
    <#if field.isBooleanType>
    ResourceBundle resource = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
    List<JepOption> result = new ArrayList<JepOption>();
    
    JepOption option = new JepOption();
    option.set(${field.fieldIdAsParameter}Options.${field.fieldId}, 0);
    option.set(${field.fieldIdAsParameter}Options.${field.displayValueForComboBox}, resource.getString("no"));
    result.add(option);
    
    option = new JepOption();
    option.set(${field.fieldIdAsParameter}Options.${field.fieldId}, 1);
    option.set(${field.fieldIdAsParameter}Options.${field.displayValueForComboBox}, resource.getString("yes"));
    result.add(option);
    
    return result;
    <#else>
    String sqlQuery = 
      " begin " 
      + " ? := ${form.dbPackage}.get${field.fieldIdAsParameter};" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(${field.option});
            dto.setName(rs.getString(${field.fieldIdAsParameter}Options.${field.displayValueForComboBox}));
          }
        }
    );
    </#if>    
  }
  </#if>
  </#list>
}
