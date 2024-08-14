package pl.selfcloud.announcement.domain.model;


import static java.util.Collections.singletonList;
import static pl.selfcloud.announcement.domain.service.util.AnnouncementUtils.getPrincipal;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.domain.model.detail.AnnouncementDetails;
import pl.selfcloud.announcement.api.event.announcement.AnnouncementCancelledEvent;
import pl.selfcloud.announcement.api.event.announcement.AnnouncementCreatedEvent;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.api.event.announcement.AnnouncementRejectedEvent;
import pl.selfcloud.announcement.api.event.announcement.AnnouncementUnblockedEvent;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.api.state.change.AnnouncementCancelReason;
import pl.selfcloud.announcement.api.state.change.AnnouncementRejectReason;
import pl.selfcloud.announcement.api.state.change.AnnouncementUnblockReason;
import pl.selfcloud.announcement.domain.model.detail.mapper.AnnouncementDetailsMapper;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;
import pl.selfcloud.announcement.domain.service.exception.state.UnsupportedStateTransitionException;
import pl.selfcloud.security.api.detail.CustomerDetails;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "announcements")
public class Announcement implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Embedded
  private AnnouncementDetails details;
  @Embedded
  private CustomerDetails<Long, String> customerDetails;
  @Enumerated(EnumType.STRING)
  private AnnouncementState state;

  @OneToMany(targetEntity = Image.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "announcements_images",
      joinColumns = @JoinColumn(
          name = "announcement_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "image_id", referencedColumnName = "id"))
  private List<Image> images;


  public static ResultWithDomainEvents<Announcement, AnnouncementDomainEvent> create(
      AnnouncementDto announcementDto){

    Announcement announcement = AnnouncementMapper.mapToAnnouncementBeforeCreating(announcementDto, getPrincipal());

    //TODO probably the eventuate tram uses java 8 and there is a problem with date format mapping
    announcement.getDetails().setCreatedAt(null);

    AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(AnnouncementDetailsMapper.mapToDetailsDto(announcement.details));

    return new ResultWithDomainEvents<>(announcement, Collections.singletonList(event));
  }
  public  List<AnnouncementDomainEvent> reject(final AnnouncementRejectReason reason){
    switch (state) {
      case PENDING:
        this.state = AnnouncementState.REJECTED;
        return singletonList(new AnnouncementRejectedEvent(this.id, reason));
      default:
        throw new UnsupportedStateTransitionException(state);
    }
  }

  public  List<AnnouncementDomainEvent> cancel(final AnnouncementCancelReason reason){
    switch (state) {
      case APPROVED:
        this.state = AnnouncementState.CANCELLED;
        return singletonList(new AnnouncementCancelledEvent(this.id, reason));
      default:
        throw new UnsupportedStateTransitionException(state);
    }
  }

  public  List<AnnouncementDomainEvent> block(final AnnouncementRejectReason reason){
    switch (state) {
      case APPROVED:
        this.state = AnnouncementState.BLOCKED;
        return singletonList(new AnnouncementRejectedEvent(this.id, reason));
      default:
        throw new UnsupportedStateTransitionException(state);
    }
  }

  public  List<AnnouncementDomainEvent> unblock(final AnnouncementUnblockReason reason){
    switch (state) {
      case BLOCKED:
        this.state = AnnouncementState.APPROVED;
        return singletonList(new AnnouncementUnblockedEvent(this.id, reason));
      default:
        throw new UnsupportedStateTransitionException(state);
    }
  }

}

