package org.k4rthik.labs.clipshare.network;

import org.k4rthik.labs.clipshare.Params;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: kvenugopal
 * Date  : 10/21/14
 */
public class NetworkManager implements Runnable
{
    private static Params networkParams = null;
    private static NetworkManager INSTANCE = null;

    Socket connectionSocket = null;
    ObjectOutputStream writeToPeerStream = null;

    private NetworkManager()
    {
        if(networkParams == null)
        {
            throw new NullPointerException("No network parameters provided. Cannot initialize Network Manager");
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
            System.out.println("Waiting for incoming connection from client...");

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
        System.out.println("Attempting to send data to peer");
        try
        {
            UpdateMessage updateMessage = new UpdateMessage((String)(clipboardData.getTransferData(DataFlavor.stringFlavor)), currentRevision, sourceMachine);

            try
            {
                writeToPeerStream.reset();
                writeToPeerStream.writeObject(updateMessage);
                writeToPeerStream.flush();
            } catch (IOException e)
            {
                System.out.println("Looks like the stream is not open: " + e.toString());
            }
        } catch(UnsupportedFlavorException e)
        {
            System.err.println("This is IMPOSSIBRU: " + e.toString());
        }
    }

    @Override
    public void run()
    {

        if(networkParams.getMode() == 'c')
            startClientMode();
        else if(networkParams.getMode() == 's')
            startServerMode();

        if(connectionSocket != null)
        {
            try
            {
                writeToPeerStream = new ObjectOutputStream(connectionSocket.getOutputStream());
                new Thread(new ConnectionThread(connectionSocket)).start();
            } catch (Exception e)
            {
                System.err.println("Error starting connection thread. Cannot send data to peer: " + e.toString());
                try
                {
                    connectionSocket.close();
                } catch (IOException ioe)
                {
                    /* Ignore */
                }
            }
        }
    }
}
