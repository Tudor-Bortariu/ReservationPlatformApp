package ro.demo.ReservationPlatformApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor @Setter @Getter
public class User {

    @Id
    @Column
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String username;

    @Column(nullable = false, length = 64)
    private String password;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column (nullable = false)
    private String firstName;

    @Column (nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Location> locationList;
}
