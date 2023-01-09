package ro.demo.ReservationPlatformApp.service;

import ro.demo.ReservationPlatformApp.model.Location;
import ro.demo.ReservationPlatformApp.model.Reservation;
import ro.demo.ReservationPlatformApp.model.Stylist;
import ro.demo.ReservationPlatformApp.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationServiceApi {

    void makeReservation(Location location,
                         String reservationDayOfWeek,
                         LocalTime reservationTime,
                         String firstName,
                         String lastName,
                         String phoneNumber,
                         LocalDate reservationDate,
                         String service,
                         Stylist chosenStylist,
                         List<Reservation> reservationList,
                         User user);
}
