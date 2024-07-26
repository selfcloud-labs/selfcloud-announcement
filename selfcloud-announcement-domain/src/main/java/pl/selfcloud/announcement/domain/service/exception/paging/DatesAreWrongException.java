package pl.selfcloud.announcement.domain.service.exception.paging;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class DatesAreWrongException extends PagingExecution {

  public DatesAreWrongException() {
    super("The date created_before cannot be less than created_after date.");
  }
}
