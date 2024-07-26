package pl.selfcloud.announcement.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class PriceBelowZeroException extends DomainException{

  public PriceBelowZeroException(String value) {
    super("The given price is wrong. Price cannot be below 0.");
  }
}
