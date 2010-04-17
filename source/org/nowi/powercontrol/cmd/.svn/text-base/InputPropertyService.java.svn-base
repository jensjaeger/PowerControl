package org.nowi.powercontrol.cmd;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nowi.powercontrol.PowerControlIfc;
import org.nowi.powercontrol.PowerControlSwitch;
import org.nowi.powercontrol.PowerControlSwitch.SwitchCommand;

public class InputPropertyService
{
    private static final int MIN_VALID_PORT = 0;
    private static final int MAX_VALID_PORT = 65535;
    public static final String CMD_OPTION_COMMAND = "c";
    public static final String CMD_OPTION_PROTOCOL = "x";
    public static final String CMD_OPTION_HOST = "h";
    public static final String CMD_OPTION_UDP_PORT = "up";
    public static final String CMD_OPTION_UDP_RESPONSE_PORT = "ur";
    public static final String CMD_OPTION_LOGIN = "l";
    public static final String CMD_OPTION_PASSWORD = "p";
    public static final String CMD_OPTION_OUTLET = "o";
    public static final String CMD_OPTION_TIMEOUT = "t";
    public static final String CMD_OPTION_OUTPUT_TYPE = "k";
    public static final String CMD_OPTION_OUTPUT_FILE = "f";

    private static final String VERSION = "V1.02";

    public static final String APPLICATION_NAME = "PowerControl";
    private static final String HELP_FORMATTER_FOOTER = "\n" + APPLICATION_NAME + " " + VERSION
                    + " \u00A9 2010 Christoph Nowak";

    private static final String HELP_FORMATTER_HEADER = "\n" + APPLICATION_NAME
                    + " - THE MARVELLOUS JAVA BASED POWER CONTROL TOOL: ";

    public static enum OutputFormat
    {
        TEXT("txt", ".txt", "Text Format"), XML("xml", ".xml", "Extensible Markup Language");

        private final String format;
        private final String fileExtension;
        private final String description;

        OutputFormat(String format, String fileExtension, String description)
        {
            this.format = format;
            this.fileExtension = fileExtension;
            this.description = description;
        }

        public static OutputFormat getOutputFormat(String outputFormatName)
        {
            String lowerOutputFormatName = outputFormatName.toLowerCase();

            for (OutputFormat outputFormat : EnumSet.allOf(OutputFormat.class))
            {
                if (outputFormat.getFormat().equals(lowerOutputFormatName))
                {
                    return outputFormat;
                }
            }

            return null;
        }

        public String getFormat()
        {
            return format;
        }

        public String getFileExtension()
        {
            return fileExtension;
        }

        public String getDescription()
        {
            return description;
        }
    }

    private static final class InstanceHolder
    {
        static final InputPropertyService INSTANCE = new InputPropertyService();
    }

    private InputPropertyService()
    {
    }

    public static InputPropertyService getInstance()
    {
        return InstanceHolder.INSTANCE;
    }

    public Configuration init(String[] args) throws ParseException, IOException
    {
        CommandLineParser parser = new GnuParser();
        Options options = null;
        CommandLine line = null;

        try
        {
            options = createOptions();
            line = parser.parse(options, args);
        }

        catch (ParseException e)
        {
            printHelp(options);
            throw e;
        }

        Configuration config = getConfiguration(line);

        validateParameters(config);

        return config;
    }

    private void validateParameters(Configuration config) throws ParseException
    {
        if (Configuration.UDP_PROTOCOL.equals(config.getProtocol()))
        {
            // if port and receive port are set, they must be different and valid!
            if (config.getUdpPort() < MIN_VALID_PORT || config.getUdpPort() > MAX_VALID_PORT)
            {
                throw new ParseException("invalid udp port: " + config.getUdpPort());
            }

            if (config.getUdpResponsePort() < MIN_VALID_PORT || config.getUdpResponsePort() > MAX_VALID_PORT)
            {
                throw new ParseException("invalid receive udp port: " + config.getUdpResponsePort());
            }

            if (config.getUdpPort() == config.getUdpResponsePort())
            {
                throw new ParseException("udp port and receive udp port must be different");
            }
        }
    }

