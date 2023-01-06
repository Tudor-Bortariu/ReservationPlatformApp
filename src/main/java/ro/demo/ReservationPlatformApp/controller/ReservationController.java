package ro.demo.ReservationPlatformApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ro.demo.ReservationPlatformApp.api.ReservationApi;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaLocationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaReservationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaServiceRepository;
import ro.demo.ReservationPlatformApp.repository.JpaStylistRepository;
import ro.demo.ReservationPlatformApp.service.IAuthenticationFacade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ReservationController implements ReservationApi {

    @Autowired
    JpaLocationRepository locationRepository;

    @Autowired
    JpaStylistRepository stylistRepository;

    @Autowired
    JpaServiceRepository serviceRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

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
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        Location location = locationRepository.findLocationById(locationId).get();

        model.addAttribute("location", location);
        model.addAttribute("minDate", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now().plusYears(1));
        model.addAttribute("serviceList", location.getServiceList());
        model.addAttribute("stylistList", location.getStylistList());

        return "reservationManagement/makeReservation";
    }

    @Override
    public RedirectView makeReservation(Model model,
                                        @PathVariable UUID locationId,
                                        @RequestParam String firstName,
                                        @RequestParam String lastName,
                                        @RequestParam String phoneNumber,
                                        @RequestParam LocalDate reservationDate,
                                        @RequestParam LocalTime reservationTime,
                                        @RequestParam String service,
                                        @RequestParam String stylist){
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        Location location = locationRepository.findLocationById(locationId).get();
        Stylist chosenStylist = stylistRepository.findStylistByLocationIdAndName(locationId, stylist).get();
        String reservationDayOfWeek = reservationDate.getDayOfWeek().name();

        List<Reservation> reservationList = reservationRepository.findAllReservationsByLocationId(locationId, LocalDate.now());

        if(location.getWorkingDays().contains(reservationDayOfWeek) && reservationTime.isAfter(location.getOpeningHour())
        && reservationTime.isBefore(location.getClosingHour())){
            Reservation reservation = new Reservation(UUID.randomUUID(), firstName, lastName, phoneNumber, reservationDate, reservationTime,
                    location, service, chosenStylist);

            if(reservationList.contains(reservation)){
                throw new IllegalArgumentException("A reservation already exists for that time and date at the selected stylist.");
            }

            reservation.setUser(user);
            reservation.setDayOfWeek(reservationDate.getDayOfWeek().name());

            reservationRepository.saveAndFlush(reservation);
        }else{
            throw new IllegalArgumentException("Reservation Date or Time is out of the location weekly schedule.");
        }

        return new RedirectView("/reservations");
    }

    @Override
    public String locationView(Model model,
                               @PathVariable UUID locationId){
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        model.addAttribute("location", locationRepository.findLocationById(locationId).get());

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
