package com.technology.jep.jepria.client.history.place;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class JepViewListPlace extends JepWorkstatePlace {

  @Prefix(VIEW_LIST_WORKSTATE_ID)
  public static class Tokenizer implements PlaceTokenizer<JepViewListPlace> {

    public JepViewListPlace getPlace(String token) {
      JepScopeStack.instance.setFromHistoryToken(token);
      return new JepViewListPlace();
    }

    public String getToken(JepViewListPlace place) {
      return JepScopeStack.instance.toHistoryToken(place);
    }
  }
  
  /**
   * Создает новый Place.
   */
  public JepViewListPlace() {
    super(WorkstateEnum.VIEW_LIST);
  }

  public String getDisplayName() {
    return JepTexts.place_name_viewList();
  }

}
