package pl.selfcloud.announcement.domain.service.publisher;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Component;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.model.AnnouncementRequest;

@Component
public class AnnouncementRequestDomainEventPublisher extends
    AbstractAggregateDomainEventPublisher<AnnouncementRequest, AnnouncementDomainEvent> {

  public AnnouncementRequestDomainEventPublisher(DomainEventPublisher eventPublisher) {
    super(eventPublisher, AnnouncementRequest.class, AnnouncementRequest::getId);
  }
}