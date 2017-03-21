package hotel;

import hotel.exceptions.ReservationNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HotelManagerImpl implements HotelManager
{

    public Reservation find(long id) throws ReservationNotFoundException
    {
        return null;
    }

    public List<Collection> findCustomerReservations(Customer customer)
    {
        return null;
    }

    public void placeReservation(Reservation reservation)
    {

    }

    public void delete(Reservation reservation)
    {

    }

    public List<Reservation> findReservationByRoom(Room room)
    {
        return null;
    }

    public List<Room> findAvailableRooms(Date since, Date until)
    {
        return null;
    }

    public List<Reservation> findAll()
    {
        return null;
    }
}
