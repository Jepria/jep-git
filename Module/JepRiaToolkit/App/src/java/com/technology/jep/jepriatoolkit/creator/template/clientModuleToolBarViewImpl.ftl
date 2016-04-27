package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar;
 
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.*;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;
<#assign hasCustomButton = form.toolBarCustomButtons?size != 0>
<#if hasCustomButton>
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.${form.formName?uncap_first}Text;
import com.google.gwt.core.client.GWT;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.images.${form.formName}Images;
</#if>
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
 
public class ${form.formName}ToolBarViewImpl extends ToolBarViewImpl implements ${form.formName}ToolBarView {
 	<#if hasCustomButton>
 	
 	public static final ${form.formName}Images ${form.formName?uncap_first}Images = GWT.create(${form.formName}Images.class);
 	</#if>
 	
	public ${form.formName}ToolBarViewImpl() {
		super();
		
		<#if !form.hasToolBarView?? || form.toolBarButtons?size != 0>
		removeAll();
		</#if>
		<#if form.isToolBarOff>
		asWidget().setVisible(false);
		<#else>
		<#list form.toolBarButtons as button>
		
		<#if button.isSeparator>
		addSeparator(${button.buttonId?upper_case});
		<#else>
		addButton(
			${button.buttonId?upper_case}, 
			<#if button.getImageAsString(form.formName)??>
			${button.getImageAsString(form.formName)},
			</#if>
			${button.getTextAsString(form.formName)});
		</#if>
	 	</#list> 			
		</#if>
	}
}
