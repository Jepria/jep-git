package org.jepria.validator.rules.v10_3_0;

/**
 * Список валидационных правил, соответствующих переводу
 * на версию Jepria-10.3.0 в необходимом для валидации порядке
 * 
 * Используется для передачи аргументом {@code --ruleList} в интерфейс командной строки
 * 
 *  @see {@link org.jepria.validator.user.CLI}
 */
public class RuleList extends org.jepria.validator.rules.v09_x_x.RuleList {

  private static final long serialVersionUID = 1L;

  public RuleList() {
    add(new org.jepria.validator.rules.v10_3_0.InitActivityMappersRule());
    add(new org.jepria.validator.rules.v10_3_0.GetServiceGetEventBusRule());
    add(new org.jepria.validator.rules.v10_3_0.MainViewImplRule());
    add(new org.jepria.validator.rules.v10_3_0.CollapseAllGwtXmlRule());
    add(new org.jepria.validator.rules.v10_3_0.MytmpBuildXmlRule());
    add(new org.jepria.validator.rules.v10_3_0.GroovyAllBuildXmlRule());
    add(new org.jepria.validator.rules.v10_3_0.CreateMainModulePresenterTypeRule());
  }

}
