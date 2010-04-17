package org.nowi.powercontrol;

import junit.framework.TestCase;

import org.nowi.powercontrol.http.HTTPSwitch;
import org.nowi.powercontrol.udp.UDPSwitch;

public class PowerControlSwitchFactoryTest extends TestCase
{
    public void testCreateUdpLowerCase()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("udp");
        assertTrue(powerControl instanceof UDPSwitch);
    }

    public void testCreateUdpUpperCase()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("UDP");
        assertTrue(powerControl instanceof UDPSwitch);
    }

    public void testCreateHttpLowerCase()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("http");
        assertTrue(powerControl instanceof HTTPSwitch);
    }

    public void testCreateHttpUpperCase()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("HTTP");
        assertTrue(powerControl instanceof HTTPSwitch);
    }

    public void testCreateNullFactory()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create(null);
        assertNull(powerControl);
    }

    public void testCreateEmptyFactory()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("");
        assertNull(powerControl);
    }

    public void testCreateNoFactory()
    {
        PowerControlIfc powerControl = PowerControlSwitchFactory.create("FTP");
        assertNull(powerControl);
    }
}
