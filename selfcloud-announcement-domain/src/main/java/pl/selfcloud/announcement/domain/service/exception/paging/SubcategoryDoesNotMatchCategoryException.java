package pl.selfcloud.announcement.domain.service.exception.paging;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class SubcategoryDoesNotMatchCategoryException extends PagingExecution {

  public SubcategoryDoesNotMatchCategoryException(String category, String subcategory) {
    super("The subcategory " + subcategory + " does not match the " + category);
  }
}
