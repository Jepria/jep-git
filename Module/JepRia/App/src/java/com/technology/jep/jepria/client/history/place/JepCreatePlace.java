package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepCreatePlace extends JepWorkstatePlace {
  @Prefix(CREATE_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepCreatePlace> {

    public JepCreatePlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepCreatePlace();
    }

    public String getToken(JepCreatePlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }

  /**
   * Создает новый Place.
   */
  public JepCreatePlace() {
    super(WorkstateEnum.CREATE);
  }

  public String getDisplayName() {
    return JepTexts.errors_security_action_add();
  }
}
