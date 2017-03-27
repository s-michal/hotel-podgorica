package hotel;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(roomManager.findAll()).isEqualTo(allRoom);
        assertThat(roomManager.findAllByFloor(2)).hasSize(2);
        Long id = null;
        Room roomOne = null;
        for (Room room : roomManager.findAllByFloor(1))
        {
            roomOne = room;
            id = room.getId();
            assertThat(id).isNotNull();
        }
        assertThat(roomManager.find(id)).isEqualTo(roomOne);
        assertThat(roomManager.findAllInPrice(BigDecimal.valueOf(800))).hasSize(2);
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
            assertThat(testRoom).isEqualTo(room);
        }
        roomManager.delete(room);
        assertThat(roomManager.findAll()).hasSize(0);
    }

    @Test
    public void testingWrongInput() throws Exception
    {
        Room one = new Room((long) 1, 4, 3, BigDecimal.valueOf(1400));
        roomManager.create(one);
        assertThatThrownBy(() -> new Room(null, 3, 2, BigDecimal.valueOf(1000)))
                .isInstanceOf(NullPointerException.class);
        Room three = new Room((long) 1, 3, 2, BigDecimal.valueOf(1000));
        assertThatThrownBy(() -> roomManager.create(three)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> roomManager.create(null)).isInstanceOf(NullPointerException.class);
    }
}