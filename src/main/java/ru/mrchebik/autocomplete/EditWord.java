package ru.mrchebik.autocomplete;

import ru.mrchebik.autocomplete.database.cluster.letter.classN.AutocompleteClusterLetterClass;

public class EditWord {
    public static StringBuilder word;
    public static AutocompleteClusterLetterClass classN;
    static int begin;
    static int end;
    static double beginGlobal;

    static {
        word = new StringBuilder();
        beginGlobal = -1;
    }

    static void clear() {
        word.setLength(0);
        if (end != -1)
            begin = end;
        else
            begin = -1;
        beginGlobal = -1;
    }

    static void concat(String fragment) {
        word = word.append(fragment);
        end = begin + word.length();
    }

    static void remove(String fragment, int caretPos) {
        word.delete(caretPos - begin, caretPos - begin + fragment.length());
        end = begin + word.length();
    }

    static boolean isOutRange(int position) {
        return word.length() != 0 &&
                position < begin || position > end;
    }
}
