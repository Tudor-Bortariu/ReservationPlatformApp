package ro.demo.ReservationPlatformApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaLocationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaReservationRepository;
import ro.demo.ReservationPlatformApp.repository.JpaStylistRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationServiceApi{
    @Autowired
    JpaReservationRepository reservationRepository;

    @Autowired
    JpaLocationRepository locationRepository;

    @Autowired
    JpaStylistRepository stylistRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Override
    public void makeReservation(UUID locationId,
                                LocalTime reservationTime,
                                String firstName,
                                String lastName,
                                String phoneNumber,
                                LocalDate reservationDate,
                                String service,
                                String chosenStylist) {
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

        String reservationDayOfWeek = reservationDate.getDayOfWeek().name();
        Location location = locationRepository.findLocationById(locationId).get();
        Stylist stylist = stylistRepository.findStylistByLocationIdAndName(locationId, chosenStylist).get();
        List<Reservation> reservationList = reservationRepository.findAllReservationsByLocationId(locationId, LocalDate.now());

        if(!location.getWorkingDays().contains(reservationDayOfWeek)){
            throw new IllegalArgumentException("Location is closed on the selected day of the week. Please choose a different date.");
        }
        if (reservationTime.isBefore(location.getOpeningHour()) && reservationTime.isAfter(location.getClosingHour())){
            throw new IllegalArgumentException("Location is closed at the selected hour. Please choose a different hour for the Reservation.");
        }

        Reservation reservation = new Reservation(UUID.randomUUID(), firstName, lastName, phoneNumber, reservationDate, reservationTime,
                location, service, stylist);

        if(!reservationList.contains(reservation)){

            reservation.setUser(user);
            reservation.setDayOfWeek(reservationDate.getDayOfWeek().name());
            reservationRepository.saveAndFlush(reservation);

        }else{
            throw new IllegalArgumentException("Reservation Time is already booked. Please choose a different Hour or Day.");
        }
    }

    public List<LocalTime> getTotalAvailableHours (Location location){

        long hours = ChronoUnit.HOURS.between(location.getOpeningHour(), location.getClosingHour());
        List<LocalTime> totalAvailableHours = new ArrayList<>();
        LocalTime openingHour = location.getOpeningHour();

        for (int i = 0; i < hours; i++) {
            if(!totalAvailableHours.contains(openingHour)){
                totalAvailableHours.add(openingHour);
            }else{
                openingHour = openingHour.plusHours(1);
                totalAvailableHours.add(openingHour);
            }
        }

        return totalAvailableHours;
    }
}
