package ru.mrchebik.arguments;

import lombok.AllArgsConstructor;
import ru.mrchebik.algorithm.AlgorithmString;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.VersionType;
import ru.mrchebik.settings.PropertyCollector;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

// TODO if error, show help command

@AllArgsConstructor
enum ArgumentsType {
    CI("", "--ci",
            "Starting in mode `Continuous Integration` testing.", ContinuousIntegration::init),
    HELP("-h", "--help",
            "Show available commands.", (value) -> {
        System.out.println("Available arguments:");
        printInfo();
        System.exit(0);
    }),
    VERSION("-v", "--version",
            "Print version of Coconut-IDE",
            (value) -> {
                System.out.println(VersionType.IDE);
                System.exit(0);
            }),
    LOCALE("", "--locale=[value]",
            "Changing the interface language. Default value: 'en'", (value) -> {
        PropertyCollector.writeProperty("locale", value);
        Locale.reset();
    });

    static AtomicInteger longestBrief;
    static AtomicInteger longestFull;
    String brief;
    String full;
    String info;
    ArgumentsTypeRunnerWrapper runnable;

    static void search(String arg) {
        Optional<ArgumentsType> type = Arrays.stream(ArgumentsType.values())
                .filter(item -> {
                    int equalIndex = item.full.indexOf("=");
                    String fullWithoutEqual = item.full.substring(0, equalIndex == -1 ?
                            item.full.length()
                            :
                            equalIndex);
                    boolean isMatch = item.brief.equals(arg) ||
                            arg.startsWith(fullWithoutEqual);

                    if (isMatch)
                        item.runnable.run(arg.substring(equalIndex + 1));

                    return isMatch;
                })
                .findFirst();

        if (!type.isPresent()) {
            System.err.println("[ERROR][Argument]: A non-existent argument \"" + arg + "\"");
            System.exit(2);
        }
    }

    private static void printInfo() {
        ArgumentsTypeAction.initLongest();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    var briefSpace = AlgorithmString.initSpaces(longestBrief, item.brief);
                    var briefAfter = ArgumentsTypeAction.initAfterBrief(item.brief);
                    var fullSpace = AlgorithmString.initSpaces(longestFull, item.full);

                    System.out.println(briefSpace + briefAfter + fullSpace + "   " + item.info);
                });
    }
}
