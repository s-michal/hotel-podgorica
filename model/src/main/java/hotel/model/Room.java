package hotel.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Room implements Serializable
{

    private Long id;
    private long number;
    private int capacity;
    private int floor;
    private BigDecimal pricePerDay;

    public Room(long number, int capacity, int floor, BigDecimal pricePerDay)
    {
        if(number < 0) {
            throw new IllegalArgumentException("Room number can't be negative");
        }
        if(capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        if(floor < 0) {
            throw new IllegalArgumentException("We have no rooms underground (yet!)");
        }
        if(pricePerDay == null || pricePerDay.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("Price must be positive number");
        }
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

    public void update(long number, int capacity, int floor, BigDecimal pricePerDay)
    {
        if(number <= 0) {
            throw new IllegalArgumentException("Number must be greater than 0");
        }
        if(capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if(floor <= 0) {
            throw new IllegalArgumentException("Floor must be greater than 0");
        }
        if(pricePerDay == null){
            throw new IllegalArgumentException("Room must have a price");
        }
        this.number = number;
        this.capacity = capacity;
        this.floor = floor;
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
