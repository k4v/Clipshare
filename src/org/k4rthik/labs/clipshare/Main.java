package org.k4rthik.labs.clipshare;

import org.k4rthik.labs.clipshare.network.ConnectionMonitor;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class Main
{
    public static void main(String[] args)
    {
        if(args.length < 2)
            System.exit(1);

        // Start program in client mode
        if(args[0].equalsIgnoreCase("c"))
        {
            String serverHost = args[1];
            int serverPort = Integer.parseInt(args[2]);
        }
        // Start program in server mode
        else if(args[0].equalsIgnoreCase("s"))
        {
            int serverPort = Integer.parseInt(args[1]);
            ConnectionMonitor connectionMonitor = new ConnectionMonitor(serverPort);
            new Thread(connectionMonitor).start();
        }
    }
}
