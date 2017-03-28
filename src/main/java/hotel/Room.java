package hotel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by uzivatel on 14.3.2017.
 */
public class Room implements Serializable
{

    private Long id;
    private long number;
    private int capacity;
    private int floor;
    private BigDecimal pricePerDay;

    public Room(long number, int capacity, int floor, BigDecimal pricePerDay)
    {
        this.number = number;
        this.capacity = capacity;
        this.floor = floor;
        this.pricePerDay = pricePerDay;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public long getNumber()
    {
        return number;
    }

    public void setNumber(Long number)
    {
        this.number = number;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public int getFloor()
    {
        return floor;
    }

    public void setFloor(int floor)
    {
        this.floor = floor;
    }

    public BigDecimal getPricePerDay()
    {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay)
    {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return id != null ? id.equals(room.id) : room.id == null;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}
