package ru.mrchebik.model;

import lombok.Getter;
import lombok.Setter;

public class EditWord {
    @Getter
    private String word;
    @Getter @Setter
    private int begin;
    @Getter
    private int end;
    @Getter @Setter
    private double beginGlobal;

    public EditWord() {
        word = "";
        beginGlobal = -1;
    }

    public void clear() {
        word = "";
        if (end != -1) {
            begin = end + 1;
        } else {
            begin = -1;
            end = -1;
        }
        beginGlobal = -1;
    }

    public void concat(String fragment) {
        word = word.concat(fragment);
        end = begin + word.length();
    }

    public void remove(String fragment, int caretPos) {
        word = word.substring(0, caretPos - begin) + word.substring(caretPos - begin + fragment.length());
        end = begin + word.length();
    }

    public boolean isOutRange(int position) {
        return !"".equals(word) &&
                position < begin || position > end;
    }

    public boolean wordIsEmpty() {
        return "".equals(word);
    }
}
