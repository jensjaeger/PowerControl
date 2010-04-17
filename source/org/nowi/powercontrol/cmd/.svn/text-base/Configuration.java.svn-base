package org.nowi.powercontrol.cmd;

import java.util.Map;
import java.util.TreeMap;

import org.nowi.powercontrol.PowerControlIfc;
import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;
import org.nowi.powercontrol.cmd.InputPropertyService.OutputFormat;

public class Configuration
{
    public static final String DEFAULT_PROTOCOL = "HTTP";
    public static final String UDP_PROTOCOL = "UDP";

    private Map<Integer, SwitchCommand> commands;

    private SwitchCommand command;

    private String protocol = DEFAULT_PROTOCOL;

    private String host;

    private int udpPort = PowerControlIfc.UDP_DEFAULT_PORT;

    private int udpResponsePort = PowerControlIfc.UDP_DEFAULT_RECEIVE_PORT;

    private String login;

    private String password;

    private int timeout = PowerControlIfc.DEFAULT_TIMEOUT_IN_MS;

    private OutputFormat outputType = OutputFormat.TEXT;

    private String outputFileName;

    public Configuration()
    {
        commands = new TreeMap<Integer, SwitchCommand>();
    }

    public Map<Integer, SwitchCommand> getCommands()
    {
        return commands;
    }

    public void setCommands(Map<Integer, SwitchCommand> command)
    {
        this.commands = command;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getUdpPort()
    {
        return udpPort;
    }

    public void setUdpPort(int udpPort)
    {
        this.udpPort = udpPort;
    }

    public int getUdpResponsePort()
    {
        return udpResponsePort;
    }

    public void setUdpResponsePort(int udpResponsePort)
    {
        this.udpResponsePort = udpResponsePort;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public OutputFormat getOutputType()
    {
        return outputType;
    }

    public void setOutputType(OutputFormat outputType)
    {
        this.outputType = outputType;
    }

    public String getOutputFileName()
    {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName)
    {
        this.outputFileName = outputFileName;
    }

    public SwitchCommand getCommand()
    {
        return command;
    }

    public void setCommand(SwitchCommand command)
    {
        this.command = command;
    }
}
