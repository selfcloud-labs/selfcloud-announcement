package pl.selfcloud.announcement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.announcement.domain.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
