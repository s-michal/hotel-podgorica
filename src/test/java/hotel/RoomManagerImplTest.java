package hotel;

import hotel.exceptions.DuplicateRoomNumberException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class RoomManagerImplTest extends TestWithDatabase
{

    RoomManagerImpl roomManager;

    @Before
    public void setUp() throws Exception
    {
        roomManager = new RoomManagerImpl(getDataSource(), null);
    }

    @Test
    public void testingCreateAndFindAllMethods() throws Exception
    {
        Room one = new Room((long) 1, 3, 1, BigDecimal.valueOf(800));
        roomManager.create(one);
        List<Room> allRoom = new ArrayList<Room>();
        allRoom.add(one);
        assertThat(roomManager.findAll()).isEqualTo(allRoom);
    }

    @Test
    public void testingFindByFloor() throws Exception
    {
        Room one = new Room((long) 1, 4, 2, BigDecimal.valueOf(1400));
        Room two = new Room((long) 2, 2, 2, BigDecimal.valueOf(600));
        Room three = new Room((long) 3, 3, 1, BigDecimal.valueOf(800));
        roomManager.create(one);
        roomManager.create(two);
        roomManager.create(three);
        assertThat(roomManager.findAllByFloor(2)).hasSize(2);
    }

    @Test
    public void testingFind() throws Exception
    {
        Room one = new Room((long) 1, 3, 1, BigDecimal.valueOf(800));
        roomManager.create(one);
        assertThat(one.getId()).isNotNull();
        assertThat(roomManager.find(one.getId())).isEqualTo(one);
    }

    @Test
    public void testingFindAllInPrice() throws Exception
    {
        Room one = new Room((long) 1, 3, 1, BigDecimal.valueOf(800));
        Room two = new Room((long) 2, 4, 2, BigDecimal.valueOf(1400));
        Room three = new Room((long) 3, 2, 2, BigDecimal.valueOf(600));
        roomManager.create(one);
        roomManager.create(two);
        roomManager.create(three);
        assertThat(roomManager.findAllInPrice(BigDecimal.valueOf(800))).hasSize(2);
    }

    @Test
    public void testingUpdate() throws Exception
    {
        Room room = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(room);
        room.setPricePerDay(BigDecimal.valueOf(1000));
        roomManager.update(room);
        assertThat(roomManager.find(room.getId())).isEqualTo(room);
    }

    @Test
    public void testingDelete() throws Exception
    {
        Room room = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(room);
        roomManager.delete(room);
        assertThat(roomManager.findAll()).hasSize(0);
    }

    @Test
    public void testingSameRoomNumber() throws Exception
    {
        Room one = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(one);
        Room two = new Room((long) 1, 3, 2, BigDecimal.valueOf(1000));
        assertThatThrownBy(() -> roomManager.create(two)).isInstanceOf(DuplicateRoomNumberException.class);
    }

    @Test
    public void testingCreateNullObject() throws Exception
    {
        assertThatThrownBy(() -> roomManager.create(null)).isInstanceOf(NullPointerException.class);
    }
}