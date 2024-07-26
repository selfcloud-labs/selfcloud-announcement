package pl.selfcloud.announcement.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class MissingValueException extends DomainException {

  public MissingValueException(String value) {
    super("Missing value: " + value + ".");
  }
}
