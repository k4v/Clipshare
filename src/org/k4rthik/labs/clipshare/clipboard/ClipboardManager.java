package org.k4rthik.labs.clipshare.clipboard;

import org.k4rthik.labs.clipshare.network.NetworkManager;
import org.k4rthik.labs.clipshare.network.UpdateMessage;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class ClipboardManager
{
    // This stores metadata on the last update: [revision ID, originator machine index]
    private int currentRevisionState;
    private static Integer machineIndex = null;

    private static ClipboardManager INSTANCE = null;
    private ClipboardListener clipboardListener = null;

    private ClipboardManager()
    {
        this.currentRevisionState = 0;
    }

    public static synchronized ClipboardManager getInstance()
    {
        if(machineIndex == null)
            throw new NullPointerException("System index not set. Cannot initialize Clipboard Manager");

        if(INSTANCE == null)
            INSTANCE = new ClipboardManager();

        return INSTANCE;
    }

    public static void init(int thisIndex)
    {
        machineIndex = thisIndex;
    }

    public synchronized int getCurrentRevision()
    {
        return currentRevisionState;
    }

    public void localClipboardEvent(Transferable clipboardContents, int currentRevision)
    {
        // Another clipboard update (probably remote) happened after this local update.
        if(currentRevision < currentRevisionState)
            return;

        // This function will be synchronized with remoteClipboardEvent() by ClipboardListener.regainOwnership()
        System.out.println(Arrays.asList(clipboardContents.getTransferDataFlavors()));
        try
        {
            if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                System.out.println("Updating clipboard to "+clipboardContents.getTransferData(DataFlavor.stringFlavor));
                clipboardListener.setContents(clipboardContents);
                currentRevisionState++;

                NetworkManager.getInstance().writeToPeer(clipboardContents, currentRevision);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean remoteClipboardEvent(UpdateMessage updateMessage)
    {
        synchronized(clipboardListener)
        {
            if (updateMessage.getUpdateRevision() > currentRevisionState)
            {
                this.currentRevisionState = updateMessage.getUpdateRevision();
                return true;
            }

            return false;
        }
    }
}
