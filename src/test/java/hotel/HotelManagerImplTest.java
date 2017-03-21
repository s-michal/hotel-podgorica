package hotel;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class HotelManagerImplTest
{

    private HotelManagerImpl manager;

    private Reservation reservation;

    @Before
    public void setUp() throws Exception
    {
        manager = new HotelManagerImpl();

        reservation = new Reservation();
        manager.placeReservation(new Reservation());
        manager.placeReservation(reservation);
    }

    @Test
    public void create() throws Exception
    {
        Reservation reservation = new Reservation();
        manager.placeReservation(reservation);

        assertSame("Number of reservations didn't increment.", 3, manager.findAll().size());
    }

    @Test
    public void find() throws Exception
    {
        Reservation reservation = manager.find(1);
        assertNotSame("Manager doesn't contain reservation #1", null, reservation);
    }

    @Test
    public void delete() throws Exception
    {
        manager.delete(reservation);
        assertSame("Number of reservations didn't decrement.", 1, manager.findAll());
    }

}