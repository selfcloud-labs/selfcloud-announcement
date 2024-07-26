package pl.selfcloud.announcement.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.selfcloud.announcement.api.dto.CategoryCount;
import pl.selfcloud.announcement.domain.model.Announcement;
import pl.selfcloud.common.model.Category;
import pl.selfcloud.common.model.State;
import pl.selfcloud.common.model.Subcategory;


@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Optional<Announcement> findById(Long id);

    @Query("SELECT new pl.selfcloud.announcement.api.dto.CategoryCount(a.details.category, COUNT(DISTINCT a.customerDetails.email)) " +
        "FROM Announcement a WHERE a.details.state = :state GROUP BY a.details.category")
    List<CategoryCount> countUniqueContractorsInCategories(@Param("state") State state);

    @Query("SELECT new pl.selfcloud.announcement.api.dto.CategoryCount(a.details.category, COUNT(a.details.category)) " +
        "FROM Announcement a WHERE a.details.state = :state GROUP BY a.details.category")
    List<CategoryCount> countUniqueAnnouncementsInCategories(@Param("state") State state);

    @Query("select distinct a from Announcement a where a.details.state = ?1 and details.price between ?2 and ?3 and details.createdAt between ?4 and ?5")
    List<Announcement> findAnnouncements(State state, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);

    @Query("select distinct a from Announcement a where a.details.state = ?1 and details.category = ?2 and details.price between ?3 and ?4 and details.createdAt between ?5 and ?6")
    List<Announcement> findAnnouncements(State state, Category category, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);

    @Query("select distinct a from Announcement a where a.details.state = ?1 and details.category = ?2 and details.subcategory = ?3 and details.price between ?4 and ?5 and details.createdAt between ?6 and ?7")
    List<Announcement> findAnnouncements(State state, Category category, Subcategory subcategory, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);

    @Query("select distinct a from Announcement a left join fetch a.images where a.details.state = ?1 and details.price between ?2 and ?3 and details.createdAt between ?4 and ?5")
    List<Announcement> findAnnouncementsWithImages(State state, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);
    @Query("select distinct a from Announcement a left join fetch a.images where a.details.state = ?1 and details.category = ?2 and details.price between ?3 and ?4 and details.createdAt between ?5 and ?6")
    List<Announcement> findAnnouncementsWithImages(State state, Category category, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);

    @Query("select distinct a from Announcement a left join fetch a.images where a.details.state = ?1 and details.category = ?2 and details.subcategory = ?3 and details.price between ?4 and ?5 and details.createdAt between ?6 and ?7")
    List<Announcement> findAnnouncementsWithImages(State state, Category category, Subcategory subcategory, double minimalPrice, double maximalPrice, LocalDateTime created_after, LocalDateTime created_before, Pageable pageable);

    List<Announcement> findByCustomerDetailsCustomerIdAndDetailsState(Long customerId, State state);
    List<Announcement> findByCustomerDetailsEmailAndDetailsState(String email, State state);

}
