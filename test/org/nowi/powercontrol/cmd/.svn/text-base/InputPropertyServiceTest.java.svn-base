package org.nowi.powercontrol.cmd;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.cli.ParseException;
import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;

public class InputPropertyServiceTest extends TestCase
{
    private static final String ARG_DELIMITER = " ";

    public void testEmptyCommandLine()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " ";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Empty command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testUnknownSwitchInCommandLine()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -y unknownSwitch";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Unknown switch in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingAllMandatoryParameters()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -f file.txt -k TXT -l login -o 1=ON -t 1000 -up 7777 -ur 7778";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing all mandatory parameters in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingHostParameterForQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c QUERY -p password -x UDP";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing host parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingLoginParameterForHttpQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c QUERY -p password -h 192.168.0.0 -x HTTP";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing login parameter in command line is not allowed for HTTP QUERY!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingPasswordParameterForHttpQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c QUERY -h 192.168.0.0 -x HTTP";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing password parameter in command line is not allowed for QUERY!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingPasswordParameterForUdpQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c QUERY -h 192.168.0.0 -x UDP";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing password parameter in command line is not allowed for QUERY!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongProtocolParameterForQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x HTML -p password -l login";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong protocol parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testDefaultProtocolParameterForQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c QUERY -h 192.168.0.0 -l login -p password";

        try
        {
            Configuration config = InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            assertEquals(Configuration.DEFAULT_PROTOCOL, config.getProtocol());
        }

        catch (ParseException e)
        {
            fail("There must be no ParseException!");
        }

