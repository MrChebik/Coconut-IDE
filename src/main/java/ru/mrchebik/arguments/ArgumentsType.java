package ru.mrchebik.arguments;

import lombok.AllArgsConstructor;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.helper.arguments.ArgumentsTypeHelper;
import ru.mrchebik.project.VersionType;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public enum ArgumentsType {
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
        System.out.print(VersionType.IDE);
        System.exit(0);
    });

    public String brief;
    public String full;
    public String info;
    public Runnable runnable;

    public static AtomicInteger longestBrief;
    public static AtomicInteger longestFull;

    public static void search(String arg) {
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
        ArgumentsTypeHelper.initLongestArgs();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    var briefSpace = ArgumentsTypeHelper.initSpaces(longestBrief, item.brief);
                    var briefAfter = ArgumentsTypeHelper.initAfter (item.brief);
                    var  fullSpace = ArgumentsTypeHelper.initSpaces(longestFull , item.full );

                    System.out.println(briefSpace + briefAfter + fullSpace + "   " + item.info);
                });
    }
}
