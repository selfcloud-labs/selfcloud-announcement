package pl.selfcloud.announcement.domain.model.detail;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.State;
import pl.selfcloud.common.model.Subcategory;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Embeddable
@NoArgsConstructor
public class AnnouncementDetails {

  private String title;
  @Enumerated(EnumType.STRING)
  private Category category;
  @Enumerated(EnumType.STRING)
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

