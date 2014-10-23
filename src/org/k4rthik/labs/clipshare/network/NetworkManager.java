package org.k4rthik.labs.clipshare.network;

import org.k4rthik.labs.clipshare.Params;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: kvenugopal
 * Date  : 10/21/14
 */
public class NetworkManager
{
    private static Params networkParams = null;
    private static NetworkManager INSTANCE = null;

    Socket connectionSocket = null;

    private NetworkManager()
    {
        if(networkParams == null)
        {
            throw new NullPointerException("No network parameters provided. Cannot initialize Network Manager");
        }

        if(networkParams.getMode() == 'c')
            startClientMode();
        else if(networkParams.getMode() == 's')
            startServerMode();

        if(connectionSocket != null)
        {
            new Thread(new ConnectionThread(connectionSocket)).start();
        }
    }

    public static synchronized NetworkManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new NetworkManager();
        }

        return INSTANCE;
    }

    public static synchronized void init(Params execParams)
    {
        networkParams = execParams;
    }

    private void startClientMode()
    {
        try
        {
            System.out.println("Attempting to connect to server...");
            connectionSocket = new Socket(networkParams.getPeerHostname(), networkParams.getPeerPort());
            System.out.println("Connection established at " + System.currentTimeMillis());
        } catch (Exception e)
        {
            System.out.println("Exception caught connecting to server on " + networkParams.getPeerPort());
            System.out.println(e.getMessage());
        }
    }

    private void startServerMode()
    {
        try
        {
            ServerSocket serverSocket =
                    new ServerSocket(networkParams.getPeerPort());
            System.out.println("Waiting for client...");

            connectionSocket = serverSocket.accept();
            System.out.println("Connection established at " + System.currentTimeMillis());
        } catch(IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + networkParams.getPeerPort() + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    public void writeToPeer(Transferable clipboardData, int[] currentRevision, int sourceMachine) throws IOException
    {
        UpdateMessage updateMessage = new UpdateMessage(clipboardData, currentRevision, sourceMachine);
        ObjectOutputStream writeToPeerStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        writeToPeerStream.writeObject(updateMessage);
        writeToPeerStream.close();
    }
}
