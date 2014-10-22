package org.k4rthik.labs.clipshare.clipboard;

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
    String clipboardData;
    int currentRevisionState;

    private static ClipboardManager INSTANCE = null;

    private ClipboardManager()
    {
        this.currentRevisionState = 0;
    }

    public static synchronized ClipboardManager getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new ClipboardManager();

        return INSTANCE;
    }

    Object LOCK = new Object();

    public void localClipboardEvent(Transferable clipboardContents)
    {
        synchronized(LOCK)
        {
            System.out.println(Arrays.asList(clipboardContents.getTransferDataFlavors()));
            try
            {
                if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    //System.out.println ("Reading new clipboard data of type " + DataFlavor.stringFlavor);
                    //System.out.println(clipboardContents.getTransferData(DataFlavor.stringFlavor));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean remoteClipboardEvent(UpdateMessage updateMessage)
    {
        synchronized(LOCK)
        {
            if (updateMessage.getUpdateRevision() > currentRevisionState)
            {
                this.clipboardData = updateMessage.getNewClipboardData();
                this.currentRevisionState = updateMessage.getUpdateRevision();

                return true;
            }

            return false;
        }
    }
}
