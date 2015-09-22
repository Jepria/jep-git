package com.technology.jep.jepriashowcase.request.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.*;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.requestText;
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.scopeModuleIds;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_QUANTITY;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_FROM;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_TO;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_STATUS_CODE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.SHOP_ID;

import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriashowcase.request.shared.service.RequestServiceAsync;
 
public class RequestDetailFormPresenter<E extends PlainEventBus, S extends RequestServiceAsync> 
		extends DetailFormPresenter<RequestDetailFormView, E, S, StandardClientFactory<E, S>> { 
 
	public RequestDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
		super(scopeModuleIds, place, clientFactory);
	}
 
	public void bind() {
		super.bind();
		// Здесь размещается код связывания presenter-а и view 
		
		fields.addFieldListener(REQUEST_STATUS_CODE, JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(JepEvent event) {
				service.getRequestStatus(new FirstTimeUseAsyncCallback<List<JepOption>>(event) {
					@Override
					public void onSuccessLoad(List<JepOption> result) {
						fields.setFieldOptions(REQUEST_STATUS_CODE, result);
					}
				});
			}
		});
		
		// определяем кастомный валидатор взаимодействия полей начальной и конечной дат
		view.setCustomValidator(BEGIN_END_DATE_CUSTOM_VALIDATOR_ID, new Validator() {
			@Override
			public boolean isValid() {
				// Перед проверкой, очищаем предыдущие ошибки.
				fields.get(REQUEST_DATE_FROM).clearInvalid();
				Date beginDate = fields.getFieldValue(REQUEST_DATE_FROM);
				Date endDate = fields.getFieldValue(REQUEST_DATE_TO);
				if (!JepRiaUtil.isEmpty(beginDate) 
						&& !JepRiaUtil.isEmpty(endDate) 
							&& beginDate.after(endDate)){
					fields.get(REQUEST_DATE_FROM).markInvalid(requestText.request_detail_request_date_error_beginEndDates());
					return false;
				}
				return true;
			}
		});
		
		// определяем кастомный валидатор поля request_date
		view.setCustomValidator(REQUEST_DATE_CUSTOM_VALIDATOR_ID, new Validator() {
			@Override
			public boolean isValid() {
				// Перед проверкой, очищаем предыдущие ошибки.
				fields.get(REQUEST_DATE).clearInvalid();
				Date beginDate = fields.getFieldValue(REQUEST_DATE);
				Date endDate = new Date();
				CalendarUtil.resetTime(endDate);
				if (!JepRiaUtil.isEmpty(beginDate) 
							&& beginDate.after(endDate)){
					fields.get(REQUEST_DATE).markInvalid(requestText.request_detail_request_date_error_sysdate());
					return false;
				}
				return true;
			}
		});
	}
 
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		fields.setFieldVisible(REQUEST_DATE_FROM, SEARCH.equals(workstate));
		fields.setFieldVisible(REQUEST_DATE_TO, SEARCH.equals(workstate));
		fields.setFieldVisible(SHOP_ID, SEARCH.equals(workstate));
		
		fields.setFieldVisible(GOODS_QUANTITY, !SEARCH.equals(workstate));
	}
 
}
