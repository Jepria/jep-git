package com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_ALBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_EDTB_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_ENBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_VSBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_CHECKBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_DATE_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_DUAL_LIST_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LIST_FIELD_CHECKALL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LIST_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LONG_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_MONEY_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_AREA_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TREE_FIELD_CASC_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TREE_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TREE_FIELD_NODES_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_MAX_ROW_COUNT_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicImages;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicText;
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
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LIST_FIELD_CHECKALL;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LONG_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_MONEY_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_NUMBER_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_AREA_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD_CASC;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TREE_FIELD_NODES;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepCheckBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDualListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepLongField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
import com.technology.jep.jepria.client.widget.field.multistate.JepNumberField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextAreaField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.client.widget.field.tree.TreeField.CheckCascade;
import com.technology.jep.jepria.client.widget.field.tree.TreeField.CheckNodes;

public class ArsenicDetailFormViewImpl extends StandardDetailFormViewImpl implements DetailFormView {  
 
  public ArsenicDetailFormViewImpl() {
    HorizontalPanel title = new HorizontalPanel();
    title.getElement().getStyle().setMarginTop(30, Unit.PX);
    title.getElement().getStyle().setMarginLeft(30, Unit.PX);
    title.getElement().getStyle().setMarginBottom(30, Unit.PX);
    Image image = new Image(arsenicImages.chemical());
    image.getElement().getStyle().setMarginRight(30, Unit.PX);
    title.add(image);
    title.add(new HTML(arsenicText.titleText()));
    panel.add(title);
    
    
    // group: checkboxes switching field states
    JepCheckBoxField checkBox_switchVsbl = new JepCheckBoxField(
        ARSENIC_CHECKBOX_SWITCH_VSBL_ID, arsenicText.detail_jepCheckBox_switch_vsbl()) {{
          setLabelWidth(250);
          setFieldWidth(50);
        }};
    JepCheckBoxField checkBox_switchEnbl = new JepCheckBoxField(
        ARSENIC_CHECKBOX_SWITCH_ENBL_ID, arsenicText.detail_jepCheckBox_switch_enbl()) {{
          setLabelWidth(250);
          setFieldWidth(50);
        }};
    JepCheckBoxField checkBox_switchEdtb = new JepCheckBoxField(
        ARSENIC_CHECKBOX_SWITCH_EDTB_ID, arsenicText.detail_jepCheckBox_switch_edtb()) {{
          setLabelWidth(250);
          setFieldWidth(50);
        }};
    JepCheckBoxField checkBox_switchAlbl = new JepCheckBoxField(
        ARSENIC_CHECKBOX_SWITCH_ALBL_ID, arsenicText.detail_jepCheckBox_switch_albl()) {{
          setLabelWidth(250);
          setFieldWidth(50);
        }};
    HorizontalPanel checkBoxPanel = new HorizontalPanel();
    checkBoxPanel.add(checkBox_switchVsbl);
    checkBoxPanel.add(checkBox_switchEnbl);
    checkBoxPanel.add(checkBox_switchEdtb);
    checkBoxPanel.add(checkBox_switchAlbl);
    panel.add(checkBoxPanel);
    
    
    // group: texts, nums, checkbox
    JepTextField textField = new JepTextField(ARSENIC_JEP_TEXT_FIELD_ID, arsenicText.detail_jepTextField()) {{
      setLabelWidth(200);
    }};
    JepTextAreaField textAreaField = new JepTextAreaField(ARSENIC_JEP_TEXT_AREA_FIELD_ID, arsenicText.detail_jepTextAreaField()) {{
      setLabelWidth(200);
    }};
    VerticalPanel textFieldPanel = new VerticalPanel();
    textFieldPanel.add(textField);
    textFieldPanel.add(textAreaField);
    
    JepLongField longField = new JepLongField(ARSENIC_JEP_LONG_FIELD_ID, arsenicText.detail_jepLongField()) {{
      setLabelWidth(200);
    }};
    JepMoneyField moneyField = new JepMoneyField(ARSENIC_JEP_MONEY_FIELD_ID, arsenicText.detail_jepMoneyField()) {{
      setLabelWidth(200);
    }};
    JepNumberField numberField = new JepNumberField(ARSENIC_JEP_NUMBER_FIELD_ID, arsenicText.detail_jepNumberField()) {{
      setLabelWidth(200);
    }};
    JepDateField dateField = new JepDateField(ARSENIC_JEP_DATE_FIELD_ID, arsenicText.detail_jepDateField()) {{
      setLabelWidth(200);
    }};
    VerticalPanel numFieldPanel = new VerticalPanel();
    numFieldPanel.add(longField);
    numFieldPanel.add(moneyField);
    numFieldPanel.add(numberField);
    numFieldPanel.add(dateField);
    
    JepCheckBoxField checkBoxField = new JepCheckBoxField(ARSENIC_JEP_CHECKBOX_FIELD_ID, arsenicText.detail_jepCheckBoxField()) {{
      setLabelWidth(200);
    }};
    
    HorizontalPanel textAndNumPanel = new HorizontalPanel();
    textAndNumPanel.getElement().getStyle().setPaddingTop(30, Unit.PX);
    textAndNumPanel.add(textFieldPanel);
    textAndNumPanel.add(numFieldPanel);
    textAndNumPanel.add(checkBoxField);
    panel.add(textAndNumPanel);
    
    
    // group: combos, dual
    JepComboBoxField comboBoxFieldNotLazy = new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID,
        createInfoIcon(arsenicText.detail_jepComboBoxField_notlazy_hint()) + new InlineHTML("&nbsp;") +
        arsenicText.detail_jepComboBoxField_notlazy()) {{
      setLabelWidth(300);
    }};
    JepComboBoxField comboBoxFieldSimple = new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID,
        createInfoIcon(arsenicText.detail_jepComboBoxField_simple_hint()) + new InlineHTML("&nbsp;") +
        arsenicText.detail_jepComboBoxField_simple()) {{
      setLabelWidth(300);
    }};
    JepComboBoxField comboBoxFieldDurable = new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID,
        createInfoIcon(arsenicText.detail_jepComboBoxField_durable_hint()) + new InlineHTML("&nbsp;") +
        arsenicText.detail_jepComboBoxField_durable()) {{
      setLabelWidth(300);
    }};
    JepComboBoxField comboBoxFieldReloading = new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID,
        createInfoIcon(arsenicText.detail_jepComboBoxField_reloading_hint()) + new InlineHTML("&nbsp;") +
        arsenicText.detail_jepComboBoxField_reloading()) {{
      setLabelWidth(300);
      setEmptyText(arsenicText.startTyping());
    }};
    JepComboBoxField comboBoxField3chReloading = new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID,
        createInfoIcon(arsenicText.detail_jepComboBoxField_reloading_hint()) + new InlineHTML("&nbsp;") + 
        arsenicText.detail_jepComboBoxField_3ch_reloading()) {{
      setLabelWidth(300);
      setEmptyText(arsenicText.startTyping3ch());
    }};
    VerticalPanel comboPanel = new VerticalPanel();
    comboPanel.add(comboBoxFieldNotLazy);
    comboPanel.add(comboBoxFieldSimple);
    comboPanel.add(comboBoxFieldDurable);
    comboPanel.add(comboBoxFieldReloading);
    comboPanel.add(comboBoxField3chReloading);
    
    JepDualListField dualListField = new JepDualListField(ARSENIC_JEP_DUAL_LIST_FIELD_ID, arsenicText.detail_jepDualListField()) {{
      getElement().getStyle().setPaddingTop(30, Unit.PX);
      setLabelWidth(200);
      setFieldWidth(420);
    }};
    HorizontalPanel comboAndDualPanel = new HorizontalPanel();
    comboAndDualPanel.getElement().getStyle().setPaddingTop(30, Unit.PX);
    comboAndDualPanel.add(comboPanel);
    comboAndDualPanel.add(dualListField);
    panel.add(comboAndDualPanel);
    
    
    // group: lists
    JepListField listField = new JepListField(ARSENIC_JEP_LIST_FIELD_ID, arsenicText.detail_jepListField()) {{
      setLabelWidth(200);
    }};
    JepListField listFieldCheckAll = new JepListField(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID, arsenicText.detail_jepListField_checkAll()) {{
      setSelectAllCheckBoxVisible(true);
      setLabelWidth(200);
    }};
    HorizontalPanel listFieldPanel = new HorizontalPanel();
    listFieldPanel.getElement().getStyle().setPaddingTop(30, Unit.PX);
    listFieldPanel.add(listField);
    listFieldPanel.add(listFieldCheckAll);
    panel.add(listFieldPanel);
    
    
    // group: trees
    JepTreeField treeField = new JepTreeField(ARSENIC_JEP_TREE_FIELD_ID, arsenicText.detail_jepTreeField()) {{
      setCheckNodes(CheckNodes.LEAF);
//      setSelectAllCheckBoxVisible(true);
      setLabelWidth(200);
    }};
    JepTreeField treeFieldNodes = new JepTreeField(ARSENIC_JEP_TREE_FIELD_NODES_ID, arsenicText.detail_jepTreeField_nodes()) {{
      setSelectAllCheckBoxVisible(true);
      setLabelWidth(200);
    }};
    JepTreeField treeFieldCasc = new JepTreeField(ARSENIC_JEP_TREE_FIELD_CASC_ID, arsenicText.detail_jepTreeField_casc()) {{
      setCheckStyle(CheckCascade.PARENTS);
      setSelectAllCheckBoxVisible(true);
      setLabelWidth(200);
    }};
    HorizontalPanel treeFieldPanel = new HorizontalPanel();
    treeFieldPanel.getElement().getStyle().setPaddingTop(30, Unit.PX);
    treeFieldPanel.add(treeField);
    treeFieldPanel.add(treeFieldNodes);
    treeFieldPanel.add(treeFieldCasc);
    panel.add(treeFieldPanel);
    
    
    
    JepIntegerField integerField = new JepIntegerField(ARSENIC_MAX_ROW_COUNT_ID, arsenicText.detail_maxRowCount()) {{
      setLabelWidth(200);
      getElement().getStyle().setPaddingTop(30, Unit.PX);
    }};
    panel.add(integerField);
    
    
    
    
    fields.put(DETAILFORM_CHECKBOX_SWITCH_VSBL, checkBox_switchVsbl);
    fields.put(DETAILFORM_CHECKBOX_SWITCH_ENBL, checkBox_switchEnbl);
    fields.put(DETAILFORM_CHECKBOX_SWITCH_EDTB, checkBox_switchEdtb);
    fields.put(DETAILFORM_CHECKBOX_SWITCH_ALBL, checkBox_switchAlbl);
    
    fields.put(DETAILFORM_JEP_TEXT_FIELD, textField);
    fields.put(DETAILFORM_JEP_TEXT_AREA_FIELD, textAreaField);
    fields.put(DETAILFORM_JEP_LONG_FIELD, longField);
    fields.put(DETAILFORM_JEP_MONEY_FIELD, moneyField);
    fields.put(DETAILFORM_JEP_NUMBER_FIELD, numberField);
    fields.put(DETAILFORM_JEP_DATE_FIELD, dateField);
    fields.put(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, comboBoxFieldNotLazy);
    fields.put(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, comboBoxFieldSimple);
    fields.put(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, comboBoxFieldDurable);
    fields.put(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, comboBoxFieldReloading);
    fields.put(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, comboBoxField3chReloading);
    fields.put(DETAILFORM_JEP_DUAL_LIST_FIELD, dualListField);
    fields.put(DETAILFORM_JEP_CHECKBOX_FIELD, checkBoxField);
    fields.put(DETAILFORM_JEP_LIST_FIELD, listField);
    fields.put(DETAILFORM_JEP_LIST_FIELD_CHECKALL, listFieldCheckAll);
    
    fields.put(DETAILFORM_JEP_TREE_FIELD, treeField);
    fields.put(DETAILFORM_JEP_TREE_FIELD_NODES, treeFieldNodes);
    fields.put(DETAILFORM_JEP_TREE_FIELD_CASC, treeFieldCasc);
    
    fields.put(MAX_ROW_COUNT, integerField);
  }
  
  private String createInfoIcon(String title){
    Anchor link = new Anchor();
    link.setTitle(title);
    //выравнивание ссылки по правому краю (на ссылки не распространяется действие text-align)
    //необходимо использовать атрибут float
    link.addStyleName("x-tool-expand-north");
    Image copyImage = new Image(JepImages.help());
    copyImage.getElement().getStyle().setMarginBottom(-4, Unit.PX);
    link.getElement().appendChild(copyImage.getElement());
    
    return link.toString();
  } 
}
