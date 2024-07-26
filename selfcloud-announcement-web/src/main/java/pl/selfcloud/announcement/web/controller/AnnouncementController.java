package pl.selfcloud.announcement.web.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.api.common.PageSize;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.domain.service.AnnouncementService;
import pl.selfcloud.announcement.domain.service.exception.AnnouncementNotFoundException;
import pl.selfcloud.announcement.domain.service.exception.DomainException;
import pl.selfcloud.announcement.domain.service.exception.paging.PagingExecution;
import pl.selfcloud.announcement.domain.service.exception.state.AnnouncementIsNotEnabledException;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/announcements")
public class AnnouncementController {

  private final AnnouncementService announcementService;

  @Autowired
  public AnnouncementController(AnnouncementService announcementService) {
    this.announcementService = announcementService;
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/{id}")
  public ResponseEntity<AnnouncementDto> getAnnouncement(@PathVariable final long id){
    return new ResponseEntity<>(
        announcementService.getById(id),
        HttpStatus.OK);
  }

  @Secured("ADMIN")
  @GetMapping("/customers/{id}")
  public ResponseEntity<List<AnnouncementDto>> getAnnouncementByCustomerId(@PathVariable final long id){
    return new ResponseEntity<>(
        announcementService.getAllByCustomerId(id),
        HttpStatus.OK);
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/customers/email/{email}")
  public ResponseEntity<List<AnnouncementDto>> getAnnouncementByCustomerEmail(@PathVariable final String email){
    return new ResponseEntity<>(
        announcementService.getAllByCustomerEmail(email),
        HttpStatus.OK);
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/categories/announcements/count")
  public ResponseEntity<Map<Category, Long>> getContractorsCount(){

    return new ResponseEntity<>(
        announcementService.getCategoriesAnnouncementsCount(),
        HttpStatus.OK);
  }
  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/categories/contractors/count")
  public ResponseEntity<Map<Category, Long>> getUniqueCustomersByCategory() {
    return new ResponseEntity<>(
        announcementService.countUniqueCustomersByCategory(),
        HttpStatus.OK);
  }
  @Secured("READ_ANNOUNCEMENT")
  @GetMapping
  public ResponseEntity<Collection<AnnouncementDto>> getAnnouncements(){

    return new ResponseEntity<>(
        announcementService.getAll(),
        HttpStatus.OK);
  }

  @Secured("READ_ANNOUNCEMENT")
  @GetMapping("/page/{number}")
  public ResponseEntity<Collection<AnnouncementDto>> getPagedAnnouncements(
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
        announcementService.getPagedAnnouncements(--page, size, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, sortDirection, orderBy),
        HttpStatus.OK);
  }

  @Secured("CREATE_ANNOUNCEMENT")
  @PostMapping
  public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody final AnnouncementDto announcement) {

    return new ResponseEntity<>(
        announcementService.create(announcement),
        HttpStatus.CREATED);
  }


  @Secured("UPDATE_ANNOUNCEMENT")
  @PutMapping("/{id}")
  public ResponseEntity<AnnouncementDto> updateOrder(@PathVariable final long id, @RequestBody final AnnouncementDto announcement){
    return new ResponseEntity<>(
        announcementService.update(id, announcement),
        HttpStatus.OK);
  }
  @Secured("DELETE_ANNOUNCEMENT")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAnnouncement(@PathVariable final long id){
    announcementService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  @ExceptionHandler(AnnouncementNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleAnnouncementNotFound(AnnouncementNotFoundException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
  }


  @ExceptionHandler(DomainException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ResponseEntity<String> handleCreateOrUpdateExecution(DomainException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_ACCEPTABLE)
        .body(exception.getMessage());
  }
  @ExceptionHandler(PagingExecution.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ResponseEntity<String> handlePagingExecution(PagingExecution exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_ACCEPTABLE)
        .body(exception.getMessage());
  }

  @ExceptionHandler(AnnouncementIsNotEnabledException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleAnnouncementIsNotEnabledException(AnnouncementIsNotEnabledException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
  }
}
