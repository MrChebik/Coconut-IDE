package ru.mrchebik.inject;

import ru.mrchebik.algorithm.AlgorithmString;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Injector {
    public static void initInjection(Object... objects) {
        var map = Arrays.stream(objects)
                .map(Injector::toName_Object)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        com.airhacks.afterburner.injection.Injector.setConfigurationSource(map::get);
    }

    private static Map.Entry toName_Object(Object object) {
        var name = object.getClass().getSimpleName();
        var lower = AlgorithmString.lowerFirstChar(name);

        return Map.entry(lower, object);
    }
}
