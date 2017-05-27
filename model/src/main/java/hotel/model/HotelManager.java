package hotel.model;

import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.ReservationNotFoundException;
import hotel.model.exceptions.RoomHasReservationException;

import java.util.List;

public interface HotelManager
{

    Reservation find(long id) throws ReservationNotFoundException, ApplicationException;

    List<Reservation> findAll();

    List<Reservation> findCustomerReservations(Customer customer) throws ApplicationException;

    void placeReservation(Reservation reservation) throws RoomHasReservationException;

    void delete(Reservation reservation) throws ReservationNotFoundException;

    List<Reservation> findReservationByRoom(Room room);

    List<Reservation> findReservationsByCustomer(Customer customer);

    void update(Reservation reservation);

}
