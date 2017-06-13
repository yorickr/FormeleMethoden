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
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

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

//                        StatusPanel.Instance().setStatus("Generating taal", 90);

//                        new PopupFrame(name, regex);

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        topPanel.add(button);

        topPanel.add(Box.createHorizontalGlue());
        this.add(topPanel);


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

        this.add(bottomPanel);



        //------------
        //Advanced PANEL
        //-----------

        JPanel advPanel = new JPanel();
        advPanel.setLayout(new BoxLayout(advPanel, BoxLayout.LINE_AXIS));
        advPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JComboBox<String> f1Combo = new JComboBox<String>(findFiles());
        JComboBox<String> f2Combo = new JComboBox<String>(findFiles());

        JButton orButton = new JButton("or");
        orButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        StatusPanel.Instance().setStatus("Parsing files", 10);

                        String file1 = (String)f1Combo.getSelectedItem();
                        String name1 = file1.replaceAll(".dot", "");
                        String file2 = (String)f2Combo.getSelectedItem();
                        String name2 = file2.replaceAll(".dot", "");

                        Importable p1 = FileParser.read(file1);
                        Importable p2 = FileParser.read(file1);

                        DFA<String> dfa1 = null;

                        if(p1.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting NDFA to DFA", 30);
                            NDFA<String> ndfa1 = (NDFA<String>)p1;
                            dfa1 = ndfa1.toDFA().minimaliseerHopcroft();
                        }
                        if(p1.type == Importable.Type.DFA)
                        {
                            dfa1 = (DFA<String>)p1;
                        }
                        if(p1.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to DFA", 30);
                            RegExp regex = (RegExp) p1;
                            NDFA<String> ndfa1 = ThompsonConverter.convert(regex);
                            dfa1 = ndfa1.toDFA().minimaliseerHopcroft();
                        }

                        DFA<String> dfa2 = null;

                        if(p2.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting NDFA to DFA", 50);
                            NDFA<String> ndfa2 = (NDFA<String>)p2;
                            dfa2 = ndfa2.toDFA().minimaliseerHopcroft();
                        }
                        if(p2.type == Importable.Type.DFA)
                        {
                            dfa2 = (DFA<String>)p2;
                        }
                        if(p2.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to DFA", 50);
                            RegExp regex2 = (RegExp) p2;
                            NDFA<String> ndfa2 = ThompsonConverter.convert(regex2);
                            dfa2 = ndfa2.toDFA().minimaliseerHopcroft();
                        }

                        StatusPanel.Instance().setStatus("Executing or operation", 65);
                        DFA<String> of = dfa1.of(dfa2);

                        StatusPanel.Instance().setStatus("Generating image", 80);

                        File f = Graph.generateImage(of);
                        TabPanel.Instance().addGraph(name1 + " or " + name2, f, of);

                        StatusPanel.Instance().setStatus("Done", 100);

                    }
                });
            }
        });

        JButton andButton = new JButton("and");
        andButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        StatusPanel.Instance().setStatus("Parsing files", 10);

                        String file1 = (String)f1Combo.getSelectedItem();
                        String name1 = file1.replaceAll(".dot", "");
                        String file2 = (String)f2Combo.getSelectedItem();
                        String name2 = file2.replaceAll(".dot", "");

                        Importable p1 = FileParser.read(file1);
                        Importable p2 = FileParser.read(file1);

                        DFA<String> dfa1 = null;

                        if(p1.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting NDFA to DFA", 30);
                            NDFA<String> ndfa1 = (NDFA<String>)p1;
                            dfa1 = ndfa1.toDFA().minimaliseerHopcroft();
                        }
                        if(p1.type == Importable.Type.DFA)
                        {
                            dfa1 = (DFA<String>)p1;
                        }
                        if(p1.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to DFA", 30);
                            RegExp regex = (RegExp) p1;
                            NDFA<String> ndfa1 = ThompsonConverter.convert(regex);
                            dfa1 = ndfa1.toDFA().minimaliseerHopcroft();
                        }

                        DFA<String> dfa2 = null;

                        if(p2.type == Importable.Type.NDFA)
                        {
                            StatusPanel.Instance().setStatus("Converting NDFA to DFA", 50);
                            NDFA<String> ndfa2 = (NDFA<String>)p2;
                            dfa2 = ndfa2.toDFA().minimaliseerHopcroft();
                        }
                        if(p2.type == Importable.Type.DFA)
                        {
                            dfa2 = (DFA<String>)p2;
                        }
                        if(p2.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to DFA", 50);
                            RegExp regex2 = (RegExp) p2;
                            NDFA<String> ndfa2 = ThompsonConverter.convert(regex2);
                            dfa2 = ndfa2.toDFA().minimaliseerHopcroft();
                        }

                        StatusPanel.Instance().setStatus("Executing and operation", 65);
                        DFA<String> en = dfa1.en(dfa2);

                        StatusPanel.Instance().setStatus("Generating image", 80);

                        File f = Graph.generateImage(en);
                        TabPanel.Instance().addGraph(name1 + " and " + name2, f, en);

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        JButton equalsButton = new JButton("equals");
        equalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BackgroundWorker.instance().addWorker(new BackgroundWorker.Worker() {
                    @Override
                    public void execute() {
                        StatusPanel.Instance().setStatus("Parsing files", 10);

                        String file1 = (String)f1Combo.getSelectedItem();
                        String name1 = file1.replaceAll(".dot", "");
                        String file2 = (String)f2Combo.getSelectedItem();
                        String name2 = file2.replaceAll(".dot", "");

                        Importable p1 = FileParser.read(file1);
                        Importable p2 = FileParser.read(file1);

                        Automata<String> aut = null;

                        if(p1.type == Importable.Type.NDFA )
                        {
                            aut = (NDFA<String>)p1;
                        }
                        if(p1.type == Importable.Type.DFA)
                        {
                            aut = (DFA<String>)p1;
                        }
                        if(p1.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to NDFA", 30);
                            RegExp regex = (RegExp) p1;
                            aut = (NDFA<String>)ThompsonConverter.convert(regex);
                        }

                        Automata<String> aut2 = null;

                        if(p2.type == Importable.Type.NDFA)
                        {
                            aut2 = (NDFA<String>)p2;
                        }
                        if(p2.type == Importable.Type.DFA)
                        {
                            aut2 = (DFA<String>)p2;
                        }
                        if(p2.type == Importable.Type.REGEX)
                        {
                            StatusPanel.Instance().setStatus("Converting REGEX to NDFA", 50);
                            RegExp regex2 = (RegExp) p2;
                            aut2 = (NDFA<String>)ThompsonConverter.convert(regex2);
                        }

                        StatusPanel.Instance().setStatus("Executing equals operation", 80);

                        boolean equals = aut.equals(aut2);

                        new TextPanel(name1 + " equals " + name2, (equals?"They are equal":"They are not equal"));

                        StatusPanel.Instance().setStatus("Done", 100);
                    }
                });
            }
        });

        advPanel.add(f1Combo);
        advPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        advPanel.add(equalsButton);
        advPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        advPanel.add(andButton);
        advPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        advPanel.add(orButton);
        advPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        advPanel.add(f2Combo);
        advPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        this.add(advPanel);
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
