package pl.selfcloud.announcement.domain.model.mapper;

import pl.selfcloud.announcement.api.dto.AnnouncementRequestDto;
import pl.selfcloud.announcement.domain.model.AnnouncementRequest;
import pl.selfcloud.announcement.domain.model.detail.mapper.AnnouncementRequestDetailsMapper;

public class AnnouncementRequestMapper {


  public static AnnouncementRequest mapToAnnouncementRequest(final AnnouncementRequestDto requestDto){
    return AnnouncementRequest.builder()
        .id(requestDto.getId())
        .customerId(requestDto.getCustomerId())
        .announcement(AnnouncementMapper.mapToAnnouncement(requestDto.getAnnouncement()))
        .details(AnnouncementRequestDetailsMapper.mapToRequestDetails(requestDto.getDetails()))
        .build();
  }

  public static AnnouncementRequestDto mapToAnnouncementRequestDto(final AnnouncementRequest request){
    return AnnouncementRequestDto.builder()
        .id(request.getId())
        .customerId(request.getCustomerId())
        .announcement(AnnouncementMapper.mapToAnnouncementDto(request.getAnnouncement()))
        .details(AnnouncementRequestDetailsMapper.mapToRequestDetailsDto(request.getDetails()))
        .build();
  }
}
