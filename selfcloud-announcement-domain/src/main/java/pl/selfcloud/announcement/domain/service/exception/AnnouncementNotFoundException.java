package pl.selfcloud.announcement.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class AnnouncementNotFoundException extends DomainException{

  public AnnouncementNotFoundException(long id) {
    super("The Announcement with id " + id + " not found.");
  }
}
