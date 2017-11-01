package org.jepria.validator.core.base;

import org.jepria.validator.core.base.resource.Resource;

/**
 * Сборщик сообщений при валидации нескольких ресурсов,
 * позволяющий группировать их по валидационным правилам или валидируемым ресурсам
 */
public interface MessageCollector {
  /**
   * Обработать сообщение от конкретного валидационного правила, по поводу конкретного ресурса.  
   */
  void collect(ValidatorRule rule, Resource resource, Message message);
  
  /**
   * Обработать исключение от конкретного валидационного правила, по поводу конкретного ресурса.  
   */
  void collect(ValidatorRule rule, Resource resource, Throwable e);
  
  /**
   * Заглушка сборщика сообщений. Только трассирует исключения в стандартный поток ошибок.
   */
  public static MessageCollector DUMMY = new MessageCollector() {
    @Override
    public void collect(ValidatorRule rule, Resource resource, Throwable e) {
      e.printStackTrace();
    }
    
    @Override
    public void collect(ValidatorRule rule, Resource resource, Message message) {
    }
  };
}
