package ru.mrchebik.gui.node.codearea;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.PlainTextChange;
import org.reactfx.value.Var;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.model.EditWord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Autocomplete extends Popup {
    private EditWord editWord;

    private CodeArea codeArea;
    private Stage stage;
    private boolean begin;

    private final ScrollPane scrollPane;
    private final ListView<CodeArea> listOptions;

    Autocomplete(CodeArea codeArea, Stage stage) {
        super();

        this.editWord = new EditWord();
        this.begin = true;
        this.codeArea = codeArea;
        this.stage = stage;

        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(200);
        scrollPane.setMaxHeight(92);
        scrollPane.setFitToHeight(true);
        listOptions = new ListView<>();
        listOptions.setPrefWidth(198);
        listOptions.setMaxHeight(90);
        listOptions.getStylesheets().add("css/snippet.css");
        scrollPane.setContent(listOptions);

        EventHandler<KeyEvent> eventEnterOrTab = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER ||
                    keyEvent.getCode() == KeyCode.TAB) {
                doOption();
            }
        };

        scrollPane.setOnKeyPressed(eventEnterOrTab);
        listOptions.setOnKeyPressed(eventEnterOrTab);
        getContent().add(scrollPane);
    }

    /** Indicates whether popup has been hidden since its item (caret/selection) is outside viewport
     * and should be shown when that item becomes visible again */
    private final Var<Boolean> hideTemporarily = Var.newSimpleVar(true);
    private boolean isHiddenTemporarily() { return hideTemporarily.getValue(); }
    private void setHideTemporarily(boolean value) { hideTemporarily.setValue(value); }

    public void setOptions(List<String> options) {
        listOptions.getItems().clear();
        scrollPane.setPrefHeight(options.size()*16 + options.size()*4 + 2);
        listOptions.setPrefHeight(options.size()*16 + options.size()*4);
        options.forEach(option -> {
            CodeArea codeArea = new CodeArea(option);
            codeArea.setPrefHeight(16);
            codeArea.setEditable(false);
            codeArea.setStyle("-fx-background-color: transparent");
            listOptions.getItems().add(codeArea);
        });
        listOptions.getItems().get(0).getStyleClass().add("first-elem");
        listOptions.getSelectionModel().selectFirst();
    }

    private void doOption() {
        if (!isHiddenTemporarily()) {
            codeArea.deleteText(editWord.getBegin(), editWord.getEnd());
            codeArea.insertText(editWord.getBegin(), listOptions.getSelectionModel().getSelectedItem().getText() + " ");
            hideSnippet();
        }
    }

    void callSnippet(List<PlainTextChange> changeList) {
        String inserted = changeList.get(0).getInserted();

        if (begin) {
            begin = false;
            return;
        }

        if (inserted.equals(" ") || inserted.equals("    ") || inserted.length() != 0 && inserted.charAt(0) == 10) {
            hideSnippet();
        } else {
            if (!inserted.equals("")) {
                if (editWord.getWord().length() == 0) {
                    int caretPos = codeArea.getCaretPosition();
                    if (caretPos == 0) {
                        editWord.setBegin(0);
                    } else {
                        editWord.setBegin(caretPos - 1);
                    }
                }
                editWord.concat(changeList.get(0).getInserted());
            } else {
                if (editWord.getWord().length() != 0) {
                    editWord.remove(changeList.get(0).getRemoved(), codeArea.getCaretPosition());
                }
            }
            List<String> options = new ArrayList<>();
            if (!editWord.getWord().equals("")) {
                options = Stream.of(Highlight.getKEYWORDS()).filter(word -> word.startsWith(editWord.getWord())).collect(Collectors.toList());
            }
            if (!editWord.getWord().equals("") && !options.isEmpty()) {
                if (editWord.getBeginGlobal() == -1) {
                    Bounds bounds = codeArea.caretBoundsProperty().getValue().get();
                    double x = bounds.getMaxX() - 13.7;
                    double y = bounds.getMaxY();
                    editWord.setBeginGlobal(x);

                    setX(editWord.getBeginGlobal());
                    setY(y);
                }

                setOptions(options);

                if (isHiddenTemporarily()) {
                    show(stage);
                    setHideTemporarily(false);
                }
            } else {
                hideTemporarily();
            }
        }
    }

    private void hideSnippet() {
        editWord.clear();
        hideTemporarily();
    }

    private void hideTemporarily() {
        if (!isHiddenTemporarily()) {
            hide();
            setHideTemporarily(true);
        }
    }

    void checkCaretPosition() {
        int position = codeArea.getCaretPosition();
        boolean isOutRange = editWord.isOutRange(position);
        if (isOutRange) {
            hideSnippet();
        }
    }
}
