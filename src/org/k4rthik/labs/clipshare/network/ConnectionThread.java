package org.k4rthik.labs.clipshare.network;

import org.k4rthik.labs.clipshare.clipboard.ClipboardManager;

import java.io.IOException;
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
            ObjectInputStream readFromPeerStream = new ObjectInputStream(clientConnection.getInputStream());
            Object readObject = readFromPeerStream.readObject();
            if(readObject instanceof UpdateMessage)
            {
                UpdateMessage updateMessage = (UpdateMessage)readObject;
                ClipboardManager.getInstance().remoteClipboardEvent(updateMessage);
            }
        } catch (IOException e)
        {
            System.err.println("Error reading stream from peer");
            e.printStackTrace(System.err);
        } catch (ClassNotFoundException e)
        {
            System.err.println("Error reading message object");
            e.printStackTrace(System.err);
        }
    }

    public void terminate()
    {

    }
}
