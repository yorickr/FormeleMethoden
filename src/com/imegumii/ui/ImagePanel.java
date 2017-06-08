package com.imegumii.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 8-6-2017.
 */
public class ImagePanel extends JPanel {

    private static ImagePanel panel;

    public static ImagePanel Instance()
    {
        if(panel == null)
            panel = new ImagePanel();

        return panel;
    }

    private ImageIcon image;
    private JLabel label;

    private ImagePanel()
    {
        super();

        image = new ImageIcon();
        label = new JLabel();
        label.setIcon(image);

        this.add(label);
    }

    public void setImage(File f)
    {
        try {
            BufferedImage bfimg = ImageIO.read(f);
            image = new ImageIcon(bfimg);
            label.setIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
