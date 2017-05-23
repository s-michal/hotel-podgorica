package hotel.gui;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseView
{

    private ResourceBundle resources = ResourceBundle.getBundle("Resources", Locale.getDefault());

    protected String translate(String text)
    {
        String val = resources.getString(text);
        try
        {
            return resources.containsKey(text) ? new String(val.getBytes("ISO-8859-1"), "UTF-8") : text;
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return text;
    }

    public abstract Component getPanel();

}
