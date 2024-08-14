package pl.selfcloud.announcement.domain.service.exception;

public class AnnouncementOwnerException extends RuntimeException {

  public AnnouncementOwnerException(){
    super("The announcement cannot be requested by owner of its.");
  }
}
