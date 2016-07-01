package com.technology.jep.jepriashowcase.arsenic.client.ui.form.list;
 
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepriashowcase.feature.client.FeatureClientConstant.featureText;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;
 
public class ArsenicListFormViewImpl extends StandardListFormViewImpl {
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
	private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
 
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	@Override
	protected List<JepColumn> getColumnConfigurations() {
		return new ArrayList<JepColumn>() {{
//			add(new JepColumn(FEATURE_ID, featureText.feature_list_feature_id(), 150, new NumberCell(defaultNumberFormatter)));
//			add(new JepColumn(FEATURE_NAME, featureText.feature_list_feature_name(), 150));
//			add(new JepColumn(FEATURE_NAME_EN, featureText.feature_list_feature_name_en(), 150));
//			add(new JepColumn(DESCRIPTION, featureText.feature_list_description(), 150));
//			add(new JepColumn(DATE_INS, featureText.feature_list_date_ins(), 150, new DateCell(defaultDateFormatter)));
//			add(new JepColumn(OPERATOR_NAME, featureText.feature_list_operator_name(), 150));
		}};
	}
}