        catch (IOException e)
        {
            fail("There must be no IOException!");
        }
    }

    public void testMissingPasswordParameterForUdpReset()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME + " -c RESET -h 192.168.0.0 -x UDP";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing password parameter in command line is not allowed for UDP RESET!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongProtocolParameterForUdpReset()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c RESET -h 192.168.0.0 -x HTTP -p password -l login";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong protocol parameter in command line is not allowed for UDP RESET!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testInvalidPortForUdpQuery1()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -up -1";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Invalid value in parameter port in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testInvalidPortForUdpQuery2()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -up 65536";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Invalid value in parameter port in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testInvalidReceivePortForUdpQuery1()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -ur -1";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Invalid value in parameter receive port in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testInvalidReceivePortForUdpQuery2()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -ur 65536";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Invalid value in parameter receive port in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testIdenticalPortsForUdpQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -ur 7777 -up 7777";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Identical values in parameter ports in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongDataTypeForTimeoutParameterForUdpQuery()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x UDP -p password -ur 77 -up 75 -t wrongDatatype";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong data type in parameter timeout in command line is not allowed for UDP Query!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testUnknownCommandInCommandLine()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c PRAY -h 192.168.0.0 -x UDP -p password -ur 77 -up 75";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Unknown command in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testUnknownProtocolInCommandLine()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c QUERY -h 192.168.0.0 -x PROTEIN -p password -ur 77 -up 75";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Unknown protocol in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingOutletParameterInCommandLineForHttpSwitch()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing outlet parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongOutletParameterInCommandLineForHttpSwitch1()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 0=ON";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong outlet parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingOutletParameterValueInCommandLineForHttpSwitch()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o ";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing outlet parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongOutletParameterInCommandLineForHttpSwitch2()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=UPS";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong outlet parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongOutletParameterInCommandLineForHttpSwitch3()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=UPS,2=LALA";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong outlet parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testWrongOutputTypeParameterInCommandLineForHttpSwitch()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=ON -k TEXT";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Wrong output type parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testMissingOutputTypeParameterInCommandLineForHttpSwitch()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=ON -k";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("Missing output type parameter in command line is not allowed!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be a ParseException instead of an IOException!");
        }
    }

    public void testParametersInCommandLineForHttpSwitchOutlet1ToOn()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=ON";

        try
        {
            Configuration config = InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));

            assertEquals(config.getCommand(), null);
            assertEquals(config.getHost(), "192.168.0.0");
            assertEquals(config.getProtocol(), Configuration.DEFAULT_PROTOCOL);
            assertEquals(config.getLogin(), "login");
            assertEquals(config.getPassword(), "password");
            assertTrue(config.getCommands().containsKey(1));
            assertEquals(config.getCommands().get(1), SwitchCommand.ON);
        }

        catch (ParseException e)
        {
            fail("There must be no ParseException!");
        }

        catch (IOException e)
        {
            fail("There must be no ParseException!");
        }
    }

    public void testParametersInCommandLineForHttpMultiSwitch1()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=ON -o 2=OFF -o 3=ON";

        try
        {
            Configuration config = InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));

            assertEquals(config.getCommand(), null);
            assertEquals(config.getHost(), "192.168.0.0");
            assertEquals(config.getProtocol(), Configuration.DEFAULT_PROTOCOL);
            assertEquals(config.getLogin(), "login");
            assertEquals(config.getPassword(), "password");
            assertTrue(config.getCommands().containsKey(1));
            assertEquals(config.getCommands().get(1), SwitchCommand.ON);
            assertTrue(config.getCommands().containsKey(2));
            assertEquals(config.getCommands().get(2), SwitchCommand.OFF);
            assertTrue(config.getCommands().containsKey(3));
            assertEquals(config.getCommands().get(3), SwitchCommand.ON);
        }

        catch (ParseException e)
        {
            fail("There must be no ParseException!");
        }

        catch (IOException e)
        {
            fail("There must be no ParseException!");
        }
    }

    public void testParametersInCommandLineForHttpMultiSwitchWitchDifferentCasesForStatus()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=on -o 2=Off -o 3=On";

        try
        {
            Configuration config = InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));

            assertEquals(config.getCommand(), null);
            assertEquals(config.getHost(), "192.168.0.0");
            assertEquals(config.getProtocol(), Configuration.DEFAULT_PROTOCOL);
            assertEquals(config.getLogin(), "login");
            assertEquals(config.getPassword(), "password");
            assertTrue(config.getCommands().containsKey(1));
            assertEquals(config.getCommands().get(1), SwitchCommand.ON);
            assertTrue(config.getCommands().containsKey(2));
            assertEquals(config.getCommands().get(2), SwitchCommand.OFF);
            assertTrue(config.getCommands().containsKey(3));
            assertEquals(config.getCommands().get(3), SwitchCommand.ON);
        }

        catch (ParseException e)
        {
            fail("There must be no ParseException!");
        }

        catch (IOException e)
        {
            fail("There must be no ParseException!");
        }
    }

    public void testParametersInCommandLineForHttpMultiSwitchWitchDuplicatedOutlets()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x HTTP -l login -p password -o 1=on -o 2=Off -o 3=On -o 1=off";

        try
        {
            InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));
            fail("There must be a ParseException due to duplicated outlet to switch!");
        }

        catch (ParseException e)
        {
            assertTrue(true);
        }

        catch (IOException e)
        {
            fail("There must be no IOException!");
        }
    }

    public void testParametersInCommandLineForUdpMultiSwitch1()
    {
        String commandLine = InputPropertyService.APPLICATION_NAME
                        + " -c SWITCH -h 192.168.0.0 -x UDP -p password -o 1=ON -o 2=OFF -o 3=ON";

        try
        {
            Configuration config = InputPropertyService.getInstance().init(commandLine.split(ARG_DELIMITER));

            assertEquals(config.getCommand(), null);
            assertEquals(config.getHost(), "192.168.0.0");
            assertEquals(config.getProtocol(), Configuration.UDP_PROTOCOL);
            assertEquals(config.getPassword(), "password");
            assertTrue(config.getCommands().containsKey(1));
            assertEquals(config.getCommands().get(1), SwitchCommand.ON);
            assertTrue(config.getCommands().containsKey(2));
            assertEquals(config.getCommands().get(2), SwitchCommand.OFF);
            assertTrue(config.getCommands().containsKey(3));
            assertEquals(config.getCommands().get(3), SwitchCommand.ON);
        }

        catch (ParseException e)
        {
            fail("There must be no ParseException!");
        }

        catch (IOException e)
        {
            fail("There must be no ParseException!");
        }
    }
}
