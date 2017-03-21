package hotel;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoomManagerImplTest
{

    RoomManagerImpl roomManager;

    public void setUp() throws Exception
    {
        roomManager = new RoomManagerImpl();
    }

    @Test
    public void testingFindMethods() throws Exception
    {
        Room one = new Room((long) 1, 3, 1, BigDecimal.valueOf(800));
        Room two = new Room((long) 2, 4, 2, BigDecimal.valueOf(1400));
        Room three = new Room((long) 3, 2, 2, BigDecimal.valueOf(600));
        roomManager.create(one);
        roomManager.create(two);
        roomManager.create(three);
        List<Room> allRoom = new ArrayList<Room>();
        allRoom.add(one);
        allRoom.add(two);
        allRoom.add(three);
        assertEquals("In database is not created all rooms.", allRoom, roomManager.findAll());
        assertEquals("Find by all is not implemented correctly.", 2, roomManager.findAllByFloor(2).size());
        Long id = null;
        Room roomOne = null;
        for (Room room : roomManager.findAllByFloor(1))
        {
            roomOne = room;
            id = room.getId();
            assertNotNull("ID of room isn't exist.", id);
        }
        assertEquals("Room with this ID isn't same.", roomOne, roomManager.find(id));
        assertEquals(2, roomManager.findAllInPrice(BigDecimal.valueOf(800)));
    }

    @Test
    public void testingUpdateDelete() throws Exception
    {
        Room room = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(room);
        room.setPricePerDay(BigDecimal.valueOf(1000));
        roomManager.update(room);
        for (Room testRoom : roomManager.findAll())
        {
            assertEquals("Rooms aren't same, update wasn't correct.", room, testRoom);
        }
        roomManager.delete(room);
        assertNull("Room wasn't deleted.", roomManager.findAll());
    }

    @Test
    public void testingWrongInput() throws Exception
    {
        Room one = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(one);
        try
        {
            Room two = new Room(null, 3, 2, BigDecimal.valueOf(1000));
            throw new IllegalArgumentException("Input is null.");
        } catch (IllegalArgumentException iax)
        {
        }
        try
        {
            Room three = new Room((long) 1, 3, 2, BigDecimal.valueOf(1000));
            roomManager.create(three);
            throw new IllegalArgumentException("Room with this number has already existed.");
        } catch (IllegalArgumentException iax)
        {
        }
        try
        {
            roomManager.create(null);
            throw new NullPointerException("Creating room may not be null.");
        } catch (NullPointerException npe)
        {
        }
    }
}