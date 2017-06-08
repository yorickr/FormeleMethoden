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

        this.setSize(700, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        container.add(ImagePanel.Instance(), BorderLayout.CENTER);
        container.add(RegexPanel.Instance(), BorderLayout.NORTH);

        this.setContentPane(container);

        this.setVisible(true);
    }
}
