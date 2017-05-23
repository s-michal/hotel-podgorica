package hotel.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseView
{

    private static final ResourceBundle resources = ResourceBundle.getBundle("Resources", Locale.getDefault());

    private static final Logger logger = LoggerFactory.getLogger(BaseView.class);

    public static String translate(String text)
    {
        if(resources.containsKey(text)) {
            return resources.getString(text);
        }

        logger.error(String.format("Translation for '%s' not found in %s", text, resources.getLocale().getLanguage()));
        return text;
    }

    public abstract Component getPanel();

}
