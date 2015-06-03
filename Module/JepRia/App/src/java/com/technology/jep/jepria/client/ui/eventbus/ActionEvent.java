package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.EventHandler;

/**
 * Событие-действие пользователя, которое, возможно, необходимо контролировать наличием определенной роли(-ей) у проьзователя.
 */
abstract public class ActionEvent<H extends EventHandler> extends BusEvent<H> {
}
