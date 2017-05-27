package hotel.workers;

import javax.swing.SwingWorker;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class CallbackWorker extends SwingWorker<Boolean, Boolean>
{

    private Supplier<Boolean> callback;
    private Runnable after;

    public CallbackWorker(Supplier<Boolean> callback, Runnable after)
    {
        Objects.requireNonNull(callback);
        this.callback = callback;
        this.after = after;
    }

    @Override
    protected Boolean doInBackground() throws Exception
    {
        return callback.get();
    }

    @Override
    protected void done()
    {
        try {
            if(get() && after != null) {
                after.run();
            }
        } catch(InterruptedException | ExecutionException e) {

        }
    }

}
