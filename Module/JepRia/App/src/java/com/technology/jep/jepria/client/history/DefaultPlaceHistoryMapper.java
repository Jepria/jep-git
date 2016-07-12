package com.technology.jep.jepria.client.history;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.technology.jep.jepria.client.history.place.JepCreatePlace;
import com.technology.jep.jepria.client.history.place.JepEditPlace;
import com.technology.jep.jepria.client.history.place.JepSearchPlace;
import com.technology.jep.jepria.client.history.place.JepSelectedPlace;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;

@WithTokenizers({
  JepCreatePlace.Tokenizer.class
  , JepEditPlace.Tokenizer.class
  , JepSearchPlace.Tokenizer.class
  , JepSelectedPlace.Tokenizer.class
  , JepViewDetailPlace.Tokenizer.class
  , JepViewListPlace.Tokenizer.class
})
public interface DefaultPlaceHistoryMapper extends PlaceHistoryMapper {
}
