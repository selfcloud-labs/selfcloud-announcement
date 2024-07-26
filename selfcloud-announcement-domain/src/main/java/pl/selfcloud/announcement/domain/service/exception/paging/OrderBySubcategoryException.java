package pl.selfcloud.announcement.domain.service.exception.paging;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class OrderBySubcategoryException extends PagingExecution {

  public OrderBySubcategoryException() {
    super("The ordering by subcategory is not possible if a category filter is not set.");
  }
}
