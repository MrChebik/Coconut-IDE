package ru.mrchebik.autocomplete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.mrchebik.autocomplete.database.cluster.letter.classN.AutocompleteClusterLetterClass;

@RequiredArgsConstructor
public class AutocompleteItem {
    @NonNull
    public String flag;
    @NonNull
    public String text;
    @NonNull
    public String packageName;
    @NonNull
    public String classN;
    @NonNull
    public String returnTypeS;

    public AutocompleteClusterLetterClass returnType;

    @Override
    public String toString() {
        return flag +
                " " +
                text +
                (packageName.length() == 0 ?
                        "  "
                        :
                        " (" + packageName + ")");
    }
}
