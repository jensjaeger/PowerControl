package org.nowi.powercontrol;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.cli.ParseException;
import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;
import org.nowi.powercontrol.cmd.Configuration;
import org.nowi.powercontrol.cmd.InputPropertyService;
import org.nowi.powercontrol.cmd.InputPropertyService.OutputFormat;

public class PowerControl
{
    private static final int ERROR_UNKNOWN_ERROR = 256;
    private static final int ERROR_CODE_OK = 0;
    private static final int ERROR_CODE_PARSE_PARAMETERS = 1;
    private static final int ERROR_CODE_OUTPUT_FILE_ERROR = 2;
    private static final int ERROR_CODE_COMMUNICATION_ERROR = 4;
    private static final int ERROR_CODE_TIMEOUT_ERROR = 8;
    private static final int ERROR_CODE_AUTHENTICATION_ERROR = 16;

    public static void main(String[] args)
    {
        Configuration config = null;

        try
        {
            config = InputPropertyService.getInstance().init(args);
        }

        catch (ParseException e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_CODE_PARSE_PARAMETERS);
        }

        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_CODE_OUTPUT_FILE_ERROR);
        }

        try
        {
            PowerControlState state = null;
            PowerControlIfc powerSwitchControl = PowerControlSwitchFactory.create(config.getProtocol());

            if (config.getCommand() == null)
            {
                // switch on/off
                state = doSwitch(config, powerSwitchControl);
            }
            else if (SwitchCommand.QUERY.equals(config.getCommand()))
            {
                // query
                state = doQuery(config, powerSwitchControl);
            }
            else
            {
                // reset not yet implemented
            }

            if (state != null)
            {
                if (state.getHttpErrorCode() != null)
                {
                    switch (state.getHttpErrorCode())
                    {
                        case 401:
                            System.exit(ERROR_CODE_AUTHENTICATION_ERROR);
                            break;

                        default:
                            System.exit(state.getHttpErrorCode());
                            break;
                    }
                }
            }
        }

        catch (TimeoutException e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_CODE_TIMEOUT_ERROR);
        }

        catch (SocketTimeoutException e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_CODE_TIMEOUT_ERROR);
        }

        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_CODE_COMMUNICATION_ERROR);
        }

        catch (Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(ERROR_UNKNOWN_ERROR);
        }

        System.exit(ERROR_CODE_OK);
    }

    private static PowerControlState doQuery(Configuration config, PowerControlIfc powerSwitchControl)
                    throws Exception, IOException
    {
        PowerControlParameters parameters = new PowerControlParameters();

        parameters.host = config.getHost();
        parameters.login = config.getLogin();
        parameters.outputToXml = config.getOutputType().equals(OutputFormat.XML);
        parameters.password = config.getPassword();
        parameters.port = config.getUdpPort();
        parameters.responsePort = config.getUdpResponsePort();
        parameters.timeoutInMs = config.getTimeout();

        PowerControlState queryResult = powerSwitchControl.queryPowerControl(parameters);

        doOutput(config, parameters, queryResult);

        return queryResult;
    }

    private static PowerControlState doSwitch(Configuration config, PowerControlIfc powerSwitchControl)
                    throws Exception
    {
        PowerControlParameters parameters = new PowerControlParameters();

        parameters.host = config.getHost();
        parameters.login = config.getLogin();
        parameters.outputToXml = config.getOutputType().equals(OutputFormat.XML);
        parameters.password = config.getPassword();
        parameters.port = config.getUdpPort();
        parameters.responsePort = config.getUdpResponsePort();
        parameters.timeoutInMs = config.getTimeout();

        PowerControlState switchResult = null;

        for (Integer outlet : config.getCommands().keySet())
        {
            SwitchCommand command = config.getCommands().get(outlet);
            parameters.command = command;
            parameters.outlet = outlet;

            switchResult = powerSwitchControl.switchPowerControl(parameters);
        }

        doOutput(config, parameters, switchResult);

        return switchResult;
    }

    private static void doOutput(Configuration config, PowerControlParameters parameters, PowerControlState switchResult)
                    throws IOException
    {
        if (switchResult != null)
        {
            if (config.getOutputFileName() != null)
            {
                switchResult.saveToFile(config.getOutputFileName(), parameters.outputToXml);
            }
            else
            {
                System.out.println(parameters.outputToXml ? switchResult.toXml() : switchResult.toString());
            }
        }
    }
}
