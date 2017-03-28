package hotel;

import hotel.database.Association;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable
{

    private Long id;

    @Association
    private Room room;

    @Association
    private Customer customer;

    private LocalDate since;
    private LocalDate until;

    public Reservation(Room room, Customer customer, LocalDate since, LocalDate until)
    {
        this.room = room;
        this.customer = customer;
        this.since = since;
        this.until = until;
    }

    public void setId(long id)
    {
        if(this.id != null) {
            throw new IllegalStateException("Reservation already has identity");
        }
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public LocalDate getSince()
    {
        return since;
    }

    public LocalDate getUntil()
    {
        return until;
    }

    public Room getRoom()
    {
        return room;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}
