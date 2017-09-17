package ru.mrchebik.gui.updater.tab;

import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import ru.mrchebik.model.syntax.Highlight;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by mrchebik on 9/16/17.
 */
public class CodePlace extends CodeArea {
    private Executor executor;
    private Highlight highlight;

    CodePlace(String text, Highlight highlight) {
        executor = Executors.newSingleThreadExecutor();
        this.highlight = highlight;

        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(20))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.richChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
        this.replaceText(0, 0, text);
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

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
    }
}
