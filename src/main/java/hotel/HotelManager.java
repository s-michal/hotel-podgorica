package hotel;

import hotel.exceptions.ApplicationException;
import hotel.exceptions.ReservationNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface HotelManager
{

    public Reservation find(long id) throws ReservationNotFoundException, ApplicationException;

    public List<Reservation> findAll() throws ApplicationException;

    public List<Reservation> findCustomerReservations(Customer customer) throws ApplicationException;

    public void placeReservation(Reservation reservation) throws ApplicationException;

    public void delete(Reservation reservation) throws ReservationNotFoundException;

    public List<Reservation> findReservationByRoom(Room room) throws ApplicationException;

    public List<Room> findAvailableRooms(Date since, Date until) throws ApplicationException;

}
