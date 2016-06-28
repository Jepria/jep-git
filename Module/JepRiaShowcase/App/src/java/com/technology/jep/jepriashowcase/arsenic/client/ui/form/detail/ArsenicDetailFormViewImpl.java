package com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.*;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicImages;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicText;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.*;

import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
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

public class ArsenicDetailFormViewImpl extends StandardDetailFormViewImpl implements DetailFormView {	
 
	@SuppressWarnings("serial")
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
			
			put(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID,
							createInfoIcon(arsenicText.detail_jepComboBoxField_notlazy_hint()) + new InlineHTML("&nbsp;") +
							arsenicText.detail_jepComboBoxField_notlazy()) {{
						getElement().getStyle().setPaddingTop(30, Unit.PX);
						setLabelWidth(220);
					}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID,
							createInfoIcon(arsenicText.detail_jepComboBoxField_simple_hint()) + new InlineHTML("&nbsp;") +
							arsenicText.detail_jepComboBoxField_simple()) {{
						setLabelWidth(220);
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID,
							createInfoIcon(arsenicText.detail_jepComboBoxField_durable_hint()) + new InlineHTML("&nbsp;") +
							arsenicText.detail_jepComboBoxField_durable()) {{
						setLabelWidth(220);
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID,
							createInfoIcon(arsenicText.detail_jepComboBoxField_reloading_hint()) + new InlineHTML("&nbsp;") +
							arsenicText.detail_jepComboBoxField_reloading()) {{
						setLabelWidth(220);
						setEmptyText(arsenicText.startTyping());
			}});
			put(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, 
					new JepComboBoxField(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID,
							createInfoIcon(arsenicText.detail_jepComboBoxField_reloading_hint()) + new InlineHTML("&nbsp;") + 
							arsenicText.detail_jepComboBoxField_3ch_reloading()) {{
						setLabelWidth(220);
						setEmptyText(arsenicText.startTyping3ch());
			}});
			
			put(DETAILFORM_JEP_DUAL_LIST_FIELD, new JepDualListField(ARSENIC_JEP_DUAL_LIST_FIELD_ID, arsenicText.detail_jepDualListField()) {{
				getElement().getStyle().setPaddingTop(30, Unit.PX);
				setFieldWidth(420);
			}});
			
			put(DETAILFORM_JEP_CHECKBOX_FIELD, new JepCheckBoxField(ARSENIC_JEP_CHECKBOX_FIELD_ID, arsenicText.detail_jepCheckBoxField()));
			put(DETAILFORM_JEP_LIST_FIELD, new JepListField(ARSENIC_JEP_LIST_FIELD_ID, arsenicText.detail_jepListField()));
		}};
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
