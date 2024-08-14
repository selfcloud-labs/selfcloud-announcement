package pl.selfcloud.announcement.domain.model.detail.mapper;

import pl.selfcloud.announcement.api.detail.AnnouncementDetailsDto;
import pl.selfcloud.announcement.domain.model.detail.AnnouncementDetails;

public class AnnouncementDetailsMapper {

  public static AnnouncementDetails mapToDetails(final AnnouncementDetailsDto detailsDto){

    return AnnouncementDetails.builder()
        .title(detailsDto.getTitle())
        .category(detailsDto.getCategory())
        .subcategory(detailsDto.getSubcategory())
        .shortDescription(detailsDto.getShortDescription())
        .longDescription(detailsDto.getLongDescription())
        .price(detailsDto.getPrice())
        .createdAt(detailsDto.getCreatedAt())
        .modifiedAt(detailsDto.getModifiedAt())
        .estimatedDeliveryTime(detailsDto.getEstimatedDeliveryTime())
        .supportAfterImplementation(detailsDto.getSupportAfterImplementation())
        .availablePatchesNumber(detailsDto.getAvailablePatchesNumber())
        .build();
  }

  public static AnnouncementDetailsDto mapToDetailsDto(final AnnouncementDetails details) {
    return AnnouncementDetailsDto.builder()
        .title(details.getTitle())
        .category(details.getCategory())
        .subcategory(details.getSubcategory())
        .shortDescription(details.getShortDescription())
        .longDescription(details.getLongDescription())
        .price(details.getPrice())
        .createdAt(details.getCreatedAt())
        .modifiedAt(details.getModifiedAt())
        .estimatedDeliveryTime(details.getEstimatedDeliveryTime())
        .supportAfterImplementation(details.getSupportAfterImplementation())
        .availablePatchesNumber(details.getAvailablePatchesNumber())
        .build();
  }
}
