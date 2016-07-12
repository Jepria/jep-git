package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepViewDetailPlace extends JepWorkstatePlace {

  @Prefix(VIEW_DETAILS_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepViewDetailPlace> {

    public JepViewDetailPlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepViewDetailPlace();
    }

    public String getToken(JepViewDetailPlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }
  
  /**
   * Создает новый Place.
   */
  public JepViewDetailPlace() {
    super(WorkstateEnum.VIEW_DETAILS);
  }

  public String getDisplayName() {
    return JepTexts.errors_security_action_viewDetails();
  }
}
