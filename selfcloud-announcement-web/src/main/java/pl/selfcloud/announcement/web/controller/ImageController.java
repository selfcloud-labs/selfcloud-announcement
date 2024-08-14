package pl.selfcloud.announcement.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.api.common.PageSize;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.AnnouncementWithImagesDto;
import pl.selfcloud.announcement.domain.service.AnnouncementImageService;
import pl.selfcloud.announcement.domain.service.AnnouncementService;
import pl.selfcloud.announcement.domain.service.exception.image.AddingImageException;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/announcements")
public class ImageController {

  private final AnnouncementImageService announcementImageService;

  @Autowired
  public ImageController(AnnouncementImageService announcementImageService) {
    this.announcementImageService = announcementImageService;
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/{id}/images")
  public @ResponseBody AnnouncementWithImagesDto getImages(
      @PathVariable final long id) throws IOException {

    return announcementImageService.getAnnouncementWithImagesById(id);
  }
  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/{id}/images/{imageId}")
  public @ResponseBody byte[] getImage(
      @PathVariable final long id,
      @PathVariable final int imageId) throws IOException{

    return announcementImageService.getAnnouncementWithImageById(id, imageId);
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/{id}/images/count")
  public ResponseEntity<Integer> getImagesNumber(@PathVariable final long id){

    return new ResponseEntity<>(
        announcementImageService.countImages(id),
        HttpStatus.OK);
  }

  @Secured("CREATE_ANNOUNCEMENT")
  @PostMapping(path = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<AnnouncementDto> addFiles(
      @PathVariable final long id,
      @RequestParam("file") List<MultipartFile> files) throws IOException {

    return new ResponseEntity<>(
        announcementImageService.addFile(id,files),
        HttpStatus.CREATED);

  }
  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/images/page/{number}")
  public ResponseEntity<Collection<AnnouncementWithImagesDto>> getPagedAnnouncementsWithImages(
      @PathVariable("number") int page,
      @RequestParam(required = false, defaultValue = "SMALL") PageSize size,
      @RequestParam(required = false) Category category,
      @RequestParam(required = false) Subcategory subcategory,
      @RequestParam(required = false, defaultValue = "0d") Double minimalPrice,
      @RequestParam(required = false, defaultValue = "1000000d") Double maximalPrice,
      @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime created_before,
      @RequestParam(required = false, defaultValue = "1999-12-03T08:03")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime created_after,
      @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
      @RequestParam(required = false, defaultValue = "ID") OrderBy orderBy
  ){

    return new ResponseEntity<>(
        announcementImageService.getPagedAnnouncementsWithImages(--page, size, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, sortDirection, orderBy),
        HttpStatus.OK);
  }

  @Secured("DELETE_ANNOUNCEMENT")
  @DeleteMapping("/{id}/images")
  public ResponseEntity<String> deleteImagesOfAnnouncement(@PathVariable final long id){
    announcementImageService.deleteImagesOfAnnouncement(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Secured("DELETE_ANNOUNCEMENT")
  @DeleteMapping("/{id}/images/{imageId}")
  public ResponseEntity<String> deleteImageOfAnnouncement(@PathVariable final long id, @PathVariable final int imageId){
    announcementImageService.deleteImageOfAnnouncement(id, imageId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(AddingImageException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ResponseEntity<String> handleAddingImageException(AddingImageException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_ACCEPTABLE)
        .body(exception.getMessage());
  }
}
