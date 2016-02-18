package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.ejb;
 
import javax.ejb.Remote;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.ejb.${form.formName};
 
@Remote
public interface ${form.formName}Remote extends ${form.formName} {
}
