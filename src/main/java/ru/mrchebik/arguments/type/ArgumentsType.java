package ru.mrchebik.arguments.type;

import lombok.AllArgsConstructor;
import ru.mrchebik.algorithm.AlgorithmString;
import ru.mrchebik.ci.ContinuousIntegration;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.LocaleType;
import ru.mrchebik.project.VersionType;
import ru.mrchebik.settings.PropertyCollector;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public enum ArgumentsType {
    CI     ("", "--ci",
            "Starting in mode `Continuous Integration` testing.",
            ContinuousIntegration::init),

    HELP   ("-h", "--help",
            "Show available commands.",
            ArgumentsType::doHelp),

    LOCALE ("", "--locale=[" + LocaleType.getAll() + "]",
            "Changing the interface language.",
            ArgumentsType::doLocale),

    VERSION("-v", "--version",
            "Print version of Coconut-IDE.",
            ArgumentsType::doVersion);

    static AtomicInteger longestBrief;
    static AtomicInteger longestFull;
    String brief;
    String full;
    String info;
    ArgumentsTypeRunnerWrapper runnable;

    public static void search(String arg) {
        Arrays.stream(ArgumentsType.values())
                .filter(item -> {
                    var equalIndex = item.full.indexOf("=");
                    var withoutEqual = item.full.split("=")[0];
                    var isMatch = item.brief.equals(arg) ||
                            arg.startsWith(withoutEqual);

                    if (isMatch) item.runnable.run(arg.substring(equalIndex + 1));

                    return isMatch;
                })
                .findFirst();
    }

    private static void doHelp(String ignored) {
        System.out.println("Available arguments:");
        printInfo();
        System.exit(0);
    }

    private static void doLocale(String value) {
        PropertyCollector.writeProperty("locale", LocaleType.find(value).toString());
        Locale.reset();
    }

    private static void doVersion(String ignored) {
        System.out.println(VersionType.IDE);
        System.exit(0);
    }

    private static void printInfo() {
        ArgumentsTypeAction.initLongest();

        Arrays.stream(ArgumentsType.values())
                .forEach(item -> {
                    var briefSpace = AlgorithmString.getSpaces(longestBrief, item.brief);
                    var briefAfter = ArgumentsTypeAction.initAfterBrief(item.brief);
                    var fullSpace = AlgorithmString.getSpaces(longestFull, item.full);

                    System.out.println("  " + briefSpace + item.brief + briefAfter + item.full + fullSpace + "   " + item.info);
                });
    }
}
