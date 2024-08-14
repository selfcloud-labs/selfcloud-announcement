package pl.selfcloud.announcement.api.detail;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnnouncementRequestDetailsDto implements Serializable {

  private Long id;
}
