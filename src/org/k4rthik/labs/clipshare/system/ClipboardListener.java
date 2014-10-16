package org.k4rthik.labs.clipshare.system;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
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
        System.out.println("Flavor changed");
        try
        {
            // Some time for the clipboard to be ready
            Thread.sleep(50);
        } catch(InterruptedException e)
        {
            /* Ignore */
        } finally
        {
            regainOwnership();
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable)
    {
        System.out.println("Owner changed");
        try
        {
            // Some time for the clipboard to be ready
            Thread.sleep(50);
        } catch(InterruptedException e)
        {
            /* Ignore */
        } finally
        {
            regainOwnership();
        }
    }

    // In case clipboard changes, ownership is lost, take it back again.
    private void regainOwnership()
    {
        // Try to get clipboard contents. This can fail if clipboard was recently changed.
        // TODO: Check if this sleep event causes missed updates and its effect on system.
        Transferable clipboardContents = null;
        boolean retryOnException;
        do
        {
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
            systemClipboard.setContents(clipboardContents, this);
            ClipboardManager.getInstance().localClipboardEvent(clipboardContents);
        } catch (Exception e)
        {
            System.err.println("Unable to regain control of system clipboard. Shutting down.");
            terminate();
        }
    }

    @Override
    public void run()
    {
        regainOwnership();
        while(keepAlive);
    }
}
