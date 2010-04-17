package org.nowi.powercontrol;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface PowerControlIfc
{
    public static final int UDP_DEFAULT_PORT = 75;
    public static final int UDP_DEFAULT_RECEIVE_PORT = 77;
    public static final int DEFAULT_TIMEOUT_IN_MS = 5000;

    PowerControlState switchPowerControl(PowerControlParameters parameters) throws TimeoutException, IOException,
                    Exception;

    PowerControlState queryPowerControl(PowerControlParameters parameters) throws TimeoutException, IOException,
                    Exception;
}
