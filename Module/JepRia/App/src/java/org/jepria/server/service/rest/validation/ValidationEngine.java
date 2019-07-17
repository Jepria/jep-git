package org.jepria.server.service.rest.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.jepria.server.service.rest.validation.PathBase.NodeBase;

public class ValidationEngine {

  public static <T> ConstraintValidator<?, ? super T> createValidator(
      Class<? extends ConstraintValidator<?, ? super T>> validatorClass) {
    if (validatorClass != null) {
      return createValidators(Arrays.asList(validatorClass)).iterator().next();
    } else {
      return null;
    }
  }

  public static <T> Collection<? extends ConstraintValidator<?, ? super T>> createValidators(
      Collection<Class<? extends ConstraintValidator<?, ? super T>>> validatorClasses) {

    final List<ConstraintValidator<?, ? super T>> ret = new ArrayList<>();

    if (validatorClasses != null) {
      for (Class<? extends ConstraintValidator<?, ? super T>> validatorClass: validatorClasses) {
        ConstraintValidator<?, ? super T> validator = newValidatorInstance(validatorClass);
        ret.add(validator);
      }
    }

    return ret;
  }

  protected static <T> ConstraintValidator<?, ? super T> newValidatorInstance(
      Class<? extends ConstraintValidator<?, ? super T>> validatorClass) { 
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> void validate(ConstraintValidator<?, ? super T> validator, T target)
      throws ConstraintViolationException {
    if (validator != null) {
      validate(Arrays.asList(validator), target);
    }
  }

  public static <T> void validate(Collection<? extends ConstraintValidator<?, ? super T>> validators, T target)
      throws ConstraintViolationException {

    boolean isValid = true;
    final Set<ConstraintViolation<?>> violations = new HashSet<>();

    if (validators != null) {

      final ConstraintValidatorContextImpl context = new ConstraintValidatorContextImpl();

      for (ConstraintValidator<?, ? super T> validator: validators) {

        context.violations = new HashSet<>();

        try { // for case of direct throwing from within ConstraintValidator.isValid method
          if (!validator.isValid(target, context)) {
            isValid = false;

            if (context.violations != null) {
              violations.addAll(context.violations);
            }
          }

        } catch (ConstraintViolationException e) {
          isValid = false;

          if (e.getConstraintViolations() != null) {
            violations.addAll(e.getConstraintViolations());
          }
        }
      }
    }

    if (!isValid || !violations.isEmpty()) { // isValid might be false together with violations empty
      throw new ConstraintViolationException(violations);
    }
  }

  private static class ConstraintValidatorContextImpl implements ConstraintValidatorContext {

    protected Set<ConstraintViolation<?>> violations;

    @Override
    public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate) {
      return new ConstraintViolationBuilderImpl(messageTemplate) {
        @Override
        public ConstraintValidatorContext addConstraintViolation() {
          violations.add(buildViolation());
          return ConstraintValidatorContextImpl.this;
        }
      };
    }

    @Override
    public void disableDefaultConstraintViolation() {
      // TODO Auto-generated method stub
    }

    @Override
    public String getDefaultConstraintMessageTemplate() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
      // TODO Auto-generated method stub
      return null;
    }

    protected abstract class ConstraintViolationBuilderImpl implements ConstraintValidatorContext.ConstraintViolationBuilder {

      protected final List<Path.Node> pathElements = new ArrayList<>();
      protected final String messageTemplate;

      public ConstraintViolationBuilderImpl(String messageTemplate) {
        this.messageTemplate = messageTemplate;
      }

      protected ConstraintViolation<?> buildViolation() {
        return new ConstraintViolationBase(new PathBase(pathElements), messageTemplate);
      }

      @Override
      public LeafNodeBuilderCustomizableContext addBeanNode() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public NodeBuilderDefinedContext addNode(String arg0) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public NodeBuilderDefinedContext addParameterNode(int arg0) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public NodeBuilderCustomizableContext addPropertyNode(String name) {
        pathElements.add(new NodeBase(pathElements.size(), name));
        
        return new NodeBuilderCustomizableContextImpl() {
          @Override
          public ConstraintValidatorContext addConstraintViolation() {
            violations.add(buildViolation());
            return ConstraintValidatorContextImpl.this;
          }
        };
      }

      private abstract class NodeBuilderCustomizableContextImpl implements 
      ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext {

        @Override
        public LeafNodeBuilderCustomizableContext addBeanNode() {
          // TODO Auto-generated method stub
          return null;
        }

        @Override
        public NodeBuilderCustomizableContext addNode(String arg0) {
          // TODO Auto-generated method stub
          return null;
        }

        @Override
        public NodeBuilderCustomizableContext addPropertyNode(String name) {
          pathElements.add(new NodeBase(pathElements.size(), name));
          return this;
        }

        @Override
        public NodeContextBuilder inIterable() {
          // TODO Auto-generated method stub
          return null;
        }
      }
    }
  }
}
