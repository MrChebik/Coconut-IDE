package ru.mrchebik.gui.node;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.RichTextChange;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.util.Try;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.highlight.syntax.Syntax;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    private Executor executor;
    private Highlight highlight;
    private String lastText;
    private Syntax syntax;

    public CustomCodeArea(String text, Highlight highlight, Syntax syntax, String name) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;
        this.syntax = syntax;
        this.lastText = "";
        this.name = name;

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(TAB, SHORTCUT_ANY, SHIFT_ANY)
                )
        );
        Nodes.addInputMap(this, prevent);

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                this.insertText(this.getCaretPosition(), "    ");
            }
        });
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.richChanges()
                .filter(this::isChangeable)
                .successionEnds(Duration.ofMillis(20))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.richChanges())
                .filterMap(this::getOptional)
                .subscribe(this::applyHighlighting);
        this.replaceText(0, 0, text);
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeAreaCSS = new CodeArea(this.getText());

        codeAreaCSS.setStyleSpans(0, highlighting);
        syntax.compute(this);

        lastText = this.getText();
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

    private boolean isChangeable(RichTextChange<Collection<String>, Collection<String>> ch) {
        String text = this.getText();

        return !lastText.equals(text) && !ch.getInserted().equals(ch.getRemoved());
    }
}
