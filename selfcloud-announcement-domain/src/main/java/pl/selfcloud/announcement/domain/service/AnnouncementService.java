package pl.selfcloud.announcement.domain.service;


import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.api.common.PageSize;
import pl.selfcloud.announcement.api.detail.AnnouncementDetails;
import pl.selfcloud.announcement.api.detail.CustomerDetails;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.AnnouncementWithImagesDto;
import pl.selfcloud.announcement.api.dto.CategoryCount;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.service.exception.AnnouncementNotFoundException;
import pl.selfcloud.announcement.domain.service.exception.PriceBelowZeroException;
import pl.selfcloud.announcement.domain.service.exception.image.ImageNotFoundException;
import pl.selfcloud.announcement.domain.service.exception.paging.DatesAreWrongException;
import pl.selfcloud.announcement.domain.service.exception.paging.OrderBySubcategoryException;
import pl.selfcloud.announcement.domain.service.exception.paging.SubcategoryAllowsCategoryToPagingExecution;
import pl.selfcloud.announcement.domain.service.exception.paging.SubcategoryDoesNotMatchCategoryException;
import pl.selfcloud.announcement.domain.service.exception.state.AnnouncementIsDisable;
import pl.selfcloud.announcement.domain.service.exception.state.AnnouncementIsNotEnabledException;
import pl.selfcloud.announcement.domain.service.exception.state.AnnouncementIsRemoved;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.model.mapper.ImageMapper;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementDomainEventPublisher;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.State;
import pl.selfcloud.common.model.Subcategory;


@Service
@AllArgsConstructor
public class AnnouncementService {

  @Autowired
  private final AnnouncementRepository announcementRepository;
  @Autowired
  private final AnnouncementDomainEventPublisher domainEventPublisher;


  @Cacheable(cacheNames = "announcement", key = "#id")
  public AnnouncementDto getById(final long id){
    Announcement announcement = announcementExists(id);
    isEnabled(announcement);
    return AnnouncementMapper.mapToAnnouncementDto(announcement);
  }

  public List<AnnouncementDto> getAllByCustomerId(final long id){

    return announcementRepository
        .findByCustomerDetailsCustomerIdAndDetailsState(id, State.ENABLED)
        .stream().map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }

  public List<AnnouncementDto> getAllByCustomerEmail(final String email){

    return announcementRepository
        .findByCustomerDetailsEmailAndDetailsState(email, State.ENABLED)
        .stream().map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }
  @Cacheable(cacheNames = "getAnnouncementWithImagesById", key = "#id")
  public AnnouncementWithImagesDto getAnnouncementWithImagesById(final long id){
    Announcement announcement = announcementExists(id);
    isEnabled(announcement);
    return AnnouncementMapper.mapToAnnouncementWithImages(announcement);
  }

  @Cacheable(cacheNames = "getAnnouncementWithImageById", key = "#imageId")
  public byte[] getAnnouncementWithImageById(final long id, final int imageId){
    Announcement announcement = announcementExists(id);
    isEnabled(announcement);
    if (imageId > announcement.getImages().size() || imageId <= 0){
      throw new ImageNotFoundException();
    }
    return announcement.getImages().get(imageId -1).getImage();
  }

  @Cacheable(cacheNames = "announcement")
  public Collection<AnnouncementDto> getAll(){
    return announcementRepository.findAll().stream()
        .filter(announcement -> announcement.getDetails().getState().equals(State.ENABLED))
        .map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }

