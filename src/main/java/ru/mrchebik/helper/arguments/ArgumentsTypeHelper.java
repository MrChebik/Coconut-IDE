package ru.mrchebik.helper.arguments;

import ru.mrchebik.arguments.ArgumentsType;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ArgumentsTypeHelper {
    public static void initLongestArgs() {
        ArgumentsType.longestBrief = new AtomicInteger(-1);
        ArgumentsType.longestFull  = new AtomicInteger(-1);

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    longest(ArgumentsType.longestBrief, item.brief);
                    longest(ArgumentsType.longestFull, item.full);
                });
    }

    private static void longest(AtomicInteger atomic, String argument) {
        int length = argument.length();

        if (atomic.get() < length)
            atomic.set(length);
    }

    public static String initSpaces(AtomicInteger atomic, String text) {
        int diff = atomic.get() - text.length();

        return text + (diff == 0 ?
                    ""
                :
                    String.format("%" + diff + "s", ""));
    }

    public static String initAfter(String text) {
        return text.isEmpty() ?
                    "  "
                :
                    ", ";
    }
}
