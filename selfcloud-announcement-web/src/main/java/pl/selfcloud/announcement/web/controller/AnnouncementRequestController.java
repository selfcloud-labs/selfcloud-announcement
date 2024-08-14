package pl.selfcloud.announcement.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.selfcloud.announcement.api.dto.AnnouncementRequestDto;
import pl.selfcloud.announcement.domain.service.AnnouncementRequestService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/announcements")
public class AnnouncementRequestController {

  private final AnnouncementRequestService announcementRequestService;

  @Autowired
  public AnnouncementRequestController(final AnnouncementRequestService announcementRequestService){
    this.announcementRequestService = announcementRequestService;
  }

  @PostMapping("{id}/request")
  public ResponseEntity<AnnouncementRequestDto> createAnnouncementRequest(@PathVariable final Long id){

    return new ResponseEntity<>(
        announcementRequestService.create(id),
        HttpStatus.CREATED
    );
  }
}
