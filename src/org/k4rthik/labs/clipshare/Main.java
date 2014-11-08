package org.k4rthik.labs.clipshare;

import org.k4rthik.labs.clipshare.clipboard.ClipboardManager;
import org.k4rthik.labs.clipshare.gui.MainWindow;
import org.k4rthik.labs.clipshare.network.NetworkManager;

import javax.swing.*;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class Main
{
    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            printUsage(1);
        }

        int machineIndex = 0;

        Params execParams = null;
        // Start program in client mode
        if(args[0].equalsIgnoreCase("-c"))
        {
            if(args.length < 2)
            {
                printUsage(1);
            }
            execParams = new Params('c', args[1], Integer.parseInt(args[2]));
            machineIndex = 1;
        }
        // Start program in server mode
        else if(args[0].equalsIgnoreCase("-s"))
        {
            if(args.length < 1)
            {
                printUsage(1);
            }
            execParams = new Params('s', "localhost", Integer.parseInt(args[1]));
            machineIndex = 0;
        }
        else if(args[0].equalsIgnoreCase("-h"))
        {
            printUsage(0);
        }

        ClipboardManager.init(machineIndex);
        NetworkManager.init(execParams);

        new Thread(ClipboardManager.getInstance()).start();
        new Thread(NetworkManager.getInstance()).start();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void printUsage(int exitCode)
    {
        System.out.println("java -jar Clipshare.jar");
        System.out.println("  -c {server hostname} {server port}");
        System.out.println("| -s {server port}");

        System.exit(exitCode);
    }

    private static void createAndShowGUI()
    {
        //Create and set up the window.
        JFrame frame = new JFrame("Clipshare");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new MainWindow();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
