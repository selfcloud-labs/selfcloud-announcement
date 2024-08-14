package pl.selfcloud.announcement.api.dto;

import jakarta.persistence.Embedded;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.detail.AnnouncementRequestDetailsDto;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequestDto implements Serializable {

  private Long id;
  private Long customerId;
  private AnnouncementDto announcement;
  private AnnouncementRequestDetailsDto details;

}
