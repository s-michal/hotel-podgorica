package hotel.gui;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseView
{

    private ResourceBundle resources = ResourceBundle.getBundle("Resources", Locale.getDefault());

    protected String translate(String text)
    {
        return resources.containsKey(text) ? resources.getString(text) : text;
    }

    public abstract Component getPanel();

}
