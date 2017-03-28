package hotel;

public class ApplicationException extends Exception
{

    public ApplicationException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

}
