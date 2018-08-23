package ru.mrchebik.helper;

public class StringAction {
    public static String lowerFirstChar(String word) {
        var classChar = word.toCharArray();
        classChar[0] += 32;

        return new String(classChar);
    }
}
