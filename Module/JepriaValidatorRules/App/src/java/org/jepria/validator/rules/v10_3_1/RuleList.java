package org.jepria.validator.rules.v10_3_1;

/**
 * Список валидационных правил, соответствующих переводу
 * на версию Jepria-10.3.1 в необходимом для валидации порядке
 * 
 * Используется для передачи аргументом {@code --ruleList} в интерфейс командной строки
 * 
 *  @see {@link org.jepria.validator.user.CLI}
 */
public class RuleList extends org.jepria.validator.rules.v10_3_0.RuleList {

  private static final long serialVersionUID = 1L;

  public RuleList() {
    // валидации прикладного кода не требуется
  }

}
