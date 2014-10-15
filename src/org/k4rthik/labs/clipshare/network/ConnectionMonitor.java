package org.k4rthik.labs.clipshare.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class ConnectionMonitor implements Runnable
{
    // Lock object to synchronize access to the clientSockets list
    Object LOCK;

    int serverPort;
    List<Socket> clientSockets = null;

    boolean keepAlive = true;

    public ConnectionMonitor(int serverPort, List<Socket> clientSockets, Object LOCK)
    {
        this.serverPort = serverPort;
        this.clientSockets = clientSockets;

        this.LOCK = LOCK;
    }

    public void terminate()
    {
        this.keepAlive = false;
    }

    public void run()
    {
        while(keepAlive)
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket(serverPort);
                System.out.print("Waiting for client... ");
                Socket clientSocket = serverSocket.accept();

                synchronized(LOCK)
                {
                    clientSockets.add(clientSocket);
                    System.out.println("Connected to client "+clientSockets.size()+": "
                            +clientSocket.getInetAddress().getHostName());
                }
            } catch(IOException e)
            {
                System.err.println("Error connecting to client: "+e);
                e.printStackTrace(System.err);
            }
        }
    }
}
