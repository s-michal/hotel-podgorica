package hotel;


import java.time.LocalDate;

/**
 * Created by xsustera on 8.3.17.
 */
public class Customer
{

    private Long id;
    private String name;
    private String address;
    private LocalDate birthDate;

    public Customer(String name, String address, LocalDate birthDate)
    {
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
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

    public void setAddress(String address)
    {
        this.address = address;
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
