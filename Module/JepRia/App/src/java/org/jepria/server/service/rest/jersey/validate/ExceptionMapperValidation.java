package org.jepria.server.service.rest.jersey.validate;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExceptionMapperValidation implements ExceptionMapper<ValidationException> {

  @Override
  public Response toResponse(ValidationException e) {
    List<ConstraintViolationDto> violationsDto = new ArrayList<>();

    Collection<ConstraintViolation<?>> violations = e.getViolations();
    if (violations != null) {

      for (ConstraintViolation<?> violation: violations) {
        ConstraintViolationDto violationDto = new ConstraintViolationDto();
        violationDto.setPropertyPath(violation.getPropertyPath().toString());
        violationDto.setViolationDescription(violation.getMessage());
        violationsDto.add(violationDto);
      }
    }

    return Response.status(Response.Status.BAD_REQUEST).entity(violationsDto).build();
  }

  public static class ConstraintViolationDto {

    private String propertyPath;
    private String violationDescription;

    public String getPropertyPath() {
      return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
      this.propertyPath = propertyPath;
    }

    public String getViolationDescription() {
      return violationDescription;
    }

    public void setViolationDescription(String violationDescription) {
      this.violationDescription = violationDescription;
    }
  }
}
