package pl.selfcloud.announcement.domain.service.exception.state;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.selfcloud.common.model.State;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class AnnouncementIsNotEnabledException extends RuntimeException{
  public AnnouncementIsNotEnabledException(long id, State state) {
    super("Announcement with id " + id + " has state " + state + ".");
  }
}
