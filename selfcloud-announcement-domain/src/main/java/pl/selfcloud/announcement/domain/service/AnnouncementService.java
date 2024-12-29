package pl.selfcloud.announcement.domain.service;


import static pl.selfcloud.announcement.domain.service.util.AnnouncementMapper.mapToAnnouncementsDto;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.announcementExists;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.arePagingConditionForEmptyCategoryValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isDateValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isEnabled;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isSubcategoryValidForCategory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.api.common.PageSize;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.CategoryCount;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;


@Service
@AllArgsConstructor
public class AnnouncementService {

  @Autowired
  private final AnnouncementRepository announcementRepository;


  @Cacheable(cacheNames = "announcement", key = "#id")
  public AnnouncementDto getById(final long id){
    Announcement announcement = announcementExists(id, announcementRepository);
    isEnabled(announcement);
    return AnnouncementMapper.mapToAnnouncementDto(announcement);
  }

  public List<AnnouncementDto> getAllByCustomerId(final long id){

    return announcementRepository
        .findByCustomerDetailsCustomerIdAndState(id, AnnouncementState.APPROVED)
        .stream().map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }

  public List<AnnouncementDto> getAllByCustomerEmail(final String email){

    return announcementRepository
        .findByCustomerDetailsEmailAndState(email, AnnouncementState.APPROVED)
        .stream().map(AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }


  @Cacheable(cacheNames = "announcement")
  public Collection<AnnouncementDto> getAll(){
    return announcementRepository.findAll().stream()
        .filter(announcement -> announcement.getState().equals(AnnouncementState.APPROVED))
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
          announcementRepository.findAnnouncements(AnnouncementState.APPROVED, minimalPrice, maximalPrice, created_after, created_before, page)
      );
    }else {

      if (subcategory == null){
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncements(
                AnnouncementState.APPROVED, category, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }else {
        isSubcategoryValidForCategory(category, subcategory);
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncements(
                AnnouncementState.APPROVED, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }
    }
  }

  public Collection<AnnouncementDto> getPagedAnnouncementsWithImages(
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
          announcementRepository.findAnnouncementsWithImages(AnnouncementState.APPROVED, minimalPrice, maximalPrice, created_after, created_before, page)
      );
    }else {

      if (subcategory == null){
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncementsWithImages(
                AnnouncementState.APPROVED, category, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }else {
        isSubcategoryValidForCategory(category, subcategory);
        return mapToAnnouncementsDto(
            announcementRepository.findAnnouncementsWithImages(
                AnnouncementState.APPROVED, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }
    }
  }


  @Cacheable(cacheNames = "getCategoriesAnnouncementsCount")
  public Map<Category, Long> getCategoriesAnnouncementsCount() {

    return announcementRepository
        .countUniqueAnnouncementsInCategories(AnnouncementState.APPROVED)
        .stream()
        .collect(Collectors.toMap(CategoryCount::getCategory, CategoryCount::getNumber));
  }

  @Cacheable(cacheNames = "getCategoriesContractorsCount")
  public Map<Category, Long> countUniqueCustomersByCategory() {

    return announcementRepository
        .countUniqueContractorsInCategories(AnnouncementState.APPROVED)
        .stream()
        .collect(Collectors.toMap(CategoryCount::getCategory, CategoryCount::getNumber));
  }


}
