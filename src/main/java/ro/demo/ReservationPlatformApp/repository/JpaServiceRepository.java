package ro.demo.ReservationPlatformApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.demo.ReservationPlatformApp.model.Location;
import ro.demo.ReservationPlatformApp.model.Service;

import java.util.List;
import java.util.UUID;

public interface JpaServiceRepository extends JpaRepository<Service, UUID> {

    @Query(value = "SELECT l FROM #{#entityName} l WHERE l.id = :id")
    List<Location> findAllServicesByLocationId (UUID id);
}
