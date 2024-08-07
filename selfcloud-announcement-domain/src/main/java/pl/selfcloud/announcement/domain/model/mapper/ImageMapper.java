package pl.selfcloud.announcement.domain.model.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.selfcloud.announcement.api.dto.ImageDto;
import pl.selfcloud.announcement.domain.model.Image;
import pl.selfcloud.announcement.domain.service.exception.image.FileSizeTooLargeException;
import pl.selfcloud.announcement.domain.service.exception.image.InvalidPathSequenceException;

public class ImageMapper {

  public static Image mapToImage(final ImageDto imageDto) {
    return Image.builder()
        .id(imageDto.getId())
        .title(imageDto.getTitle())
        .image(imageDto.getImage())
        .extension(imageDto.getExtension())
        .build();
  }

  public static List<Image> mapToImages(final List<ImageDto> imagesDto) {
    return imagesDto.stream()
        .map(ImageMapper::mapToImage)
        .collect(Collectors.toList());
  }

  public static ImageDto mapToImageDto(final Image image) {
    return ImageDto.builder()
        .id(image.getId())
        .title(image.getTitle())
        .image(image.getImage())
        .extension(image.getExtension())
        .build();
  }

  public static List<Image> mapMultipartFilesToImages(final List<MultipartFile> files) throws IOException {

    List<Image> images = new ArrayList<>();

    for (MultipartFile file : files) {

      String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      if(fileName.contains("..")) {
        throw new InvalidPathSequenceException();
      }
      if (file.getBytes().length > (1024 * 1024)) {
        throw new FileSizeTooLargeException();
      }

      Image image = Image.builder()
          .image(file.getBytes())
          .title(getFileName(fileName))
          .extension(getFileExtension(fileName))
          .build();

      images.add(image);
    }

    return images;
  }

  private static String getFileName(final String file){
    return splitFileName(file)[0];
  }

  private static String getFileExtension(final String file){
    return splitFileName(file)[1];
  }

  private static String[] splitFileName(final String fileName) {
    String[] parts = fileName.split("\\.");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Wrong file format: " + fileName);
    }
    return parts;
  }

  public static List<ImageDto> mapToImagesDto(final List<Image> images) {
    return images.stream()
        .map(ImageMapper::mapToImageDto)
        .collect(Collectors.toList());
  }
}
