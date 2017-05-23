package hotel.workers;

import javax.swing.*;
import java.util.Objects;

public class CallbackWorker extends SwingWorker<Boolean, Void>
{

    private Runnable callback;
    private Runnable after;

    public CallbackWorker(Runnable callback, Runnable after)
    {
        Objects.requireNonNull(callback);
        this.callback = callback;
        this.after = after;
    }

    @Override
    protected Boolean doInBackground() throws Exception
    {
        callback.run();
        return true;
    }

    @Override
    protected void done()
    {
        if(after != null) {
            after.run();
        }
    }

}
