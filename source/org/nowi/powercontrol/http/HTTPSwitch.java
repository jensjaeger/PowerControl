package org.nowi.powercontrol.http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nowi.powercontrol.PowerControlParameters;
import org.nowi.powercontrol.PowerControlState;
import org.nowi.powercontrol.PowerControlSwitch;

/**
 * 
 * @author Christoph Nowak
 * 
 * 
 */

public class HTTPSwitch extends PowerControlSwitch
{
    // pattern for switch power control
    private static final String HTML_TAG_REQUEST_OUTLET_PATTERN = "value\\=([I|O]) name\\=F([0-9])";

    // patterns for net config
    private static final String HTML_TAG_NAME1_PATTERN = "\\<title\\>ANEL\\-Elektronik ([^\\<]+)\\<\\/title\\>";
    private static final String HTML_TAG_NAME2_PATTERN = "\\<input type\\=\\\"text\\\" name\\=\\\"host\\\" value\\=\\\"([^\\\"]{0,15})\\\" maxlength\\=15\\>";
    private static final String HTML_TAG_HOST_PATTERN = "\\<input type\\=\\\"text\\\" name\\=\\\"ip\\\" value\\=\\\"(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\\"\\>";
    private static final String HTML_TAG_MASK_PATTERN = "\\<input type\\=\\\"text\\\" name\\=\\\"sub\\\" value\\=\\\"(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\\"\\>";
    private static final String HTML_TAG_GATEWAY_PATTERN = "\\<input type\\=\\\"text\\\" name\\=\\\"gw\\\" value\\=\\\"(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\\"\\>";
    private static final String HTML_TAG_MAC_ADDRESS_PATTERN = "\\<input type\\=\\\"text\\\" name\\=\\\"mac\\\" value\\=\\\"([0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2}\\:[0-9a-fA-F]{2})\\\" disabled\\>";
    private static final String HTML_TAG_HTTP_PORT_PATTERN = "\\<input style\\=text\\-align\\:center\\; type\\=text name\\=\\\"port\\\" value\\=(\\d{1,5}) size\\=4\\>";

    // html-page names from power control web server
    private static final String REQUEST_WEB_PAGE = "mobile.htm";
    private static final String REQUEST_NET_WEB_PAGE = "netcfg.htm";

    // html-param switch command
    private static final String HTTP_SWITCH_COMMAND = "x=60&F%d=%s";

    @Override
    public PowerControlState switchPowerControl(PowerControlParameters parameters) throws Exception
    {
        PowerControlState result = null;

        if (isNecessaryToSwitch(parameters))
        {
            // send request for the switch
            String hostMobilePage = PowerControlSwitch.getNormalizedHost(parameters.host, REQUEST_WEB_PAGE);
            String command = String.format(HTTP_SWITCH_COMMAND, parameters.outlet - 1, parameters.command
                            .getHttpStatus());
            HTTPResult htmlSwitchResult = HTTPRequestManager.sendPostRequest(hostMobilePage, command,
                            parameters.timeoutInMs, parameters.login, parameters.password);

            powerControlState = createStateObjectFromHttpResult(htmlSwitchResult.getResultAsString(), null);

            if (htmlSwitchResult.isError())
            {
                powerControlState.setErrorText(htmlSwitchResult.getHttpErrorText());
                powerControlState.setHttpErrorCode(htmlSwitchResult.getHttpResponseCode());
            }

            result = powerControlState;
        }
        else
        {
            result = parameters.queryResult;
        }

        return result;
    }

    @Override
    public PowerControlState queryPowerControl(PowerControlParameters parameters) throws Exception
    {
        // send request query power control
        String hostMobilePage = PowerControlSwitch.getNormalizedHost(parameters.host, REQUEST_WEB_PAGE);
        HTTPResult htmlSwitchStatusResult = HTTPRequestManager.sendGetRequest(hostMobilePage, "",
                        parameters.timeoutInMs, parameters.login, parameters.password);

        // send request to get net config
        String hostNetConfigPage = PowerControlSwitch.getNormalizedHost(parameters.host, REQUEST_NET_WEB_PAGE);
        HTTPResult htmlNetConfigResult = HTTPRequestManager.sendGetRequest(hostNetConfigPage, "",
                        parameters.timeoutInMs, parameters.login, parameters.password);

        powerControlState = createStateObjectFromHttpResult(htmlSwitchStatusResult.getResultAsString(),
                        htmlNetConfigResult.getResultAsString());

        if (htmlSwitchStatusResult.isError())
        {
            powerControlState.setErrorText(htmlSwitchStatusResult.getHttpErrorText());
            powerControlState.setHttpErrorCode(htmlSwitchStatusResult.getHttpResponseCode());
        }
        else if (htmlNetConfigResult.isError())
        {
            powerControlState.setErrorText(htmlNetConfigResult.getHttpErrorText());
            powerControlState.setHttpErrorCode(htmlNetConfigResult.getHttpResponseCode());
        }

        return powerControlState;
    }

    private PowerControlState createStateObjectFromHttpResult(String htmlSwitchResult, String htmlNetConfigResult)
    {
        PowerControlState state = new PowerControlState();

        if (htmlSwitchResult != null)
        {
            setOutletsState(htmlSwitchResult, state.getOutlets());
        }

        if (htmlNetConfigResult != null)
        {
            setNetConfigState(htmlNetConfigResult, state);
        }

        return state;
    }

    private void setOutletsState(String htmlSwitchResult, Map<Integer, SwitchCommand> outlets)
    {
        Pattern pattern = Pattern.compile(HTML_TAG_REQUEST_OUTLET_PATTERN);
        Matcher matcher = pattern.matcher(htmlSwitchResult);

        while (matcher.find())
        {
            if (matcher.groupCount() == 2)
            {
                SwitchCommand outletStatus = convertHttpStatus(matcher.group(1));
                int outletNumber = convertHttpOutlet(matcher.group(2));

                outlets.put(outletNumber, outletStatus);
            }
        }
    }

    private void setNetConfigState(String htmlNetConfigResult, PowerControlState state)
    {
        Pattern pattern = Pattern.compile(HTML_TAG_NAME1_PATTERN);
        Matcher matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setName1(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_NAME2_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setName2(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_HOST_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setHost(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_MASK_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setMask(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_GATEWAY_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setGateway(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_MAC_ADDRESS_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setMacAddress(matcher.group(1));
        }

        pattern = Pattern.compile(HTML_TAG_HTTP_PORT_PATTERN);
        matcher = pattern.matcher(htmlNetConfigResult);
        if (matcher.find() && matcher.groupCount() == 1)
        {
            state.setHttpPort(PowerControlSwitch.parseInt(matcher.group(1), DEFAULT_HTTP_PORT));
        }
    }

    private int convertHttpOutlet(String httpOutlet)
    {
        try
        {
            return Integer.parseInt(httpOutlet) + 1;
        }

        catch (NumberFormatException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    private SwitchCommand convertHttpStatus(String status)
    {
        SwitchCommand result = SwitchCommand.OFF;

        if (SwitchCommand.ON.getHttpStatus().equals(status))
        {
            return SwitchCommand.ON;
        }

        return result;
    }
}
