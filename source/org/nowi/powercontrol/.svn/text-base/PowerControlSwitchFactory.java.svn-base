package org.nowi.powercontrol;

import org.nowi.powercontrol.http.HTTPSwitch;
import org.nowi.powercontrol.udp.UDPSwitch;

public class PowerControlSwitchFactory
{
    public static enum SwitchProtocol
    {
        UDP, HTTP;
    }

    public static PowerControlIfc create(String protocol)
    {
        PowerControlIfc powerSwitcher = null;

        if (protocol != null)
        {
            String upperCaseProtocol = protocol.trim().toUpperCase();

            if (SwitchProtocol.UDP.name().equals(upperCaseProtocol))
            {
                powerSwitcher = new UDPSwitch();
            }
            else if (SwitchProtocol.HTTP.name().equals(upperCaseProtocol))
            {
                powerSwitcher = new HTTPSwitch();
            }
        }

        return powerSwitcher;
    }
}
