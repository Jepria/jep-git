package com.technology.jep.jepria.shared.record;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JepParentRecord extends JepRecord implements IsSerializable {
  
  private static final long serialVersionUID = 3687986901376868853L;

  protected JepParentRecord() {
  }

  public JepParentRecord(JepRecord record) {
    super(record);
  }

}
