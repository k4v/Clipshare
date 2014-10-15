package org.k4rthik.labs.clipshare.system;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class ClipboardManager
{
    String clipboardData;

    // This stores metadata on the last update: [revision ID, originator machine index]
    int[] currentRevisionState;

    public ClipboardManager()
    {
        this.currentRevisionState = new int[]{0,0};
    }

    public synchronized boolean setClipboardData(UpdateNotification updateNotification)
    {
        if(updateNotification.getNewRevision() >= currentRevisionState[1])
        {
            if((updateNotification.getNewRevision() > currentRevisionState[1])
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
