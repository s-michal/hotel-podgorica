package hotel;

import java.util.Date;

/**
 * Created by xsustera on 8.3.17.
 */
public class Customer
{

    private Long id;
    private String name;
    private String address;
    private Date birthDate;

    public Customer(String name, String adrress)
    {
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

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }
}
