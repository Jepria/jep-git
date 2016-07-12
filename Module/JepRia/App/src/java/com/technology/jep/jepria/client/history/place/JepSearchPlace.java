package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepSearchPlace extends JepWorkstatePlace {

  @Prefix(SEARCH_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepSearchPlace> {

    public JepSearchPlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepSearchPlace();
    }

    public String getToken(JepSearchPlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }
  
  /**
   * Создает новый Place.
   */
  public JepSearchPlace() {
    super(WorkstateEnum.SEARCH);
  }

  public String getDisplayName() {
    return JepTexts.errors_security_action_search();
  }
}
