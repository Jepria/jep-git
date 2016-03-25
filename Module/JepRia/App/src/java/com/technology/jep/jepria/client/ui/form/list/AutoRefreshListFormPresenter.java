package com.technology.jep.jepria.client.ui.form.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_REFRESH_DELAY;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * РџСЂРµР·РµРЅС‚РµСЂ СЃРїРёСЃРѕС‡РЅРѕР№ С„РѕСЂРјС‹, РїСЂРµРґРѕСЃС‚Р°РІР»СЏСЋС‰РёР№ РІРѕР·РјРѕР¶РЅРѕСЃС‚СЊ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏ.
 */
public class AutoRefreshListFormPresenter<
	V extends ListFormView,
	E extends PlainEventBus,
	S extends JepDataServiceAsync,
	F extends StandardClientFactory<E, S>>
	extends ListFormPresenter<V, E , S, F> {

	/**
	 * Р¤Р»Р°Рі, РїРѕРєР°Р·С‹РІР°СЋС‰РёР№, Р°РєС‚РёРІРµРЅ РёР»Рё РЅРµС‚ С‚Р°Р№РјРµСЂ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏ.
	 */
	protected boolean refreshTimerActive = false;
	
	/**
	 * РўР°Р№РјРµСЂ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏ.
	 */
	protected Timer refreshTimer;

	/**
	 * Р�РЅС‚РµСЂРІР°Р» РјРµР¶РґСѓ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏРјРё РІ РјРёР»Р»РёСЃРµРєСѓРЅРґР°С….
	 */
	private int refreshDelay = DEFAULT_REFRESH_DELAY;
	
	public AutoRefreshListFormPresenter(Place place,
			F clientFactory) {
		super(place, clientFactory);
	}

	/**
	 * РЈСЃС‚Р°РЅР°РІР»РёРІР°РµС‚ РёРЅС‚РµСЂРІР°Р» РјРµР¶РґСѓ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏРјРё.
	 * @param refreshDelay РёРЅС‚РµСЂРІР°Р» РІ РјРёР»Р»РёСЃРµРєСѓРЅРґР°С… (РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ РїРѕР»РѕР¶РёС‚РµР»СЊРЅС‹Рј)
	 */
	public void setRefreshDelay(int refreshDelay) {
		if (refreshDelay <= 0) {
			throw new IllegalArgumentException(JepTexts.errors_list_refreshDelayIllegalValue());
		}
		this.refreshDelay = refreshDelay;
	}

	/**
	 * Р’РѕР·РІСЂР°С‰Р°РµС‚ РёРЅС‚РµСЂРІР°Р» РјРµР¶РґСѓ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏРјРё.
	 * @return РёРЅС‚РµСЂРІР°Р» РІ РјРёР»Р»РёСЃРµРєСѓРЅРґР°С…
	 */
	public int getRefreshDelay() {
		return refreshDelay;
	}

	/**
	 * РњРµС‚РѕРґ, РїСЂРѕРІРµСЂСЏСЋС‰РёР№ РЅРµРѕР±С…РѕРґРёРјРѕСЃС‚СЊ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏ Рё РїСЂРё РЅРµРѕР±С…РѕРґРёРјРѕСЃС‚Рё СѓСЃС‚Р°РЅР°РІР»РёРІР°СЋС‰РёР№ С‚Р°Р№РјРµСЂ.
	 */
	@Override
	protected void onRefreshSuccess(PagingResult<JepRecord> pagingResult) {
		super.onRefreshSuccess(pagingResult);
		/*
		 * РџСЂРѕРІРµСЂСЏРµРј РЅРµРѕР±С…РѕРґРёРјРѕСЃС‚СЊ РѕР±РЅРѕРІР»РµРЅРёСЏ, РµСЃР»Рё С‚Р°Р№РјРµСЂ РµС‰С‘ РЅРµ СЃРѕР·РґР°РЅ.
		 */
		if (!refreshTimerActive) {
			clientFactory.getService().isRefreshNeeded(listUID, new JepAsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					/*
					 * РџСЂРё РЅРµРѕР±С…РѕРґРёРјРѕСЃС‚Рё СЃС‚Р°РІРёРј С‚Р°Р№РјРµСЂ, РѕР±РЅРѕРІР»СЏСЋС‰РёР№ СЃРїРёСЃРѕС‡РЅСѓСЋ С„РѕСЂРјСѓ.
					 */
					if (result) {
						refreshTimer = new Timer() {
							public void run() {
								refreshTimerActive = false; // С‚Р°Р№РјРµСЂ СЃСЂР°Р±РѕС‚Р°Р»
								if ((VIEW_LIST.equals(_workstate)) || (SELECTED.equals(_workstate))) {
									eventBus.refresh();
								}
							}
						};
						/*
						 * Р—Р°РїСЂРµС‚РёРј СЃРѕР·РґР°РІР°С‚СЊ РЅРѕРІС‹Рµ С‚Р°Р№РјРµСЂС‹, РїРѕРєР° РЅРµ СЃСЂР°Р±РѕС‚Р°Р» СЃСѓС‰РµСЃС‚РІСѓСЋС‰РёР№.
						 */
						refreshTimerActive = true;
						refreshTimer.schedule(refreshDelay);
					}
				}
			});
		}
	}

	/**
	 * РџРµСЂРµРѕРїСЂРµРґРµР»С‘РЅРЅС‹Р№ РјРµС‚РѕРґ, РѕС‚РєР»СЋС‡Р°СЋС‰РёР№ С‚Р°Р№РјРµСЂ Р°РІС‚РѕРѕР±РЅРѕРІР»РµРЅРёСЏ РїСЂРё СѓС…РѕРґРµ СЃРѕ СЃРїРёСЃРѕС‡РЅРѕР№ С„РѕСЂРјС‹.
	 */
	@Override
	public void onChangeWorkstate(WorkstateEnum newWorkstate) {
		/*
		 * Р•СЃР»Рё СѓС…РѕРґРёРј СЃРѕ СЃРїРёСЃРѕС‡РЅРѕР№ С„РѕСЂРјС‹, С‚Р°Р№РјРµСЂ РЅРµРѕР±С…РѕРґРёРјРѕ РѕС‚РєР»СЋС‡РёС‚СЊ.
		 */
		if (!(SELECTED.equals(newWorkstate) || VIEW_LIST.equals(newWorkstate)) && refreshTimerActive){
			refreshTimer.cancel();
			refreshTimerActive = false;
		}
		super.onChangeWorkstate(newWorkstate);
	}
	
}
