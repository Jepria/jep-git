module.title=${moduleName}
<#list forms as form>
submodule.${form.formName?lower_case}.title=${form.formTitle}
</#list>