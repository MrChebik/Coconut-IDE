package ru.mrchebik.view;

import ru.mrchebik.controller.Compile;
import ru.mrchebik.controller.New;
import ru.mrchebik.controller.Run;
import ru.mrchebik.controller.Save;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mrchebik on 07.05.16.
 */
public class Frame extends JFrame {

    public static JTextArea code = new JTextArea();
    public static JTextArea out = new JTextArea();

    public Frame() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, code, out);
        splitPane.setResizeWeight(1);

        add(splitPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(1, 4));
        add(southPanel, BorderLayout.SOUTH);

        JButton newFile = new JButton("New");
        newFile.addActionListener(e -> New.start());
        southPanel.add(newFile);

        JButton compile = new JButton("Compile");
        compile.addActionListener(e -> Compile.start());
        southPanel.add(compile);

        JButton run = new JButton("Run");
        run.addActionListener(e -> Run.start());
        southPanel.add(run);

        JButton save = new JButton("Save");
        save.addActionListener(e -> Save.start());
        southPanel.add(save);

        setSize(1200, 800);
        splitPane.setDividerLocation((getHeight() - 35) / 2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
