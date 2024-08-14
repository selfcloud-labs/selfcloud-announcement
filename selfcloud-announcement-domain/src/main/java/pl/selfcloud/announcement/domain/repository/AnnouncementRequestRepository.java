package pl.selfcloud.announcement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.announcement.domain.model.AnnouncementRequest;

@Repository
public interface AnnouncementRequestRepository extends JpaRepository<AnnouncementRequest, Long> {

}
