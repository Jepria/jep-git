package com.technology.jep.jepria.auto.conditions;

import com.technology.jep.jepria.auto.HasText;

/**
 * Проверка изменения текста заданного объекта HasText
 */
public class TextChangeChecker implements ConditionChecker {
  private HasText hasText;
  private String oldText;

  public TextChangeChecker(HasText hasText) {
    this.hasText = hasText;
    this.oldText = hasText.getText();
  }
  
  @Override
  public boolean isSatisfied() {
    return !oldText.equals(hasText.getText());
  }
}
