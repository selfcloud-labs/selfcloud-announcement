package pl.selfcloud.announcement.api.event.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.detail.AnnouncementDetailsDto;
import pl.selfcloud.announcement.api.dto.AnnouncementRequestDto;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequestCreatedEvent implements AnnouncementDomainEvent {

  private AnnouncementRequestDto announcementRequestDto;
}