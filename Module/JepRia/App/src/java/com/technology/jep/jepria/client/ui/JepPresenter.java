package com.technology.jep.jepria.client.ui;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;

abstract public class JepPresenter<E extends EventBus, F extends ClientFactory<E>> extends JepActivity<E, F> {

	protected Place place;

	/**
	 * Переменная именуется с '_' чтобы уменьшить вероятность "накладок" в adjustToWorkstate.
	 */
	protected WorkstateEnum _workstate = null;
	
	public JepPresenter(Place place, F clientFactory) {
		super(clientFactory);
		this.place = place;
	}
	
	/**
	 * Используется в случаях, когда презентеры не пересоздаются для новых Place.
	 * @param place новый Place
	 */
	public void setPlace(Place place) {
		this.place = place;
		changeWorkstate(place);
	}

	public Place getPlace() {
		return place;
	}
	
	protected void changeWorkstate(Place place) {
		changeWorkstate(((JepWorkstatePlace)place).getWorkstate());
	}

	protected void changeWorkstate(WorkstateEnum newWorkstate) {
		// Только в случае, если действительно изменяется состояние
		// и оно применимо для данного презентера
		if(newWorkstate != null 
				&& !newWorkstate.equals(_workstate)
					&& isAcceptableWorkstate(newWorkstate)) {
			onChangeWorkstate(newWorkstate);

			_workstate = newWorkstate;
			adjustToWorkstate(_workstate);
		}
		else {
			// смена состояния происходит всегда, чтобы у каждого презентера  
			// хранилась актуальная копия текущего рабочего состояния модуля
			_workstate = newWorkstate;
		}
	}

	protected void onChangeWorkstate(WorkstateEnum workstate) {

	}

	/**
	 * Метод предназначен для перекрытия наследниками, "желающими" управлять
	 * формой с учётом состояний клиентского модуля.
	 * 
	 * @param workstate состояние клиентского модуля
	 */
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		logger.trace(getClass() + ".adjustToWorkstate : workstate = " + workstate);
	}
	
	/**
	 * Метод проверки соответствия рабочего состояния и текущего презентера.<br>
	 * Особенности:<br>
	 * В общем случае, смена состояния презентера должна происходить всегда.
	 * Однако в случаях списочной и детальной форм, фильтрация отдельных состояний
	 * позволяет существенно повысить производительность путем сокращения числа "лишних"
	 * срабатываний презентера.<br>
	 * Также для обработки специфичных прикладному модулю случаев, достаточно переопределить
	 * метод с необходимой бизнес-логикой.  
	 * 
	 * @param workstate	рабочее состояние
	 * @return	true - если рабочее состояние применимо для текущего презентера
	 */
	protected boolean isAcceptableWorkstate(WorkstateEnum workstate){
		return true;
	}
}
