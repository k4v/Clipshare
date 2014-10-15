package org.k4rthik.labs.clipshare.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class EchoClient
{
    public void startClient(String serverHost, int serverPort)
    {
        try {
            System.out.print("Attempting to connect to server... ");
            Socket echoSocket = new Socket(serverHost, serverPort);
            System.out.println("connection established at "+System.currentTimeMillis());

            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);

            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));

            String userInput;
            while ((userInput = stdIn.readLine()) != null)
            {
                out.println(userInput);
            }
        } catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + serverHost);
            System.exit(1);
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    serverHost);
            System.exit(1);
        }
    }
}
