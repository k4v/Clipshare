package org.k4rthik.labs.clipshare.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 *
 * The ConnectionThread manages a connection to a single peer. It listens
 * for incoming data from the connected socket and notifies the Clipboard
 * Manager of the update. No writing to the peer happens here.
 */
public class ConnectionThread implements Runnable
{
    Socket clientConnection;

    public ConnectionThread(Socket clientConnection)
    {
        this.clientConnection = clientConnection;
    }

    @Override
    public void run()
    {
        try
        {
            InputStream objectInput = new ObjectInputStream(clientConnection.getInputStream());
        } catch (IOException e)
        {

        }
    }

    public void terminate()
    {

    }
}
