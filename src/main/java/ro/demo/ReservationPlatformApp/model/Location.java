package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "locations")
@NoArgsConstructor @Setter @Getter
public class Location {

    @Id
    @Column(name = "location_id")
    private UUID id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private List<String> workingDays;

    @Column
    private LocalTime openingHour;

    @Column
    private LocalTime closingHour;

    @Column
    private byte[] locationProfilePicture;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Service> services;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Location(UUID id, String name, String phoneNumber, List<String> workingDays, LocalTime openingHour, LocalTime closingHour) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.workingDays = workingDays;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }
}
