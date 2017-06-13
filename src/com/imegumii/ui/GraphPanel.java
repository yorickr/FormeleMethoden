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
import java.awt.geom.Point2D;
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
        closeTab.setForeground(new Color(200, 100, 100));
        closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TabPanel.Instance().remove(GraphPanel.this);
            }
        });

        JButton exportData = new JButton("Export data");
        exportData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        String filename = "data." + Util.randomString(5) + ".dot";
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
            }
        });

        JButton exportImage = new JButton("Export image");
        exportImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        String filename = "img." + Util.randomString(5) + ".png";
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

        JButton minimizeButton = new JButton("Hopcroft");
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        if(automata.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting to DFA ", 20);
                            DFA<String> convert = ((NDFA<String>) automata).toDFA();

                            StatusPanel.Instance().setStatus("Generating image for DFA", 30);
                            File img = Graph.generateImage(convert);
                            TabPanel.Instance().addGraph(name, img, convert);

                            StatusPanel.Instance().setStatus("Minimizing DFA", 50);
                            DFA<String> mini = convert.minimaliseerHopcroft();

                            StatusPanel.Instance().setStatus("Generating minimized image", 70);
                            File img2 = Graph.generateImage(mini);
                            TabPanel.Instance().addGraph(name, img2, mini);
                            
                            StatusPanel.Instance().setStatus("Done", 100);
                        }
                        else if(automata.type == Importable.Type.DFA)
                        {
                            StatusPanel.Instance().setStatus("Minimizing dfa." + name, 40);
                            DFA<String> dfa = (DFA<String>) automata;
                            DFA<String> mini = dfa.minimaliseerHopcroft();

                            StatusPanel.Instance().setStatus("Generating minimized image", 60);
                            File img3 = Graph.generateImage(mini);
                            TabPanel.Instance().addGraph(name, img3, mini);

                            StatusPanel.Instance().setStatus("Done", 100);
                        }
                    }
                });
            }
        });

        JButton minimize2Button = new JButton("Bryzowski");
        minimize2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        if(automata.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting to DFA ", 20);
                            DFA<String> convert = ((NDFA<String>) automata).toDFA();

                            StatusPanel.Instance().setStatus("Generating image for DFA", 30);
                            File img = Graph.generateImage(convert);
                            TabPanel.Instance().addGraph(name, img, convert);

                            StatusPanel.Instance().setStatus("Minimizing DFA", 50);
                            DFA<String> mini = convert.minimaliseer();

                            StatusPanel.Instance().setStatus("Generating minimized image", 70);
                            File img2 = Graph.generateImage(mini);
                            TabPanel.Instance().addGraph(name, img2, mini);

                            StatusPanel.Instance().setStatus("Done", 100);
                        }
                        else if(automata.type == Importable.Type.DFA)
                        {
                            StatusPanel.Instance().setStatus("Minimizing dfa." + name, 40);
                            DFA<String> dfa = (DFA<String>) automata;
                            DFA<String> mini = dfa.minimaliseer();

                            StatusPanel.Instance().setStatus("Generating minimized image", 60);
                            File img3 = Graph.generateImage(mini);
                            TabPanel.Instance().addGraph(name, img3, mini);

                            StatusPanel.Instance().setStatus("Done", 100);
                        }
                    }
                });
            }
        });

        JButton toDfaButton = new JButton("to DFA");
        toDfaButton.addActionListener(e -> {
            BackgroundWorker.instance().addWorker(() -> {
                StatusPanel.Instance().setStatus("Converting to DFA ", 10);
                DFA<String> convert = ((NDFA<String>) automata).toDFA();

                StatusPanel.Instance().setStatus("Generating image for DFA", 50);
                File img = Graph.generateImage(convert);
                TabPanel.Instance().addGraph(name, img, convert);

                StatusPanel.Instance().setStatus("Done", 100);
            });
        });

        JButton reverseButton = new JButton("Reverse");
        reverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {

                        StatusPanel.Instance().setStatus("Reversing ", 20);

                        NDFA<String> reverse = ((DFA<String>) automata).reverse();

                        StatusPanel.Instance().setStatus("Generating reversed image", 70);

                        File img = Graph.generateImage(reverse);

                        TabPanel.Instance().addGraph("Reverse " + name, img, reverse);

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        JButton notButton = new JButton("Not");
        notButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {

                        StatusPanel.Instance().setStatus("Nothing ", 20);

                        DFA<String> not = ((DFA<String>) automata).ontkenning();

                        StatusPanel.Instance().setStatus("Generating not image", 70);

                        File img = Graph.generateImage(not);

                        TabPanel.Instance().addGraph("Not " + name, img, not);

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        JButton showPopup = new JButton("Show popup");
        showPopup.addActionListener(e -> {
            new PopupFrame(name, automata);
        });

        JButton showGrammar = new JButton("Show grammar");
        showGrammar.addActionListener(e -> {
            BackgroundWorker.instance().addWorker(() -> {
                GrammarConverter c = new GrammarConverter();
                new TextPanel("Grammar", c.toGrammar((NDFA<String>) automata).printToString());
            });
        });

        buttonPanel.add(Box.createHorizontalGlue());

        if(automata.type == Importable.Type.DFA) {
            buttonPanel.add(notButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPanel.add(reverseButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        buttonPanel.add(showPopup);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        if (automata.type == Importable.Type.NDFA) {
            buttonPanel.add(showGrammar);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

            buttonPanel.add(toDfaButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        buttonPanel.add(minimize2Button);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
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
