package ru.mrchebik.language.java.autocomplete;

import com.github.javaparser.ParseProblemException;
import javafx.geometry.Bounds;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.PlainTextChange;
import ru.mrchebik.autocomplete.Autocomplete;
import ru.mrchebik.autocomplete.AutocompleteWrapper;
import ru.mrchebik.autocomplete.EditWord;
import ru.mrchebik.autocomplete.database.AutocompleteDatabase;
import ru.mrchebik.autocomplete.database.AutocompleteItem;
import ru.mrchebik.language.java.autocomplete.analyser.JavaAutocompleteAnalyser;
import ru.mrchebik.language.java.symbols.CustomSymbolsType;
import ru.mrchebik.language.java.symbols.SymbolsType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaAutocomplete extends Autocomplete implements AutocompleteWrapper {

    public JavaAutocomplete(Stage stage) {
        super(stage);
    }

    @Override
    protected void setOptions(List<AutocompleteItem> options) {
        mainArea.clear();

        options.forEach(option -> {
            if (option.packageName == 0) {
                String result = "";
                String text = option.toString();
                int diff = 57 - text.length() - option.getReturnTypeS().length();
                String parameters = diff <= 0 ?
                        option.parameters.split(", ")[0].contains(" ") ?
                                deliver(option.parameters.split(", "))
                                :
                                ""
                        :
                        option.parameters;
                result = option.getFlag() +
                        " " +
                        option.text +
                        (option.flag == 2 || option.flag == 4 ?
                                "(" + parameters + ")"
                                :
                                "");
                diff = 57 - result.length() - option.getReturnTypeS().length();

                String space = diff == 0 ?
                        ""
                        :
                        String.format("%" + diff + "s", "");

                mainArea.appendText(result + space + option.getReturnTypeS() + "\n");
            } else
                mainArea.appendText(option.toString() + "\n");
        });
        maxLength.set(0);

        mainArea.deletePreviousChar();
        mainArea.moveTo(0);
        mainArea.requestFollowCaret();

        mainArea.setPrefHeight(options.size() < 10 ?
                options.size() * 16 + options.size() * 4
                :
                200);

        AutocompleteDatabase.cache = options;
    }

    @Override
    public void doOption() {
        if (!hideTemporarily) {
            AutocompleteItem item = AutocompleteDatabase.cache.get(mainArea.getCurrentParagraph());

            // TODO invisible
            String CLASS_NAME = item.text;
            EditWord.classN = item.returnType;
            AutocompleteDatabase.cache.clear();

            codeAreaFocused.insertText(EditWord.end, item.text.substring(EditWord.end - EditWord.begin));

            String packageName = item.getPackageName();
            String insertImport = "";
            int lastPosition = codeAreaFocused.getCaretPosition() - (item.text.contains("()") ? 1 : 0);
            if (!packageName.isEmpty() &&
                    !packageName.equals("java.lang") &&
                    !codeAreaFocused.getText().contains(packageName)) {
                String text = codeAreaFocused.getText();
                int indexImport = text.indexOf("import");
                int indexPackage = text.indexOf("package");
                int indexInsert = 0;

                if (indexImport != -1) {
                    insertImport = "import " + packageName + "." + CLASS_NAME + ";\n";
                    indexInsert = indexImport;
                } else if (indexPackage != -1) {
                    insertImport = "\n\nimport " + packageName + "." + CLASS_NAME + ";\n\n";
                    indexInsert = codeAreaFocused.getText().substring(indexPackage).indexOf(";");
                } else
                    insertImport = "import " + packageName + "." + CLASS_NAME + ";\n\n";
                codeAreaFocused.insertText(indexInsert, insertImport);
            }
            codeAreaFocused.moveTo(lastPosition + insertImport.length());
            hideSnippet();
        }
    }

    @Override
    public void callSnippet(List<PlainTextChange> changeList, CodeArea codeArea) {
        codeAreaFocused = codeArea;
        try {
            userCl = JavaAutocompleteAnalyser.RAW.parse(codeAreaFocused.getText()).getType(0).getNameAsString();
        } catch (ParseProblemException ignored) {
        }

        String inserted = changeList.get(0).getInserted();

        if (begin || wasSameSymbol) {
            begin = false;
            wasSameSymbol = false;
            return;
        }

        if (inserted.length() == 1) {
            char firstChar = inserted.charAt(0);
            int position = codeAreaFocused.getCaretPosition();
            String nextChar = codeAreaFocused.getText(position, codeAreaFocused.getLength() < position + 1 ?
                    position
                    :
                    position + 1);

            if (Character.isMirrored(firstChar)) {
                if (Arrays.stream(SymbolsType.MIRROR.getSymbols()).anyMatch(item -> item.endsWith(nextChar))) {
                    String text = codeAreaFocused.getText(0, position);
                    char target = Arrays.stream(SymbolsType.MIRROR.getSymbols()).filter(option -> option.endsWith(nextChar)).findFirst().get().charAt(0);
                    boolean isOpened = false;

                    for (int i = 0; i < text.length(); i++)
                        if (text.charAt(i) == target)
                            isOpened = !isOpened;

                    if (!isOpened && position < codeAreaFocused.getText().length()) {
                        codeAreaFocused.deleteText(position, position + 1);
                        codeAreaFocused.moveTo(position + 1);
                    } else
                        pasteSimilarSymbol(inserted, SymbolsType.MIRROR.getSymbols(), true);
                } else
                    pasteSimilarSymbol(inserted, SymbolsType.MIRROR.getSymbols(), true);

                return;
            } else if (Arrays.stream(SymbolsType.SAME.getSymbols()).anyMatch(item -> item.contains(inserted))) {
                if (Arrays.stream(SymbolsType.SAME.getSymbols()).anyMatch(item -> item.contains(nextChar))) {
                    String text = codeAreaFocused.getText(0, position);
                    char target = Arrays.stream(SymbolsType.SAME.getSymbols()).filter(option -> option.startsWith(nextChar)).findFirst().get().charAt(0);
                    boolean isOpened = false;

                    for (int i = 0; i < text.length(); i++)
                        if (text.charAt(i) == target)
                            isOpened = !isOpened;

                    if (!isOpened) {
                        codeAreaFocused.deleteText(position, position + 1);
                        codeAreaFocused.moveTo(position + 1);
                    } else
                        pasteSimilarSymbol(inserted, SymbolsType.SAME.getSymbols(), false);
                } else {
                    String text = codeAreaFocused.getText(0, position);

                    if (text.length() > 2 &&
                            position - 3 > -1) {
                        String previousPair = text.substring(position - 3, position - 1);
                        if (previousPair.charAt(0) == previousPair.charAt(1) &&
                                previousPair.charAt(0) == inserted.charAt(0))
                            codeAreaFocused.insertText(position - 2, "\\");
                        else {
                            char target = Arrays.stream(SymbolsType.SAME.getSymbols()).filter(option -> option.startsWith(inserted)).findFirst().get().charAt(0);
                            boolean isOpened = false;

                            for (int i = 0; i < text.length(); i++)
                                if (text.charAt(i) == target)
                                    isOpened = !isOpened;

                            if (!isOpened) {
                                String additionalCondition = codeAreaFocused.getText(position, codeAreaFocused.getText().length());

                                for (int i = 0; i < additionalCondition.length(); i++)
                                    if (additionalCondition.charAt(i) == target) {
                                        codeAreaFocused.insertText(position - 1, "\\");
                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            codeAreaFocused.moveTo(position + 1);
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

        if (".".equals(inserted)) {
            if (EditWord.begin == EditWord.end) {
                EditWord.begin++;
                EditWord.end++;
            }
            if (EditWord.classN != null) {
                List<AutocompleteItem> options = EditWord.classN.items;
                options.sort((a, b) -> {
                    if (a.text.length() != b.text.length())
                        return a.text.length() - b.text.length();
                    if (a.parameters.length() != b.parameters.length())
                        return a.parameters.length() - b.parameters.length();

                    return a.parameters.compareTo(b.parameters);
                });

                if (!options.isEmpty()) {
                    Bounds bounds = codeAreaFocused.caretBoundsProperty().getValue().get();
                    double y = bounds.getMaxY();


                    if (EditWord.beginGlobal == -1) {
                        EditWord.beginGlobal = bounds.getMaxX() - 30;

                        setX(EditWord.beginGlobal);
                    }

                    setY(y);

                    setOptions(options);

                    if (hideTemporarily) {
                        show(stage);
                        hideTemporarily = false;
                    }
                } else
                    hideTemporarily();
            }
        } else if (" ".equals(inserted) || CustomSymbolsType.TAB.getCustom().equals(inserted) || !inserted.isEmpty() && inserted.charAt(0) == 10) {
            EditWord.classN = null;
            hideSnippet();
        } else {
            if (!inserted.isEmpty() && !" ".equals(inserted)) {
                if (EditWord.word.length() == 0) {
                    int caretPos = codeAreaFocused.getCaretPosition();
                    if (caretPos == 0) {
                        EditWord.begin = (0);
                    } else {
                        EditWord.begin = caretPos - 1;
                    }
                }
                EditWord.concat(changeList.get(0).getInserted());
            } else {
                AutocompleteDatabase.cache.clear();
                if (EditWord.word.length() != 0) {
                    try {
                        EditWord.remove(changeList.get(0).getRemoved(), codeAreaFocused.getCaretPosition());
                    } catch (StringIndexOutOfBoundsException ignored) {
                        EditWord.clear();
                    }
                } else {
                    EditWord.clear();
                    EditWord.classN = null;
                }
            }

            if (EditWord.word.length() != 0) {
                List<AutocompleteItem> options = AutocompleteDatabase.searchClusters(userCl);
                options.sort((a, b) -> {
                    if (a.text.length() != b.text.length())
                        return a.text.length() - b.text.length();
                    if (a.parameters.length() != b.parameters.length())
                        return a.parameters.length() - b.parameters.length();

                    return a.parameters.compareTo(b.parameters);
                });

                if (!options.isEmpty() && codeAreaFocused.caretBoundsProperty().getValue().isPresent()) {
                    Bounds bounds = codeAreaFocused.caretBoundsProperty().getValue().get();
                    double y = bounds.getMaxY();

                    if (EditWord.beginGlobal == -1) {
                        EditWord.beginGlobal = bounds.getMaxX() - 30;

                        setX(EditWord.beginGlobal);
                    }

                    setY(y);

                    setOptions(options);

                    if (hideTemporarily) {
                        show(stage);
                        hideTemporarily = false;
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
            int position = codeAreaFocused.getCaretPosition();
            codeAreaFocused.insertText(position, Character.toString(findSame.get().charAt(1)));
            EditWord.clear();
        }
    }

    private String deliver(String[] fragments) {
        return Arrays.stream(fragments)
                .map(fragment -> fragment.substring(0, fragment.indexOf(" ")))
                .collect(Collectors.joining(", "));
    }
}
