package ru.mrchebik.injection;

import com.airhacks.afterburner.injection.Injector;
import ru.mrchebik.helper.StringAction;

import java.util.Arrays;
import java.util.HashMap;

public class Injection {
    public static void initInjection(Object... objects) {
        var injections = new HashMap<>();
        Arrays.stream(objects).forEach(object -> {
            var className = object.getClass().getSimpleName();
            var modifName = StringAction.lowerFirstChar(className);
            injections.put(modifName, object);
        });
        Injector.setConfigurationSource(injections::get);
    }
}
