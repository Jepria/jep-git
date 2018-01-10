package org.jepria.validator.rules.v09_x_x;

import java.util.ArrayList;

import org.jepria.validator.core.base.ValidatorRule;

/**
 * Список валидационных правил, соответствующих переводу
 * на версии Jepria-9 в необходимом для валидации порядке
 * 
 * Используется для передачи аргументом {@code --ruleList} в интерфейс командной строки
 * 
 *  @see {@link org.jepria.validator.user.CLI}
 */
public class RuleList extends ArrayList<ValidatorRule> {

  private static final long serialVersionUID = 1L;

  public RuleList() {
    add(new org.jepria.validator.rules.v09_x_x.EjbLocalRefWebXmlRule());
    add(new org.jepria.validator.rules.v09_x_x.EjbToDaoRule());
    add(new org.jepria.validator.rules.v09_x_x.EjbResourceRemoveRule());
    
    add(new org.jepria.validator.rules.v09_x_x.ModuleItemsMainClientFactoryRule());
    
    add(new org.jepria.validator.rules.v09_x_x.SetDndEnabledRule());
  }

}
