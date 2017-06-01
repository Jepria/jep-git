package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.detail;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;
<#if !form.isDependent>
import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
</#if>
<#if form.isMain>
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.*;
</#if>
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;
<#assign hasCustomButtons = (form.toolBarCustomButtonsOnDetailForm?size != 0)>
<#if hasCustomButtons>
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.event.shared.EventBus;
</#if> 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
<#if hasCustomButtons>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.${form.formName}EventBus;
<#else>
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
</#if>
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
<#if form.hasOptionField>
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import java.util.List;
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}ServiceAsync;
<#list form.toolBarCustomButtonsOnDetailForm as button>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.event.${button.customEvent}Event;
</#list>

public class ${form.formName}DetailFormPresenter<E extends <#if hasCustomButtons>${form.formName}<#else>Plain</#if>EventBus, S extends ${form.formName}ServiceAsync> 
  extends DetailFormPresenter<${form.formName}DetailFormView, E, S, StandardClientFactory<E, S>><#if !hasCustomButtons> {</#if>
    <#if hasCustomButtons>implements <#list form.toolBarCustomButtonsOnDetailForm as button>${button.customEvent}Event.Handler<#if button_has_next>, </#if></#list> {</#if>
  
  public ${form.formName}DetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
    super(<#if form.isMain>scopeModuleIds, </#if>place, clientFactory);
  }  
  <#if form.presenterBody??>
  ${form.presenterBody}
  <#else>
  
  <#if form.hasOptionField>
  
  private S service = clientFactory.getService();
   </#if>
   <#if hasCustomButtons>
   
  @Override
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    super.start(container, eventBus);
    <#list form.toolBarCustomButtonsOnDetailForm as button>
    eventBus.addHandler(${button.customEvent}Event.TYPE, this);
    </#list>
  }
  </#if>
  
  <#if !form.hasOptionField>
  /* 
  </#if>
  public void bind() {
    super.bind();
    // TODO Здесь размещается код связывания presenter-а и view 
    
    <#if form.hasOptionField>
    <#list form.fields as field>
    <#if field.isOptionField>
    <#if field.isComboBoxField>
    fields.addFieldListener(${field.fieldId}, JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
      @Override
      public void handleEvent(final JepEvent event) {
        service.get${field.fieldIdAsParameter}(new FirstTimeUseAsyncCallback<List<JepOption>>(event) {
          @Override
          public void onSuccessLoad(List<JepOption> result){
            fields.setFieldOptions(${field.fieldId}, result);
          }
        });
      }
    });
    <#else>
    service.get${field.fieldIdAsParameter}(new JepAsyncCallback<List<JepOption>>() {
      @Override
      public void onSuccess(List<JepOption> result){
        fields.setFieldOptions(${field.fieldId}, result);
      }
    });
    </#if>
    </#if>
    </#list>
    </#if>
  }
  <#if !form.hasOptionField>
  */
  </#if>
 
  protected void adjustToWorkstate(WorkstateEnum workstate) {
    <#list form.fields as field>
    <#if field.visibility??>
    ${field.visibility}<#rt><#-- remove right line breaks -->
    </#if>
    </#list>
    <#list form.fields as field>
    <#if field.mandatory??>
    ${field.mandatory}<#rt><#-- remove right line breaks -->
    </#if>
    </#list>
    <#list form.fields as field>
    <#if field.editable??>
    ${field.editable}<#rt><#-- remove right line breaks -->
    </#if>
    </#list>
    <#list form.fields as field>
    <#if field.enabled??>
    ${field.enabled}<#rt><#-- remove right line breaks -->
    </#if>
    </#list>
    <#list form.fields as field>
    <#if field.height??>
    ${field.height}<#rt><#-- remove right line breaks -->
    </#if>
    </#list>
    <#if !form.isDependent>
    fields.setFieldVisible(MAX_ROW_COUNT, SEARCH.equals(workstate));
    fields.setFieldAllowBlank(MAX_ROW_COUNT, !SEARCH.equals(workstate));
    fields.setFieldValue(MAX_ROW_COUNT, 25);
    </#if>
  }
   <#list form.toolBarCustomButtonsOnDetailForm as button>
   
  public void on${button.customEvent}Event(${button.customEvent}Event event) { 
    //TODO: your business logic; 
  }
  </#list>
  </#if>
}
