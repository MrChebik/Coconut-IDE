package ru.mrchebik.autocomplete;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AutocompleteItem {
    public String flag;
    public String text;
    public String packageName;

    public String classN;

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
