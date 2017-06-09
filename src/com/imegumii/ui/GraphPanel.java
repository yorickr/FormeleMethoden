package com.imegumii.ui;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 8-6-2017.
 */
public class GraphPanel extends JPanel {

    private String name;
    private File imageLocation;
    private String export;

    public String getName()
    {
        return name;
    }

    public GraphPanel(String name, File f, String export)
    {
        super(new BorderLayout());

        this.name = name;
        this.imageLocation = f;
        this.export = export;

        this.add(new GraphImagePanel(imageLocation), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JButton closeTab = new JButton("Close");
        closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TabPanel.Instance().remove(GraphPanel.this);
            }
        });

        JButton exportData = new JButton("Export raw data");
        exportData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = "raw." + name.toLowerCase() + ".dot";
                File file = new File("export/" + filename);

                if(file.exists()){
                    int reply = JOptionPane.showConfirmDialog(null, "The file " + filename + "already exists. Do you want to overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
                    if (reply != JOptionPane.YES_OPTION) {
                        StatusPanel.Instance().setStatus("Cancelled export");
                        return;
                    }
                }

                PrintWriter w = null;
                try {
                    w = new PrintWriter(file);
                    w.print(export);
                    w.flush();
                    w.close();
                    StatusPanel.Instance().setStatus("Success: " + filename);
                } catch (FileNotFoundException e1) {
                    StatusPanel.Instance().setStatus("Something went wrong while exporting the file");
                }
            }
        });

        JButton exportImage = new JButton("Export image");
        exportImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String filename = "img." + name.toLowerCase() + ".png";
                File file = new File("export/" + filename);

                if(file.exists()){
                    int reply = JOptionPane.showConfirmDialog(null, "The file " + filename + "already exists. Do you want to overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
                    if (reply != JOptionPane.YES_OPTION) {
                        StatusPanel.Instance().setStatus("Cancelled export");
                        return;
                    }
                }

                try {
                    FileUtils.copyFile(f, file);
                    StatusPanel.Instance().setStatus("Success: " + filename);
                } catch (IOException e1) {
                    StatusPanel.Instance().setStatus("Something went wrong while exporting the file");
                }
            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exportImage);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(exportData);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(closeTab);

        this.add(buttonPanel, BorderLayout.SOUTH);

    }
}
