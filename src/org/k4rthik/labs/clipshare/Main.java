package org.k4rthik.labs.clipshare;

import org.k4rthik.labs.clipshare.network.NetworkManager;

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

        Params execParams = null;
        // Start program in client mode
        if(args[0].equalsIgnoreCase("c"))
        {
            execParams = new Params('c', Integer.parseInt(args[1]), args[2]);
        }
        // Start program in server mode
        else if(args[0].equalsIgnoreCase("s"))
        {
            execParams = new Params('c', Integer.parseInt(args[2]), "localhost");
        }

        NetworkManager.init(execParams);
        NetworkManager.getInstance();
    }
}
