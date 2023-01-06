package ro.demo.ReservationPlatformApp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.demo.ReservationPlatformApp.model.Location;
import ro.demo.ReservationPlatformApp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaLocationRepository extends JpaRepository<Location, UUID> {

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.user = :user")
    List<Location> findAllLocationsByUser (User user);

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.user = :user AND l.id = :id")
    Optional<Location> findLocationByIdAndUser(User user, UUID id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM #{#entityName} l WHERE l.id = :id AND l.user = :user")
    void deleteByLocationId(User user, @Param("id") UUID id);

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.id = :id")
    Optional<Location> findLocationById(UUID id);
}
