package pl.selfcloud.announcement.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class UnauthorizedToThisOperationException extends RuntimeException {

  public UnauthorizedToThisOperationException() {
    super("This user cannot use this resource.");
  }
}