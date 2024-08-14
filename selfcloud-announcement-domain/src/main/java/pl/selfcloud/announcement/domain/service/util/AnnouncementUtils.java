package pl.selfcloud.announcement.domain.service.util;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.domain.model.detail.AnnouncementDetails;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.service.AnnouncementManagementService;
import pl.selfcloud.announcement.domain.service.exception.AnnouncementNotFoundException;
import pl.selfcloud.announcement.domain.service.exception.AnnouncementOwnerException;
import pl.selfcloud.announcement.domain.service.exception.PriceBelowZeroException;
import pl.selfcloud.announcement.domain.service.exception.UnauthorizedToThisOperationException;
import pl.selfcloud.announcement.domain.service.exception.paging.DatesAreWrongException;
import pl.selfcloud.announcement.domain.service.exception.paging.OrderBySubcategoryException;
import pl.selfcloud.announcement.domain.service.exception.paging.SubcategoryAllowsCategoryToPagingExecution;
import pl.selfcloud.announcement.domain.service.exception.paging.SubcategoryDoesNotMatchCategoryException;
import pl.selfcloud.announcement.domain.service.exception.state.AnnouncementIsNotEnabledException;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;
import pl.selfcloud.security.api.detail.CustomerDetails;

public class AnnouncementUtils {

  public static Announcement announcementExists(final long id, final AnnouncementRepository repository) {
    Optional<Announcement> announcement = repository.findById(id);
    if (announcement.isEmpty()) {
      throw new AnnouncementNotFoundException(id);
    }
    return announcement.get();
  }

  public static void isEnabled(final Announcement announcement){
    if (!announcement.getState().equals(AnnouncementState.APPROVED)) {
      throw new AnnouncementIsNotEnabledException(announcement.getId(), announcement.getState());
    }
  }

  public static void checkCustomerIsValid(final Long ownerId, final Long customerId) {

    if (!Objects.equals(ownerId, customerId)) throw new UnauthorizedToThisOperationException();
  }


  public static Announcement checkAllValueExist(final AnnouncementDto announcementDto, final Announcement announcementToEdit) {
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


  public static void checkAllValuesAreValid(AnnouncementDto announcement) {

    if (announcement.getPrice() < 0) throw new PriceBelowZeroException("");

    isSubcategoryValidForCategory(
        announcement.getCategory(),
        announcement.getSubcategory()
    );

  }

  public static void checkAllValuesAreValid(Announcement announcement) {
    checkAllValuesAreValid(AnnouncementMapper.mapToAnnouncementDto(announcement));
  }

  public static void isSubcategoryValidForCategory(Category category, Subcategory subcategory){
    if (!category.equals(subcategory.getCategory())){
      throw new SubcategoryDoesNotMatchCategoryException(
          category.name(), subcategory.name()
      );
    }
  }

  public static void isDateValid(LocalDateTime created_after, LocalDateTime created_before){
    if (!created_after.isBefore(created_before)) throw new DatesAreWrongException();
  }

  public static void arePagingConditionForEmptyCategoryValid(Subcategory subcategory, OrderBy orderBy){
    if (subcategory != null)
      throw new SubcategoryAllowsCategoryToPagingExecution();
    if (Objects.equals(orderBy.getValue(), OrderBy.SUBCATEGORY.getValue()))
      throw new OrderBySubcategoryException();
  }


  public static CustomerDetails<Long, String> getPrincipal() {
    return (CustomerDetails<Long, String>) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
  }

  public static void checkAnnouncementIsValidToCreateRequest(final Announcement announcement){

    Long announcementOwnerId = announcement.getCustomerDetails().getCustomerId();
    if (announcementOwnerId.equals(getPrincipal().getCustomerId())){
      throw new AnnouncementOwnerException();
    }

  }


}
