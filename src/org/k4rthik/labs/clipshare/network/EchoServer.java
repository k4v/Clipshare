package org.k4rthik.labs.clipshare.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class EchoServer
{
    public void startServer(int portNumber)
    {
        try
        {
            ServerSocket serverSocket =
                    new ServerSocket(portNumber);
            System.out.print("Waiting for client... ");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established at "+System.currentTimeMillis());

            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                out.println(inputLine);
            }
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
