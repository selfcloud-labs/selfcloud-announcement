package pl.selfcloud.announcement.domain.model;


import static pl.selfcloud.announcement.domain.service.AnnouncementService.getPrincipal;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.detail.AnnouncementDetails;
import pl.selfcloud.announcement.api.detail.CustomerDetails;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.event.AnnouncementCreatedEvent;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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

        AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(announcement.details);

        return new ResultWithDomainEvents<>(announcement, Collections.singletonList(event));
    }

}

