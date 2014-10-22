package org.k4rthik.labs.clipshare.network;

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
    private final String newClipboardData;
    private final int updateRevision;

    public UpdateMessage(String clipboardData, int updateRevision)
    {
        this.newClipboardData = clipboardData;
        this.updateRevision = updateRevision;
    }

    public String getNewClipboardData()
    {
        return newClipboardData;
    }

    public int getUpdateRevision()
    {
        return updateRevision;
    }
}
