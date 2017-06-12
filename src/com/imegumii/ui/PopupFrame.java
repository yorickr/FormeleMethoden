package com.imegumii.ui;

import com.imegumii.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kenny on 12-6-2017.
 */
public class PopupFrame extends JFrame {

    public PopupFrame(String name, RegExp r)
    {
        super(name);

        this.setSize(600, 600);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DFA<String> minimizedDfa = ThompsonConverter.convert(r).toDFA().minimaliseer();

        JPanel container = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel acceptPanel = new JPanel();
        acceptPanel.setLayout(new BoxLayout(acceptPanel, BoxLayout.LINE_AXIS));
        acceptPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel generatePanel = new JPanel();
        generatePanel.setLayout(new BoxLayout(generatePanel, BoxLayout.LINE_AXIS));
        generatePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JTextField operaties = new JTextField();
        JTextField lengte = new JTextField();

        JButton generateButton = new JButton("Generate taal");
//        JButton cancelButton = new JButton("Cancel operation");

        JTextField acceptString = new JTextField();

        JButton acceptButton = new JButton("Accept string");

        generateButton.addActionListener(e -> {
            textArea.setText(""); // reset
            BackgroundWorker.instance().addWorker(() -> {
                long currentTime = System.currentTimeMillis();
                Taal t = r.getTaal(Integer.parseInt(operaties.getText()), Integer.parseInt(lengte.getText()));
                textArea.append("The language contains " + t.getSymbols().size() + " strings\n");
                textArea.append("It took " + (System.currentTimeMillis() - currentTime) + " milliseconds to generate and filter them\n");
                t.getSymbols().forEach(s -> textArea.append("The language contains: " + s + "\n"));
            });
        });

//        cancelButton.addActionListener(e -> {
//            BackgroundWorker.instance().cancel();
//        });

        acceptButton.addActionListener(e -> {
            textArea.setText("");
            BackgroundWorker.instance().addWorker(() -> {
                textArea.append("Does the regex accept " + acceptString.getText() + " ? " + (minimizedDfa.accepteer(acceptString.getText()) ? "yes" : "no"));
            });
        });

        operaties.setText("4");
        lengte.setText("100");

        container.add(scrollPane, BorderLayout.CENTER);

        generatePanel.add(generateButton);
        generatePanel.add(Box.createRigidArea(new Dimension(10,0)));

//        generatePanel.add(cancelButton);
//        generatePanel.add(Box.createRigidArea(new Dimension(10,0)));

        generatePanel.add(new JLabel("Max operations"));
        generatePanel.add(Box.createRigidArea(new Dimension(10,0)));

        generatePanel.add(operaties);
        generatePanel.add(Box.createRigidArea(new Dimension(10,0)));

        generatePanel.add(new JLabel("Max length"));
        generatePanel.add(Box.createRigidArea(new Dimension(10,0)));

        generatePanel.add(lengte);

        acceptPanel.add(acceptButton);
        acceptPanel.add(Box.createRigidArea(new Dimension(10,0)));

        acceptPanel.add(new JLabel("Input a string"));
        acceptPanel.add(Box.createRigidArea(new Dimension(10,0)));

        acceptPanel.add(acceptString);
        acceptPanel.add(Box.createRigidArea(new Dimension(10,0)));

        topPanel.add(generatePanel);
        topPanel.add(acceptPanel);

        container.add(topPanel, BorderLayout.NORTH);

        this.setContentPane(container);

        this.setVisible(true);
    }
}
