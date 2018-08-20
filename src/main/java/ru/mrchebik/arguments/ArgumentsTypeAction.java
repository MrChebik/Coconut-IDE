package ru.mrchebik.arguments;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

class ArgumentsTypeAction {
    static String initAfterBrief(String text) {
        return text.isEmpty() ?
                "  "
                :
                ", ";
    }

    static void initLongest() {
        ArgumentsType.longestBrief = new AtomicInteger(-1);
        ArgumentsType.longestFull = new AtomicInteger(-1);

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    longest(ArgumentsType.longestBrief, item.brief);
                    longest(ArgumentsType.longestFull, item.full);
                });
    }

    static String initSpaces(AtomicInteger atomic,
                             String text) {
        int diff = atomic.get() - text.length();

        return text + (diff == 0 ?
                ""
                :
                String.format("%" + diff + "s", ""));
    }

    private static void longest(AtomicInteger atomic,
                                String argument) {
        int length = argument.length();

        if (atomic.get() < length)
            atomic.set(length);
    }
}
