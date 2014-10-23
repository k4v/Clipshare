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

    public synchronized int[] getCurrentRevision()
    {
        return currentRevisionState;
    }

    // This function will be synchronized with remoteClipboardEvent() by ClipboardListener.regainOwnership()
    public boolean localClipboardEvent(Transferable clipboardContents, int[] updateTimeRevision)
    {
        String newClipboardText = null;
        // Don't update if same string is copied to clipboard again.
        try
        {
            if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                newClipboardText = (String) (clipboardContents.getTransferData(DataFlavor.stringFlavor));
                if (newClipboardText.equals(currentClipboardData))
                {
                    System.out.println("No change in clipboard contents. Ignoring.");
                    return false;
                }
            }
            else
            {
                System.err.println("Current clipboard content does not have String representation");
                return false;
            }
        } catch (Exception e)
        {
            System.err.println("Unable to validate current clipboard contents");
            return false;
        }

        boolean writeAccepted = true;
        if(Arrays.equals(updateTimeRevision, currentRevisionState))
        {
            currentRevisionState[machineIndex]++;
        }
        else if(compareRevisionStates(updateTimeRevision, machineIndex, currentRevisionState, 1-machineIndex))
        {
            currentRevisionState = updateTimeRevision;
        }
        else
        {
            writeAccepted = false;
        }

        System.out.println("Local update "+(writeAccepted ? "accepted" : "rejected"));
        if(writeAccepted)
        {
            try
            {
                System.out.println("Updating clipboard to "+newClipboardText);
                clipboardListener.setContents(clipboardContents);
                NetworkManager.getInstance().writeToPeer(clipboardContents, currentRevisionState, machineIndex);
            } catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        return writeAccepted;
    }

    public boolean remoteClipboardEvent(UpdateMessage updateMessage)
    {
        synchronized(clipboardListener)
        {
            // I am fully synced with other machine's local changes
            if(compareRevisionStates(currentRevisionState, machineIndex, updateMessage.getUpdateRevision(), updateMessage.getSourceMachine()))
            {
                currentRevisionState[machineIndex]++;
                currentRevisionState[updateMessage.getSourceMachine()] = updateMessage.getUpdateRevision()[updateMessage.getSourceMachine()];

                return true;
            }
            return false;
        }
    }

    private boolean compareRevisionStates(int[] localState, int localIndex, int[] remoteState, int remoteIndex)
    {
        boolean compareResult = (((localState[localIndex] == remoteState[localIndex]) || (remoteIndex > localIndex))
                && (localState[remoteIndex] < remoteState[remoteIndex]));

        System.out.println("Comparing "+Arrays.asList(localState)+" and "+Arrays.asList(remoteState)+": "+compareResult);
        return compareResult;
    }
}
