package hotel.model;

import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.ReservationNotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface HotelManager
{

    Reservation find(long id) throws ReservationNotFoundException, ApplicationException;

    List<Reservation> findAll() throws ApplicationException;

    List<Reservation> findCustomerReservations(Customer customer) throws ApplicationException;

    void placeReservation(Reservation reservation) throws ApplicationException;

    void delete(Reservation reservation) throws ReservationNotFoundException;

    List<Reservation> findReservationByRoom(Room room);

    List<Reservation> findReservationsByCustomer(Customer customer);

}