    @SuppressWarnings("static-access")
    private Options createOptions()
    {
        Options options = new Options();

        Option command = OptionBuilder.withArgName("command").hasArg().withDescription(
                        "command to execute (SWITCH, QUERY)").isRequired().create(CMD_OPTION_COMMAND);

        Option protocol = OptionBuilder.withArgName("protocol").hasArg().withDescription(
                        "protocol for command (UDP, HTTP=default)").create(CMD_OPTION_PROTOCOL);

        Option host = OptionBuilder.withArgName("host").hasArg().withDescription(
                        "IP address of the multiple socket outlet").isRequired().create(CMD_OPTION_HOST);

        Option udpPort = OptionBuilder.withArgName("port").hasArg().withDescription("port for UDP (default: 75)")
                        .create(CMD_OPTION_UDP_PORT);

        Option udpResponsePort = OptionBuilder.withArgName("receive port").hasArg().withDescription(
                        "receive port for UDP (default: 77)").create(CMD_OPTION_UDP_RESPONSE_PORT);

        Option login = OptionBuilder.withArgName("login/user").hasArg().withDescription("login/user for HTTP").create(
                        CMD_OPTION_LOGIN);

        Option password = OptionBuilder.withArgName("password").hasArg().withDescription("password for UDP/HTTP")
                        .isRequired().create(CMD_OPTION_PASSWORD);

        Option outlet = OptionBuilder.withArgName("outlet").hasArg().withDescription(
                        "outlet to switch (e.g. 1=ON|OFF|TOGGLE)").create(CMD_OPTION_OUTLET);

        Option timeout = OptionBuilder.withArgName("timeout").hasArg().withDescription("timeout in ms (default: 5000)")
                        .create(CMD_OPTION_TIMEOUT);

        Option outputType = OptionBuilder.withArgName("kind of output").hasArg().withDescription(
                        "kind of output (TXT=default, XML)").create(CMD_OPTION_OUTPUT_TYPE);

        Option outputFile = OptionBuilder.withArgName("output file").hasArg()
                        .withDescription("name of the output file").create(CMD_OPTION_OUTPUT_FILE);

        options.addOption(command);
        options.addOption(protocol);
        options.addOption(host);
        options.addOption(udpPort);
        options.addOption(udpResponsePort);
        options.addOption(login);
        options.addOption(password);
        options.addOption(outlet);
        options.addOption(timeout);
        options.addOption(outputType);
        options.addOption(outputFile);

        return options;
    }

    public void printHelp(Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(120, APPLICATION_NAME, HELP_FORMATTER_HEADER, options, HELP_FORMATTER_FOOTER, true);
    }

