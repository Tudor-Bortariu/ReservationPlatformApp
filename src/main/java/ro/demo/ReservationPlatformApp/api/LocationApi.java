package ro.demo.ReservationPlatformApp.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ro.demo.ReservationPlatformApp.model.WorkingDays;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequestMapping("locationManagement")
public interface LocationApi {

    @GetMapping("")
    String locationManagement(Model model);

    @GetMapping("/addLocation")
    String addLocationForm(Model model);

    @PostMapping("/addLocation")
    RedirectView addLocation(Model model,
                             @RequestParam String locationName,
                             @RequestParam String phoneNumber,
                             @RequestParam List<String> workingDays,
                             @RequestParam LocalTime openingHour,
                             @RequestParam LocalTime closingHour,
                             @RequestParam MultipartFile locationProfilePicture);

    @GetMapping("/delete/{id}")
    RedirectView deleteLocation(Model model, @PathVariable UUID id);

    @GetMapping("/edit/{id}")
    String editLocationForm(Model model, @PathVariable UUID id);

    @PostMapping("/edit/{id}")
    RedirectView editLocation(Model model,
                              @PathVariable UUID id,
                              @RequestParam String updatedLocationName,
                              @RequestParam String updatedPhoneNumber,
                              @RequestParam List<String> updatedWorkingDays,
                              @RequestParam LocalTime updatedOpeningHour,
                              @RequestParam LocalTime updatedClosingHour,
                              @RequestParam MultipartFile updatedProfilePicture);

    @GetMapping("/image/{id}")
    void getLocationImage(@PathVariable UUID id,
                          HttpServletResponse response) throws IOException;

    @GetMapping("/addService/{id}")
    String addServiceForm(Model model,
                          @PathVariable UUID id);

    @PostMapping("/addService/{id}")
    RedirectView addService(Model model,
                            @PathVariable UUID id,
                            @RequestParam String serviceName,
                            @RequestParam Double servicePrice);
}
