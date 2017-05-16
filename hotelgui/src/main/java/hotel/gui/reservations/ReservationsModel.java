package hotel.gui.reservations;

import hotel.model.HotelManager;
import hotel.model.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReservationsModel extends AbstractTableModel
{

    private HotelManager manager;
    private List<Reservation> reservations;
    private String[] columnNames = {"ID", "Customer", "Since", "Until", "Room", "Price"};

    private static Logger logger = LoggerFactory.getLogger(ReservationsModel.class);

    public ReservationsModel(HotelManager manager)
    {
        Objects.requireNonNull(manager);
        this.manager = manager;
    }

    @Override
    public int getRowCount()
    {
        return getReservations().size();
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Reservation reservation = getReservations().get(rowIndex);
        switch (columnIndex) {
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
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return LocalDate.class;
            case 3:
                return LocalDate.class;
            case 4:
                return Long.class;
            case 5:
                return BigDecimal.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    private List<Reservation> getReservations()
    {
        if (this.reservations == null) {
            logger.debug("Loading reservations");
            this.reservations = manager.findAll();
        }
        return this.reservations;
    }

}