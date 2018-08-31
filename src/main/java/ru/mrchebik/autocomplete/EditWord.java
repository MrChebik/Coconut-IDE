package ru.mrchebik.autocomplete;

import ru.mrchebik.autocomplete.database.cluster.letter.classN.AutocompleteClusterLetterClass;

public class EditWord {
    public static StringBuilder word;
    public static AutocompleteClusterLetterClass classN;
    public static int begin;
    public static int end;
    public static double beginGlobal;

    static {
        word = new StringBuilder();
        beginGlobal = -1;
    }

    public static void clear() {
        word.setLength(0);
        if (end != -1)
            begin = end;
        else
            begin = -1;
        beginGlobal = -1;
    }

    public static void concat(String fragment) {
        word = word.append(fragment);
        end = begin + word.length();
    }

    public static void remove(String fragment, int caretPos) {
        word.delete(caretPos - begin, caretPos - begin + fragment.length());
        end = begin + word.length();
    }

    public static boolean isOutRange(int position) {
        return word.length() != 0 &&
                position < begin || position > end;
    }
}
