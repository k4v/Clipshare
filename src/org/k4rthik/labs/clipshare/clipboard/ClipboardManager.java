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
    private String currentClipboardData;
    private int[]  currentRevisionState;
    private static Integer machineIndex = null;

    private static ClipboardManager INSTANCE = null;
    private ClipboardListener clipboardListener = null;

    private ClipboardManager()
    {
        this.currentRevisionState = new int[]{0, 0};
        this.currentClipboardData = null;
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
        return currentRevisionState[machineIndex];
    }

    // This function will be synchronized with remoteClipboardEvent() by ClipboardListener.regainOwnership()
    public void localClipboardEvent(Transferable clipboardContents, int currentRevision)
    {
        // Don't update if same string is copied to clipboard again.
        try
        {
            if (clipboardContents.getTransferData(DataFlavor.stringFlavor).equals(currentClipboardData))
            {

            }
        } catch (Exception e)
        {
            System.out.println("Unable to validate current clipboard contents");
        }


        if(
                // Another clipboard update (probably remote) happened after this local update.
                (currentRevision < currentRevisionState[machineIndex])
                // Updates by "server" peer get precedence
             || ((currentRevision == currentRevisionState[machineIndex]) && (machineIndex == 1))
          )
        {
            return;
        }

        System.out.println(Arrays.asList(clipboardContents.getTransferDataFlavors()));
        try
        {
            if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                String clipboardText = (String)(clipboardContents.getTransferData(DataFlavor.stringFlavor));
                System.out.println("Updating clipboard to "+clipboardText);
                clipboardListener.setContents(clipboardContents);
                currentRevisionState[machineIndex]++;

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
            // I am fully synced with other machine's local changes
            if(currentRevisionState[machineIndex] == currentRevisionState[updateMessage.getSourceMachine()])
            {
                // New remote update from the other machine
                if(currentRevisionState[updateMessage.getSourceMachine()] < updateMessage.getUpdateRevision()[updateMessage.getSourceMachine()])
                {
                    currentRevisionState[machineIndex]++;
                    currentRevisionState[updateMessage.getSourceMachine()] = updateMessage.getUpdateRevision()[updateMessage.getSourceMachine()];
                }
            }
            else if ((machineIndex > updateMessage.getSourceMachine())
                    && (updateMessage.getUpdateRevision()[updateMessage.getSourceMachine()] > currentRevisionState[updateMessage.getSourceMachine()]))
            {
                currentRevisionState[machineIndex]++;
                currentRevisionState[updateMessage.getSourceMachine()] = updateMessage.getUpdateRevision()[updateMessage.getSourceMachine()];
            }

            return false;
        }
    }
}
