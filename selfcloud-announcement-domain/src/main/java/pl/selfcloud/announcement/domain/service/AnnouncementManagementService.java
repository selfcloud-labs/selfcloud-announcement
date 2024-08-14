package pl.selfcloud.announcement.domain.service;

import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.announcementExists;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.checkAllValueExist;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.checkAllValuesAreValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.checkCustomerIsValid;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.getPrincipal;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementDomainEventPublisher;

@Service
@AllArgsConstructor
public class AnnouncementManagementService {

  @Autowired
  private final AnnouncementRepository announcementRepository;
  @Autowired
  private final AnnouncementDomainEventPublisher domainEventPublisher;

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

    Announcement announcementToEdit = announcementExists(id, announcementRepository);

    checkCustomerIsValid(announcementToEdit.getId(), getPrincipal().getCustomerId());
    checkAllValuesAreValid(announcementToEdit);

    Announcement savedAnnouncement = announcementRepository.save(
        checkAllValueExist(announcementDto, announcementToEdit)
    );

    return AnnouncementMapper.mapToAnnouncementDto(savedAnnouncement);
  }


  @CacheEvict(cacheNames =   {"getCategoriesAnnouncementsCount", "getCategoriesContractorsCount", "announcement"}, allEntries=true, key = "#id")
  public void delete(final long id){
    Announcement announcement = announcementExists(id, announcementRepository);
    checkCustomerIsValid(announcement.getId(), getPrincipal().getCustomerId());
    
    announcement.cancel(null);
    announcementRepository.save(announcement);
  }



}
