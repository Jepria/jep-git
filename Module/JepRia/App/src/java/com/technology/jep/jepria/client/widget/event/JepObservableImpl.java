package com.technology.jep.jepria.client.widget.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JepObservableImpl implements JepObservable {
  
  private Map<JepEventType, List<JepListener>> listenerMap = new HashMap<JepEventType, List<JepListener>>();

  public void addListener(JepEventType eventType, JepListener listener) {
    getListeners(eventType).add(listener);
  }

  public List<JepListener> getListeners(JepEventType eventType) {
    List<JepListener> listeners = listenerMap.get(eventType);
    if(listeners == null) {
      listeners = new LinkedList<JepListener>();
      listenerMap.put(eventType, listeners);
    }
    return listeners;
  }

  public void notifyListeners(JepEventType eventType, JepEvent event) {
    List<JepListener> listeners = getListeners(eventType);
    for(JepListener listener: listeners) {
      listener.handleEvent(event);
    }
  }

  public void removeListener(JepEventType eventType, JepListener listener) {
    getListeners(eventType).remove(listener);
  }

  public Set<JepListener> getListeners() {
    Set<JepListener> result = new HashSet<JepListener>();
    Collection<List<JepListener>> listenerList = listenerMap.values();
    for(List<JepListener> listeners: listenerList) {
      for(JepListener listener: listeners) {
        result.add(listener);
      }
    }
    return result;
  }

  public void removeListener(JepListener removingListener) {
    Collection<List<JepListener>> listenerList = listenerMap.values();
    for(List<JepListener> listeners: listenerList) {
      for(JepListener listener: listeners) {
        if(removingListener == listener) {
          listeners.remove(listener);
          break; // Не return. Теоретически один listener может обслуживать разные типы событий 
        }
      }
    }
  }

}
