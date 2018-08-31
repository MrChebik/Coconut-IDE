package ru.mrchebik.util;

import ru.mrchebik.inject.Injector;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сontains algorithms that return a String.
 */
public class StringUtil {
    /**
     * @param max
     *        The maximum length of text.
     *
     * @param text
     *        One of the texts used to calculate the maximum length.
     *
     * @return
     *        Returns an empty string if the difference is less than or equal to 0.
     *        Otherwise, a string with spaces.
     *
     * @see   ru.mrchebik.arguments.type.ArgumentsType#printInfo
     */
    public static String getSpaces(AtomicInteger max,
                                   String text) {
        var diff = max.get() - text.length();

        return (diff <= 0 ?
                ""
                :
                String.format("%" + diff + "s", ""));
    }

    /**
     * @param word
     * @return
     *        Word starting with a small letter.
     *
     * @see Injector#toName_Object(Object)
     */
    public static String lowerFirstChar(String word) {
        var nameChr = word.toCharArray();
        nameChr[0] += 32;

        return new String(nameChr);
    }
}
