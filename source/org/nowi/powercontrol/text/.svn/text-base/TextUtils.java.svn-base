package org.nowi.powercontrol.text;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class TextUtils
{
    private ResourceBundle defaultResourceBundle = null;
    private ResourceBundle errorResourceBundle = null;

    private static final class InstanceHolder
    {
        static final TextUtils INSTANCE = new TextUtils();
    }

    public static TextUtils getInstance()
    {
        return InstanceHolder.INSTANCE;
    }

    private TextUtils()
    {
        defaultResourceBundle = ResourceBundle.getBundle("text");
        errorResourceBundle = ResourceBundle.getBundle("error");
    }

    public String getText(String key)
    {
        String text = key;

        if (defaultResourceBundle != null)
        {
            try
            {
                text = defaultResourceBundle.getString(key);
            }

            catch (MissingResourceException ignored)
            {
            }
        }

        return text;
    }

    public String getErrorText(String key)
    {
        String text = key;

        if (errorResourceBundle != null)
        {
            try
            {
                text = errorResourceBundle.getString(key);
            }

            catch (MissingResourceException ignored)
            {
            }
        }

        return text;
    }
}
