package org.nowi.powercontrol;

public abstract class PowerControlSwitch implements PowerControlIfc
{
    private static final String URL_DELIMITER = "/";
    protected static final int DEFAULT_HTTP_PORT = 80;
    protected static final String HTTP_PROTOCOL = "http://";

    protected static final String LABEL_DELIMITER = ": ";
    protected static final String MAC_ADDRESS_DELIMITER = ":";

    protected PowerControlState powerControlState;

    public static enum SwitchCommand
    {
        ON("On", "1", "I", "on", true),
        OFF("Off", "0", "O", "off", false),
        RESET("RESET", "", "", "Reset:", false),
        QUERY("QUERY", "", "", "wer da?\r\n", false),
        SWITCH("SWITCH", "", "", "Sw_", false),
        TOGGLE("TOGGLE", "", "", "", false);

        private final String description;
        private final String udpStatus;
        private final String httpStatus;
        private final String udpCommand;
        private final boolean isOn;

        private SwitchCommand(String newDescription, String newUdpStatus, String newHttpStatus, String newUdpCommand,
                        boolean newStatus)
        {
            this.description = newDescription;
            this.udpStatus = newUdpStatus;
            this.httpStatus = newHttpStatus;
            this.udpCommand = newUdpCommand;
            this.isOn = newStatus;
        }

        public SwitchCommand toggle()
        {
            if (SwitchCommand.OFF == this)
            {
                return SwitchCommand.ON;
            }
            else if (SwitchCommand.ON == this)
            {
                return SwitchCommand.OFF;
            }
            else
            {
                return this;
            }
        }

        @Override
        public String toString()
        {
            return description;
        }

        public String getUdpStatus()
        {
            return udpStatus;
        }

        public String getHttpStatus()
        {
            return httpStatus;
        }

        public boolean isOn()
        {
            return isOn;
        }

        public String getUdpCommand()
        {
            return udpCommand;
        }
    }

    public abstract PowerControlState switchPowerControl(PowerControlParameters parameters) throws Exception;

    public abstract PowerControlState queryPowerControl(PowerControlParameters parameters) throws Exception;

    protected boolean isNecessaryToSwitch(PowerControlParameters parameters) throws Exception
    {
        boolean doSwitch = true;

        boolean rememberOutputToXml = parameters.outputToXml;

        try
        {
            parameters.outputToXml = true;

            PowerControlState state = queryPowerControl(parameters);

            SwitchCommand currentState = state.getOutlets().get(parameters.outlet);
            if (currentState != null)
            {
                boolean doToggle = SwitchCommand.TOGGLE == parameters.command;
                doSwitch = doToggle || !(currentState == parameters.command);

                if (doToggle)
                {
                    parameters.command = currentState.toggle();
                }
                else if (!doSwitch)
                {
                    state.resetAllFieldsExceptOutlets();
                    parameters.queryResult = state;
                }
            }
        }

        finally
        {
            parameters.outputToXml = rememberOutputToXml;
        }

        return doSwitch;
    }

    public static String convertToHexString(String decimalString, String delimiter)
    {
        StringBuilder buf = null;

        if (decimalString != null)
        {
            String[] numbers = delimiter != null && delimiter.trim().length() > 0 ? decimalString.split(delimiter)
                            : new String[] { decimalString };

            if (numbers != null && numbers.length > 0)
            {
                buf = new StringBuilder(17);

                int i = 0;
                for (String number : numbers)
                {
                    try
                    {
                        int iNumber = Integer.parseInt(number);

                        buf.append(String.format("%02X", iNumber));

                        if (i < numbers.length - 1)
                        {
                            buf.append(MAC_ADDRESS_DELIMITER);
                        }
                    }

                    catch (NumberFormatException ignored)
                    {
                        return null;
                    }

                    i++;
                }
            }
        }

        return buf != null ? buf.toString() : null;
    }

    public static String getNormalizedHost(String parameterHost, String page)
    {
        StringBuilder hostBuf = null;

        if (parameterHost != null)
        {
            hostBuf = new StringBuilder();

            if (!parameterHost.toLowerCase().startsWith(HTTP_PROTOCOL))
            {
                hostBuf.append(HTTP_PROTOCOL);
            }

            hostBuf.append(parameterHost);

            if (!hostBuf.toString().endsWith(URL_DELIMITER))
            {
                hostBuf.append(URL_DELIMITER);
            }

            if (page != null)
            {
                hostBuf.append(page);
            }
        }

        return hostBuf != null ? hostBuf.toString() : null;
    }

    public static int parseInt(String intString, int defaultValue)
    {
        int result = defaultValue;

        try
        {
            if (intString != null)
            {
                result = Integer.parseInt(intString.trim());
            }
        }

        catch (NumberFormatException ignored)
        {
        }

        return result;
    }
}
