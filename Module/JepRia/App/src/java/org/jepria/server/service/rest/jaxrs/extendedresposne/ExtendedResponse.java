package org.jepria.server.service.rest.jaxrs.extendedresposne;

public interface ExtendedResponse {
  
  public static final String HEADER_NAME__EXTENDED_RESPONSE = "extended-response";
  
  public static interface ProducerConfigurator {
    void setFactory(Factory factory);
  }
  
  public static interface Factory {
    Object createExtendedResponse(String value);
  }
}
