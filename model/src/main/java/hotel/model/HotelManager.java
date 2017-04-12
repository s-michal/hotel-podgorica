package hotel.model;

import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.ReservationNotFoundException;

import java.time.LocalDate;
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

}
