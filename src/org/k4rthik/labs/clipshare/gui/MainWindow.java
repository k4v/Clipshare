package org.k4rthik.labs.clipshare.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Author: kvenugopal
 * Date  : 11/4/14
 */
public class MainWindow extends JPanel
{
    JRadioButton serverModeSelectRadio;
    JRadioButton clientModeSelectRadio;
    JLabel hostnameLabel;
    JTextField hostnameField;
    JLabel portLabel;
    JTextField portField;
    JButton connectButton;
    JButton disconnectButton;

    boolean isConnected = false;

    public MainWindow()
    {
        super(new GridBagLayout());
        initComponents();

        JPanel modeSelectPanel = new JPanel(new GridLayout(1, 2));
        modeSelectPanel.add(serverModeSelectRadio);
        modeSelectPanel.add(clientModeSelectRadio);

        // Add Server/Client radio buttons
        add(modeSelectPanel, new GridBagConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));

        // Add server host text field
        add(hostnameLabel,   new GridBagConstraints(0, 1, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(5, 5, 2, 5), 0, 0));
        add(hostnameField,   new GridBagConstraints(3, 1, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(5, 5, 2, 5), 0, 0));

        // Add server port text field
        add(portLabel,       new GridBagConstraints(0, 2, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(2, 5, 5, 5), 0, 0));
        add(portField,       new GridBagConstraints(3, 2, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(2, 5, 5, 5), 0, 0));

        // Add connect/disconnect buttons
        add(connectButton,   new GridBagConstraints(1, 3, 3, 1, 0, 0, GridBagConstraints.LINE_END,   GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
        add(disconnectButton,new GridBagConstraints(4, 3, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));


    }

    private void initComponents()
    {
        serverModeSelectRadio = new JRadioButton("Server");
        clientModeSelectRadio = new JRadioButton("Client");

        ButtonGroup modeSelectGroup = new ButtonGroup();
        modeSelectGroup.add(serverModeSelectRadio);
        modeSelectGroup.add(clientModeSelectRadio);

        hostnameLabel = new JLabel("Server hostname/IP");
        hostnameField = new JTextField();
        hostnameField.setPreferredSize(new Dimension(200, 20));

        portLabel = new JLabel("Server port");
        portField = new JTextField();
        portField.setPreferredSize(new Dimension(50, 20));

        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
    }
}
