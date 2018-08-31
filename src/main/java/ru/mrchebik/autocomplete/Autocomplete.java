package ru.mrchebik.autocomplete;

import javafx.event.Event;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import ru.mrchebik.autocomplete.database.AutocompleteDatabase;
import ru.mrchebik.autocomplete.database.AutocompleteItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static org.fxmisc.wellbehaved.event.EventPattern.anyOf;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

// TODO learn level
public class Autocomplete extends Popup implements AutocompleteWrapper {
    protected static CodeArea mainArea;
    protected static CodeArea codeAreaFocused;
    protected static VirtualizedScrollPane scrollPane;

    protected static Stage stage;

    protected static boolean begin;
    protected static boolean wasSameSymbol;
    protected static boolean hideTemporarily = true;

    protected static AtomicInteger maxLength;

    protected static String userCl;

    protected Autocomplete(Stage stage) {
        Autocomplete.stage = stage;

        begin = true;
        wasSameSymbol = false;

        maxLength = new AtomicInteger();

        mainArea = new CodeArea();
        mainArea.setEditable(false);
        mainArea.setPrefWidth(474);
        mainArea.getStylesheets().add("css/snippet.css");
        mainArea.getStyleClass().add("autocomplete");
        // TODO highlight paragraph on mouse moved
        //mainArea.setOnMouseEntered(event -> mainArea.moveTo(, 0));
        mainArea.setOnMouseClicked(event -> doOption());
        mainArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ||
                    event.getCode() == KeyCode.TAB ||
                    event.getCode() == KeyCode.PERIOD)
                doOption();
        });
        mainArea.addEventFilter(ScrollEvent.ANY, e -> {
            ScrollBar verticalBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");

            Runnable action = () -> {
                if (e.getDeltaY() < 0)
                    verticalBar.increment();
                else
                    verticalBar.decrement();
            };
            IntStream.range(0, 4).forEach((i) -> action.run());
        });

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(LEFT),
                        keyPressed(RIGHT)
                )
        );
        Nodes.addInputMap(mainArea, prevent);

        scrollPane = new VirtualizedScrollPane(mainArea);
        getContent().add(scrollPane);
    }

    protected void setOptions(List<AutocompleteItem> options) {
        throw new UnsupportedOperationException();
    }


    protected void doOption() {
        throw new UnsupportedOperationException();
    }

    public void callSnippet(List<PlainTextChange> changeList, CodeArea codeArea) {
        throw new UnsupportedOperationException();
    }


    public void hideSnippet() {
        EditWord.clear();
        AutocompleteDatabase.cache.clear();
        hideTemporarily();
    }

    public void hideTemporarily() {
        if (!hideTemporarily) {
            hide();
            hideTemporarily = true;
        }
    }

    public void checkCaretPosition() {
        int position = codeAreaFocused.getCaretPosition();
        boolean isOutRange = EditWord.isOutRange(position);
        if (isOutRange &&
                !(position > 0 && ".".equals(codeAreaFocused.getText(position - 1, position)) && EditWord.classN != null)) {
            EditWord.classN = null;
            hideSnippet();
        }
    }
}
