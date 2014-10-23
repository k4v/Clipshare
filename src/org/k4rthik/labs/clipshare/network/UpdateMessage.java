package org.k4rthik.labs.clipshare.network;

import java.awt.datatransfer.Transferable;
import java.io.Serializable;

/**
 * Author: kvenugopal
 * Date  : 10/21/14
 */
public class UpdateMessage implements Serializable
{
    // For serialization
    private static final long serialVersionUID = 550032638229360705L;

    // Message contents describing clipboard update
    private final Transferable newClipboardData;
    private final int[] updateRevision;
    private final int sourceMachine;

    public UpdateMessage(Transferable clipboardData, int[] updateRevision, int sourceMachine)
    {
        this.newClipboardData = clipboardData;
        this.updateRevision = updateRevision;
        this.sourceMachine = sourceMachine;
    }

    public Transferable getNewClipboardData()
    {
        return newClipboardData;
    }

    public int[] getUpdateRevision()
    {
        return updateRevision;
    }

    public int getSourceMachine()
    {
        return sourceMachine;
    }
}
