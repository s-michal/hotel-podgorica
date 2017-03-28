package hotel;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

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

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }
}
