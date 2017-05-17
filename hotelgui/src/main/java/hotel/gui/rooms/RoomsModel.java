package hotel.gui.rooms;

import hotel.gui.BaseModel;
import hotel.model.HotelManager;
import hotel.model.Room;
import hotel.model.RoomManager;
import hotel.model.exceptions.RoomHasReservationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class RoomsModel extends BaseModel<Room>
{

    private RoomManager manager;
    private HotelManager hotelManager;
    private List<Room> rooms;

    private static Logger logger = LoggerFactory.getLogger(RoomsModel.class);

    RoomsModel(RoomManager manager, HotelManager hotelManager)
    {
        super(
                new String[] {"Number", "Capacity", "Floor", "Price per day"},
                new Class<?>[] {Long.class, Integer.class, Integer.class, BigDecimal.class}
        );

        Objects.requireNonNull(manager);
        Objects.requireNonNull(hotelManager);
        this.manager = manager;
        this.hotelManager = hotelManager;
    }

    @Override
    public int getRowCount()
    {
        return getRooms().size();
    }

    @Override
    protected Object getColumnValue(int rowIndex, int columnIndex)
    {
        Room room = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return room.getNumber();
            case 1:
                return room.getCapacity();
            case 2:
                return room.getFloor();
            case 3:
                return room.getPricePerDay();
        }
        throw new IllegalArgumentException("columnIndex");
    }

    @Override
    protected Room getRow(int rowIndex)
    {
        return getRooms().get(rowIndex);
    }

    public void invalidate()
    {
        rooms = null;
        fireTableDataChanged();
    }

    public void deleteRoom(Room room) throws RoomHasReservationException
    {
        try {
            manager.delete(room);
        } catch(RoomHasReservationException e) {
            throw e;
        } finally
        {
            invalidate();
        }
    }

    public boolean hasReservations(Room room)
    {
        return hotelManager.findReservationByRoom(room).size() > 0;
    }

    private List<Room> getRooms()
    {
        if (rooms == null) {
            logger.debug("Loading rooms");
            rooms = manager.findAll();
        }
        return rooms;
    }

}