package org.k4rthik.labs.clipshare;

/**
 * Author: kvenugopal
 * Date  : 10/21/14
 */
public class Params
{
    char MODE;
    String peerHostname;
    int peerPort;

    public Params(char MODE, int peerPort, String peerHostname)
    {
        this.MODE = MODE;
        this.peerHostname = peerHostname;
        this.peerPort = peerPort;
    }

    public char getMode()
    {
        return MODE;
    }

    public String getPeerHostname()
    {
        return peerHostname;
    }

    public int getPeerPort()
    {
        return peerPort;
    }
}
