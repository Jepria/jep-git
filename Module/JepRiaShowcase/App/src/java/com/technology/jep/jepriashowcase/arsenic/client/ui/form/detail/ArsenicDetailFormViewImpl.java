package com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_DATE_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_INTEGER_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LONG_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_MONEY_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_AREA_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicImages;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicText;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_DATE_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_INTEGER_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LONG_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_MONEY_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_NUMBER_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_AREA_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_FIELD;

import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepLongField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
import com.technology.jep.jepria.client.widget.field.multistate.JepNumberField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextAreaField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class ArsenicDetailFormViewImpl extends StandardDetailFormViewImpl implements DetailFormView {	
 
	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			
			HorizontalPanel title = new HorizontalPanel();
			title.getElement().getStyle().setMarginTop(30, Unit.PX);
			title.getElement().getStyle().setMarginLeft(30, Unit.PX);
			title.getElement().getStyle().setMarginBottom(30, Unit.PX);
			Image image = new Image(arsenicImages.chemical());
			image.getElement().getStyle().setMarginRight(30, Unit.PX);
			title.add(image);
			title.add(new HTML(arsenicText.titleText()));
			put("", title);
			
			put(DETAILFORM_JEP_TEXT_FIELD, new JepTextField(ARSENIC_JEP_TEXT_FIELD_ID, arsenicText.detail_jepTextField()));
			put(DETAILFORM_JEP_TEXT_AREA_FIELD, new JepTextAreaField(ARSENIC_JEP_TEXT_AREA_FIELD_ID, arsenicText.detail_jepTextAreaField()));
			put(DETAILFORM_JEP_INTEGER_FIELD, new JepIntegerField(ARSENIC_JEP_INTEGER_FIELD_ID, arsenicText.detail_jepIntegerField()));
			put(DETAILFORM_JEP_LONG_FIELD, new JepLongField(ARSENIC_JEP_LONG_FIELD_ID, arsenicText.detail_jepLongField()));
			put(DETAILFORM_JEP_MONEY_FIELD, new JepMoneyField(ARSENIC_JEP_MONEY_FIELD_ID, arsenicText.detail_jepMoneyField()));
			put(DETAILFORM_JEP_NUMBER_FIELD, new JepNumberField(ARSENIC_JEP_NUMBER_FIELD_ID, arsenicText.detail_jepNumberField()));
			put(DETAILFORM_JEP_DATE_FIELD, new JepDateField(ARSENIC_JEP_DATE_FIELD_ID, arsenicText.detail_jepDateField()));
			put(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID, arsenicText.detail_jepComboBoxField_simple()) {{
				getElement().getStyle().setPaddingTop(30, Unit.PX);
				setLabelWidth(300);
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID, arsenicText.detail_jepComboBoxField_durable()) {{
				setLabelWidth(300);
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID, arsenicText.detail_jepComboBoxField_reloading()) {{
				setLabelWidth(300);
				setEmptyText(arsenicText.startTyping());
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID, arsenicText.detail_jepComboBoxField_3ch_reloading()) {{
				setLabelWidth(300);
				setEmptyText(arsenicText.startTyping3ch());
			}});
		}};
	}
 
}
