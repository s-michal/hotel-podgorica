package hotel;

import hotel.exceptions.DuplicateRoomNumberException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by uzivatel on 14.3.2017.
 */
public interface RoomManager
{

    void create(Room room) throws ApplicationException, DuplicateRoomNumberException;

    void update(Room room) throws ApplicationException;

    Room find(Long id) throws ApplicationException;

    List<Room> findAll() throws ApplicationException;

    List<Room> findAllByFloor(int floor) throws ApplicationException;

    List<Room> findAllInPrice(BigDecimal price) throws ApplicationException;

    void delete(Room room);
}
