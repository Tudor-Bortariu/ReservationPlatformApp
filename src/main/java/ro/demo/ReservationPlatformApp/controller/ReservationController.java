package ro.demo.ReservationPlatformApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ro.demo.ReservationPlatformApp.api.ReservationControllerApi;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaLocationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaReservationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaStylistRepository;
import ro.demo.ReservationPlatformApp.service.ReservationServiceImpl;
import ro.demo.ReservationPlatformApp.service.SecurityServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class ReservationController implements ReservationControllerApi {

    @Autowired
    JpaLocationRepository locationRepository;

    @Autowired
    JpaStylistRepository stylistRepository;

    @Autowired
    ReservationServiceImpl reservationService;

    @Autowired
    SecurityServiceImpl securityService;

    @Autowired
    JpaReservationRepository reservationRepository;

    @Override
    public String availableLocationsList(Model model){

        model.addAttribute("locationList", locationRepository.findAll());

        return "reservationManagement/availableLocations";
    }

    @Override
    public String reservationForm(Model model,
                                  @PathVariable UUID locationId){
        Location location = locationRepository.findLocationById(locationId).get();

        model.addAttribute("location", location);
        model.addAttribute("minDate", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now().plusYears(1));
        model.addAttribute("serviceList", location.getServiceList());
        model.addAttribute("stylistList", location.getStylistList());
        model.addAttribute("totalAvailableHours", reservationService.getTotalAvailableHours(location));

        return "reservationManagement/makeReservation";
    }

    @Override
    public RedirectView makeReservation(Model model,
                                        @PathVariable UUID locationId,
                                        @RequestParam String firstName,
                                        @RequestParam String lastName,
                                        @RequestParam String phoneNumber,
                                        @RequestParam LocalDate reservationDate,
                                        @RequestParam String reservationTime,
                                        @RequestParam String service,
                                        @RequestParam String stylist){

        LocalTime reservationHour = LocalTime.parse(reservationTime);

        reservationService.makeReservation(locationId, reservationHour, firstName, lastName, phoneNumber,
                reservationDate, service, stylist);

        return new RedirectView("/reservations");
    }

    @Override
    public String locationView(Model model,
                               @PathVariable UUID locationId){
        Location location = locationRepository.findLocationById(locationId).get();

        model.addAttribute("location", location);
        model.addAttribute("openingDay", location.getWorkingDays().get(0));
        model.addAttribute("closingDay", location.getWorkingDays().get(location.getWorkingDays().size() - 1));

        return "reservationManagement/locationView";
    }

    @Override
    public void getImageForLocationView(@PathVariable UUID id,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("image/jpg, image/png");

        Optional<Location> location = locationRepository.findLocationById(id);

        InputStream imageStream = new ByteArrayInputStream(location.get().getLocationProfilePicture());
        IOUtils.copy(imageStream, response.getOutputStream());
    }

    @Override
    public void getImageForGeneralView(@PathVariable UUID id,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("image/jpg, image/png");

        Optional<Location> location = locationRepository.findLocationById(id);

        InputStream imageStream = new ByteArrayInputStream(location.get().getLocationProfilePicture());
        IOUtils.copy(imageStream, response.getOutputStream());
    }
}