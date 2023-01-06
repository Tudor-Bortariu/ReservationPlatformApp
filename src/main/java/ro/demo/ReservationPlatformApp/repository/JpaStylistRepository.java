package ro.demo.ReservationPlatformApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.demo.ReservationPlatformApp.model.Location;
import ro.demo.ReservationPlatformApp.model.Service;
import ro.demo.ReservationPlatformApp.model.Stylist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaStylistRepository extends JpaRepository<Stylist, UUID> {

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.id = :id")
    List<Stylist> findAllStylistsByLocationId (UUID id);

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.location.id = :id AND l.name = :name")
    Optional<Stylist> findStylistByLocationIdAndName (UUID id, String name);
}
