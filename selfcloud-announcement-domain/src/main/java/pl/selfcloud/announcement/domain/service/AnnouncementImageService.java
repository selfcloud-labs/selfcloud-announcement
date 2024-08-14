package pl.selfcloud.announcement.domain.service;

import static pl.selfcloud.announcement.domain.service.util.AnnouncementMapper.mapToAnnouncementsWithImagesDto;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.announcementExists;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.arePagingConditionForEmptyCategoryValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isDateValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isEnabled;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.isSubcategoryValidForCategory;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.selfcloud.announcement.api.common.OrderBy;
import pl.selfcloud.announcement.api.common.PageSize;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.AnnouncementWithImagesDto;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.model.mapper.ImageMapper;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.service.exception.image.ImageNotFoundException;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;

@Service
@AllArgsConstructor
public class AnnouncementImageService {

  @Autowired
  private final AnnouncementRepository announcementRepository;


  @Cacheable(cacheNames = "getAnnouncementWithImageById", key = "#imageId")
  public byte[] getAnnouncementWithImageById(final long id, final int imageId){
    Announcement announcement = announcementExists(id, announcementRepository);
    isEnabled(announcement);
    if (imageId > announcement.getImages().size() || imageId <= 0){
      throw new ImageNotFoundException();
    }
    return announcement.getImages().get(imageId -1).getImage();
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

  @CacheEvict(cacheNames = {"getAnnouncementWithImageById", "getAnnouncementWithImagesById","countImages"}, key = "#id", allEntries=true)
  public void deleteImagesOfAnnouncement(final long id){
    Announcement announcement = announcementRepository.findById(id).get();
    announcement.getImages().clear();
    announcementRepository.save(announcement);
  }

  @Cacheable(cacheNames = "getAnnouncementWithImagesById", key = "#id")
  public AnnouncementWithImagesDto getAnnouncementWithImagesById(final long id){
    Announcement announcement = announcementExists(id, announcementRepository);
    isEnabled(announcement);
    return AnnouncementMapper.mapToAnnouncementWithImages(announcement);
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
          announcementRepository.findAnnouncementsWithImages(AnnouncementState.APPROVED, minimalPrice, maximalPrice, created_after, created_before, page)
      );
    }else {

      if (subcategory == null){
        return mapToAnnouncementsWithImagesDto(
            announcementRepository.findAnnouncementsWithImages(
                AnnouncementState.APPROVED, category, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }else {
        isSubcategoryValidForCategory(category, subcategory);
        return mapToAnnouncementsWithImagesDto(
            announcementRepository.findAnnouncementsWithImages(
                AnnouncementState.APPROVED, category, subcategory, minimalPrice, maximalPrice, created_after, created_before, page)
        );
      }
    }
  }

  @Cacheable(cacheNames = "countImages", key = "#id")
  public Integer countImages(final long id){
    Announcement announcement = announcementExists(id, announcementRepository);
    return announcement.getImages().size();
  }


  @Transactional
  @CacheEvict(cacheNames = {"getAnnouncementWithImageById", "getAnnouncementWithImagesById","countImages"}, key = "#announcementId")
  public AnnouncementDto addFile(final long announcementId, final List<MultipartFile> files) throws IOException {

    Announcement announcement = announcementExists(announcementId, announcementRepository);
    announcement.getImages().addAll(ImageMapper.mapMultipartFilesToImages(files));
    return AnnouncementMapper.mapToAnnouncementDto(announcementRepository.save(announcement));
  }


}
