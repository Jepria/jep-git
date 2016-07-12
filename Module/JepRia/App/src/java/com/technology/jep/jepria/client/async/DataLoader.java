package com.technology.jep.jepria.client.async;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.dto.JepDto;

public interface DataLoader<T extends JepDto> {
  void load(Object loadConfig, AsyncCallback<List<T>> asyncCallback);
}
