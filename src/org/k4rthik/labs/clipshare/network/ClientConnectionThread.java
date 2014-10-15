package org.k4rthik.labs.clipshare.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 *
 * The Client Connection thread manages a connection to a single client.
 * It listens for incoming data from the client socket and notifies the
 * ClipboardManager of the update.
 */
public class ClientConnectionThread implements Runnable
{
    private int clientIndex;
    Socket clientConnection;

    public ClientConnectionThread(int clientIndex, Socket clientConnection)
    {
        this.clientIndex = clientIndex;
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
}
