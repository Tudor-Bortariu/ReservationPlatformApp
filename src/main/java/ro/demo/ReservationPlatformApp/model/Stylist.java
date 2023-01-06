package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stylists")
@NoArgsConstructor @Getter @Setter
public class Stylist {

    @Id
    @Column(name = "stylist_id")
    private UUID id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "stylist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Reservation> reservationList;

    public Stylist (UUID id, String name, Location location){
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public void addReservationToStylist (Reservation reservation){

        if (reservation.getReservationTime().isAfter(location.getOpeningHour())
                && reservation.getReservationTime().isBefore(location.getClosingHour())
                && !reservationList.contains(reservation)
                && location.getWorkingDays().contains(reservation.getReservationDate().getDayOfWeek().name())){

            reservationList.add(reservation);

        }else{
            throw new IllegalArgumentException("Please choose a valid hour for the reservation!");
        }
    }

}
