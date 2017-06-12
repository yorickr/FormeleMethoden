package com.imegumii.ui;

import com.imegumii.Automata;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by kenny on 9-6-2017.
 */
public class TabPanel extends JTabbedPane {

    private static TabPanel panel;

    public static TabPanel Instance()
    {
        if(panel == null)
            panel = new TabPanel();

        return panel;
    }

    private TabPanel()
    {
        super();
    }

    public void addGraph(String name, File image, Automata<String> a)
    {
        this.addTab(name, new GraphPanel(name, image, a));
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    public void removeGraph(GraphPanel component)
    {
        this.remove(component);
    }

}
