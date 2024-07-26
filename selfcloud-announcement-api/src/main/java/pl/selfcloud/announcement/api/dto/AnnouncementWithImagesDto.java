package pl.selfcloud.announcement.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.State;
import pl.selfcloud.common.model.Subcategory;

@Builder
@Getter
public class AnnouncementWithImagesDto {
  private long id;
  private String title;
  private String shortDescription;
  private String longDescription;
  private Category category;
  private Subcategory subCategory;
  private double price;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private List<ImageDto> images;
  private State state;
}