package pl.selfcloud.announcement.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.selfcloud.announcement.domain.repository.AnnouncementRepository;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementDomainEventPublisher;

@Service
@AllArgsConstructor
public class AnnouncementAdminService {

  @Autowired
  private final AnnouncementRepository announcementRepository;
  @Autowired
  private final AnnouncementDomainEventPublisher domainEventPublisher;

}
