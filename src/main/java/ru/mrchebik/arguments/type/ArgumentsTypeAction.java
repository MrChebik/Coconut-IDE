package ru.mrchebik.arguments.type;

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
        ArgumentsType.longestBrief = new AtomicInteger();
        ArgumentsType.longestFull = new AtomicInteger();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    longest(ArgumentsType.longestBrief, item.brief);
                    longest(ArgumentsType.longestFull, item.full);
                });
    }

    private static void longest(AtomicInteger atomic,
                                String argument) {
        int length = argument.length();

        if (atomic.get() < length)
            atomic.set(length);
    }
}
