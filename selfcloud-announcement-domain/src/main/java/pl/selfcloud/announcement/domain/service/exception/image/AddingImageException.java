package pl.selfcloud.announcement.domain.service.exception.image;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.selfcloud.announcement.domain.service.exception.DomainException;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class AddingImageException extends DomainException {

  public AddingImageException() {
    super("Exception by adding image.");
  }
}
