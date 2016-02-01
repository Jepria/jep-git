${form.formName?uncap_first}.title=${form.formTitleEn}
<#list form.fields as field>
${form.formName?uncap_first}.list.${field.fieldId?lower_case}=${field.fieldListFormNameEn}
${form.formName?uncap_first}.detail.${field.fieldId?lower_case}=${field.fieldDetailFormNameEn}
</#list>
<#list form.toolBarCustomButtons as button>
${button.text}=${button.nameEn}
</#list>
${form.formName?uncap_first}.detail.row_count=Row Count