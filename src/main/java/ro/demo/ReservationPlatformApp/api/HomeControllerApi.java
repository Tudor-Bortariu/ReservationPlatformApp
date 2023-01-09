package ro.demo.ReservationPlatformApp.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface HomeControllerApi {

    @GetMapping("/")
    String homePage(Model model);
}
