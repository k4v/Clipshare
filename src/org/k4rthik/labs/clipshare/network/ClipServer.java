package org.k4rthik.labs.clipshare.network;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class ClipServer implements Runnable
{
    private int serverPort;
    List<Socket> clientSockets = null;

    public ClipServer(int startOnPort)
    {
        this.serverPort = startOnPort;
        clientSockets = new ArrayList<Socket>(1);
    }

    @Override
    public void run()
    {

    }
}
