package hotel.gui.reservations;

import hotel.gui.BaseModel;
import hotel.model.HotelManager;
import hotel.model.Reservation;
import hotel.model.exceptions.ReservationCannotBeCanceledNow;
import hotel.model.exceptions.RoomHasReservationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReservationsModel extends BaseModel<Reservation>
{

    private HotelManager manager;
    private List<Reservation> reservations;

    private static Logger logger = LoggerFactory.getLogger(ReservationsModel.class);

    public ReservationsModel(HotelManager manager)
    {
        super(
                new String[] {"ID", "Customer", "Since", "Until", "Room", "Price"},
                new Class<?>[] {Long.class, String.class, LocalDate.class, LocalDate.class, Long.class, BigDecimal.class}
        );

        Objects.requireNonNull(manager);
        this.manager = manager;
    }

    @Override
    public int getRowCount()
    {
        return getReservations().size();
    }

    @Override
    protected Object getColumnValue(int rowIndex, int columnIndex)
    {
        Reservation reservation = getRow(rowIndex);
        switch(columnIndex) {
            case 0:
                return reservation.getId();
            case 1:
                return reservation.getCustomer().getName();
            case 2:
                return reservation.getSince();
            case 3:
                return reservation.getUntil();
            case 4:
                return reservation.getRoom().getNumber();
            case 5:
                return reservation.getTotalPrice();
        }
        throw new IllegalArgumentException("columnIndex");
    }

    public void cancelReservation(Reservation reservation) throws ReservationCannotBeCanceledNow
    {
        try {
            reservation.cancel(LocalDate.now());
            manager.update(reservation);
        } catch(ReservationCannotBeCanceledNow e) {
            throw e;
        } finally {
            reload();
        }
    }

    @Override
    protected Reservation getRow(int rowIndex)
    {
        return getReservations().get(rowIndex);
    }

    private List<Reservation> getReservations()
    {
        if (this.reservations == null) {
            logger.debug("Loading reservations");
            this.reservations = manager.findAll();
        }
        return this.reservations;
    }

    public void reload()
    {
        this.reservations = null;
        getReservations();
        fireTableDataChanged();
    }

    public void placeReservation(Reservation reservation) throws RoomHasReservationException
    {
        Objects.requireNonNull(reservation);
        manager.placeReservation(reservation);
        reload();
    }

}