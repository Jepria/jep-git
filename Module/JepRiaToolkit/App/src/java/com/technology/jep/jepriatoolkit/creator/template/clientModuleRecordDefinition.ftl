package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
<#if form.hasLikeField>
import static com.technology.jep.jepria.shared.field.JepLikeEnum.*;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
</#if>
import com.technology.jep.jepria.shared.record.<#if form.hasLobFields>lob.</#if>Jep<#if form.hasLobFields>Lob</#if>RecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class ${form.formName}RecordDefinition extends Jep<#if form.hasLobFields>Lob</#if>RecordDefinition {
  
  public static final ${form.formName}RecordDefinition instance = new ${form.formName}RecordDefinition();
 
  private ${form.formName}RecordDefinition() {
    super(buildTypeMap()
      , new String[]{${form.primaryKey!''}}
      <#if form.hasLobFields>
      , "<#if form.table??>${form.table}<#else>table_name</#if>"
      , buildFieldMap()
      </#if>
    );
    <#if form.hasLikeField>
    super.setLikeMap(buildLikeMap());
    </#if>
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    <#assign MIME_TYPE_FIELD_ID = "MIME_TYPE" EXTENSION_FIELD_ID = "EXTENSION">
    <#assign likeContent = "" hasMimeType = false hasExtension = false>
    <#assign lobField = "">
    <#list form.fields as field><#t>
    typeMap.put(${field.fieldId?upper_case}, ${field.fieldType?upper_case});
    <#if field.isLOB><#assign lobField = field.fieldId></#if>
    <#if field.fieldLike??><#assign likeContent = likeContent + "\n\t\tlikeMap.put(" + field.fieldId?upper_case + ", " + field.fieldLike?upper_case + ");"></#if>
    <#if !hasMimeType && field.fieldId?upper_case == MIME_TYPE_FIELD_ID><#assign hasMimeType=true></#if>
    <#if !hasExtension && field.fieldId?upper_case == EXTENSION_FIELD_ID><#assign hasExtension=true></#if>
    </#list>
    <#if form.hasLobFields>
    <#if !hasMimeType>
    typeMap.put(${MIME_TYPE_FIELD_ID}, STRING);
    </#if>
    <#if !hasExtension>
    typeMap.put(${EXTENSION_FIELD_ID}, STRING);
    </#if>
    </#if>
    return typeMap;
  }
   <#if form.hasLikeField>
   
  private static Map<String, JepLikeEnum> buildLikeMap() {
    Map<String, JepLikeEnum> likeMap = new HashMap<String, JepLikeEnum>();${likeContent}
    return likeMap;
  }
  </#if>  
  <#if form.hasLobFields>
  
  private static Map<String, String> buildFieldMap() {
    Map<String, String> fieldMap = new HashMap<String, String>();
    fieldMap.put(${form.primaryKey}, ${form.primaryKey});
    fieldMap.put(${lobField}, ${lobField});
    return fieldMap;
  }
  </#if>
}
