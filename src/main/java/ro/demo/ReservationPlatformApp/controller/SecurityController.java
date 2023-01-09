package ro.demo.ReservationPlatformApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import ro.demo.ReservationPlatformApp.api.SecurityControllerApi;
import ro.demo.ReservationPlatformApp.configuration.SecurityConfig;
import ro.demo.ReservationPlatformApp.model.Role;
import ro.demo.ReservationPlatformApp.model.User;
import ro.demo.ReservationPlatformApp.repository.JpaUserRepository;

import java.util.UUID;

@Controller
public class SecurityController implements SecurityControllerApi {

    @Autowired
    private SecurityConfig securityConfig = new SecurityConfig();
    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public String invoiceManagement(Model model) {

        return "security/register";
    }

    @Override
    public String sendRegistrationForm (Model model,
                                        @RequestParam String username,
                                        @RequestParam("initialPassword") String initialPassword,
                                        @RequestParam("passwordCheck") String passwordCheck,
                                        @RequestParam String firstName,
                                        @RequestParam String lastName,
                                        @RequestParam String role){

        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is not available. Please insert a different Username.");
        } else {
            if(!initialPassword.equals(passwordCheck)){
                throw new IllegalArgumentException("Passwords are not matching. Please make sure you complete the passwords correctly.");
            }

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(securityConfig.passwordEncoder().encode(passwordCheck));
            switch (role){
                case "owner": user.setRole(Role.BUSINESS_OWNER);
                break;
                case "client": user.setRole(Role.CLIENT);
                break;
                case "stylist": user.setRole(Role.STYLIST);
                break;
            }

            userRepository.saveAndFlush(user);
        }

        return "home/homePage";
    }

    @Override
    public String logoutConfirmation (Model model){

        return "security/logoutConfirmation";
    }
}
