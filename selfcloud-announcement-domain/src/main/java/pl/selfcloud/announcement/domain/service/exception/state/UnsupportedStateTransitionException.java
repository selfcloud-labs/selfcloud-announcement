package pl.selfcloud.announcement.domain.service.exception.state;

import pl.selfcloud.announcement.api.state.AnnouncementState;

public class UnsupportedStateTransitionException extends RuntimeException{
  public UnsupportedStateTransitionException(AnnouncementState state) {
    super("Announcement has state " + state + ".");
  }
}
