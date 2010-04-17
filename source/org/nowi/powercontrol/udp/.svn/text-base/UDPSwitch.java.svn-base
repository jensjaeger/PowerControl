package org.nowi.powercontrol.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.nowi.powercontrol.PowerControlParameters;
import org.nowi.powercontrol.PowerControlState;
import org.nowi.powercontrol.PowerControlSwitch;

public class UDPSwitch extends PowerControlSwitch implements Runnable
{
    private static final String ZERO_BYTE = "\000";
    private static final String UDP_COMMAND_NEWLINE = "\r\n";
    private static final String UDP_BROADCAST_ADDRESS = "255.255.255.255";
    private static final int CURCUIT_TIME_IN_MS = 500;

    protected AtomicBoolean isReadyForUDPSend = new AtomicBoolean(false);
    protected AtomicInteger atomicResponsePort = new AtomicInteger();
    protected AtomicInteger atomicTimeoutInMs = new AtomicInteger();

    protected Exception innerException;

    @Override
    public PowerControlState switchPowerControl(PowerControlParameters parameters) throws Exception
    {
        if (isNecessaryToSwitch(parameters))
        {
            Thread.sleep(CURCUIT_TIME_IN_MS);

            try
            {
                StringBuilder buf = new StringBuilder(64);

                if (SwitchCommand.RESET == parameters.command)
                {
                    buf.append(SwitchCommand.RESET.getUdpCommand());
                    buf.append(parameters.password);
                }
                else
                {
                    buf.append(SwitchCommand.SWITCH.getUdpCommand());
                    buf.append(parameters.command == SwitchCommand.ON ? SwitchCommand.ON.getUdpCommand()
                                    : SwitchCommand.OFF.getUdpCommand());
                    buf.append(parameters.outlet);
                    buf.append(parameters.password);
                    buf.append(ZERO_BYTE);
                    buf.append(UDP_COMMAND_NEWLINE);
                }

                // Get the internet address of the specified host
                InetAddress address = InetAddress.getByName(parameters.host);

                // Initialize a datagram packet with data and address
                DatagramPacket packet = new DatagramPacket(buf.toString().getBytes(), buf.toString().length(), address,
                                parameters.port);

                // Create a datagram socket, send the packet through it, close it.
                DatagramSocket dSocket = new DatagramSocket();

                try
                {
                    dSocket.setSoTimeout(parameters.timeoutInMs);
                    dSocket.send(packet);
                }

                finally
                {
                    dSocket.close();
                }
            }

            catch (Exception e)
            {
                e.printStackTrace();
                throw e;
            }
        }

        return null;
    }

