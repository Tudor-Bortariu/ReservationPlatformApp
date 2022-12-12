package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "services")
@NoArgsConstructor @Getter @Setter
public class Service {

    @Id
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public Service(UUID id, String name, Double price, Location location) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.location = location;
    }
}
