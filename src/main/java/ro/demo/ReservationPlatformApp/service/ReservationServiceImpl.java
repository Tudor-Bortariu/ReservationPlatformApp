package ro.demo.ReservationPlatformApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.demo.ReservationPlatformApp.model.*;
import ro.demo.ReservationPlatformApp.repository.JpaReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class ReservationServiceImpl implements ReservationServiceApi{
    @Autowired
    JpaReservationRepository reservationRepository;

    @Override
    public void makeReservation(Location location,
                                String reservationDayOfWeek,
                                LocalTime reservationTime,
                                String firstName,
                                String lastName,
                                String phoneNumber,
                                LocalDate reservationDate,
                                String service,
                                Stylist chosenStylist,
                                List<Reservation> reservationList,
                                User user) {
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
    }
}
