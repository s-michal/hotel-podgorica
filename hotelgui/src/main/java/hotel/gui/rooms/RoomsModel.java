package hotel.gui.rooms;

import hotel.model.HotelManager;
import hotel.model.Reservation;
import hotel.model.Room;
import hotel.model.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RoomsModel extends AbstractTableModel
{

    private RoomManager manager;
    private List<Room> rooms;
    private String[] columnNames = {"Number", "Capacity", "Floor", "Price per day"};

    private static Logger logger = LoggerFactory.getLogger(hotel.gui.reservations.ReservationsModel.class);

    public RoomsModel(RoomManager manager)
    {
        Objects.requireNonNull(manager);
        this.manager = manager;
    }

    @Override
    public int getRowCount()
    {
        return getRooms().size();
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Room room = getRooms().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return room.getNumber();
            case 1:
                return room.getCapacity();
            case 2:
                return room.getFloor();
            case 3:
                return room.getPricePerDay();
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
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return BigDecimal.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    private List<Room> getRooms()
    {
        if (this.rooms == null) {
            logger.debug("Loading rooms");
            this.rooms = manager.findAll();
        }
        return this.rooms;
    }

}