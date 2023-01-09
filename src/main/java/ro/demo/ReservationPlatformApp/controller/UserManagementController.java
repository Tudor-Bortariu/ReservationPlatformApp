package ro.demo.ReservationPlatformApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ro.demo.ReservationPlatformApp.api.UserManagementControllerApi;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaLocationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaReservationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaServiceRepository;
import ro.demo.ReservationPlatformApp.repository.JpaStylistRepository;
import ro.demo.ReservationPlatformApp.service.SecurityServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserManagementController implements UserManagementControllerApi {

    @Autowired
    JpaLocationRepository locationRepository;

    @Autowired
    JpaStylistRepository stylistRepository;

    @Autowired
    JpaServiceRepository serviceRepository;

    @Autowired
    SecurityServiceImpl securityService;

    @Autowired
    JpaReservationRepository reservationRepository;

    @Override
    public String accountManagement(Model model) {

        return "userManagement/userOptions";
    }

    @Override
    public String locationManagement(Model model){
        model.addAttribute("locationList", locationRepository.findAllLocationsByUser(securityService.getUser()));

        return "userManagement/locationManagement/generalView";
    }

    @Override
    public String addLocationForm(Model model) {
        return "userManagement/locationManagement/addLocation";
    }

    @Override
    public String locationView(Model model,
                        @PathVariable UUID locationId){
        model.addAttribute("location", locationRepository.findLocationByIdAndUser(securityService.getUser(), locationId).get());

        return "userManagement/locationManagement/locationView";
    }

    @Override
    public RedirectView addLocation(Model model,
                                    @RequestParam String locationName,
                                    @RequestParam String phoneNumber,
                                    @RequestParam List<String> workingDays,
                                    @RequestParam LocalTime openingHour,
                                    @RequestParam LocalTime closingHour,
                                    @RequestParam MultipartFile locationProfilePicture) {

        Location addedLocation = new Location(UUID.randomUUID(), locationName, phoneNumber, workingDays, openingHour, closingHour);
        addedLocation.setUser(securityService.getUser());

        if (locationProfilePicture != null){
            try {
                addedLocation.setLocationProfilePicture(locationProfilePicture.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        locationRepository.saveAndFlush(addedLocation);

        return new RedirectView("/locationManagement");
    }

    @Override
    public RedirectView deleteLocation(Model model, @PathVariable UUID id) {
        locationRepository.deleteByLocationId(securityService.getUser(), id);

        return new RedirectView("/locationManagement");
    }

    @Override
    public String editLocationForm(Model model, UUID id) {
        model.addAttribute("location", locationRepository.findLocationByIdAndUser(securityService.getUser(), id).get());

        return "userManagement/locationManagement/editLocation";
    }

    @Override
    public RedirectView editLocation(Model model,
                                     @PathVariable UUID id,
                                     @RequestParam String updatedLocationName,
                                     @RequestParam String updatedPhoneNumber,
                                     @RequestParam List<String> updatedWorkingDays,
                                     @RequestParam LocalTime updatedOpeningHour,
                                     @RequestParam LocalTime updatedClosingHour,
                                     @RequestParam MultipartFile updatedProfilePicture) {

        Location location = locationRepository.findLocationByIdAndUser(securityService.getUser(), id).get();

        location.setName(updatedLocationName);
        location.setPhoneNumber(updatedPhoneNumber);
        location.setWorkingDays(updatedWorkingDays);
        location.setOpeningHour(updatedOpeningHour);
        location.setClosingHour(updatedClosingHour);

        if(!updatedProfilePicture.isEmpty()){
            try {
                location.setLocationProfilePicture(updatedProfilePicture.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        locationRepository.saveAndFlush(location);

        return new RedirectView("/locationManagement");
    }

    @Override
    public void getLocationImage(@PathVariable UUID id,
                          HttpServletResponse response) throws IOException{
        response.setContentType("image/jpg, image/png");

        Optional<Location> location = locationRepository.findLocationByIdAndUser(securityService.getUser(), id);

        InputStream imageStream = new ByteArrayInputStream(location.get().getLocationProfilePicture());
        IOUtils.copy(imageStream, response.getOutputStream());
    }

    @Override
    public void getImageForLocationView(@PathVariable UUID id,
                                 HttpServletResponse response) throws IOException{
        response.setContentType("image/jpg, image/png");

        Optional<Location> location = locationRepository.findLocationByIdAndUser(securityService.getUser(), id);

        InputStream imageStream = new ByteArrayInputStream(location.get().getLocationProfilePicture());
        IOUtils.copy(imageStream, response.getOutputStream());
    }

    @Override
    public String addServiceForm(Model model,
                                 @PathVariable UUID locationId){
        model.addAttribute("location", locationRepository.findLocationByIdAndUser(securityService.getUser(), locationId).get());

        return "userManagement/locationManagement/addService";
    }

    @Override
    public RedirectView addService(Model model,
                            @PathVariable UUID locationId,
                            @RequestParam String serviceName,
                            @RequestParam Double servicePrice){

        Location location = locationRepository.findLocationByIdAndUser(securityService.getUser(), locationId).get();
        Service service = new Service(UUID.randomUUID(), serviceName, servicePrice, location);

        serviceRepository.saveAndFlush(service);

        return new RedirectView("/locationManagement");
    }

    @Override
    public String addStylistForm(Model model,
                          @PathVariable UUID locationId){
        model.addAttribute("location", locationRepository.findLocationByIdAndUser(securityService.getUser(), locationId).get());

        return "userManagement/locationManagement/addStylist";
    }

    @Override
    public RedirectView addStylist(Model model,
                            @PathVariable UUID locationId,
                            @RequestParam String stylistName){

        Location location = locationRepository.findLocationByIdAndUser(securityService.getUser(), locationId).get();
        Stylist stylist = new Stylist(UUID.randomUUID(), stylistName, location);

        stylistRepository.saveAndFlush(stylist);

        return new RedirectView("/locationManagement");
    }

    @Override
    public String displayUserReservations(Model model){
        model.addAttribute("reservationList", reservationRepository.findAllReservationsByUser(securityService.getUser(), LocalDate.now()));

        return "userManagement/reservationManagement/myReservations";
    }

    @Override
    public String currentReservationsForLocation(Model model,
                                                 @PathVariable UUID locationId){

        model.addAttribute("reservationList", reservationRepository.findAllReservationsByLocationId(locationId, LocalDate.now())
                .stream()
                .filter(location -> location.getReservationTime().isAfter(LocalTime.now())));

        return "userManagement/locationManagement/locationReservations";
    }

    @Override
    public RedirectView deleteReservation(Model model, @PathVariable UUID id){
        reservationRepository.deleteReservationByUserAndId(securityService.getUser(), id);

        return new RedirectView("/myAccount/myReservations");
    }
}