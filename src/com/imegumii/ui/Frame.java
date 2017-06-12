package com.imegumii.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kenny on 8-6-2017.
 */
public class Frame extends JFrame {

    public Frame()
    {
        super("Automata");

        this.setSize(1000, 800);
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        container.add(TabPanel.Instance(), BorderLayout.CENTER);
        container.add(InputPanel.Instance(), BorderLayout.NORTH);
        container.add(StatusPanel.Instance(), BorderLayout.SOUTH);

        this.setContentPane(container);

        this.setVisible(true);
    }
}
