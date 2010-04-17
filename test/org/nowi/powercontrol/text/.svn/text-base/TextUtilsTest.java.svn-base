package org.nowi.powercontrol.text;

import java.util.Locale;

import junit.framework.TestCase;

public class TextUtilsTest extends TestCase
{
    public void testGetTextIPGermany()
    {
        Locale.setDefault(Locale.GERMANY);
        String text = TextUtils.getInstance().getText("IPAddress");
        assertEquals(text, "IP-Adresse");
    }

    public void testGetErrorTextIPGermany()
    {
        Locale.setDefault(Locale.GERMANY);
        String text = TextUtils.getInstance().getErrorText("401");
        assertEquals(text, "Login fehlgeschlagen");
    }
}
