package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepEditPlace extends JepWorkstatePlace {

  @Prefix(EDIT_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepEditPlace> {

    public JepEditPlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepEditPlace();
    }

    public String getToken(JepEditPlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }

  /**
   * Создает новый Place.
   */
  public JepEditPlace() {
    super(WorkstateEnum.EDIT);
  }

  public String getDisplayName() {
    return JepTexts.errors_security_action_edit();
  }
}
