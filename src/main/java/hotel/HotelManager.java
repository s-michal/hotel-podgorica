package hotel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface HotelManager
{

    public Reservation find(long id);

    public List<Reservation> findAll();

    public List<Collection> findCustomerReservations(Customer customer);

    public void placeReservation(Reservation reservation);

    public void delete(Reservation reservation);

    public List<Reservation> findReservationByRoom(Room room);

    public List<Room> findAvailableRooms(Date since, Date until);

}
