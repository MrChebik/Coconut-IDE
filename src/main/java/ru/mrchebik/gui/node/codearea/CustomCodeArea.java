package ru.mrchebik.gui.node.codearea;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.util.Try;
import ru.mrchebik.autocomplete.AnalyzerAutocomplete;
import ru.mrchebik.autocomplete.Autocomplete;
import ru.mrchebik.highlight.pair.ParentsHighlight;
import ru.mrchebik.language.java.highlight.Highlight;
import ru.mrchebik.language.java.highlight.syntax.Syntax;
import ru.mrchebik.language.java.symbols.CustomSymbolsType;
import ru.mrchebik.language.java.symbols.SymbolsType;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.input.KeyCode.*;
import static org.fxmisc.wellbehaved.event.EventPattern.anyOf;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class CustomCodeArea extends CodeArea {
    @Getter
    private CodeArea codeAreaCSS;
    @Getter @Setter
    private String name;

    private Executor executor;
    private Highlight highlight;
    private Syntax syntax;

    private Autocomplete autocomplete;

    public CustomCodeArea(String text, Highlight highlight, Syntax syntax, Stage stage, String name) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;
        this.syntax = syntax;
        this.name = name;

        autocomplete = new Autocomplete(this, stage, AnalyzerAutocomplete.database);

        InputMap<Event> prevent = InputMap.consume(
                anyOf(
                        keyPressed(TAB),
                        keyPressed(ENTER),
                        keyPressed(BACK_SPACE)
                )
        );
        Nodes.addInputMap(this, prevent);

        this.caretPositionProperty().addListener(obs -> autocomplete.checkCaretPosition());

        this.setOnKeyPressed(event -> {
            int position = this.getCaretPosition();

            if (event.getCode() == TAB) {
                position = deleteSelection(position);

                this.insertText(position, CustomSymbolsType.TAB.getCustom());
            } else if (event.getCode() == KeyCode.SEMICOLON) {
                deleteSelection(-1);

                new Thread(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AnalyzerAutocomplete.callAnalysis(this.getText());
                }).start();
            } else if (event.getCode() == ENTER) {
                position = deleteSelection(position);

                this.insertText(position, "\n" + getTabLength(position));
                AnalyzerAutocomplete.callAnalysis(this.getText());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                String paragraph = this.getParagraph(this.getCurrentParagraph()).getText();

                if (paragraph.trim().length() == 0 &&
                        getTabLength(position).equals(paragraph)) {
                    this.deleteText(position - paragraph.length() - 1, position);
                } else if (position >= 4 &&
                        CustomSymbolsType.TAB.getCustom().equals(this.getText(position - 4, position))) {
                    int spaces = 0;

                    for (int i = this.getCaretColumn() - 1; i >= 0; i--) {
                        if (paragraph.charAt(i) == ' ') {
                            spaces++;
                        } else {
                            break;
                        }
                    }

                    if (spaces % 4 == 0) {
                        this.deleteText(position - 4, position);
                    } else {
                        this.deletePreviousChar();
                    }
                } else if (deleteSelection(-1) == -1) {
                    if (this.getText().length() > 1) {
                        String snippet = this.getText(position - 1, position + 1);
                        List<String> mirror = Arrays.asList(SymbolsType.MIRROR.getSymbols());
                        List<String> same = Arrays.asList(SymbolsType.SAME.getSymbols());

                        if (mirror.contains(snippet) ||
                                same.contains(snippet)) {
                            this.deleteNextChar();
                        }
                    }
                    this.deletePreviousChar();
                }
            }
        });

        this.caretPositionProperty().addListener(listener -> ParentsHighlight.compute(this));
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

    private int deleteSelection(int position) {
        IndexRange range = this.getSelection();
        if (range.getLength() != 0) {
            this.deleteText(range.getStart(), range.getEnd());
        }

        return position == -1 ? position : (position == range.getEnd() ? range.getStart() : position);
    }

    private String getTabLength(int position) {
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
        return IntStream.range(0, brackets.size()).mapToObj(i -> CustomSymbolsType.TAB.getCustom()).collect(Collectors.joining());
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeAreaCSS = new CodeArea(this.getText());

        codeAreaCSS.setStyleSpans(0, highlighting);
        //ParentsHighlight.compute(codeAreaCSS);
        syntax.compute(this);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
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
