package pl.selfcloud.announcement.api.detail;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.Subcategory;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnnouncementDetailsDto  implements Serializable {

  private String title;
  private Category category;
  private Subcategory subcategory;
  private String shortDescription;
  private String longDescription;
  private double price;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  private LocalDateTime estimatedDeliveryTime;
  private LocalDateTime supportAfterImplementation;
  private int availablePatchesNumber;

}
