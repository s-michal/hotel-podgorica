package hotel;

import java.util.Objects;

/**
 * Created by fmasa on 15.3.17.
 */
public class Reservation
{

    private Room room;

    public Reservation(Room room)
    {
        Objects.requireNonNull(room);
        this.room = room;
    }

    public Long getId()
    {
        return null;
    }

    public Room getRoom()
    {
        return room;
    }

}
