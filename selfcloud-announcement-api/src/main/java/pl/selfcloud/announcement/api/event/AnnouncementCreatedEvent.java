package pl.selfcloud.announcement.api.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.detail.AnnouncementDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementCreatedEvent implements AnnouncementDomainEvent {

  private AnnouncementDetails details;
}