package org.nowi.powercontrol;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;
import org.nowi.powercontrol.text.TextUtils;

import com.thoughtworks.xstream.XStream;

public class PowerControlState
{
    public static final String LABEL_DELIMITER = ": ";
    public static final String NEW_LINE = System.getProperty("line.separator");

    private String name1;
    private String name2;

    private String host;
    private String mask;
    private String gateway;
    private String macAddress;

    private Map<Integer, SwitchCommand> outlets;

    private Integer httpPort;

    private String errorText;
    private Integer httpErrorCode;

    public PowerControlState()
    {
        this.outlets = new HashMap<Integer, SwitchCommand>();
    }

    public void resetAllFieldsExceptOutlets()
    {
        this.name1 = null;
        this.name2 = null;
        this.host = null;
        this.mask = null;
        this.gateway = null;
        this.macAddress = null;
        this.httpPort = null;
        this.errorText = null;
        this.httpErrorCode = null;
    }

    public String getName1()
    {
        return name1;
    }

    public void setName1(String name1)
    {
        this.name1 = name1;
    }

    public String getName2()
    {
        return name2;
    }

    public void setName2(String name2)
    {
        this.name2 = name2;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getMask()
    {
        return mask;
    }

    public void setMask(String mask)
    {
        this.mask = mask;
    }

    public String getGateway()
    {
        return gateway;
    }

    public void setGateway(String gateway)
    {
        this.gateway = gateway;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public Map<Integer, SwitchCommand> getOutlets()
    {
        return outlets;
    }

    public void setOutlets(Map<Integer, SwitchCommand> outlets)
    {
        this.outlets = outlets;
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder(1024);

        if (isNotNullOrEmptyString(getErrorText()))
        {
            output.append(TextUtils.getInstance().getText("Error"));
            output.append(LABEL_DELIMITER);
            output.append(getErrorText());
            output.append(NEW_LINE);
        }

        if (getHttpErrorCode() != null)
        {
            output.append(TextUtils.getInstance().getText("ErrorCode"));
            output.append(LABEL_DELIMITER);
            output.append(getHttpErrorCode());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getName1()))
        {
            output.append(TextUtils.getInstance().getText("Name1"));
            output.append(LABEL_DELIMITER);
            output.append(getName1());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getName2()))
        {
            output.append(TextUtils.getInstance().getText("Name2"));
            output.append(LABEL_DELIMITER);
            output.append(getName2());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getHost()))
        {
            output.append(TextUtils.getInstance().getText("IPAddress"));
            output.append(LABEL_DELIMITER);
            output.append(getHost());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getMask()))
        {
            output.append(TextUtils.getInstance().getText("Mask"));
            output.append(LABEL_DELIMITER);
            output.append(getMask());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getGateway()))
        {
            output.append(TextUtils.getInstance().getText("Gateway"));
            output.append(LABEL_DELIMITER);
            output.append(getGateway());
            output.append(NEW_LINE);
        }

        if (isNotNullOrEmptyString(getMacAddress()))
        {
            output.append(TextUtils.getInstance().getText("MACAddress"));
            output.append(LABEL_DELIMITER);
            output.append(getMacAddress());
            output.append(NEW_LINE);
        }

        if (getHttpPort() != null)
        {
            output.append(TextUtils.getInstance().getText("HTTPPort"));
            output.append(LABEL_DELIMITER);
            output.append(getHttpPort());
            output.append(NEW_LINE);
        }

        final String outletKey = TextUtils.getInstance().getText("Outlet");
        for (Integer key : outlets.keySet())
        {
            SwitchCommand status = outlets.get(key);

            output.append(TextUtils.getInstance().getText(outletKey));
            output.append("-");
            output.append(key);
            output.append(LABEL_DELIMITER);
            output.append(TextUtils.getInstance().getText(status.toString()));
            output.append(NEW_LINE);
        }

        return output.toString();
    }

    public String toXml()
    {
        XStream xstream = new XStream();

        PowerControlState.setAliasesForXStream(xstream);

        return xstream.toXML(this);
    }

    public static void setAliasesForXStream(XStream xstream)
    {
        xstream.alias("powerControl", PowerControlState.class);
        xstream.alias("state", SwitchCommand.class);
        xstream.aliasType("outlet", int.class);
    }

    public void saveToFile(String fileName, boolean outputXml) throws IOException
    {
        try
        {
            OutputStream output = new BufferedOutputStream(new FileOutputStream(fileName, false));

            try
            {
                String content = outputXml ? this.toXml() : this.toString();
                output.write(content.getBytes());
                output.flush();
            }

            catch (IOException e)
            {
                e.printStackTrace();
                throw e;
            }

            finally
            {
                try
                {
                    output.close();
                }

                catch (IOException ignored)
                {
                }
            }
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    private boolean isNotNullOrEmptyString(String s)
    {
        return s != null && s.trim().length() > 0;
    }

    public String getErrorText()
    {
        return errorText;
    }

    public void setErrorText(String newErrorText)
    {
        this.errorText = newErrorText;
    }

    public Integer getHttpErrorCode()
    {
        return httpErrorCode;
    }

    public void setHttpErrorCode(Integer httpErrorCode)
    {
        this.httpErrorCode = httpErrorCode;
    }

    public Integer getHttpPort()
    {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort)
    {
        this.httpPort = httpPort;
    }
}
