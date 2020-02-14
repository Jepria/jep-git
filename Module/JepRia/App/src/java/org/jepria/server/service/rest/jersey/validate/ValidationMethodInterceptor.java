package org.jepria.server.service.rest.jersey.validate;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.validation.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

public class ValidationMethodInterceptor implements MethodInterceptor {

  protected final Validator validatorInner;
  protected final DirectValidator validatorOuter;
  
  public ValidationMethodInterceptor() {
    // TODO refactor to injection so that app layer could customize the validator
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validatorInner = factory.getValidator();
  
    // TODO refactor to injection so that app layer could customize the validator
    validatorOuter = new DirectValidatorImpl();
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    // collect violations for all arguments in a single set
    Set<ConstraintViolation<?>> violations = new HashSet<>();

    for (int i = 0; i < methodInvocation.getArguments().length; i++) {
      Object argument = methodInvocation.getArguments()[i];

      Set<ConstraintViolation<Object>> violationsForArgument = new LinkedHashSet<>();// maintain order

      { // validate an argument against annotations which it itself is annotated with (outer annotations)
        Parameter parameter = methodInvocation.getMethod().getParameters()[i];
        violationsForArgument.addAll(validateMethodParameter(argument, parameter));
      }

      { // validate an argument against annotations on its fields (inner annotations)
        if (argument != null) { // javax.validation is unsupported for null objects
          violationsForArgument.addAll(validatorInner.validate(argument));
        }
      }

      violations.addAll(violationsForArgument);
    }

    if (violations.isEmpty()) {
      // data is valid
      return methodInvocation.proceed();

    } else {
      // data is invalid
      throw new ValidationException(violations);
    }

  }

  protected Set<ConstraintViolation<Object>> validateMethodParameter(Object value, Parameter parameter) {
    final Annotation[] parameterAnnotations = parameter.getDeclaredAnnotations();

    final PathImpl propertyPath;
    { // build propertyPath
      // TODO property path must not refer to java parameters, but to jaxrs parameters (header/query/body) instead
      propertyPath = new PathImpl();
      PathNodeImpl methodNode = new PathNodeImpl();
      String methodName = parameter.getDeclaringExecutable().getDeclaringClass().getCanonicalName() + "." + parameter.getDeclaringExecutable().getName();
      methodNode.setName(methodName);
      propertyPath.add(methodNode);
      PathNodeImpl methodParameterNode = new PathNodeImpl();
      methodParameterNode.setName(parameter.getType().getCanonicalName() + "_" + parameter.getName());
      propertyPath.add(methodParameterNode);
    }

    return validatorOuter.validate(value, Arrays.asList(parameterAnnotations), propertyPath);
  }

  protected static class PathImpl extends ArrayList<Path.Node>  implements Path {
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < size(); i++) {
        if (i > 0) {
          sb.append('/'); // not a system separator
        }
        sb.append(get(i));
      }
      return sb.toString();
    }
  }

  protected static class PathNodeImpl implements Path.Node {
    private String name;
    private boolean inIterable;
    private Integer index;
    private Object key;
    private ElementKind kind;

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public boolean isInIterable() {
      return inIterable;
    }

    public void setInIterable(boolean inIterable) {
      this.inIterable = inIterable;
    }

    @Override
    public Integer getIndex() {
      return index;
    }

    public void setIndex(Integer index) {
      this.index = index;
    }

    @Override
    public Object getKey() {
      return key;
    }

    public void setKey(Object key) {
      this.key = key;
    }

    @Override
    public ElementKind getKind() {
      return kind;
    }

    public void setKind(ElementKind kind) {
      this.kind = kind;
    }

    @Override
    public <T extends Path.Node> T as(Class<T> aClass) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
      String name = getName();
      if (name == null || "".equals(name)) {
        return super.toString();
      } else {
        return name;
      }
    }
  }
}