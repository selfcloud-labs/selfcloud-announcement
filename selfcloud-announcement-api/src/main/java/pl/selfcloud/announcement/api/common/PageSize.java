package pl.selfcloud.announcement.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PageSize {
  SMALL(10), MEDIUM(25), LARGE(50);

  private final int size;

}
