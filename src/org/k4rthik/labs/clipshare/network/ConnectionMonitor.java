package org.k4rthik.labs.clipshare.network;

import org.k4rthik.labs.clipshare.system.UpdateNotification;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 *
 * The Connection Monitor listens for incoming connections
 * from client machines and creates a new client thread to
 * read incoming data from the client.
 */
public class ConnectionMonitor implements Runnable
{
    // Lock object to synchronize access to the clientSockets list
    Object LOCK;

    int serverPort;
    List<Socket> clientSockets = null;

    // Keep running the connection monitor
    boolean keepAlive = true;

    public ConnectionMonitor(int serverPort)
    {
        this.serverPort = serverPort;
        clientSockets = new ArrayList<Socket>();

        LOCK = new Object();
    }

    // Send notice to terminate this ready
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
                // Wait for a client connection
                ServerSocket serverSocket = new ServerSocket(serverPort);
                System.out.print("Waiting for client... ");
                Socket clientSocket = serverSocket.accept();

                synchronized(LOCK)
                {
                    // TODO: Check if this synchronized block prevents connection requests for a considerable time
                    // Add latest connection to list.
                    clientSockets.add(clientSocket);
                    System.out.println("Connected to client "+clientSockets.size()+": "
                            +clientSocket.getInetAddress().getHostName());

                    // Start a new client connection thread
                    ClientConnectionThread newClientConnection = new ClientConnectionThread(
                            clientSockets.size(), clientSocket);
                    new Thread(newClientConnection).start();
                }
            } catch(IOException e)
            {
                System.err.println("Error connecting to client: "+e);
                e.printStackTrace(System.err);
            }
        }
    }

    public void broadcastData(UpdateNotification updateNotification)
    {
        // If ConnectionMonitor is not running, don't broadcast data
        if(!keepAlive)
            return;

        List<Integer> invalidSockets = new ArrayList<Integer>();

        int clientIndex = 0;
        for(Socket clientSocket : clientSockets)
        {
            clientIndex++;

            // Don't send an update to the originator of this clipboard update
            if(updateNotification.getSourceMachine() == clientIndex)
            {
                continue;
            }

            // Write update notification to client's output stream
            try
            {
                ObjectOutput objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeObject(updateNotification);
                objectOutput.flush();
                objectOutput.close();
            } catch (IOException e)
            {
                System.err.println("Error sending data to client: "+clientIndex+". Terminating connection");
                try
                {
                    clientSocket.close();
                } catch (IOException ex) { /* Ignore */ } finally
                {
                    invalidSockets.add(clientIndex);
                }
            }
        }

        for (int invalidSocketIndex : invalidSockets)
        {
            Socket clientSocket = clientSockets.get(invalidSocketIndex);
            try
            {
                clientSocket.close();
            } catch (Exception e)
            {
                /* Ignore */
            } finally
            {
                clientSockets.set(invalidSocketIndex, null);
            }
        }
    }
}
