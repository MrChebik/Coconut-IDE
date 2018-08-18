package ru.mrchebik.arguments;

import lombok.AllArgsConstructor;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.project.VersionType;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
enum ArgumentsType {
    CI("", "--ci",
            "Continuous Integration testing", ContinuousIntegration::init),
    HELP("-h", "--help",
            "Show commands", () -> {
        System.out.println("Available arguments:");
        printInfo();
        System.exit(0);
    }),
    VERSION("-v", "--version",
            "Print current version / his stage build / branch of project",
            () -> {
                System.out.println(VersionType.IDE.toStringFull());
                System.exit(0);
            });

    static AtomicInteger longestBrief;
    static AtomicInteger longestFull;
    String brief;
    String full;
    String info;
    Runnable runnable;

    static void search(String arg) {
        Arrays.stream(ArgumentsType.values())
                .filter(item -> {
                    boolean isMatch = item.brief.equals(arg) ||
                            item.full.equals(arg);

                    if (isMatch)
                        item.runnable.run();

                    return isMatch;
                })
                .findFirst();
    }

    private static void printInfo() {
        ArgumentsTypeHelper.initLongest();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    var briefSpace = ArgumentsTypeHelper.initSpaces(longestBrief, item.brief);
                    var briefAfter = ArgumentsTypeHelper.initAfterBrief(item.brief);
                    var fullSpace = ArgumentsTypeHelper.initSpaces(longestFull, item.full);

                    System.out.println(briefSpace + briefAfter + fullSpace + "   " + item.info);
                });
    }
}
