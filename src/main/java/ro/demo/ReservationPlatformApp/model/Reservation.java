package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@NoArgsConstructor @Setter @Getter
public class Reservation {

    @Id
    @Column
    private UUID id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phoneNumber;

    @Column
    private LocalTime reservationTime;

    @Column
    private LocalDate reservationDate;

    @Column
    private String dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column
    private String service;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stylist_id", nullable = false)
    private Stylist stylist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Reservation(UUID id, String firstName, String lastName, String phoneNumber, LocalDate reservationDate, LocalTime reservationTime, Location location, String service, Stylist stylist) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.location = location;
        this.service = service;
        this.stylist = stylist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationTime, that.reservationTime) && Objects.equals(reservationDate, that.reservationDate) && Objects.equals(stylist, that.stylist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationTime, reservationDate, stylist);
    }
}
