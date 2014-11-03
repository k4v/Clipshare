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
    ObjectInputStream readFromPeerStream;
    boolean keepAlive = false;

    public ConnectionThread(Socket clientConnection) throws IOException
    {
        this.clientConnection = clientConnection;
        readFromPeerStream = new ObjectInputStream(clientConnection.getInputStream());
        this.keepAlive = true;
    }

    @Override
    public void run()
    {
        try
        {
            Object readObject;
            while((readObject = readFromPeerStream.readObject()) != null)
            {
                if (readObject instanceof UpdateMessage)
                {
                    UpdateMessage updateMessage = (UpdateMessage) readObject;
                    ClipboardManager.getInstance().remoteClipboardEvent(updateMessage);
                }
            }
        } catch (IOException e)
        {
            System.err.println("Error reading stream from peer: "+e.toString());
        } catch (ClassNotFoundException e)
        {
            System.err.println("Error reading message object: "+e.toString());
        }
    }

    public void terminate()
    {
        this.keepAlive = false;
    }
}
