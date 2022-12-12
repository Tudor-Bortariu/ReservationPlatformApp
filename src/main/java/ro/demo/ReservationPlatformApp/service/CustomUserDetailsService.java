package ro.demo.ReservationPlatformApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.demo.ReservationPlatformApp.model.CustomUserDetails;
import ro.demo.ReservationPlatformApp.model.User;
import ro.demo.ReservationPlatformApp.repository.JpaUserRepository;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private JpaUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
