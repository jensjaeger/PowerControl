package org.nowi.powercontrol;

import junit.framework.TestCase;

public class PowerControlSwitchTest extends TestCase
{
    public void testConvertToHexStringWithNulls()
    {
        String result = PowerControlSwitch.convertToHexString(null, null);
        assertNull(result);
    }

    public void testConvertToHexStringNull1()
    {
        String result = PowerControlSwitch.convertToHexString(null, ":");
        assertNull(result);
    }

    public void testConvertToHexStringNull2()
    {
        String result = PowerControlSwitch.convertToHexString("123", null);
        assertEquals(result, "7B");
    }

    public void testConvertToHexStringEmptyDelimiter()
    {
        String result = PowerControlSwitch.convertToHexString("123", "");
        assertEquals(result, "7B");
    }

    public void testConvertToHexStringEmpty1()
    {
        String result = PowerControlSwitch.convertToHexString("", null);
        assertEquals(result, null);
    }

    public void testConvertToHexStringEmptyAll()
    {
        String result = PowerControlSwitch.convertToHexString("", "");
        assertEquals(result, null);
    }

    public void testConvertToHexStringEmpty1WithDelimiter()
    {
        String result = PowerControlSwitch.convertToHexString("", ":");
        assertEquals(result, null);
    }

    public void testConvertToHexStringWithHexString()
    {
        String result = PowerControlSwitch.convertToHexString("ABC", null);
        assertEquals(result, null);
    }

    public void testConvertToHexStringWithMacAddressAllZero()
    {
        String result = PowerControlSwitch.convertToHexString("0:0:0:0:0:0", ":");
        assertEquals(result, "00:00:00:00:00:00");
    }

    public void testConvertToHexStringWithMacAddressAllZero2()
    {
        String result = PowerControlSwitch.convertToHexString("00:0:00:0:00:0", ":");
        assertEquals(result, "00:00:00:00:00:00");
    }

    public void testConvertToHexStringWithMacAddressAllZeroWithWrongDelimiter()
    {
        String result = PowerControlSwitch.convertToHexString("00:0:00:0:00:0", "-");
        assertEquals(result, null);
    }

    public void testConvertToHexStringWithMacAddress2()
    {
        String result = PowerControlSwitch.convertToHexString("9:10:11:12:13:14", ":");
        assertEquals(result, "09:0A:0B:0C:0D:0E");
    }

    public void testConvertToHexStringWithMacAddressOverflow()
    {
        String result = PowerControlSwitch.convertToHexString("251:252:253:254:255:256", ":");
        assertEquals(result, "FB:FC:FD:FE:FF:100");
    }

    public void testConvertToHexStringWithMacAddressInvalid()
    {
        String result = PowerControlSwitch.convertToHexString("251:252:253:254:255:ABC", ":");
        assertEquals(result, null);
    }

    public void testGetNormalizedHostWithoutHost()
    {
        String result = PowerControlSwitch.getNormalizedHost(null, "page.html");
        assertNull(result);
    }

    public void testGetNormalizedHostWithoutHttp()
    {
        String result = PowerControlSwitch.getNormalizedHost("192.168.3.3", "page.html");
        assertEquals(result, "http://192.168.3.3/page.html");
    }

    public void testGetNormalizedHostWithoutPageAndHttp()
    {
        String result = PowerControlSwitch.getNormalizedHost("192.168.3.3", null);
        assertEquals(result, "http://192.168.3.3/");
    }

    public void testGetNormalizedHostWithoutPageAndHttp2()
    {
        String result = PowerControlSwitch.getNormalizedHost("192.168.3.3", "");
        assertEquals(result, "http://192.168.3.3/");
    }

    public void testGetNormalizedHostWithPageAndHttp()
    {
        String result = PowerControlSwitch.getNormalizedHost("http://192.168.3.3", "page.html");
        assertEquals(result, "http://192.168.3.3/page.html");
    }

    public void testGetNormalizedHostWithPageAndHttpUpperCase()
    {
        String result = PowerControlSwitch.getNormalizedHost("HTTP://192.168.3.3", "page.html");
        assertEquals(result, "HTTP://192.168.3.3/page.html");
    }

    public void testParseIntWithNull()
    {
        int result = PowerControlSwitch.parseInt(null, -1);
        assertEquals(result, -1);
    }

    public void testParseIntWithEmptyString()
    {
        int result = PowerControlSwitch.parseInt("", -1);
        assertEquals(result, -1);
    }

    public void testParseIntWithSpaces()
    {
        int result = PowerControlSwitch.parseInt(" 0 ", -1);
        assertEquals(result, 0);
    }

    public void testParseIntWithNegativeNumber()
    {
        int result = PowerControlSwitch.parseInt("-999", -1);
        assertEquals(result, -999);
    }

    public void testParseIntWithPositiveNumber()
    {
        int result = PowerControlSwitch.parseInt("+999", -1);
        assertEquals(result, -1);
    }

    public void testParseIntWithIntMinimum()
    {
        int result = PowerControlSwitch.parseInt("-2147483648", -1);
        assertEquals(result, Integer.MIN_VALUE);
    }

    public void testParseIntWithIntMaximum()
    {
        int result = PowerControlSwitch.parseInt("2147483647", -1);
        assertEquals(result, Integer.MAX_VALUE);
    }

    public void testParseIntWithIntMinimumOverflow()
    {
        int result = PowerControlSwitch.parseInt("-2147483649", 2);
        assertEquals(result, 2);
    }

    public void testParseIntWithIntMaximumOverflow()
    {
        int result = PowerControlSwitch.parseInt("2147483648", 3);
        assertEquals(result, 3);
    }
}
