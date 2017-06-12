package com.imegumii.ui;

import com.imegumii.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by kenny on 8-6-2017.
 */
public class InputPanel extends JPanel {

    private static InputPanel panel;

    public static InputPanel Instance()
    {
        if(panel == null)
            panel = new InputPanel();

        return panel;
    }

    private InputPanel()
    {
        super(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        topPanel.add(new JLabel("Regular expression"));
        topPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JTextField text = new JTextField();

        topPanel.add(text);

        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton button = new JButton("Generate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        String regS = text.getText().trim();
                        String name = regS;

                        StatusPanel.Instance().setStatus("Parsing REGEX", 20);

                        RegExp regex = new RegExp();
                        regex = regex.naarRegExp(regS);

                        StatusPanel.Instance().setStatus("Converting REGEX to NDFA", 40);

                        NDFA<String> ndfa = ThompsonConverter.convert(regex);

                        StatusPanel.Instance().setStatus("Generating image for NDFA", 70);

                        File img = Graph.generateImage(ndfa);

                        TabPanel.Instance().addGraph(name, img, ndfa);

                        StatusPanel.Instance().setStatus("Generating taal", 90);

                        new PopupFrame(name, regex);

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        topPanel.add(button);

        topPanel.add(Box.createHorizontalGlue());
        this.add(topPanel, BorderLayout.NORTH);


        //------------
        //BOTTOM PANEL
        //-----------

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        bottomPanel.add(new JLabel("File"));
        bottomPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JComboBox<String> fileCombo = new JComboBox<String>(findFiles());

        bottomPanel.add(fileCombo);

        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton fileButton = new JButton("Generate");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
;
                        StatusPanel.Instance().setStatus("Parsing file", 10);

                        String file = (String)fileCombo.getSelectedItem();
                        String name = file.replaceAll(".dot", "");

                        Importable p = FileParser.read(file);

                        if(p.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Generating image for NDFA", 60);
                            NDFA<String> ndfa = (NDFA<String>)p;

                            File f = Graph.generateImage(ndfa);

                            TabPanel.Instance().addGraph(name, f, ndfa);
                        }

                        if(p.type == Importable.Type.DFA)
                        {
                            StatusPanel.Instance().setStatus("Generating image for DFA", 60);
                            DFA<String> dfa = (DFA<String>)p;

                            File f = Graph.generateImage(dfa);

                            TabPanel.Instance().addGraph(name, f, dfa);
                        }

                        if(p.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to NDFA", 40);
                            RegExp regex = (RegExp) p;

                            NDFA<String> ndfa = ThompsonConverter.convert(regex);

                            StatusPanel.Instance().setStatus("Generating image for NDFA", 70);

                            File f = Graph.generateImage(ndfa);

                            TabPanel.Instance().addGraph(name, f, ndfa);
                        }

                        StatusPanel.Instance().setStatus("Done", 100);

                    }
                });
            }
        });

        bottomPanel.add(fileButton);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private String[] findFiles()
    {
        File folder = new File("input");
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".dot")) {
                fileNames.add(listOfFiles[i].getName());
            }
        }

        String[] files = new String[fileNames.size()];
        files = fileNames.toArray(files);

        return files;
    }
}
