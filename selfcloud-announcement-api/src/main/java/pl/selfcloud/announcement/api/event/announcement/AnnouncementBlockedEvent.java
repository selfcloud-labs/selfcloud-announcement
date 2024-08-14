package pl.selfcloud.announcement.api.event.announcement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.api.state.change.AnnouncementBlockReason;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementBlockedEvent implements AnnouncementDomainEvent {

  private Long id;
  private AnnouncementBlockReason reason;
}
