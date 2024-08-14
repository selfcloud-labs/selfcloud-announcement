package pl.selfcloud.announcement.domain.service;

import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.announcementExists;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.checkAnnouncementIsValidToCreateRequest;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.selfcloud.announcement.api.dto.AnnouncementRequestDto;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.AnnouncementRequest;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementRequestMapper;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.repository.AnnouncementRequestRepository;
import pl.selfcloud.announcement.domain.service.exception.AnnouncementNotFoundException;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementDomainEventPublisher;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementRequestDomainEventPublisher;

@AllArgsConstructor
@Service
public class AnnouncementRequestService {

  @Autowired
  private final AnnouncementRequestRepository announcementRequestRepository;
  @Autowired
  private final AnnouncementRepository announcementRepository;
  @Autowired
  private final AnnouncementRequestDomainEventPublisher domainEventPublisher;

  public AnnouncementRequestDto create(final Long announcementId){

    Announcement announcement = announcementExists(announcementId, announcementRepository);

    checkAnnouncementIsValidToCreateRequest(announcement);

    ResultWithDomainEvents<AnnouncementRequest, AnnouncementDomainEvent> requestResultWithEvents =
        AnnouncementRequest.create(announcement);

    AnnouncementRequest createdRequest = requestResultWithEvents.result;
    announcementRequestRepository.save(createdRequest);

    domainEventPublisher.publish(createdRequest, requestResultWithEvents.events);
    return AnnouncementRequestMapper.mapToAnnouncementRequestDto(createdRequest);
  }

}
