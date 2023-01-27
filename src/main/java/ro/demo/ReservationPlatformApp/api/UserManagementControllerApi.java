package ro.demo.ReservationPlatformApp.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequestMapping("myAccount")
public interface UserManagementControllerApi {

    @GetMapping("")
    String accountManagement(Model model);

    @GetMapping("/locationManagement")
    String locationManagement(Model model);

    @GetMapping("/locationManagement/addLocation")
    String addLocationForm(Model model);

    @GetMapping("/locationManagement/locationView/{locationId}")
    String locationView(Model model,
                        @PathVariable UUID locationId);

    @PostMapping("/locationManagement/addLocation")
    RedirectView addLocation(Model model,
                             @RequestParam String locationName,
                             @RequestParam String phoneNumber,
                             @RequestParam List<String> workingDays,
                             @RequestParam LocalTime openingHour,
                             @RequestParam LocalTime closingHour,
                             @RequestParam MultipartFile locationProfilePicture);

    @GetMapping("/locationManagement/delete/{id}")
    RedirectView deleteLocation(Model model, @PathVariable UUID id);

    @GetMapping("/locationManagement/edit/{id}")
    String editLocationForm(Model model, @PathVariable UUID id);

    @PostMapping("/locationManagement/edit/{id}")
    RedirectView editLocation(Model model,
                              @PathVariable UUID id,
                              @RequestParam String updatedLocationName,
                              @RequestParam String updatedPhoneNumber,
                              @RequestParam List<String> updatedWorkingDays,
                              @RequestParam LocalTime updatedOpeningHour,
                              @RequestParam LocalTime updatedClosingHour,
                              @RequestParam MultipartFile updatedProfilePicture);

    @GetMapping("/locationManagement/image/{id}")
    void getLocationImage(@PathVariable UUID id,
                          HttpServletResponse response) throws IOException;

    @GetMapping("/locationManagement/locationView/image/{id}")
    void getImageForLocationView(@PathVariable UUID id,
                          HttpServletResponse response) throws IOException;

    @GetMapping("/locationManagement/addService/{locationId}")
    String addServiceForm(Model model,
                          @PathVariable UUID locationId);

    @PostMapping("/locationManagement/addService/{locationId}")
    RedirectView addService(Model model,
                            @PathVariable UUID locationId,
                            @RequestParam String serviceName,
                            @RequestParam Double servicePrice);

    @GetMapping("/locationManagement/addStylist/{locationId}")
    String addStylistForm(Model model,
                          @PathVariable UUID locationId);

    @PostMapping("/locationManagement/addStylist/{locationId}")
    RedirectView addStylist(Model model,
                            @PathVariable UUID locationId,
                            @RequestParam String stylistName);

    @GetMapping("/myReservations")
    String displayUserReservations(Model model);

    @GetMapping("/locationReservations/{locationId}")
    String currentReservationsForLocation(Model model,
                                          @PathVariable UUID locationId);

    @GetMapping("/myReservations/delete/{id}")
    RedirectView deleteReservation(Model model, @PathVariable UUID id);

    @GetMapping("/myReservations/edit/{id}")
    String editReservationForm(Model model, @PathVariable UUID id);

    @PostMapping("/myReservations/edit/{id}")
    RedirectView editReservation(Model model,
                           @PathVariable UUID id,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String phoneNumber,
                           @RequestParam LocalDate reservationDate,
                           @RequestParam String reservationTime,
                           @RequestParam String service,
                           @RequestParam String stylist);
}
