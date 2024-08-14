package pl.selfcloud.announcement.domain.service.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.AnnouncementWithImagesDto;
import pl.selfcloud.announcement.api.state.AnnouncementState;
import pl.selfcloud.announcement.domain.model.Announcement;

public class AnnouncementMapper {

  public static Collection<AnnouncementDto> mapToAnnouncementsDto(List<Announcement> announcements){
    return announcements.stream()
        .filter(announcement -> announcement.getState().equals(AnnouncementState.APPROVED))
        .map(pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper::mapToAnnouncementDto)
        .collect(Collectors.toList());
  }

  public static Collection<AnnouncementWithImagesDto> mapToAnnouncementsWithImagesDto(List<Announcement> announcements){
    return announcements.stream()
        .filter(announcement -> announcement.getState().equals(AnnouncementState.APPROVED))
        .map(pl.selfcloud.announcement.domain.model.mapper.AnnouncementMapper::mapToAnnouncementWithImages)
        .collect(Collectors.toList());
  }

}
