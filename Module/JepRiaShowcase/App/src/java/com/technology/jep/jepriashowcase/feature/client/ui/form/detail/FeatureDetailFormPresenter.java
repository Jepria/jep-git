package com.technology.jep.jepriashowcase.feature.client.ui.form.detail;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.*;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
public class FeatureDetailFormPresenter<E extends PlainEventBus, S extends JepDataServiceAsync> extends
		DetailFormPresenter<FeatureDetailFormViewImpl, E, S, StandardClientFactory<E, S>> {

	public FeatureDetailFormPresenter(Place place,
			StandardClientFactory<E, S> clientFactory) {
		super(place, clientFactory);
	}

	@Override
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		super.adjustToWorkstate(workstate);
		
		fields.setFieldVisible(FEATURE_ID, SEARCH.equals(workstate) || EDIT.equals(workstate) || VIEW_DETAILS.equals(workstate));
		fields.setFieldVisible(FROM_DATE_INS, SEARCH.equals(workstate));
		fields.setFieldVisible(TO_DATE_INS, SEARCH.equals(workstate));
		fields.setFieldVisible(DATE_INS, VIEW_DETAILS.equals(workstate));
		fields.setFieldVisible(DESCRIPTION, !SEARCH.equals(workstate));
		fields.setFieldVisible(OPERATOR_NAME, VIEW_DETAILS.equals(workstate));
		fields.setFieldVisible(MAX_ROW_COUNT, SEARCH.equals(workstate));
		
		fields.setFieldAllowBlank(FEATURE_NAME, !EDIT.equals(workstate));
		
	}
}
