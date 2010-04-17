package org.nowi.powercontrol;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.nowi.powercontrol.cmd.InputPropertyServiceTest;
import org.nowi.powercontrol.http.HTTPSwitchTest;
import org.nowi.powercontrol.text.TextUtilsTest;
import org.nowi.powercontrol.udp.UDPSwitchTest;

public class TestAll extends TestCase
{
    public static Test suite()
    {
        TestSuite allTests = new TestSuite();
        allTests.addTestSuite(InputPropertyServiceTest.class);
        allTests.addTestSuite(PowerControlSwitchTest.class);
        allTests.addTestSuite(PowerControlSwitchFactoryTest.class);
        allTests.addTestSuite(TextUtilsTest.class);
        allTests.addTestSuite(UDPSwitchTest.class);
        allTests.addTestSuite(HTTPSwitchTest.class);
        return allTests;
    }
}
