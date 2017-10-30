package org.jepria.validator.core.base.resource;

import com.github.javaparser.ast.CompilationUnit;

public class JavaResource extends AbstractResource {
  
  public JavaResource(PlainResource resource) {
    super(resource);
  }

  public CompilationUnit asCompilationUnit() {
    return ResourceUtils.asJava(newInputStream());
  }
}
