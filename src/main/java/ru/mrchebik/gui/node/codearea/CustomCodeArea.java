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
import ru.mrchebik.process.autocomplete.AnalyzerAutocomplete;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;
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

    public CustomCodeArea(String text, Highlight highlight, Syntax syntax, Stage stage, AnalyzerAutocomplete analyzer, String name) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;
        this.syntax = syntax;
        this.name = name;

        autocomplete = new Autocomplete(this, stage, analyzer.getDatabase());

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(TAB),
                        keyPressed(ENTER)
                )
        );
        Nodes.addInputMap(this, prevent);

        this.caretPositionProperty().addListener(obs -> autocomplete.checkCaretPosition());

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                this.insertText(this.getCaretPosition(), "    ");
            }
            if (event.getCode() == KeyCode.SEMICOLON) {
                new Thread(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    analyzer.callAnalysis(this.getText());
                }).start();
            }
            if (event.getCode() == ENTER) {
                int position = this.getCaretPosition();

                char open = '{';
                char close = '}';
                Stack<Character> brackets = new Stack<>();
                String textToAnalyze = this.getText(0, position);
                for (int i = 0; i < position; i++) {
                    char charToAnalyze = textToAnalyze.charAt(i);
                    if (charToAnalyze == open) {
                        brackets.push(open);
                    } else if (charToAnalyze == close) {
                        brackets.pop();
                    }
                }
                String tabLength = IntStream.range(0, brackets.size()).mapToObj(i -> "    ").collect(Collectors.joining());

                this.insertText(position, "\n" + tabLength);
                analyzer.callAnalysis(this.getText());
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
