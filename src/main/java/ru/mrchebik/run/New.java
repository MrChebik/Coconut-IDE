package ru.mrchebik.run;

import ru.mrchebik.frame.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mrchebik on 14.05.16.
 */
public class New implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Frame.code.setText(null);
    }

}
