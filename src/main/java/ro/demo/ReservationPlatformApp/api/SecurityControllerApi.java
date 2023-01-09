package ro.demo.ReservationPlatformApp.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SecurityControllerApi {

    @GetMapping("/register")
    String invoiceManagement(Model model);

    @PostMapping("/register")
    String sendRegistrationForm (Model model,
                                 @RequestParam String username,
                                 @RequestParam("initialPassword") String initialPassword,
                                 @RequestParam("passwordCheck") String passwordCheck,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String role);

    @GetMapping("/logoutConfirm")
    String logoutConfirmation (Model model);
}
