package hotel.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
public class ReservationTest
{

    @Test
    public void cancelReservationBeforeReservation() throws Exception
    {
        LocalDate since = LocalDate.now();
        Reservation reservation = new Reservation(mockRoom(), mockCustomer(), since, since.plusDays(10));
        reservation.cancel(since.minusDays(2));
        assertThat(reservation.isCanceled()).isTrue();
    }


    @Test
    public void CantCancelReservationAfterSinceDate() throws Exception
    {
        LocalDate since = LocalDate.now();
        Reservation reservation = new Reservation(mockRoom(), mockCustomer(), since, since.plusDays(10));
        assertThatThrownBy(() -> reservation.cancel(since.plusDays(2)));
        assertThat(reservation.isCanceled()).isFalse();
    }

    private Room mockRoom()
    {
        return mock(Room.class);
    }

    private Customer mockCustomer()
    {
        return mock(Customer.class);
    }

}
