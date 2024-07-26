package pl.selfcloud.announcement.domain.service.exception.paging;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class SubcategoryAllowsCategoryToPagingExecution extends PagingExecution {

  public SubcategoryAllowsCategoryToPagingExecution() {
    super("The category cannot be empty while the subcategory is set in the filter.");
  }
}
