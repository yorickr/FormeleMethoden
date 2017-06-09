package com.imegumii.ui;

import javax.swing.*;
import java.awt.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kenny on 9-6-2017.
 */
public class StatusPanel extends JPanel {

    private static StatusPanel panel;

    private JLabel status;
    private JProgressBar progress;

    public static StatusPanel Instance()
    {
        if(panel == null)
            panel = new StatusPanel();

        return panel;
    }

    public StatusPanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        progress = new JProgressBar();
        progress.setValue(0);
        progress.setPreferredSize(new Dimension(250, 0));

        status = new JLabel("Application ready");

        this.add(progress);
        this.add(Box.createHorizontalGlue());
        this.add(status);
    }

    public void setStatus(String text, int progress)
    {
        this.progress.setValue(progress);
        this.status.setText(text);
    }

    public void setStatus(int progress)
    {
        this.progress.setValue(progress);
    }

    public void setStatus(String text)
    {
        this.status.setText(text);
    }
}
