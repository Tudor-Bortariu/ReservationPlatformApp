package ro.demo.ReservationPlatformApp.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface HomeApi {

    @GetMapping("/")
    String homePage(Model model);
}
