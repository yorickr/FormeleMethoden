package com.imegumii.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by kenny on 8-6-2017.
 */
public class RegexPanel extends JPanel {

    private static RegexPanel panel;

    public static RegexPanel Instance()
    {
        if(panel == null)
            panel = new RegexPanel();

        return panel;
    }

    private RegexPanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextField text = new JTextField();

        this.add(text);

        this.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton button = new JButton("Generate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImagePanel.Instance().setImage(new File("images/en.png"));
                System.out.println("Refreshing image");
            }
        });

        this.add(button);

        this.add(Box.createHorizontalGlue());
    }
}
