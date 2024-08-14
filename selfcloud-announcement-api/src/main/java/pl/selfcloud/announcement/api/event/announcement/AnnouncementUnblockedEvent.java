package pl.selfcloud.announcement.api.event.announcement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.event.AnnouncementDomainEvent;
import pl.selfcloud.announcement.api.state.change.AnnouncementUnblockReason;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementUnblockedEvent implements AnnouncementDomainEvent {

  private Long id;
  private AnnouncementUnblockReason reason;
}
