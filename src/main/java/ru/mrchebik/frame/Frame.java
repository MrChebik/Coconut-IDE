package ru.mrchebik.frame;

import ru.mrchebik.run.Compile;
import ru.mrchebik.run.New;
import ru.mrchebik.run.Run;
import ru.mrchebik.run.Save;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mrchebik on 07.05.16.
 */
public class Frame extends JFrame {

    public static JTextArea code = new JTextArea();
    public static JTextArea out = new JTextArea();
    public static JButton save = new JButton("Save");

    public Frame() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, code, out);
        splitPane.setResizeWeight(1);

        add(splitPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(1, 4));
        add(southPanel, BorderLayout.SOUTH);

        JButton newFile = new JButton("New");
        newFile.addActionListener(new New());
        southPanel.add(newFile);

        JButton compile = new JButton("Compile");
        compile.addActionListener(new Compile());
        southPanel.add(compile);

        JButton run = new JButton("Run");
        run.addActionListener(new Run());
        southPanel.add(run);

        save.addActionListener(new Save());
        southPanel.add(save);

        setSize(1200, 800);
        splitPane.setDividerLocation((getHeight() - 35) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
