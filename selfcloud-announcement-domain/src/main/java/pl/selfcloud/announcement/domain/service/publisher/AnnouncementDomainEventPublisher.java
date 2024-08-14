package pl.selfcloud.announcement.domain.service.publisher;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Component;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.Announcement;

@Component
public class AnnouncementDomainEventPublisher extends
    AbstractAggregateDomainEventPublisher<Announcement, AnnouncementDomainEvent> {

  public AnnouncementDomainEventPublisher(DomainEventPublisher eventPublisher) {
    super(eventPublisher, Announcement.class, Announcement::getId);
  }

}