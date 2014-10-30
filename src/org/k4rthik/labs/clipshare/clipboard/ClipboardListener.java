package org.k4rthik.labs.clipshare.clipboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Author: kvenugopal
 * Date  : 10/15/14
 *
 * Listens for changes to clipboard contents and notifies the ClipboardManager
 * with an update message
 */
public class ClipboardListener implements ClipboardOwner, FlavorListener, Runnable
{
    private Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Keep thread running
    private boolean keepAlive;

    public ClipboardListener()
    {
        // Take ownership of the clipboard
        systemClipboard.setContents(systemClipboard.getContents(null), this);
    }

    public void terminate()
    {
        this.keepAlive = false;
    }

    @Override
    public void flavorsChanged(FlavorEvent flavorEvent)
    {
        int[] currentRevision = ClipboardManager.getInstance().getCurrentRevision();

        System.out.println("Clipboard update detected (Flavor)");
        regainOwnership(currentRevision);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable)
    {
        int[] currentRevision = ClipboardManager.getInstance().getCurrentRevision();

        System.out.println("Clipboard update detected (Owner)");

        try
        {
            Thread.sleep(50);
        } catch (InterruptedException ie)
        {
            /* Ignore */
        }

        regainOwnership(currentRevision);
    }

    // In case clipboard changes, ownership is lost, take it back again.
    private synchronized void regainOwnership(int[] currentRevision)
    {
        // Try to get clipboard contents. This can fail if clipboard was recently changed.
        // TODO: Check if this sleep event causes missed updates and its effect on system.
        Transferable clipboardContents = null;
        boolean retryOnException;
        do
        {
            System.out.println("Trying to get ownership");
            retryOnException = false;
            try
            {
                clipboardContents = systemClipboard.getContents(null);
            } catch (Exception e)
            {
                retryOnException = true;
                try
                {
                    Thread.sleep(50);
                } catch (InterruptedException ie)
                {
                    /* Ignore */
                }
            }
        } while(retryOnException);

        try
        {
            ClipboardManager.getInstance().localClipboardEvent(clipboardContents, currentRevision);
        } catch (Exception e)
        {
            System.err.println("Unable to regain control of system clipboard. Shutting down.");
            terminate();
        }
    }

    public void setContents(Transferable newContents)
    {
        systemClipboard.setContents(newContents, this);
    }

    public void setContents(String newContents)
    {
        StringSelection stringSelection = new StringSelection(newContents);
        setContents(stringSelection);
    }

    @Override
    public void run()
    {
        keepAlive = true;
        //regainOwnership(ClipboardManager.getInstance().getCurrentRevision());

        System.out.println("Starting clipboard listener");
        while(keepAlive);
        System.out.println("Stopping clipboard listener");
    }
}
