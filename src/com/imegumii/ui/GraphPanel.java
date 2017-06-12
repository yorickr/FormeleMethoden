package com.imegumii.ui;

import com.imegumii.*;
import org.apache.commons.io.FileUtils;
import org.apache.xpath.SourceTree;

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
    private Automata<String> automata;

    public String getName()
    {
        return name;
    }

    public GraphPanel(String name, File f, Automata<String> automata)
    {
        super(new BorderLayout());

        this.name = name;
        this.imageLocation = f;
        this.automata = automata;

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
                    w.print(automata.getTransitions());
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

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        String filename = "img." + name.toLowerCase() + ".png";
                        File file = new File("export/" + filename);

                        if(file.exists()){
                            int reply = JOptionPane.showConfirmDialog(GraphPanel.this, "The file " + filename + "already exists. Do you want to overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
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
            }
        });

        JButton minimizeButton = new JButton("Minimize graph");
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        if(automata.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting to DFA " + name, 20);

                            DFA<String> convert = ((NDFA<String>) automata).toDFA();

                            StatusPanel.Instance().setStatus("Generating image for DFA " + name, 30);

                            Graph.generateImage(convert, "cdfa");

                            TabPanel.Instance().addGraph("DFA: " + name, new File("images/cdfa.png"), convert);

                            StatusPanel.Instance().setStatus("Minimizing cdfa." + name, 50);

                            DFA<String> mini = convert.minimaliseerHopcroft();

                            StatusPanel.Instance().setStatus("Generating minimized image", 70);

                            Graph.generateImage(mini, "mcdfa");

                            TabPanel.Instance().addGraph("DFA: mcdfa." + name, new File("images/mcdfa.png"), mini);
                            StatusPanel.Instance().setStatus("Done", 100);
                        }
                        else if(automata.type == Importable.Type.DFA)
                        {
                            DFA<String> dfa = (DFA<String>) automata;

                            StatusPanel.Instance().setStatus("Minimizing dfa." + name, 40);

                            DFA<String> mini = dfa.minimaliseerHopcroft();

                            StatusPanel.Instance().setStatus("Generating minimized image", 60);

                            Graph.generateImage(mini, "mcdfa");

                            TabPanel.Instance().addGraph("DFA: mcdfa." + name, new File("images/mcdfa.png"), mini);

                            StatusPanel.Instance().setStatus("Done", 100);

                        }
                    }
                });
            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(minimizeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(exportImage);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(exportData);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(closeTab);

        this.add(buttonPanel, BorderLayout.SOUTH);

    }
}
