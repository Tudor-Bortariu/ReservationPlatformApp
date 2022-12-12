package ro.demo.ReservationPlatformApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ro.demo.ReservationPlatformApp.api.LocationApi;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaLocationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaServiceRepository;
import ro.demo.ReservationPlatformApp.service.IAuthenticationFacade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LocationController implements LocationApi {

    @Autowired
    JpaLocationRepository locationRepository;

    @Autowired
    JpaServiceRepository serviceRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Override
    public String locationManagement(Model model) {
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        model.addAttribute("locationList", locationRepository.findAllLocationsByUser(user));

        return "locationManagement/generalView";
    }

    @Override
    public String addLocationForm(Model model) {
        return "locationManagement/addLocation";
    }

    @Override
    public RedirectView addLocation(Model model,
                                    @RequestParam String locationName,
                                    @RequestParam String phoneNumber,
                                    @RequestParam List<String> workingDays,
                                    @RequestParam LocalTime openingHour,
                                    @RequestParam LocalTime closingHour,
                                    @RequestParam MultipartFile locationProfilePicture) {

        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        Location addedLocation = new Location(UUID.randomUUID(), locationName, phoneNumber, workingDays, openingHour, closingHour);
        addedLocation.setUser(user);

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
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        locationRepository.deleteByLocationId(user, id);

        return new RedirectView("/locationManagement");
    }

    @Override
    public String editLocationForm(Model model, UUID id) {
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        model.addAttribute("location", locationRepository.findLocationById(user, id).get());

        return "locationManagement/editLocation";
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

        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        Location location = locationRepository.findLocationById(user, id).get();

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
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        response.setContentType("image/jpg, image/png");

        Optional<Location> location = locationRepository.findLocationById(user, id);

        InputStream imageStream = new ByteArrayInputStream(location.get().getLocationProfilePicture());
        IOUtils.copy(imageStream, response.getOutputStream());
    }

    @Override
    public String addServiceForm(Model model,
                                 @PathVariable UUID id){
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        model.addAttribute("location", locationRepository.findLocationById(user, id).get());

        return "locationManagement/addService";
    }

    @Override
    public RedirectView addService(Model model,
                            @PathVariable UUID id,
                            @RequestParam String serviceName,
                            @RequestParam Double servicePrice){

        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        Location location = locationRepository.findLocationById(user, id).get();
        Service service = new Service(UUID.randomUUID(), serviceName, servicePrice, location);

        serviceRepository.saveAndFlush(service);

        return new RedirectView("/locationManagement");
    }
}
