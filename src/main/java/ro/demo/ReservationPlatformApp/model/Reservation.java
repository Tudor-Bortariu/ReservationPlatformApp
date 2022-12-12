package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime reservationTime;

//    @Column
//    private Service service;

//    @Column
//    private User user;
}
