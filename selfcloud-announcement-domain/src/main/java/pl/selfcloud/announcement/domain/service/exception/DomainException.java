package pl.selfcloud.announcement.domain.service.exception;

public abstract class DomainException extends RuntimeException{

  public DomainException(Object value) {
    super("Domain exception: " + value);
  }
}
