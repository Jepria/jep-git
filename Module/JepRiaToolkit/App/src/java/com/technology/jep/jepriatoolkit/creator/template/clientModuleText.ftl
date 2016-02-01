${form.formName?uncap_first}.title=${form.formTitle}
<#list form.fields as field>
${form.formName?uncap_first}.list.${field.fieldId?lower_case}=${field.fieldListFormName}
${form.formName?uncap_first}.detail.${field.fieldId?lower_case}=${field.fieldDetailFormName}
</#list>
<#list form.toolBarCustomButtons as button>
${button.text}=${button.name}
</#list>
${form.formName?uncap_first}.detail.row_count=Количество записей