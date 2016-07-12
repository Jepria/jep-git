package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepSelectedPlace extends JepWorkstatePlace {

  @Prefix(SELECTED_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepSelectedPlace> {

    public JepSelectedPlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepSelectedPlace();
    }

    public String getToken(JepSelectedPlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }
  
  /**
   * Создает новый Place.
   */
  public JepSelectedPlace() {
    super(WorkstateEnum.SELECTED);
  }

  public String getDisplayName() {
    return JepTexts.place_name_select();
  }
}
