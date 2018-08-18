package ru.mrchebik.autocomplete;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.PlainTextChange;
import ru.mrchebik.language.java.symbols.CustomSymbolsType;
import ru.mrchebik.language.java.symbols.SymbolsType;
import ru.mrchebik.model.EditWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Autocomplete extends Popup {
    private final ListView<CodeArea> listOptions;
    private CodeArea codeArea;
    private Stage stage;
    private boolean begin;
    private boolean wasSameSymbol;
    private AtomicInteger index;
    private AtomicInteger maxLength;
    private AutocompleteDatabase database;
    @Getter
    @Setter
    private boolean hideTemporarily = true;

    public Autocomplete(CodeArea codeArea, Stage stage, AutocompleteDatabase database) {
        super();

        this.begin = true;
        this.wasSameSymbol = false;
        this.index = new AtomicInteger();
        this.maxLength = new AtomicInteger();
        this.codeArea = codeArea;
        this.stage = stage;

        this.database = database;

        codeArea.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue)
                hideSnippet();
        });

        listOptions = new ListView<>();
        listOptions.getStylesheets().add("css/snippet.css");
        listOptions.setOnMousePressed(event -> doOption());

        EventHandler<KeyEvent> eventEnterOrTab = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER ||
                    keyEvent.getCode() == KeyCode.TAB)
                doOption();
        };

        listOptions.setOnKeyPressed(eventEnterOrTab);
        getContent().add(listOptions);
    }

    private void setOptions(List<AutocompleteItem> options) {
        listOptions.getItems().clear();
        if (options.size() < 5)
            listOptions.setPrefHeight(options.size() * 16 + options.size() * 4);
        else
            listOptions.setPrefHeight(100);
        options.forEach(this::getMaxLength);
        options.forEach(option -> {
            StringBuilder blank = new StringBuilder();
            for (int i = 0; i < maxLength.get() - option.getText().length(); i++)
                blank.append(" ");
            CodeArea codeArea = new CodeArea(option.getType() + " " + option.getText() + blank + (option.getPackageName().isEmpty() ? " " : " | " + option.getPackageName()));
            codeArea.setPrefHeight(16);
            codeArea.setEditable(false);
            codeArea.getStyleClass().add("list-item");
            codeArea.setAccessibleHelp(String.valueOf(index.getAndIncrement()));
            codeArea.setAccessibleText(option.getPasteText());
            codeArea.setAccessibleRoleDescription(option.getPackageName());
            codeArea.setOnMouseEntered(event -> listOptions.getSelectionModel().select(Integer.parseInt(codeArea.getAccessibleHelp())));
            codeArea.setOnMousePressed(event -> doOption());
            codeArea.addEventFilter(ScrollEvent.ANY, e -> {
                ScrollBar verticalBar = (ScrollBar) listOptions.lookup(".scroll-bar:vertical");
                if (e.getDeltaY() < 0) {
                    verticalBar.increment();
                    verticalBar.increment();
                    verticalBar.increment();
                    verticalBar.increment();
                } else {
                    verticalBar.decrement();
                    verticalBar.decrement();
                    verticalBar.decrement();
                    verticalBar.decrement();
                }
            });
            listOptions.getItems().add(codeArea);
        });
        listOptions.getSelectionModel().selectFirst();
        index.set(0);
        maxLength.set(0);
        listOptions.getItems().forEach(this::getMaxLength);
        listOptions.setPrefWidth(maxLength.get() * 8 + 16 + 8 + 16 + 16);
        maxLength.set(0);
    }

    private void getMaxLength(CodeArea area) {
        calcMaxLength(area.getText());
    }

    private void getMaxLength(AutocompleteItem item) {
        calcMaxLength(item.getText());
    }

    private void calcMaxLength(String text) {
        int length = text.length();
        if (maxLength.get() == 0)
            maxLength.set(length);
        else if (length > maxLength.get())
            maxLength.set(length);
    }

    private void doOption() {
        if (!isHideTemporarily()) {
            CodeArea item = listOptions.getSelectionModel().getSelectedItem();
            String inserted = item.getAccessibleText();
            String insertImport = "";
            String packageName = item.getAccessibleRoleDescription();

            codeArea.insertText(EditWord.getEnd(), inserted.substring(EditWord.getEnd() - EditWord.getBegin()));

            int lastPosition = codeArea.getCaretPosition() - (inserted.contains("()") ? 1 : 0);
            if (!packageName.isEmpty() &&
                    !codeArea.getText().contains(packageName)) {
                String text = codeArea.getText();
                int indexImport = text.indexOf("import");
                int indexPackage = text.indexOf("package");
                int indexInsert = 0;

                if (indexImport != -1) {
                    insertImport = "import " + item.getAccessibleRoleDescription() + ";\n";
                    indexInsert = indexImport;
                } else if (indexPackage != -1) {
                    insertImport = "\n\nimport " + item.getAccessibleRoleDescription() + ";\n\n";
                    indexInsert = codeArea.getText().substring(indexPackage).indexOf(";");
                } else
                    insertImport = "import " + item.getAccessibleRoleDescription() + ";\n\n";
                codeArea.insertText(indexInsert, insertImport);
            }
            codeArea.moveTo(lastPosition + insertImport.length());
            hideSnippet();
        }
    }

    public void callSnippet(List<PlainTextChange> changeList) {
        String inserted = changeList.get(0).getInserted();

        if (begin || wasSameSymbol) {
            begin = false;
            wasSameSymbol = false;
            return;
        }

        if (inserted.length() == 1) {
            char firstChar = inserted.charAt(0);
            int position = codeArea.getCaretPosition();
            String nextChar = codeArea.getText(position, codeArea.getLength() < position + 1 ?
                    position
                    :
                    position + 1);

            if (Character.isMirrored(firstChar)) {
                if (Arrays.stream(SymbolsType.MIRROR.getSymbols()).anyMatch(item -> item.endsWith(nextChar))) {
                    String text = codeArea.getText(0, position);
                    char target = Arrays.stream(SymbolsType.MIRROR.getSymbols()).filter(option -> option.endsWith(nextChar)).findFirst().get().charAt(0);
                    boolean isOpened = false;

                    for (int i = 0; i < text.length(); i++)
                        if (text.charAt(i) == target)
                            isOpened = !isOpened;

                    if (!isOpened) {
                        codeArea.deleteText(position, position + 1);
                        codeArea.moveTo(position + 1);
                    } else
                        pasteSimilarSymbol(inserted, SymbolsType.MIRROR.getSymbols(), true);
                } else
                    pasteSimilarSymbol(inserted, SymbolsType.MIRROR.getSymbols(), true);

                return;
            } else if (Arrays.stream(SymbolsType.SAME.getSymbols()).anyMatch(item -> item.contains(inserted))) {
                if (Arrays.stream(SymbolsType.SAME.getSymbols()).anyMatch(item -> item.contains(nextChar))) {
                    String text = codeArea.getText(0, position);
                    char target = Arrays.stream(SymbolsType.SAME.getSymbols()).filter(option -> option.startsWith(nextChar)).findFirst().get().charAt(0);
                    boolean isOpened = false;

                    for (int i = 0; i < text.length(); i++)
                        if (text.charAt(i) == target)
                            isOpened = !isOpened;

                    if (!isOpened) {
                        codeArea.deleteText(position, position + 1);
                        codeArea.moveTo(position + 1);
                    } else
                        pasteSimilarSymbol(inserted, SymbolsType.SAME.getSymbols(), false);
                } else {
                    String text = codeArea.getText(0, position);

                    if (text.length() > 2 &&
                            position - 3 > -1) {
                        String previousPair = text.substring(position - 3, position - 1);
                        if (previousPair.charAt(0) == previousPair.charAt(1) &&
                                previousPair.charAt(0) == inserted.charAt(0))
                            codeArea.insertText(position - 2, "\\");
                        else {
                            char target = Arrays.stream(SymbolsType.SAME.getSymbols()).filter(option -> option.startsWith(inserted)).findFirst().get().charAt(0);
                            boolean isOpened = false;

                            for (int i = 0; i < text.length(); i++)
                                if (text.charAt(i) == target)
                                    isOpened = !isOpened;

                            if (!isOpened) {
                                String additionalCondition = codeArea.getText(position, codeArea.getText().length());

                                for (int i = 0; i < additionalCondition.length(); i++)
                                    if (additionalCondition.charAt(i) == target) {
                                        codeArea.insertText(position - 1, "\\");
                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            codeArea.moveTo(position + 1);
                                        }).start();
                                        return;
                                    }
                                pasteSimilarSymbol(inserted, SymbolsType.SAME.getSymbols(), false);
                            } else
                                pasteSimilarSymbol(inserted, SymbolsType.SAME.getSymbols(), false);
                        }
                    } else
                        pasteSimilarSymbol(inserted, SymbolsType.SAME.getSymbols(), false);
                }
                return;
            }
        }

        if (".".equals(inserted) || " ".equals(inserted) || CustomSymbolsType.TAB.getCustom().equals(inserted) || !inserted.isEmpty() && inserted.charAt(0) == 10) {
            hideSnippet();
        } else {
            if (!inserted.isEmpty() && !" ".equals(inserted)) {
                if (EditWord.getWord().length() == 0) {
                    int caretPos = codeArea.getCaretPosition();
                    if (caretPos == 0) {
                        EditWord.setBegin(0);
                    } else {
                        EditWord.setBegin(caretPos - 1);
                    }
                }
                EditWord.concat(changeList.get(0).getInserted());
            } else {
                if (!EditWord.getWord().isEmpty()) {
                    try {
                        EditWord.remove(changeList.get(0).getRemoved(), codeArea.getCaretPosition());
                    } catch (StringIndexOutOfBoundsException ignored) {
                        EditWord.clear();
                    }
                } else
                    EditWord.clear();
            }

            if (!EditWord.getWord().isEmpty()) {
                List<AutocompleteItem> options = new ArrayList<>();

                List<String> optionsKeywords = database.getKeywords().stream().filter(word -> word.startsWith(EditWord.getWord())).collect(Collectors.toList());
                List<TextPackage> optionsClass = new ArrayList<>();
                List<TextPackage> optionsMethods = new ArrayList<>();
                List<TextPackage> optionsVariables = new ArrayList<>();

                database.getClassList().forEach(classItem -> {
                    String packageName = classItem.getPackageClass() + "." + classItem.getNameClass();

                    if (classItem.getNameClass().startsWith(EditWord.getWord()))
                        optionsClass.add(new TextPackage(classItem.getNameClass(), packageName));
                    classItem.getVariables().forEach(variable -> {
                        if (variable.startsWith(EditWord.getWord()))
                            optionsVariables.add(new TextPackage(variable, packageName));
                    });
                    classItem.getMethods().forEach(method -> {
                        if (method.startsWith(EditWord.getWord()))
                            optionsMethods.add(new TextPackage(method, packageName));
                    });
                });

                optionsKeywords.forEach(item -> {
                    AutocompleteItem autocompleteItem = new AutocompleteItem(" ", item, item + " ", "");
                    options.add(autocompleteItem);
                });

                optionsClass.forEach(item -> {
                    AutocompleteItem autocompleteItem = new AutocompleteItem("?", item.getText(), item.getText(), item.getPackageText());
                    options.add(autocompleteItem);
                });

                optionsVariables.forEach(item -> {
                    AutocompleteItem autocompleteItem = new AutocompleteItem("V", item.getText(), item.getText() + " ", item.getPackageText());
                    options.add(autocompleteItem);
                });

                optionsMethods.forEach(item -> {
                    AutocompleteItem autocompleteItem = new AutocompleteItem("M", item.getText(), item.getText() + "()", item.getPackageText());
                    options.add(autocompleteItem);
                });

                if (!options.isEmpty()) {
                    Bounds bounds = codeArea.caretBoundsProperty().getValue().get();
                    double y = bounds.getMaxY();

                    if (EditWord.getBeginGlobal() == -1) {
                        double x = bounds.getMaxX() - 30;
                        EditWord.setBeginGlobal(x);

                        setX(EditWord.getBeginGlobal());
                    }

                    setY(y);

                    setOptions(options);

                    if (isHideTemporarily()) {
                        show(stage);
                        setHideTemporarily(false);
                    }
                } else
                    hideTemporarily();
            } else
                hideTemporarily();
        }
    }

    private void pasteSimilarSymbol(String symbol, String[] options, boolean mirror) {
        Optional<String> findSame = Arrays.stream(options).filter(option -> option.startsWith(symbol)).findFirst();

        if (findSame.isPresent()) {
            wasSameSymbol = !mirror;
            int position = codeArea.getCaretPosition();
            codeArea.insertText(position, Character.toString(findSame.get().charAt(1)));
            EditWord.clear();
        }
    }

    private void hideSnippet() {
        EditWord.clear();
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
        boolean isOutRange = EditWord.isOutRange(position);
        if (isOutRange)
            hideSnippet();
    }
}
