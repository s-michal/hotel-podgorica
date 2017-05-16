package hotel.model.exceptions;

public class CustomerHasReservationsException extends Exception
{

    public CustomerHasReservationsException(String message)
    {
        super(message);
    }

}
