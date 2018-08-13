package ru.mrchebik.model;

import lombok.Getter;
import lombok.Setter;

public class EditWord {
    @Getter
    private static String word;
    @Getter @Setter
    private static int begin;
    @Getter
    private static int end;
    @Getter @Setter
    private static double beginGlobal;

    static {
        word = "";
        beginGlobal = -1;
    }

    public static void clear() {
        word = "";
        if (end != -1)
            begin = end + 1;
        else
            begin = -1;
        beginGlobal = -1;
    }

    public static void concat(String fragment) {
        word = word.concat(fragment);
        end = begin + word.length();
    }

    public static void remove(String fragment, int caretPos) {
        word = word.substring(0, caretPos - begin) + word.substring(caretPos - begin + fragment.length());
        end = begin + word.length();
    }

    public static boolean isOutRange(int position) {
        return !word.isEmpty() &&
                position < begin || position > end;
    }
}
