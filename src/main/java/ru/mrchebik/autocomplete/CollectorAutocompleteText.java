package ru.mrchebik.autocomplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Attempt to reduce memory usage with
 * content `String` on `AutocompleteItem` by id.
 * -----------
 * The result: < 20 Mb
 * Was ~245 Mb
 * Now ~225 Mb
 *
 * @since 0.3.2
 */
public class CollectorAutocompleteText {
    // TODO parameters to int
    public static String[] flag;
    public static List<String> packageName;
    public static List<String> returnTypeS;

    static {
        flag = new String[]{" ", "A", "C", "I", "M", "V", "?"};
        packageName = new ArrayList<>();
        returnTypeS = new ArrayList<>();
    }

    public static int addPackageName(String text) {
        return addToList(packageName, text);
    }

    public static int addReturnTypeS(String text) {
        return addToList(returnTypeS, text);
    }

    private static int addToList(List<String> list, String text) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equals(text))
                return i;
        list.add(text);

        return list.size() - 1;
    }
}
