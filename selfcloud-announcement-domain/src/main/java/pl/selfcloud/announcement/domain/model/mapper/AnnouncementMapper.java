package pl.selfcloud.announcement.domain.model.mapper;

import java.time.LocalDateTime;
import java.util.Optional;
import pl.selfcloud.announcement.api.detail.AnnouncementDetails;
import pl.selfcloud.announcement.api.detail.CustomerDetails;
import pl.selfcloud.announcement.api.dto.AnnouncementDto;
import pl.selfcloud.announcement.api.dto.AnnouncementWithImagesDto;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.announcement.domain.service.exception.MissingValueException;
import pl.selfcloud.common.model.State;

public class AnnouncementMapper {

  public static Announcement mapToAnnouncementBeforeCreating(final AnnouncementDto announcementDto, final CustomerDetails<Long, String> pair){


    return Announcement.builder()
        .details(AnnouncementDetails.builder()
            .title(Optional.ofNullable(announcementDto.getTitle())
                .orElseThrow(() -> new MissingValueException("title")))
            .shortDescription(Optional.ofNullable(announcementDto.getShortDescription())
                .orElseThrow(() -> new MissingValueException("short description")))
            .longDescription(Optional.ofNullable(announcementDto.getLongDescription())
                .orElseThrow(() -> new MissingValueException("long description")))
            .price(Optional.of(announcementDto.getPrice())
                .orElseThrow(() -> new MissingValueException("price")))
            .category(Optional.ofNullable(announcementDto.getCategory())
                .orElseThrow(() -> new MissingValueException("category")))
            .subcategory(Optional.ofNullable(announcementDto.getSubcategory())
                .orElseThrow(() -> new MissingValueException("subcategory")))
            .createdAt(LocalDateTime.now())
            .state(State.ENABLED)
            .build())
        .customerDetails(pair)

        .build();
  }

  public static Announcement mapToAnnouncement(final AnnouncementDto announcementDto){
    return Announcement.builder()
        .details(AnnouncementDetails.builder()
            .title(announcementDto.getTitle())
            .shortDescription(announcementDto.getShortDescription())
            .longDescription(announcementDto.getLongDescription())
            .price(announcementDto.getPrice())
            .createdAt(announcementDto.getCreatedAt())
            .modifiedAt(announcementDto.getModifiedAt())
            .category(announcementDto.getCategory())
            .subcategory(announcementDto.getSubcategory())
            .build())
        .build();
  }


  public static AnnouncementDto mapToAnnouncementDto(final Announcement announcement){
    AnnouncementDetails details = announcement.getDetails();
    return AnnouncementDto.builder()
        .id(announcement.getId())
        .title(details.getTitle())
        .shortDescription(details.getShortDescription())
        .longDescription(details.getLongDescription())
        .price(details.getPrice())
        .createdAt(details.getCreatedAt())
        .modifiedAt(details.getModifiedAt())
        .category(details.getCategory())
        .subcategory(details.getSubcategory())
        .state(details.getState())
        .build();
  }

  public static AnnouncementWithImagesDto mapToAnnouncementWithImages(final Announcement announcement){
    AnnouncementDetails details = announcement.getDetails();
    return AnnouncementWithImagesDto.builder()
        .id(announcement.getId())
        .title(details.getTitle())
        .shortDescription(details.getShortDescription())
        .longDescription(details.getLongDescription())
        .price(details.getPrice())
        .createdAt(details.getCreatedAt())
        .modifiedAt(details.getModifiedAt())
        .category(details.getCategory())
        .subCategory(details.getSubcategory())
        .images(ImageMapper.mapToImagesDto(announcement.getImages()))
        .state(details.getState())
        .build();
  }
}