package hotel;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by uzivatel on 14.3.2017.
 */
public class RoomManagerImplTest {

    RoomManagerImpl roomManager;

    public void setUp() throws Exception {
        roomManager = new RoomManagerImpl();
        Room one = new Room();
        one.setNumber((long) 1);
        one.setCapacity(3);
        one.setFloor(1);
        one.setPricePerDay(BigDecimal.valueOf(800));
        Room two = new Room();
        two.setNumber((long) 2);
        two.setCapacity(4);
        two.setFloor(2);
        two.setPricePerDay(BigDecimal.valueOf(1400));
        Room three = new Room();
        three.setNumber((long) 3);
        three.setCapacity(2);
        three.setFloor(2);
        three.setPricePerDay(BigDecimal.valueOf(600));
        roomManager.create(one);
        roomManager.create(two);
        roomManager.create(three);
    }

    @Test
    public void create() throws Exception {
        Room room = new Room();
        room.setNumber((long) 4);
        roomManager.create(room);
        assertEquals("In database is not created new room", 4, roomManager.findAll().size());
    }

    @Test
    public void update() throws Exception {
        Room room = new Room();
        room.setNumber((long) 2);
        room.setCapacity(4);
        room.setPricePerDay(BigDecimal.valueOf(1400));
        room.setFloor(3);
        roomManager.update(room);
        assertEquals(1, roomManager.findAllByFloor(3).size());
    }

    @Test
    public void find() throws Exception {
        assertNotNull("Room with ID is not exist", roomManager.find((long) 1));
    }

    @Test
    public void findAll() throws Exception {
        assertEquals("Not returns all rooms",3, roomManager.findAll().size());
    }

    @Test
    public void findAllByFloor() throws Exception {
        assertEquals(2, roomManager.findAllByFloor(2).size());
    }

    @Test
    public void findAllInPrice() throws Exception {
        assertEquals(2, roomManager.findAllInPrice(BigDecimal.valueOf(800)));
    }

    @Test
    public void delete() throws Exception {
        roomManager.delete(roomManager.find((long) 1));
        assertEquals(2, roomManager.findAll().size());
    }

}