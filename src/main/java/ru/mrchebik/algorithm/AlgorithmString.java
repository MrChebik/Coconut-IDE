package ru.mrchebik.algorithm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сontains algorithms that return a String.
 */
public class AlgorithmString {
    /**
     * @param max
     *        The maximum length of text.
     * @param text
     *        One of the texts used to calculate the maximum length
     * @return
     *        Returns an empty string if the difference is less than or equal to 0.
     *        Otherwise, a string with spaces.
     * @see   ru.mrchebik.arguments.ArgumentsType#printInfo
     * @since 0.3.2
     */
    public static String initSpaces(AtomicInteger max,
                                    String text) {
        int diff = max.get() - text.length();

        return (diff <= 0 ?
                ""
                :
                String.format("%" + diff + "s", ""));
    }
}
