package com.imegumii.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kenny on 12-6-2017.
 */
public class PopupFrame extends JFrame {

    public PopupFrame(String name, String content)
    {
        super(name);

        this.setSize(300, 800);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(content);

        container.add(textArea, BorderLayout.CENTER);

        this.setContentPane(container);

        this.setVisible(true);
    }
}
