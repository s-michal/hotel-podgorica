package hotel.webapp.forms.exceptions;

public class ValidationException extends Throwable
{

    public ValidationException(String message)
    {
        super(message);
    }

    public ValidationException(Throwable e)
    {
        super(e);
    }

}
