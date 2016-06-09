package com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE;

import java.util.ArrayList;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.async.TypingTimeoutAsyncCallback;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.arsenic.shared.service.ArsenicServiceAsync;

public class ArsenicDetailFormPresenter<E extends PlainEventBus, S extends ArsenicServiceAsync> 
		extends DetailFormPresenter<ArsenicDetailFormViewImpl, E, S, StandardClientFactory<E, S>> { 
 
	public ArsenicDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
		super(place, clientFactory);
	}
	
	public void bind() {
		super.bind();
		// Здесь размещается код связывания presenter-а и view
		
		fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(final JepEvent event) {
				service.durableFetch(0, new FirstTimeUseAsyncCallback<Void>(event) {
					@Override
					public void onSuccessLoad(Void result) {
						((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>() {{
							add(new JepOption("Option1", "1"));
							add(new JepOption("Option2", "2"));
							add(new JepOption("Option3", "3"));
							add(new JepOption("Option4", "4"));
						}});
					}
				});
				
			}
		});
		
		fields.get(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(final JepEvent event) {
				service.durableFetch(1000, new FirstTimeUseAsyncCallback<Void>(event) {
					@Override
					public void onSuccessLoad(Void result) {
						((JepComboBoxField)event.getSource()).setOptions(new ArrayList<JepOption>() {{
							add(new JepOption("Option1", "1"));
							add(new JepOption("Option2", "2"));
							add(new JepOption("Option3", "3"));
							add(new JepOption("Option4", "4"));
						}});
					}
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
	}
}