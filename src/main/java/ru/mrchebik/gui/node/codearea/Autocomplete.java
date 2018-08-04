package ru.mrchebik.gui.node.codearea;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.PlainTextChange;
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

    @Getter @Setter
    private boolean hideTemporarily = true;

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

    private void setOptions(List<String> options) {
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
        if (!isHideTemporarily()) {
            codeArea.deleteText(editWord.getBegin(), editWord.getEnd());
            codeArea.insertText(editWord.getBegin(), listOptions.getSelectionModel().getSelectedItem().getText() + " ");
            hideSnippet();
        }
    }

    public void callSnippet(List<PlainTextChange> changeList) {
        String inserted = changeList.get(0).getInserted();

        if (begin) {
            begin = false;
            return;
        }

        if (" ".equals(inserted) || "    ".equals(inserted) || inserted.length() != 0 && inserted.charAt(0) == 10) {
            hideSnippet();
        } else {
            if (!" ".equals(inserted)) {
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

            if (!editWord.wordIsEmpty()) {
                List<String> options = Stream.of(Highlight.getKEYWORDS()).filter(word -> word.startsWith(editWord.getWord())).collect(Collectors.toList());

                if (!options.isEmpty()) {
                    if (editWord.getBeginGlobal() == -1) {
                        Bounds bounds = codeArea.caretBoundsProperty().getValue().get();
                        double x = bounds.getMaxX() - 13.7;
                        double y = bounds.getMaxY();
                        editWord.setBeginGlobal(x);

                        setX(editWord.getBeginGlobal());
                        setY(y);
                    }

                    setOptions(options);

                    if (isHideTemporarily()) {
                        show(stage);
                        setHideTemporarily(false);
                    }
                } else {
                    hideTemporarily();
                }
            }
        }
    }

    private void hideSnippet() {
        editWord.clear();
        hideTemporarily();
    }

    private void hideTemporarily() {
        if (!isHideTemporarily()) {
            hide();
            setHideTemporarily(true);
        }
    }

    public void checkCaretPosition() {
        int position = codeArea.getCaretPosition();
        boolean isOutRange = editWord.isOutRange(position);
        if (isOutRange) {
            hideSnippet();
        }
    }
}