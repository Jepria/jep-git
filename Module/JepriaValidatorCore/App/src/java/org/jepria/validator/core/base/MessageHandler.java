package org.jepria.validator.core.base;

/**
 * Интерфейс объекта, способного обрабатывать сообщения и исключения
 */
public interface MessageHandler {
  void handleMessage(Message message);
  void handleThrowable(Throwable e);
}
