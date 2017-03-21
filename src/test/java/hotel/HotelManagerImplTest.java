package hotel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import hotel.exceptions.ReservationNotFoundException;
import org.junit.Test;

public class HotelManagerImplTest
{

    @Test
    public void create() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        manager.placeReservation(new Reservation());
        manager.placeReservation(new Reservation());

        Reservation reservation = new Reservation();
        manager.placeReservation(reservation);

        assertThat(manager.findAll())
                .hasSize(3);

        assertThat(reservation.getId())
                .isNotNull();
    }

    @Test
    public void createWithNull() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();

        assertThatThrownBy(() -> manager.placeReservation(null))
                .isInstanceOf(NullPointerException.class);

        assertThat(manager.findAll())
                .isEmpty();
    }

    @Test
    public void find() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        manager.placeReservation(new Reservation());

        Reservation reservation = new Reservation();
        manager.placeReservation(reservation);

        assertThat(manager.find(reservation.getId()))
                .isEqualTo(reservation);
    }

    @Test
    public void findForUnknownIdThrowsException() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        manager.placeReservation(new Reservation());

        assertThatThrownBy(() -> manager.find(666))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    public void delete() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        Reservation reservation = new Reservation();

        manager.placeReservation(reservation);
        manager.delete(reservation);

        assertThat(manager.findAll())
                .isEmpty();
    }

    @Test
    public void deleteOfNotPersistedReservationThrowsException() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();

        assertThatThrownBy(() -> manager.delete(new Reservation()))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    public void reservationForNonexistingRoomAreNone() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();

        Room room = mock(Room.class);
        when(room.getId()).thenReturn(null);

        assertThat(manager.findReservationByRoom(room))
                .isEmpty(); // Or throw exception?
    }

    @Test
    public void reservationsForRoom()
    {
        HotelManagerImpl manager = new HotelManagerImpl();

        Room room = mock(Room.class);
        when(room.getId()).thenReturn(new Long(10));

        Reservation first = new Reservation(room);
        Reservation second = new Reservation(room);
        manager.placeReservation(first);
        manager.placeReservation(second);

        assertThat(manager.findReservationByRoom(room))
                .containsExactlyInAnyOrder(first, second);
    }

}