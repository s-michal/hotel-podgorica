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
        manager.placeReservation(createReservation());
        manager.placeReservation(createReservation());

        Reservation reservation = createReservation();
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
        manager.placeReservation(createReservation());

        Reservation reservation = createReservation();
        manager.placeReservation(reservation);

        assertThat(manager.find(reservation.getId()))
                .isEqualTo(reservation);
    }

    @Test
    public void findForUnknownIdThrowsException() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        manager.placeReservation(createReservation());

        assertThatThrownBy(() -> manager.find(666))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    public void delete() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();
        Reservation reservation = createReservation();

        manager.placeReservation(reservation);
        manager.delete(reservation);

        assertThat(manager.findAll())
                .isEmpty();
    }

    @Test
    public void deleteOfNotPersistedReservationThrowsException() throws Exception
    {
        HotelManagerImpl manager = new HotelManagerImpl();

        assertThatThrownBy(() -> manager.delete(createReservation()))
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

        Reservation first = createReservation(room);
        Reservation second = createReservation(room);
        manager.placeReservation(first);
        manager.placeReservation(second);

        assertThat(manager.findReservationByRoom(room))
                .containsExactlyInAnyOrder(first, second);
    }

    private Reservation createReservation(Room room)
    {
        return new Reservation(room);
    }

    private Reservation createReservation()
    {
        Room room = mock(Room.class);
        when(room.getId()).thenReturn(new Long(10));
        return createReservation(room);
    }

}