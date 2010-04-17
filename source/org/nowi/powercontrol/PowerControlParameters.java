package org.nowi.powercontrol;

import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;

public class PowerControlParameters
{
    public String host;
    public int port = PowerControlIfc.UDP_DEFAULT_PORT;

    public int responsePort = PowerControlIfc.UDP_DEFAULT_RECEIVE_PORT;

    public String login;
    public String password;
    public int timeoutInMs = PowerControlIfc.DEFAULT_TIMEOUT_IN_MS;

    public int outlet;
    public SwitchCommand command;

    public boolean outputToXml = false;

    public PowerControlState queryResult;
}
