package ru.mrchebik.arguments;

import lombok.AllArgsConstructor;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.project.VersionType;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@AllArgsConstructor
public enum ArgumentsType {
    CI("NONE", "--ci",
            "Continuous Integration testing", ContinuousIntegration::init),
    HELP("-h", "--help",
            "Show commands", () -> {
        System.out.println("Available commands:");
        printInfo();
        System.exit(0);
    }),
    VERSION("-v", "--version",
            "Print current version / his stage build / branch of project",
            () -> {
        System.out.print(VersionType.IDE);
        System.exit(0);
    });

    private String brief;
    private String full;
    private String info;
    private Runnable runnable;

    public static void find(String arg) {
        Arrays.stream(ArgumentsType.values())
                .filter(item -> {
                    boolean isMatch = item.brief.equals(arg) ||
                                        item.full.equals(arg);

                    if (isMatch) {
                        item.runnable.run();
                    }

                    return isMatch;
                })
                .findFirst();
    }

    private static void printInfo() {
        var longestBrief = new AtomicInteger(-1);
        var longestFull  = new AtomicInteger(-1);

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    int currBrief;
                    if ("NONE".equals(item.brief)) {
                        currBrief = 0;
                    } else {
                        currBrief = item.brief.length();
                    }
                    var currFull  = item.full .length();

                    if (longestBrief.get() < currBrief) {
                        longestBrief.set(currBrief);
                    }
                    if (longestFull.get() < currFull) {
                        longestFull.set(currFull);
                    }
                });

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    int differenceBrief;
                    if ("NONE".equals(item.brief)) {
                        differenceBrief = longestBrief.get();
                    } else {
                        differenceBrief = longestBrief.get() - item.brief.length();
                    }
                    int differenceFull = longestFull.get() - item.full.length();

                    var briefSpace = new StringBuilder();
                    var  fullSpace = new StringBuilder();
                    IntStream.range(0, differenceBrief).forEach(item1 -> briefSpace.append(' '));
                    IntStream.range(0, differenceFull + 1).forEach(item1 -> fullSpace.append(' '));

                    System.out.println(("NONE".equals(item.brief) ?
                            briefSpace + "  "
                                    :
                            item.brief + ", " + briefSpace) + item.full + fullSpace + item.info);
                });
    }
}
