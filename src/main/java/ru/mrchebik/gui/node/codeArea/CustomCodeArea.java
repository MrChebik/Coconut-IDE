package ru.mrchebik.gui.node.codeArea;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStream;
import org.reactfx.util.Try;
import org.reactfx.value.Var;
import ru.mrchebik.gui.node.codeArea.event.CaretPosition;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.highlight.syntax.Syntax;
import ru.mrchebik.model.EditWord;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCombination.SHIFT_ANY;
import static javafx.scene.input.KeyCombination.SHORTCUT_ANY;
import static org.fxmisc.wellbehaved.event.EventPattern.anyOf;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class CustomCodeArea extends CodeArea {
    @Getter
    private CodeArea codeAreaCSS;
    @Getter @Setter
    private String name;

    private CaretPosition caretPosition;
    private Executor executor;
    private Highlight highlight;
    private Syntax syntax;
    private Stage stage;
    private EditWord editWord;
    private boolean begin;

    private BoundsPopup snippet;

    public CustomCodeArea(String text, Highlight highlight, Syntax syntax, Stage stage, String name) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;
        this.syntax = syntax;
        this.name = name;
        this.begin = true;
        this.editWord = new EditWord();
        this.stage = stage;

        snippet = new BoundsPopup();

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(TAB, SHORTCUT_ANY, SHIFT_ANY)
                )
        );
        Nodes.addInputMap(this, prevent);

        this.caretPositionProperty().addListener(obs -> {
            if (!editWord.getWord().equals("")) {
                int position = this.getCaretPosition();
                if (position < editWord.getBegin() ||
                        position > editWord.getEnd()) {
                    editWord.clear();
                    if (!snippet.isHiddenTemporarily()) {
                        snippet.hide();
                        snippet.setHideTemporarily(true);
                    }
                }
            }
        });

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                this.insertText(this.getCaretPosition(), "    ");
            }
        });
        caretPosition = CaretPosition.create();
        this.caretPositionProperty().addListener(listener -> caretPosition.compute(this));
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.multiPlainChanges()
                .successionEnds(Duration.ofMillis(350))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.multiPlainChanges())
                .filterMap(this::getOptional)
                .subscribe(this::applyHighlighting);
        this.multiPlainChanges()
                .subscribe(this::callSnippet);
        this.replaceText(0, 0, text);
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeAreaCSS = new CodeArea(this.getText());

        codeAreaCSS.setStyleSpans(0, highlighting);
        caretPosition.compute(codeAreaCSS);
        syntax.compute(this);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return highlight.computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private Optional<StyleSpans<Collection<String>>> getOptional(Try<StyleSpans<Collection<String>>> t) {
        if (t.isSuccess()) {
            return Optional.of(t.get());
        } else {
            t.getFailure().printStackTrace();
            return Optional.empty();
        }
    }

    private void callSnippet(List<PlainTextChange> changeList) {
        String inserted = changeList.get(0).getInserted();

        if (begin) {
            begin = false;
            return;
        }

        if (inserted.equals(" ") || inserted.equals("    ") || inserted.length() != 0 && inserted.charAt(0) == 10) {
            editWord.clear();
            if (!snippet.isHiddenTemporarily()) {
                snippet.hide();
                snippet.setHideTemporarily(true);
            }
        } else {
            if (!inserted.equals("")) {
                if (editWord.getWord().length() == 0) {
                    int caretPos = this.getCaretPosition();
                    if (caretPos == 0) {
                        editWord.setBegin(0);
                    } else {
                        editWord.setBegin(caretPos - 1);
                    }
                }
                editWord.concat(changeList.get(0).getInserted());
            } else {
                if (editWord.getWord().length() != 0) {
                    editWord.remove(changeList.get(0).getRemoved(), this.getCaretPosition());
                }
            }
            List<String> options = new ArrayList<>();
            if (!editWord.getWord().equals("")) {
                options = Stream.of(Highlight.getKEYWORDS()).filter(word -> word.startsWith(editWord.getWord())).collect(Collectors.toList());
            }
            if (!editWord.getWord().equals("") && !options.isEmpty()) {
                if (editWord.getBeginGlobal() == -1) {
                    Bounds bounds = this.caretBoundsProperty().getValue().get();
                    double x = bounds.getMaxX() - 13.7;
                    double y = bounds.getMaxY();
                    editWord.setBeginGlobal(x);

                    snippet.setX(editWord.getBeginGlobal());
                    snippet.setY(y);
                }

                snippet.setOptions(options);

                if (snippet.isHiddenTemporarily()) {
                    snippet.show(stage);
                    snippet.setHideTemporarily(false);
                }
            } else {
                if (!snippet.isHiddenTemporarily()) {
                    snippet.hide();
                    snippet.setHideTemporarily(true);
                }
            }
        }
    }

    private class BoundsPopup extends Popup {
        /** Indicates whether popup should still be shown even when its item (caret/selection) is outside viewport */
        private final Var<Boolean> showWhenItemOutsideViewport = Var.newSimpleVar(true);

        /** Indicates whether popup has been hidden since its item (caret/selection) is outside viewport
         * and should be shown when that item becomes visible again */
        private final Var<Boolean> hideTemporarily = Var.newSimpleVar(true);
        private boolean isHiddenTemporarily() { return hideTemporarily.getValue(); }
        private void setHideTemporarily(boolean value) { hideTemporarily.setValue(value); }


        private final ScrollPane scrollPane;
        private final ListView<CodeArea> listOptions;

        BoundsPopup() {
            super();
            scrollPane = new ScrollPane();
            scrollPane.setPrefWidth(200);
            scrollPane.setMaxHeight(92);
            scrollPane.setFitToHeight(true);
            listOptions = new ListView<>();
            listOptions.setPrefWidth(198);
            listOptions.setMaxHeight(90);
            listOptions.getStylesheets().add("css/snippet.css");
            scrollPane.setContent(listOptions);

            scrollPane.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER ||
                        event.getCode() == KeyCode.TAB) {
                    doOption();
                }
            });
            listOptions.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER ||
                        event.getCode() == KeyCode.TAB) {
                    doOption();
                }
            });
            getContent().add(scrollPane);
        }

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
    }

    private void doOption() {
        if (!snippet.isHiddenTemporarily()) {
            this.deleteText(editWord.getBegin(), editWord.getEnd());
            this.insertText(editWord.getBegin(), snippet.listOptions.getSelectionModel().getSelectedItem().getText() + " ");
            snippet.hide();
            snippet.setHideTemporarily(true);
            editWord.clear();
        }
    }
}
