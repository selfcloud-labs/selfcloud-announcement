package pl.selfcloud.announcement.domain.model.detail.mapper;

import pl.selfcloud.announcement.api.detail.AnnouncementRequestDetailsDto;
import pl.selfcloud.announcement.domain.model.detail.AnnouncementRequestDetails;

public class AnnouncementRequestDetailsMapper {

  public static AnnouncementRequestDetails mapToRequestDetails(final AnnouncementRequestDetailsDto requestDetailsDto){
    return AnnouncementRequestDetails.builder()
        .build();
  }

  public static AnnouncementRequestDetailsDto mapToRequestDetailsDto(final AnnouncementRequestDetails requestDetails){
    return AnnouncementRequestDetailsDto.builder()
        .build();
  }
}
