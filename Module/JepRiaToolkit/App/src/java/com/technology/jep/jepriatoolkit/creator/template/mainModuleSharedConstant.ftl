package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.shared;
 
import com.technology.jep.jepria.shared.JepRiaConstant;
 
public class ${moduleName}Constant extends JepRiaConstant  {

    <#list roleConstants as constantName>
      <#assign i = constantName?index>
  public static final String ROLE_${constantName?upper_case} = "${securityRoles[i]}";
    </#list> 
    
}
