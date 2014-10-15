package org.k4rthik.labs.clipshare.system;

import java.io.Serializable;

/**
 * Author: kvenugopal
 * Date  : 10/14/2014
 */
public class UpdateNotification implements Serializable
{
    private static final long serialVersionUID = 1009011318628449014L;

    private String clipboardData;
    private int sourceMachine;
    private int newRevision;

    public UpdateNotification(String clipboardData, int sourceMachine, int newRevision)
    {
        this.clipboardData = clipboardData;
        this.sourceMachine = sourceMachine;
        this.newRevision   = newRevision;
    }

    public String getClipboardData()
    {
        return clipboardData;
    }

    public int getSourceMachine()
    {
        return sourceMachine;
    }

    public int getNewRevision()
    {
        return newRevision;
    }
}
