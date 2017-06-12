package com.imegumii.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;

/**
 * Created by kenny on 9-6-2017.
 */
public class GraphImagePanel extends JPanel {

    private BufferedImage image;

    public GraphImagePanel(File f)
    {
        super();

        this.setBackground(Color.WHITE);

        try {
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));

        if(image != null){

            int width = this.getWidth();
            int height = this.getHeight();

            if(image.getWidth() > image.getHeight()) {
                height = (int)((width * image.getHeight()) / image.getWidth());
            }
            else {
                width = (int) ((height * image.getWidth()) / image.getHeight());
            }

            g2d.drawImage(image, 0,0, width, height, this);
        }
    }
}
