package pl.selfcloud.announcement.domain.service.exception.image;


import pl.selfcloud.announcement.domain.service.exception.DomainException;

public class ImageNotFoundException extends DomainException {

  public ImageNotFoundException() {
    super("Image not found.");
  }
}
