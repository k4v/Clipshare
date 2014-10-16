package org.k4rthik.labs.clipshare.system;

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
    int[] currentRevisionState;

    private static ClipboardManager INSTANCE = null;

    private ClipboardManager()
    {
        this.currentRevisionState = new int[]{0,0};
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

    public boolean remoteClipboardEvent(UpdateNotification updateNotification)
    {
        synchronized(LOCK)
        {
            if (updateNotification.getNewRevision() >= currentRevisionState[1])
            {
                if ((updateNotification.getNewRevision() > currentRevisionState[1])
                        || (updateNotification.getSourceMachine() > currentRevisionState[1]))
                {
                    this.clipboardData = updateNotification.getClipboardData();
                    this.currentRevisionState[0] = updateNotification.getNewRevision();
                    this.currentRevisionState[1] = updateNotification.getSourceMachine();

                    return true;
                }
            }

            return false;
        }
    }
}
