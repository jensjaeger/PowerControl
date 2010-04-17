package org.nowi.powercontrol.udp;

import junit.framework.TestCase;

import org.nowi.powercontrol.PowerControlParameters;
import org.nowi.powercontrol.PowerControlState;
import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;
import org.nowi.powercontrol.text.TextUtils;

public class UDPSwitchTest extends TestCase
{
    private static final int CURCUIT_TIME_IN_MS = 500;
    private PowerControlParameters parameters = null;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        // TODO parameters needs to meet your configuration!
        parameters = new PowerControlParameters();
        parameters.host = "192.168.1.13";
        parameters.password = "user";
        parameters.outputToXml = false;
        parameters.port = 75;
        parameters.responsePort = 7777;
        parameters.timeoutInMs = 3000;
    }

    public void testQueryPowerControlWithTextResult()
    {
        UDPSwitch udpSwitch = new UDPSwitch();

        try
        {
            PowerControlState udpResult = udpSwitch.queryPowerControl(parameters);

            String[] lines = udpResult.toString().split(PowerControlState.NEW_LINE);

            assertTrue(lines.length >= 10);

            assertTrue(lines[0].matches(TextUtils.getInstance().getText("Name1") + PowerControlState.LABEL_DELIMITER
                            + ".*"));
            assertTrue(lines[1].matches(TextUtils.getInstance().getText("Name2") + PowerControlState.LABEL_DELIMITER
                            + ".*"));
            assertTrue(lines[2].matches(TextUtils.getInstance().getText("IPAddress")
                            + PowerControlState.LABEL_DELIMITER + "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(lines[3].matches(TextUtils.getInstance().getText("Mask") + PowerControlState.LABEL_DELIMITER
                            + "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(lines[4].matches(TextUtils.getInstance().getText("Gateway") + PowerControlState.LABEL_DELIMITER
                            + "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(lines[5]
                            .matches(TextUtils.getInstance().getText("MACAddress")
                                            + PowerControlState.LABEL_DELIMITER
                                            + "[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}"));
            assertTrue(lines[6].matches(TextUtils.getInstance().getText("HTTPPort") + PowerControlState.LABEL_DELIMITER
                            + "\\d{1,5}"));
            assertTrue(lines[7].matches(TextUtils.getInstance().getText("Outlet") + "-1"
                            + PowerControlState.LABEL_DELIMITER + "[a-zA-z]{2,3}"));
            assertTrue(lines[8].matches(TextUtils.getInstance().getText("Outlet") + "-2"
                            + PowerControlState.LABEL_DELIMITER + "[a-zA-z]{2,3}"));
            assertTrue(lines[9].matches(TextUtils.getInstance().getText("Outlet") + "-3"
                            + PowerControlState.LABEL_DELIMITER + "[a-zA-z]{2,3}"));
        }

        catch (Exception e)
        {
            fail();
        }
    }

    public void testQueryPowerControlWithXmlResult()
    {
        UDPSwitch udpSwitch = new UDPSwitch();

        try
        {
            parameters.outputToXml = true;
            PowerControlState state = udpSwitch.queryPowerControl(parameters);

            assertTrue(state.getGateway().matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(state.getHost().matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(state
                            .getMacAddress()
                            .matches(
                                            "[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}"));
            assertTrue(state.getMask().matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
            assertTrue(state.getName1().matches(".*"));
            assertTrue(state.getName2().matches(".*"));
            assertTrue(state.getHttpPort() != null);
            assertNull(state.getErrorText());
            assertNull(state.getHttpErrorCode());

            assertTrue(state.getOutlets().containsKey(1));
            assertTrue(state.getOutlets().containsKey(2));
            assertTrue(state.getOutlets().containsKey(3));

            assertTrue(state.getOutlets().get(1).name().matches("ON|OFF"));
            assertTrue(state.getOutlets().get(2).name().matches("ON|OFF"));
            assertTrue(state.getOutlets().get(3).name().matches("ON|OFF"));
        }

        catch (Exception e)
        {
            fail();
        }
    }

    public void testSwitchPowerControlAndQueryWithXml()
    {
        UDPSwitch udpSwitch = new UDPSwitch();

        try
        {
            // turn OFF everything!
            parameters.command = SwitchCommand.OFF;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);

            // turn On 1
            parameters.command = SwitchCommand.ON;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.ON, SwitchCommand.OFF, SwitchCommand.OFF);

            // turn On 2
            parameters.command = SwitchCommand.ON;
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.ON, SwitchCommand.ON, SwitchCommand.OFF);

            // turn On 3
            parameters.command = SwitchCommand.ON;
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.ON, SwitchCommand.ON, SwitchCommand.ON);

            // turn OFF everything again!
            parameters.command = SwitchCommand.OFF;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);
        }

        catch (Exception e)
        {
            fail();
        }
    }

    public void testTogglePowerControlAndQueryWithXml()
    {
        UDPSwitch udpSwitch = new UDPSwitch();

        try
        {
            // turn OFF everything!
            parameters.command = SwitchCommand.OFF;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);

            // toggle 1 (ON)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.ON, SwitchCommand.OFF, SwitchCommand.OFF);

            // toggle 1 (OFF)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 1;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);

            // toggle 2 (ON)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.ON, SwitchCommand.OFF);

            // toggle 2 (OFF)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 2;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);

            // toggle 3 (ON)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.ON);

            // toggle 3 (OFF)
            parameters.command = SwitchCommand.TOGGLE;
            parameters.outlet = 3;
            udpSwitch.switchPowerControl(parameters);
            Thread.sleep(CURCUIT_TIME_IN_MS);
            validateOutletStates(udpSwitch, SwitchCommand.OFF, SwitchCommand.OFF, SwitchCommand.OFF);
        }

        catch (Exception e)
        {
            fail();
        }
    }

    private void validateOutletStates(UDPSwitch udpSwitch, SwitchCommand o1, SwitchCommand o2, SwitchCommand o3)
                    throws Exception
    {
        parameters.outputToXml = true;
        PowerControlState state = udpSwitch.queryPowerControl(parameters);

        assertTrue(state.getOutlets().containsKey(1));
        assertTrue(state.getOutlets().containsKey(2));
        assertTrue(state.getOutlets().containsKey(3));
        assertTrue(state.getOutlets().get(1).name().equals(o1.name()));
        assertTrue(state.getOutlets().get(2).name().equals(o2.name()));
        assertTrue(state.getOutlets().get(3).name().equals(o3.name()));
    }
}
