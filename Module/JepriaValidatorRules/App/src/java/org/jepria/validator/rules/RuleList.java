package org.jepria.validator.rules;

import java.util.ArrayList;

import org.jepria.validator.core.base.ValidatorRule;

public class RuleList extends ArrayList<ValidatorRule> {

  private static final long serialVersionUID = -6121994209258314905L;
  
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
