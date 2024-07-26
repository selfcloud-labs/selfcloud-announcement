package pl.selfcloud.announcement.domain.service.exception.state;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class AnnouncementIsEnded extends RuntimeException{
  public AnnouncementIsEnded(long id) {
    super("Announcement with id " + id + " is ended.");
  }
}
