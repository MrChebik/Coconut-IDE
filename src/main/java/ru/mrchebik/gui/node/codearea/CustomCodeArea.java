package ru.mrchebik.gui.node.codearea;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.util.Try;
import ru.mrchebik.gui.node.codearea.event.CaretPosition;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.highlight.syntax.Syntax;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static javafx.scene.input.KeyCode.*;
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

    private Autocomplete autocomplete;

    public CustomCodeArea(String text, Highlight highlight, Syntax syntax, Stage stage, String name) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;
        this.syntax = syntax;
        this.name = name;

        autocomplete = new Autocomplete(this, stage);

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(TAB)
                )
        );
        Nodes.addInputMap(this, prevent);

        this.caretPositionProperty().addListener(obs -> autocomplete.checkCaretPosition());

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                this.insertText(this.getCaretPosition(), "    ");
            }
        });

        KeyCombination openBracket = new KeyCodeCombination(KeyCode.OPEN_BRACKET);                                 // [
        KeyCombination quote = new KeyCodeCombination(KeyCode.QUOTE);                                              // '
        KeyCombination commaShift = new KeyCodeCombination(KeyCode.COMMA, KeyCombination.SHIFT_DOWN);              // <
        KeyCombination quoteShift = new KeyCodeCombination(KeyCode.QUOTE, KeyCombination.SHIFT_DOWN);              // "
        KeyCombination digit9Shift = new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.SHIFT_DOWN);            // (
        KeyCombination openBracketShift = new KeyCodeCombination(KeyCode.OPEN_BRACKET, KeyCombination.SHIFT_DOWN); // {
        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (openBracket.match(event) ||
                    quote.match(event) ||
                    commaShift.match(event) ||
                    quoteShift.match(event) ||
                    digit9Shift.match(event) ||
                    openBracketShift.match(event)) {
                int position = this.getCaretPosition();
                if (openBracket.match(event)) {
                    this.insertText(position, "]");
                } else if (quote.match(event)) {
                    this.insertText(position, "'");
                } else if (commaShift.match(event)) {
                    this.insertText(position, ">");
                } else if (quoteShift.match(event)){
                    this.insertText(position, "\"");
                } else if (digit9Shift.match(event)) {
                    this.insertText(position, ")");
                } else {
                    this.insertText(position, "}");
                }
                this.moveTo(position);
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
                .subscribe(autocomplete::callSnippet);
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
}
