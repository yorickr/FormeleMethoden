package com.imegumii.ui;

import javax.swing.*;

/**
 * Created by imegumii on 13/06/2017.
 */
public class TextPanel extends JFrame {
    public TextPanel (String name, String text) {
        super(name);
        this.setSize(600, 600);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        textArea.append(text);

        this.setContentPane(scrollPane);

        this.setVisible(true);
    }
}
