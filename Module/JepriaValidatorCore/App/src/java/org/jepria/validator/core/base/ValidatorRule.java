package org.jepria.validator.core.base;

import org.jepria.validator.core.base.contextadapter.ContextReadAdapter;
import org.jepria.validator.core.base.resource.Directory;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.Transformation;

/**
 * Базовый класс для прикладных валидационных правил.
 * <br>
 * Прикладные правила следует использовать (запускать) не напрямую, а с помощью валидатора 
 * {@link org.jepria.validator.core.base.Validator#validateUnderRule}, 
 * в котором производится настройка необходимого внутреннего состояния правила.
 */
public class ValidatorRule implements IValidatorRule, MessageHandler {

  /**
   * Aдаптер для чтения внешнего контекста во время валидации и рефакторинга
   */
  // private only, no public getter
  private ContextReadAdapter contextReadAdapter;
  
  /**
   * Задаёт адаптер для чтения внешнего контекста во время валидации и рефакторинга
   * @param contextReadAdapter
   */
  public void setContextReadAdapter(ContextReadAdapter contextReadAdapter) {
    this.contextReadAdapter = contextReadAdapter;
  }
  
  // protected: for use in descendants only! For simplicity, named getContextRead
  protected ContextReadAdapter getContextRead() {
    return contextReadAdapter;
  }
  
  // private only, no public getter
  private MessageHandler messageHandler;
  
  public void setMessageHandler(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
  }
  
  /**
   * Обрабатывает сообщение, возникшее во время валидации или трансформации
   */
  @Override
  public void handleMessage(Message message) {
    if (messageHandler != null) {
      messageHandler.handleMessage(message);
    }
  }
  
  /**
   * Обрабатывает исключение, возникшее во время валидации или трансформации
   */
  @Override
  public void handleThrowable(Throwable e) {
    if (messageHandler != null) {
      messageHandler.handleThrowable(e);
    }
  }
  
  @Override
  public Transformation forPlainResource(PlainResource resource) {
    return null;
  }
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    return forPlainResource(resource);
  }

  @Override
  public Transformation forXmlResource(XmlResource resource) {
    return forPlainResource(resource);
  }
  
  @Override
  public Transformation forDirectory(Directory resource) {
    return null;
  }
}
