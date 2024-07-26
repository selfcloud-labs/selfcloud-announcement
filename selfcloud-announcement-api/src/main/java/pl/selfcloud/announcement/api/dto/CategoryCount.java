package pl.selfcloud.announcement.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.selfcloud.common.model.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoryCount {

  private Category category;
  private Long number;

}
