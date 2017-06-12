package com.imegumii.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;

/**
 * Created by kenny on 9-6-2017.
 */
public class GraphImagePanel extends JPanel {

    private BufferedImage image;

    private float scale = 1f;
    private float x = 0f, y = 0f;
    private Point dragStartScreen;
    private Point dragEndScreen;

    public GraphImagePanel(File f)
    {
        super();

        this.setBackground(Color.WHITE);

        try {
            image = ImageIO.read(f);
//            x = image.getWidth() / 2;
//            y = image.getHeight() / 2;
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
//                System.out.println("Scrolled" + scale);
                double delta = 0.05f * e.getPreciseWheelRotation();
                scale += delta;
                revalidate();
                repaint();
            }

        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                dragEndScreen = e.getPoint();
                double dx = dragEndScreen.getX() - dragStartScreen.getX();
                double dy = dragEndScreen.getY() - dragStartScreen.getY();
                x += dx;
                y += dy;
                dragStartScreen = dragEndScreen;
                dragEndScreen = null;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                dragStartScreen = e.getPoint();
                dragEndScreen = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));

        if(image != null){

//            int width = this.getWidth();
//            int height = this.getHeight();
//
//            if(image.getWidth() > image.getHeight()) {
//                height = (int)((width * image.getHeight()) / image.getWidth());
//            }
//            else {
//                width = (int) ((height * image.getWidth()) / image.getHeight());
//            }
            AffineTransform at = new AffineTransform();
            g.drawLine(-1000, 0, 1000, 0);
            g.drawLine(0, -1000, 0, 1000);
            at.scale(1, -1);
            at.setToTranslation(x, y);
            at.scale(scale, scale);
            g2d.drawImage(image, at, this);
//            g2d.drawImage(image, 0,0, width, height, this);
        }
    }
}
