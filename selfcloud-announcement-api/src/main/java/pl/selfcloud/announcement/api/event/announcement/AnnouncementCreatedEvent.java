package pl.selfcloud.announcement.api.event.announcement;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.detail.AnnouncementDetailsDto;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementCreatedEvent implements AnnouncementDomainEvent {

  private AnnouncementDetailsDto details;
}