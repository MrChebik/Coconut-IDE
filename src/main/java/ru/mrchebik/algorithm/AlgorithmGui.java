package ru.mrchebik.algorithm;

import javafx.scene.control.Tab;
import org.fxmisc.flowless.ScaledVirtualized;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;

public class AlgorithmGui {
    /**
     * @param tab
     * @return
     *        An CodeArea by Tab.
     *
     * @see ru.mrchebik.process.save.SaveTabs#run
     */
    public static CodeArea getCodeAreaByTab(Tab tab) {
        var scroll = (VirtualizedScrollPane) tab.getContent();
        var scaled = (ScaledVirtualized) scroll.getContent();

        return (CustomCodeArea) scaled.getChildrenUnmodifiable().get(0);
    }
}
