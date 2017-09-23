package ru.mrchebik.gui.node;

import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.syntax.Syntax;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.richChanges()
                .filter(ch -> !lastText.equals(this.getText()))
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(20))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.richChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
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
}
