package ro.demo.ReservationPlatformApp.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@RequestMapping("reservations")
public interface ReservationControllerApi {

    @GetMapping("")
    String availableLocationsList(Model model);

    @GetMapping("/makeReservation/{locationId}")
    String reservationForm(Model model,
                           @PathVariable UUID locationId);

    @PostMapping("/makeReservation/{locationId}")
    RedirectView makeReservation(Model model,
                                 @PathVariable UUID locationId,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String phoneNumber,
                                 @RequestParam LocalDate reservationDate,
                                 @RequestParam String reservationTime,
                                 @RequestParam String service,
                                 @RequestParam String stylist);

    @GetMapping("/image/{id}")
    void getImageForGeneralView(@PathVariable UUID id,
                                 HttpServletResponse response) throws IOException;

    @GetMapping("/locationView/{locationId}")
    String locationView(Model model,
                        @PathVariable UUID locationId);

    @GetMapping("locationView/image/{id}")
    void getImageForLocationView(@PathVariable UUID id,
                                 HttpServletResponse response) throws IOException;
}