  public Collection<AnnouncementDto> getPagedAnnouncements(
      final int pageNumber, final PageSize size,
      final Category category, final Subcategory subcategory,
      final Double minimalPrice, final Double maximalPrice,
      final LocalDateTime created_after, final LocalDateTime created_before,
      final Direction direction, final OrderBy orderBy) {

    isDateValid(created_after, created_before);

    Sort sort = Sort.by(direction, orderBy.getValue());
    Pageable page = PageRequest.of(pageNumber, size.getSize(), sort);

    if (category == null) {

      arePagingConditionForEmptyCategoryValid(subcategory, orderBy);

      return mapToAnnouncementsDto(
          announcementRepository.findAnnouncements(State.ENABLED, minimalPrice, maximalPrice, created_after, created_before, page)
      );
    }else {

      if (subcategory == null){
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncements(
                State.ENABLED, category, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }else {
        isSubcategoryValidForCategory(category, subcategory);
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncements(
                State.ENABLED, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }
    }
  }

  public Collection<AnnouncementWithImagesDto> getPagedAnnouncementsWithImages(
      final int pageNumber, final PageSize size, final Category category, final Subcategory subcategory,
      final Double minimalPrice, final Double maximalPrice,
      final LocalDateTime created_after, final LocalDateTime created_before,
      final Direction direction, final OrderBy orderBy) {

    isDateValid(created_after, created_before);

    Sort sort = Sort.by(direction, orderBy.getValue());
    Pageable page = PageRequest.of(pageNumber, size.getSize(), sort);

    if (category == null) {

      arePagingConditionForEmptyCategoryValid(subcategory, orderBy);

      return mapToAnnouncementsWithImagesDto(
          announcementRepository.findAnnouncementsWithImages(State.ENABLED, minimalPrice, maximalPrice, created_after, created_before, page)
      );
    }else {

      if (subcategory == null){
        return mapToAnnouncementsWithImagesDto(
            announcementRepository.findAnnouncementsWithImages(
                State.ENABLED, category, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }else {
        isSubcategoryValidForCategory(category, subcategory);
        return mapToAnnouncementsWithImagesDto(
            announcementRepository.findAnnouncementsWithImages(
                State.ENABLED, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }
    }
  }

  @Cacheable(cacheNames = "getCategoriesAnnouncementsCount")
  public Map<Category, Long> getCategoriesAnnouncementsCount() {

    return announcementRepository
        .countUniqueAnnouncementsInCategories(State.ENABLED)
        .stream()
        .collect(Collectors.toMap(CategoryCount::getCategory, CategoryCount::getNumber));
  }

  @Cacheable(cacheNames = "getCategoriesContractorsCount")
  public Map<Category, Long> countUniqueCustomersByCategory() {

    return announcementRepository
        .countUniqueContractorsInCategories(State.ENABLED)
        .stream()
        .collect(Collectors.toMap(CategoryCount::getCategory, CategoryCount::getNumber));
  }

  @Cacheable(cacheNames = "countImages", key = "#id")
  public Integer countImages(final long id){
    Announcement announcement = announcementExists(id);
    return announcement.getImages().size();
  }

  @Transactional
  @CacheEvict(cacheNames = {"getCategoriesAnnouncementsCount", "getCategoriesContractorsCount"}, allEntries=true)
  public AnnouncementDto create(final AnnouncementDto announcement) {
    checkAllValuesAreValid(announcement);

    ResultWithDomainEvents<Announcement, AnnouncementDomainEvent> announcementResultWithEvents =
        Announcement.create(announcement);

    Announcement createdAnnouncement = announcementResultWithEvents.result;
    announcementRepository.save(createdAnnouncement);

    domainEventPublisher.publish(createdAnnouncement, announcementResultWithEvents.events);
    return AnnouncementMapper.mapToAnnouncementDto(createdAnnouncement);
  }

  @CachePut(cacheNames = "announcement", key = "#id")
  public AnnouncementDto update(final long id, final AnnouncementDto announcementDto){

    Announcement announcementToEdit = announcementExists(id);
    checkAllValuesAreValid(announcementToEdit);

    Announcement savedAnnouncement = announcementRepository.save(
        checkAllValueExist(announcementDto, announcementToEdit));
    return AnnouncementMapper.mapToAnnouncementDto(savedAnnouncement);
  }

  @Transactional
  @CacheEvict(cacheNames = {"getAnnouncementWithImageById", "getAnnouncementWithImagesById","countImages"}, key = "#announcementId")
  public AnnouncementDto addFile(final long announcementId, final List<MultipartFile> files) throws IOException {

    Announcement announcement = announcementExists(announcementId);
    announcement.getImages().addAll(ImageMapper.mapMultipartFilesToImages(files));
    return AnnouncementMapper.mapToAnnouncementDto(announcementRepository.save(announcement));
  }

  @CacheEvict(cacheNames =   {"getCategoriesAnnouncementsCount", "getCategoriesContractorsCount", "announcement"}, allEntries=true, key = "#id")
  public void delete(final long id){
    Announcement announcement = announcementExists(id);
    announcement.getDetails().setState(State.REMOVED);
    announcementRepository.save(announcement);
  }

  @CacheEvict(cacheNames = {"getAnnouncementWithImageById", "getAnnouncementWithImagesById","countImages"}, key = "#id", allEntries=true)
  public void deleteImagesOfAnnouncement(final long id){
    Announcement announcement = announcementRepository.findById(id).get();
    announcement.getImages().clear();
    announcementRepository.save(announcement);
  }

  @CacheEvict(cacheNames = {"getAnnouncementWithImageById", "getAnnouncementWithImagesById","countImages"}, key = "#id", allEntries=true)
  public void deleteImageOfAnnouncement(final long id, final int imageId){
    Announcement announcement = announcementRepository.findById(id).get();
    if (imageId > announcement.getImages().size() || imageId <= 0){
      throw new ImageNotFoundException();
    }
    announcement.getImages().remove(imageId-1);
    announcementRepository.save(announcement);
  }

  private Announcement announcementExists(final long id) {
    Optional<Announcement> announcement = announcementRepository.findById(id);
    if (announcement.isEmpty()) {
      throw new AnnouncementNotFoundException(id);
    }
    return announcement.get();
  }

  private void isEnabled(final Announcement announcement){
    if (!announcement.getDetails().getState().equals(State.ENABLED)) {
      throw new AnnouncementIsNotEnabledException(announcement.getId(), announcement.getDetails().getState());
    }
  }

  private void announcementIsNotDisabled(final Announcement announcement){
    if (announcement.getDetails().getState().equals(State.DISABLED)){
      throw new AnnouncementIsDisable(announcement.getId());
    }
  }

  private void announcementIsNotRemoved(final Announcement announcement){
    if (announcement.getDetails().getState().equals(State.REMOVED)){
      throw new AnnouncementIsRemoved(announcement.getId());
    }
  }

  private void announcementIsNotEnded(final Announcement announcement){
    if (announcement.getDetails().getState().equals(State.ENDED)){
      throw new AnnouncementIsRemoved(announcement.getId());
    }
  }

  private static Announcement checkAllValueExist(final AnnouncementDto announcementDto, final Announcement announcementToEdit) {
    AnnouncementDetails details = announcementToEdit.getDetails();
    Optional.ofNullable(announcementDto.getTitle()).ifPresentOrElse(
        details::setTitle, () -> {});
    Optional.ofNullable(announcementDto.getShortDescription()).ifPresentOrElse(
        details::setShortDescription, () -> {});
    Optional.ofNullable(announcementDto.getLongDescription()).ifPresentOrElse(
        details::setLongDescription, () -> {});
    Optional.of(announcementDto.getPrice()).ifPresentOrElse(
        details::setPrice, () -> {});
    Optional.ofNullable(announcementDto.getCategory()).ifPresentOrElse(
        details::setCategory, () -> {});
    Optional.ofNullable(announcementDto.getSubcategory()).ifPresentOrElse(
        details::setSubcategory, () -> {});
    details.setModifiedAt(LocalDateTime.now());

    return announcementToEdit;
  }


  private void checkAllValuesAreValid(AnnouncementDto announcement) {

    if (announcement.getPrice() < 0) throw new PriceBelowZeroException("");

    isSubcategoryValidForCategory(
        announcement.getCategory(),
        announcement.getSubcategory()
    );

  }

  private void checkAllValuesAreValid(Announcement announcement) {
    checkAllValuesAreValid(AnnouncementMapper.mapToAnnouncementDto(announcement));
  }

  private void isSubcategoryValidForCategory(Category category, Subcategory subcategory){
    if (!category.equals(subcategory.getCategory())){
      throw new SubcategoryDoesNotMatchCategoryException(
          category.name(), subcategory.name()
      );
    }
  }

  private void isDateValid(LocalDateTime created_after, LocalDateTime created_before){
    if (!created_after.isBefore(created_before)) throw new DatesAreWrongException();
  }

  private void arePagingConditionForEmptyCategoryValid(Subcategory subcategory, OrderBy orderBy){
    if (subcategory != null)
      throw new SubcategoryAllowsCategoryToPagingExecution();
    if (Objects.equals(orderBy.getValue(), OrderBy.SUBCATEGORY.getValue()))
      throw new OrderBySubcategoryException();
  }
  private Collection<AnnouncementDto> mapToAnnouncementsDto(List<Announcement> announcements){
    return announcements.stream()
        .filter(announcement -> announcement.getDetails().getState().equals(State.ENABLED))
        .map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }

  private Collection<AnnouncementWithImagesDto> mapToAnnouncementsWithImagesDto(List<Announcement> announcements){
    return announcements.stream()
        .filter(announcement -> announcement.getDetails().getState().equals(State.ENABLED))
        .map(AnnouncementMapper::mapToAnnouncementWithImages)
        .collect(Collectors.toList());
  }

  public static CustomerDetails<Long, String> getPrincipal() {
    return (CustomerDetails<Long, String>) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
  }

}
