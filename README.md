# selfcloud-announcement
Allows users to create, manage, and display announcements. It also manages the visibility and categorization of announcements across the platform.

A user can publish an announcement offering their services (e.g., creating a video).
Other users can purchase this service through our platform.

The announcement is one of the basic domain entity which is used in Selflcoud environment.

## Announcement
The [Announcement](selfcloud-announcement-domain/src/main/java/pl/selfcloud/announcement/domain/model/Announcement.java) 
class is adapted to work with [eventuate-tram](https://eventuate.io/abouteventuatetram.html),
a tool that makes using saga pattern easier.

```java
@Table(name = "announcements")
public class Announcement implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Embedded
  private AnnouncementDetails details;
  @Embedded
  private CustomerDetails<Long, String> customerDetails;
  @Enumerated(EnumType.STRING)
  private AnnouncementState state;

  public static ResultWithDomainEvents<Announcement, AnnouncementDomainEvent> create(
      AnnouncementDto announcementDto) {

    Announcement announcement = AnnouncementMapper.mapToAnnouncementBeforeCreating(announcementDto,
        getPrincipal());

    AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(
        AnnouncementDetailsMapper.mapToDetailsDto(announcement.details));

    return new ResultWithDomainEvents<>(announcement, Collections.singletonList(event));
  }
}
```

## Controllers
The work with this microservice enables three controllers:
* [AnnouncementController](selfcloud-announcement-web/src/main/java/pl/selfcloud/announcement/web/controller/AnnouncementController.java) -
CRUD operations on announcements that are performed by the owner,
* [ImageController](selfcloud-announcement-web/src/main/java/pl/selfcloud/announcement/web/controller/ImageController.java) -
  CRUD operations on announcements with images that are performed by the owner,
* [AnnouncementRequestController](selfcloud-announcement-web/src/main/java/pl/selfcloud/announcement/web/controller/AnnouncementRequestController.java) -
other users can submit requests for the announcement.

The endpoints are secured by the `AUTHORITIES` and the `ROLES` provided in the token by 
_selcloud-security_.

Example:
```java
  @Secured("CREATE_ANNOUNCEMENT")
  @PostMapping
  public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody final AnnouncementDto announcement) {

    return new ResponseEntity<>(
        announcementManagementService.create(announcement),
        HttpStatus.CREATED);
  }

```