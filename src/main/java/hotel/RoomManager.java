package hotel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by uzivatel on 14.3.2017.
 */
public interface RoomManager {

    void create(Room room);

    void update(Room room);

    Room find(Long id);

    List<Room> findAll();

    List<Room> findAllByFloor(int floor);

    List<Room> findAllInPrice(BigDecimal price);

    void delete(Room room);
}
