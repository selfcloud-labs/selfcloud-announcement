package pl.selfcloud.announcement.domain.service.exception.state;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class AnnouncementIsRemoved extends RuntimeException{
  public AnnouncementIsRemoved(long id) {
    super("Announcement with id " + id + " is removed.");
  }
}
