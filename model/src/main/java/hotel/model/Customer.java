package hotel.model;


import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by xsustera on 8.3.17.
 */
public class Customer implements Serializable
{

    private Long id;
    private String name;
    private String address;
    private LocalDate birthDate;

    public Customer(String name, String address, LocalDate birthDate)
    {
        update(name, address, birthDate);
    }

    public Long getId()
    {
        return id;
    }

    void setId(Long id)
    {
        if(this.id != null) {
            throw new IllegalStateException("User already has identity");
        }
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public void update(String name, String address, LocalDate birthDate)
    {
        if(name == null || name.equals("")) {
            throw new IllegalArgumentException("Customer must have a name");
        }
        if(address == null || address.equals("")) {
            throw new IllegalArgumentException("User must have an address");
        }
        if(birthDate == null) {
            throw new IllegalArgumentException("User must have a birth date");
        }
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id != null ? id.equals(customer.id) : customer.id == null;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}
