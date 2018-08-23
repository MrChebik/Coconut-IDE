package ru.mrchebik.arguments;

import lombok.AllArgsConstructor;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.VersionType;
import ru.mrchebik.settings.PropertyCollector;

import java.util.Arrays;
import java.util.Optional;
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
            "Print current version / branch of project",
            () -> {
                System.out.println(VersionType.IDE.toStringFull());
                System.exit(0);
            }),
    LOCALE("", "--locale=[value]",
            "Changing the interface language. Default value: 'en'", () -> {
    });

    static AtomicInteger longestBrief;
    static AtomicInteger longestFull;
    String brief;
    String full;
    String info;
    Runnable runnable;

    static void search(String arg) {
        Optional<ArgumentsType> type = Arrays.stream(ArgumentsType.values())
                .filter(item -> {
                    boolean isMatch = item.brief.equals(arg) ||
                            item.full.length() > 8 && item.full.startsWith("--locale=") ?
                            arg.startsWith(item.full.substring(0, 9))
                            :
                            item.full.equals(arg);

                    if (isMatch)
                        item.runnable.run();

                    return isMatch;
                })
                .findFirst();

        if (type.isPresent()) {
            if (type.get().full.startsWith("--locale=")) {
                PropertyCollector.writeProperty("locale", arg.substring(9));
                Locale.reset();
            }
        } else {
            System.err.println("[ERROR][Argument]: A non-existent argument \"" + arg + "\"");
            System.exit(2);
        }
    }

    private static void printInfo() {
        ArgumentsTypeAction.initLongest();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    var briefSpace = ArgumentsTypeAction.initSpaces(longestBrief, item.brief);
                    var briefAfter = ArgumentsTypeAction.initAfterBrief(item.brief);
                    var fullSpace = ArgumentsTypeAction.initSpaces(longestFull, item.full);

                    System.out.println(briefSpace + briefAfter + fullSpace + "   " + item.info);
                });
    }
}
