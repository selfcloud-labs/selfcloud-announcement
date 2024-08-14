package pl.selfcloud.announcement.domain.model;

import static java.util.Collections.singletonList;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.getPrincipal;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.announcement.api.event.announcement.AnnouncementCreatedEvent;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.api.event.request.AnnouncementRequestCreatedEvent;
import pl.selfcloud.announcement.api.state.AnnouncementRequestState;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.domain.model.detail.AnnouncementRequestDetails;
import pl.selfcloud.announcement.domain.model.detail.mapper.AnnouncementDetailsMapper;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementRequestMapper;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests")
public class AnnouncementRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long customerId;
  @Enumerated
  private AnnouncementRequestState state;

  @ManyToOne
  private Announcement announcement;

  @Embedded
  private AnnouncementRequestDetails details;

  public static ResultWithDomainEvents<AnnouncementRequest, AnnouncementDomainEvent> create(
      final Announcement announcement){


//    announcement.getDetails().setCreatedAt(null);
//    announcement.getDetails().setModifiedAt(null);

    AnnouncementRequest request = AnnouncementRequest.builder()
        .customerId(getPrincipal().getCustomerId())
        .details(new AnnouncementRequestDetails())
        .announcement(announcement)
        .state(AnnouncementRequestState.CREATED)
        .build();

    //TODO probably the eventuate tram uses java 8 and there is a problem with date format mapping

    AnnouncementRequestCreatedEvent event = new AnnouncementRequestCreatedEvent(
        AnnouncementRequestMapper.mapToAnnouncementRequestDto(request)
    );

    return new ResultWithDomainEvents<>(request, singletonList(event));
  }

}
