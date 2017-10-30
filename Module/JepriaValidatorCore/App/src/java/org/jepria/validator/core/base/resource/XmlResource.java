package org.jepria.validator.core.base.resource;

import org.w3c.dom.Document;

public class XmlResource extends AbstractResource {
  
  public XmlResource(PlainResource resource) {
    super(resource);
  }

  public Document asDocument() {
    return ResourceUtils.asXml(newInputStream());
  }
}
