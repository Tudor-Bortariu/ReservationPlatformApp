package ro.demo.ReservationPlatformApp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.demo.ReservationPlatformApp.model.Reservation;
import ro.demo.ReservationPlatformApp.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query(value = "SELECT r FROM #{#entityName} r WHERE r.user = :user AND r.reservationDate >= :currentDate")
    List<Reservation> findAllReservationsByUser (User user, LocalDate currentDate);

    @Query(value = "SELECT r FROM #{#entityName} r WHERE r.id = :id")
    Optional<Reservation> findReservationById (UUID id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM #{#entityName} r WHERE r.user = :user AND r.id = :id")
    void deleteReservationByUserAndId (User user, UUID id);

    @Query(value = "SELECT r FROM #{#entityName} r WHERE r.location.id = :locationId AND r.reservationDate >= :currentDate")
    List<Reservation> findAllReservationsByLocationId (UUID locationId, LocalDate currentDate);
}
