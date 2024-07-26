package pl.selfcloud.announcement.domain.service.exception.image;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.selfcloud.announcement.domain.service.exception.DomainException;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class InvalidPathSequenceException extends DomainException {

  public InvalidPathSequenceException() {
    super("Filename contains invalid path sequence.");
  }
}