    @Override
    public PowerControlState queryPowerControl(PowerControlParameters parameters) throws Exception
    {
        PowerControlState result = null;

        try
        {
            atomicResponsePort.set(parameters.responsePort);
            atomicTimeoutInMs.set(parameters.timeoutInMs);

            Thread listenerThread = new Thread(this);
            listenerThread.setDaemon(true);
            listenerThread.start();

            if (listenerThread.isAlive())
            {
                synchronized (this)
                {
                    if (!isReadyForUDPSend.get())
                    {
                        wait(parameters.timeoutInMs, 0);
                    }
                }

                // Get the internet address of the specified host
                InetAddress address = InetAddress.getByName(UDP_BROADCAST_ADDRESS);

                // Initialize a datagram packet with data and address
                DatagramPacket packet = new DatagramPacket(SwitchCommand.QUERY.getUdpCommand().getBytes(),
                                SwitchCommand.QUERY.getUdpCommand().length(), address, parameters.port);

                // Create a datagram socket, send the packet through it, close
                // it.
                DatagramSocket dSocket = new DatagramSocket();

                try
                {
                    dSocket.setBroadcast(true);
                    dSocket.setSoTimeout(parameters.timeoutInMs);

                    dSocket.send(packet);

                    listenerThread.join(parameters.timeoutInMs);

                    if (listenerThread.isAlive())
                    {
                        // Timeout occurred; thread has not finished
                        listenerThread.interrupt();
                        throw new TimeoutException("Timeout while reading data from " + parameters.host);
                    }
                    else
                    {
                        if (getInnerException() != null)
                        {
                            throw getInnerException();
                        }
                        else
                        {
                            synchronized (this)
                            {
                                result = powerControlState;
                            }
                        }
                    }
                }

                finally
                {
                    dSocket.close();
                }
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    public void run()
    {
        queryPowerSupplyListener();
    }

    private void queryPowerSupplyListener()
    {
        try
        {
            // Create a buffer to read datagrams into.
            byte[] buffer = new byte[4096];

            // Create a packet to receive data into the buffer
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Create a socket to listen on the port.
            DatagramSocket dSocket = new DatagramSocket(atomicResponsePort.get());

            try
            {
                dSocket.setSoTimeout(atomicTimeoutInMs.get());
                dSocket.setBroadcast(true);

                // sender can start with command
                isReadyForUDPSend.set(true);

                synchronized (this)
                {
                    notify();
                }

                // Wait to receive a datagram
                dSocket.receive(packet);

                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());

                synchronized (this)
                {
                    powerControlState = createStateObjectFromUdpResult(msg);
                }
            }

            finally
            {
                dSocket.close();
            }
        }

        catch (Exception e)
        {
            setInnerException(e);
            e.printStackTrace();
        }
    }

    public synchronized Exception getInnerException()
    {
        return innerException;
    }

    private synchronized void setInnerException(Exception e)
    {
        this.innerException = e;
    }

    protected PowerControlState createStateObjectFromUdpResult(String message)
    {
        PowerControlState state = new PowerControlState();

        String[] messageLines = message.split("\\:");

        if (messageLines != null && messageLines.length == 16)
        {
            Set<Integer> availableOutlets = getAvailableOutlets(messageLines[14]);

            for (int lineNumber = 0; lineNumber < messageLines.length; lineNumber++)
            {
                String currentLine = messageLines[lineNumber];

                switch (lineNumber)
                {
                    case 0:
                        state.setName1(currentLine);
                        break;

                    case 1:
                        state.setName2(currentLine);
                        break;

                    case 2:
                        state.setHost(currentLine);
                        break;

                    case 3:
                        state.setMask(currentLine);
                        break;

                    case 4:
                        state.setGateway(currentLine);
                        break;

                    case 5:
                        state.setMacAddress(PowerControlSwitch.convertToHexString(currentLine, "\\."));
                        break;

                    // outlets...
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        int outletNumber = lineNumber - 5;
                        if (availableOutlets.contains(outletNumber))
                        {
                            state.getOutlets().put(outletNumber, getOutletState(currentLine));
                        }
                        break;

                    case 15:
                        state.setHttpPort(PowerControlSwitch.parseInt(currentLine, DEFAULT_HTTP_PORT));
                        break;

                    default:
                        break;
                }
            }
        }

        return state;
    }

    protected Set<Integer> getAvailableOutlets(String seg_Dis)
    {
        Set<Integer> outlets = null;

        try
        {
            int segDis = Integer.parseInt(seg_Dis);

            outlets = new HashSet<Integer>();

            for (int bitToTest = 0x80, outlet = 8; bitToTest > 0; bitToTest >>= 1, outlet--)
            {
                int testBit = segDis & bitToTest;

                if (testBit == 0)
                {
                    outlets.add(outlet);
                }
            }
        }

        catch (NumberFormatException e)
        {
            e.printStackTrace();
            throw e;
        }

        return outlets;
    }

    protected SwitchCommand getOutletState(String line)
    {
        SwitchCommand result = SwitchCommand.OFF;

        String[] parts = line.split("\\,");
        if (parts != null && parts.length == 2)
        {
            if (SwitchCommand.ON.getUdpStatus().equals(parts[1]))
            {
                result = SwitchCommand.ON;
            }
        }

        return result;
    }
}
