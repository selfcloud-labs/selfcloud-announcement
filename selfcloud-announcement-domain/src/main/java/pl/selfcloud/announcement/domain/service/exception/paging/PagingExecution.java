package pl.selfcloud.announcement.domain.service.exception.paging;

public abstract class PagingExecution extends RuntimeException{

  public PagingExecution(String value) {
    super(value);
  }
}
