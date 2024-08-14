package pl.selfcloud.announcement.api.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto implements Serializable {

  private long id;
  private String title;
  private String shortDescription;
  private String longDescription;
  private Category category;
  private Subcategory subcategory;
  private double price;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private AnnouncementState state;

}
