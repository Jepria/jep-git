package com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail;
 
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_CHECKBOX_SWITCH_ALBL;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_CHECKBOX_SWITCH_EDTB;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_CHECKBOX_SWITCH_ENBL;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_CHECKBOX_SWITCH_VSBL;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_CHECKBOX_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_DATE_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_DUAL_LIST_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LIST_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.*;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LONG_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_MONEY_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_NUMBER_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_AREA_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD_CASC;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD_NODES;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.async.TypingTimeoutAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepCheckBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDualListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.arsenic.shared.service.ArsenicServiceAsync;

@SuppressWarnings("serial")
public class ArsenicDetailFormPresenter<E extends PlainEventBus, S extends ArsenicServiceAsync> 
    extends DetailFormPresenter<ArsenicDetailFormViewImpl, E, S, StandardClientFactory<E, S>> { 
 
  public ArsenicDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
    super(place, clientFactory);
  }

  private static final List<JepOption> shortOptList = new ArrayList<JepOption>() {{
    add(new JepOption("Option1", "1"));
    add(new JepOption("Option2", "2"));
    add(new JepOption("Option3", "3"));
    add(new JepOption("Option4", "4"));
  }};
  
  private static final List<JepOption> longOptList = new ArrayList<JepOption>() {{
    add(new JepOption("Option1", "1"));
    add(new JepOption("Option2", "2"));
    add(new JepOption("Option3", "3"));
    add(new JepOption("Option4", "4"));
    add(new JepOption("Option5", "5"));
    add(new JepOption("Option6", "6"));
    add(new JepOption("Option7", "7"));
    add(new JepOption("Option8", "8"));
    add(new JepOption("Option9", "9"));
  }};
  
  public void bind() {
    super.bind();
    // Здесь размещается код связывания presenter-а и view
    
    fields.addFieldListener(DETAILFORM_CHECKBOX_SWITCH_VSBL, JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @Override
      public void handleEvent(JepEvent event) {
        Boolean checked = ((JepCheckBoxField)event.getSource()).getValue();
        boolean b = checked != null && checked;
        
        fields.setFieldVisible(DETAILFORM_JEP_TEXT_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_TEXT_AREA_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_LONG_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_MONEY_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_NUMBER_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_DATE_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, b);
        fields.setFieldVisible(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, b);
        fields.setFieldVisible(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, b);
        fields.setFieldVisible(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, b);
        fields.setFieldVisible(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, b);
        fields.setFieldVisible(DETAILFORM_JEP_DUAL_LIST_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_CHECKBOX_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_LIST_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_LIST_FIELD_CHECKALL, b);
        fields.setFieldVisible(DETAILFORM_JEP_TREE_FIELD, b);
        fields.setFieldVisible(DETAILFORM_JEP_TREE_FIELD_NODES, b);
        fields.setFieldVisible(DETAILFORM_JEP_TREE_FIELD_CASC, b);
      }
    });
    
    fields.addFieldListener(DETAILFORM_CHECKBOX_SWITCH_ENBL, JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @Override
      public void handleEvent(JepEvent event) {
        Boolean checked = ((JepCheckBoxField)event.getSource()).getValue();
        boolean b = checked != null && checked;
        
        fields.setFieldEnabled(DETAILFORM_JEP_TEXT_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_TEXT_AREA_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_LONG_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_MONEY_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_NUMBER_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_DATE_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, b);
        fields.setFieldEnabled(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, b);
        fields.setFieldEnabled(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, b);
        fields.setFieldEnabled(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, b);
        fields.setFieldEnabled(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, b);
        fields.setFieldEnabled(DETAILFORM_JEP_DUAL_LIST_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_CHECKBOX_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_LIST_FIELD, b);
        fields.setFieldEnabled(DETAILFORM_JEP_LIST_FIELD_CHECKALL, b);
      }
    });
    
    fields.addFieldListener(DETAILFORM_CHECKBOX_SWITCH_EDTB, JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @Override
      public void handleEvent(JepEvent event) {
        Boolean checked = ((JepCheckBoxField)event.getSource()).getValue();
        boolean b = checked != null && checked;
        
        fields.setFieldEditable(DETAILFORM_JEP_TEXT_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_TEXT_AREA_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_LONG_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_MONEY_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_NUMBER_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_DATE_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, b);
        fields.setFieldEditable(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, b);
        fields.setFieldEditable(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, b);
        fields.setFieldEditable(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, b);
        fields.setFieldEditable(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, b);
        fields.setFieldEditable(DETAILFORM_JEP_DUAL_LIST_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_CHECKBOX_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_LIST_FIELD, b);
        fields.setFieldEditable(DETAILFORM_JEP_LIST_FIELD_CHECKALL, b);
      }
    });
    
    fields.addFieldListener(DETAILFORM_CHECKBOX_SWITCH_ALBL, JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @Override
      public void handleEvent(JepEvent event) {
        Boolean checked = ((JepCheckBoxField)event.getSource()).getValue();
        boolean b = checked != null && checked;
        
        fields.setFieldAllowBlank(DETAILFORM_JEP_TEXT_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_TEXT_AREA_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_LONG_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_MONEY_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_NUMBER_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_DATE_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_DUAL_LIST_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_LIST_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_LIST_FIELD_CHECKALL, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_TREE_FIELD, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_TREE_FIELD_NODES, b);
        fields.setFieldAllowBlank(DETAILFORM_JEP_TREE_FIELD_CASC, b);
      }
    });
    
    service.durableFetch(0, new JepAsyncCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        ((JepComboBoxField)fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY)).setOptions(new ArrayList<JepOption>(shortOptList));
      }
    });
    
    fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
      @Override
      public void handleEvent(final JepEvent event) {
        service.durableFetch(0, new FirstTimeUseAsyncCallback<Void>(event) {
          @Override
          public void onSuccessLoad(Void result) {
            ((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>(shortOptList));
          }
        });
        
      }
    });
    
    fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
      @Override
      public void handleEvent(final JepEvent event) {
        service.durableFetch(1500, new FirstTimeUseAsyncCallback<Void>(event) {
          @Override
          public void onSuccessLoad(Void result) {
            ((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>(shortOptList));          }
        });
        
      }
    });
    
    fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING).addListener(JepEventType.TYPING_TIMEOUT_EVENT, new JepListener() {
      @Override
      public void handleEvent(final JepEvent event) {
        final String s = ((JepComboBoxField)event.getSource()).getRawValue();
        // Длительность загрузки опций определяется длиной уже набранного текста
        service.durableFetch(s.length() * 100, new TypingTimeoutAsyncCallback<Void>(event) {
          @Override
          public void onSuccessLoad(Void result) {
            ((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>() {{
              if (s.matches("[a-z]*")) {
                add(new JepOption(s, s));
              }
              for (char c = 'a'; c <= 'z'; c++) {
                String k = s + Character.toString(c);
                add(new JepOption(k, k));
              }
            }});
          }
        });
        
      }
    });
    
    fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING).addListener(JepEventType.TYPING_TIMEOUT_EVENT, new JepListener() {
      @Override
      public void handleEvent(final JepEvent event) {
        final String s = ((JepComboBoxField)event.getSource()).getRawValue();
        service.durableFetch(0, new TypingTimeoutAsyncCallback<Void>(event) {
          @Override
          public void onSuccessLoad(Void result) {
            if (s.length() >= 3) {
              ((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>() {{
                if (s.matches("[a-z]*")) {
                  add(new JepOption(s, s));
                }
                for (char c = 'a'; c <= 'z'; c++) {
                  add(new JepOption(s + Character.toString(c), s + Character.toString(c)));
                }
              }});
            }
          }
        });
      }
    });
    
    service.durableFetch(0, new JepAsyncCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        ((JepDualListField)fields.get(DETAILFORM_JEP_DUAL_LIST_FIELD)).setOptions(new ArrayList<JepOption>(longOptList));        
        ((JepListField)fields.get(DETAILFORM_JEP_LIST_FIELD)).setOptions(new ArrayList<JepOption>(longOptList));
        ((JepListField)fields.get(DETAILFORM_JEP_LIST_FIELD_CHECKALL)).setOptions(new ArrayList<JepOption>(longOptList));      }
    });
    
    final DataLoader<JepOption> dataLoader = new DataLoader<JepOption>() {
        public void load(final Object loadConfig, final AsyncCallback<List<JepOption>> callback) {
            service.getTreeCatalog(JepOption.<Integer>getValue(loadConfig), new JepAsyncCallback<List<JepOption>>() {
              @Override
              public void onSuccess(List<JepOption> result) {
                callback.onSuccess(result);
              }
              @Override
              public void onFailure(Throwable th){
                callback.onFailure(th);
              }
            });
        }
    };
    
    ((JepTreeField) fields.get(DETAILFORM_JEP_TREE_FIELD)).setLoader(dataLoader);
    ((JepTreeField) fields.get(DETAILFORM_JEP_TREE_FIELD_NODES)).setLoader(dataLoader);
    ((JepTreeField) fields.get(DETAILFORM_JEP_TREE_FIELD_CASC)).setLoader(dataLoader);
  }
  
  @Override
  protected void adjustToWorkstate(WorkstateEnum workstate) {
    super.adjustToWorkstate(workstate);
    
    fields.setFieldValue(DETAILFORM_CHECKBOX_SWITCH_VSBL, true);
    fields.setFieldValue(DETAILFORM_CHECKBOX_SWITCH_ENBL, true);
    fields.setFieldValue(DETAILFORM_CHECKBOX_SWITCH_EDTB, true);
    fields.setFieldValue(DETAILFORM_CHECKBOX_SWITCH_ALBL, true);
    
    fields.setFieldVisible(MAX_ROW_COUNT, SEARCH.equals(workstate));
    if (SEARCH.equals(workstate)) {
      fields.setFieldValue(MAX_ROW_COUNT, 25);
      fields.setFieldAllowBlank(MAX_ROW_COUNT, false);
    }
    
  }
  
}