    public static Configuration getConfiguration(CommandLine line) throws ParseException, IOException
    {
        Configuration config = new Configuration();

        // set command/action
        String command = line.getOptionValue(CMD_OPTION_COMMAND).trim().toUpperCase();
        if (SwitchCommand.QUERY.name().equals(command))
        {
            config.setCommand(SwitchCommand.QUERY);
        }
        else if (SwitchCommand.SWITCH.name().equals(command))
        {
            config.setCommand(null);
        }
        else
        {
            throw new ParseException("unknown command: " + command);
        }

        // set protocol
        String protocol = Configuration.DEFAULT_PROTOCOL;
        if (line.getOptionValue(CMD_OPTION_PROTOCOL) != null)
        {
            protocol = line.getOptionValue(CMD_OPTION_PROTOCOL).trim().toUpperCase();
            if (Configuration.DEFAULT_PROTOCOL.equals(protocol))
            {
                config.setProtocol(Configuration.DEFAULT_PROTOCOL);
            }
            else if (Configuration.UDP_PROTOCOL.equals(protocol))
            {
                config.setProtocol(Configuration.UDP_PROTOCOL);
            }
            else
            {
                throw new ParseException("unknown protocol: " + protocol);
            }
        }
        else
        {
            config.setProtocol(protocol);
        }

        // set host
        config.setHost(line.getOptionValue(CMD_OPTION_HOST).trim());

        // set udp port
        if (line.getOptionValue(CMD_OPTION_UDP_PORT) != null)
        {
            int udpPort = PowerControlSwitch.parseInt(line.getOptionValue(CMD_OPTION_UDP_PORT).trim(),
                            PowerControlIfc.UDP_DEFAULT_PORT);
            config.setUdpPort(udpPort);
        }

        // udp response port
        if (line.getOptionValue(CMD_OPTION_UDP_RESPONSE_PORT) != null)
        {
            int udpResponsePort = PowerControlSwitch.parseInt(line.getOptionValue(CMD_OPTION_UDP_RESPONSE_PORT).trim(),
                            PowerControlIfc.UDP_DEFAULT_RECEIVE_PORT);
            config.setUdpResponsePort(udpResponsePort);
        }

        // set login (only required for HTTP!)
        if (Configuration.DEFAULT_PROTOCOL.equals(protocol) && line.getOptionValue(CMD_OPTION_LOGIN) == null)
        {
            throw new ParseException("login required for HTTP");
        }
        else
        {
            if (line.getOptionValue(CMD_OPTION_LOGIN) != null)
            {
                config.setLogin(line.getOptionValue(CMD_OPTION_LOGIN).trim());
            }
        }

        // set password
        config.setPassword(line.getOptionValue(CMD_OPTION_PASSWORD).trim());

        // set outlet string (only required when command == SWITCH ON/OFF)
        setConfigOutletCommands(line, config);

        // set timeout
        if (line.getOptionValue(CMD_OPTION_TIMEOUT) != null)
        {
            int iTimeout = PowerControlSwitch.parseInt(line.getOptionValue(CMD_OPTION_TIMEOUT), -1);
            if (iTimeout < 0)
            {
                throw new ParseException("invalid value for timeout");
            }
            else
            {
                config.setTimeout(iTimeout);
            }
        }

        // set output type
        if (line.getOptionValue(CMD_OPTION_OUTPUT_TYPE) != null)
        {
            String outputType = line.getOptionValue(CMD_OPTION_OUTPUT_TYPE).trim().toLowerCase();

            if (OutputFormat.XML.getFormat().equals(outputType))
            {
                config.setOutputType(OutputFormat.XML);
            }
            else if (OutputFormat.TEXT.getFormat().equals(outputType))
            {
                config.setOutputType(OutputFormat.TEXT);
            }
            else
            {
                throw new ParseException("unknown kind of output: " + outputType);
            }
        }

        // check the output file (-path)
        if (line.getOptionValue(CMD_OPTION_OUTPUT_FILE) != null)
        {
            String outputFile = line.getOptionValue(CMD_OPTION_OUTPUT_FILE);
            File file = new File(outputFile);
            file.delete();
            if (!file.createNewFile())
            {
                throw new IOException("unable to create output file: " + outputFile);
            }

            config.setOutputFileName(outputFile);
        }

        return config;
    }

    private static void setConfigOutletCommands(CommandLine line, Configuration config) throws ParseException
    {
        if (config.getCommand() == null)
        {
            if (line.getOptionValue(CMD_OPTION_OUTLET) == null)
            {
                throw new ParseException("outlet required for switching on/off");
            }
            else
            {
                String[] commands = line.getOptionValues(CMD_OPTION_OUTLET);
                if (commands != null && commands.length > 0)
                {
                    for (String singleCommand : commands)
                    {
                        String[] oneCommand = singleCommand.toUpperCase().trim().split("=");
                        if (oneCommand != null && oneCommand.length == 2)
                        {
                            String outlet = oneCommand[0].trim();
                            String newState = oneCommand[1].trim();

                            int iOutlet = PowerControlSwitch.parseInt(outlet, 0);
                            if (iOutlet <= 0)
                            {
                                throw new ParseException("invalid outlet: " + singleCommand);
                            }
                            else if (!SwitchCommand.ON.name().equals(newState)
                                            && !SwitchCommand.OFF.name().equals(newState)
                                            && !SwitchCommand.TOGGLE.name().equals(newState))
                            {
                                throw new ParseException("invalid state: " + singleCommand);
                            }
                            else
                            {
                                SwitchCommand switchCommand = SwitchCommand.ON.name().equals(newState) ? SwitchCommand.ON
                                                : SwitchCommand.OFF.name().equals(newState) ? SwitchCommand.OFF
                                                                : SwitchCommand.TOGGLE;

                                if (config.getCommands().containsKey(iOutlet))
                                {
                                    throw new ParseException("duplicated outlet " + iOutlet + " to switch!");
                                }
                                else
                                {
                                    config.getCommands().put(iOutlet, switchCommand);
                                }
                            }
                        }
                        else
                        {
                            throw new ParseException("invalid outlet command: " + singleCommand);
                        }
                    }
                }
                else
                {
                    throw new ParseException("empty/invalid outlet command!");
                }
            }
        }
    }
}
