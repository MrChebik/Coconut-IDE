package ru.mrchebik.locale;

import ru.mrchebik.language.Language;
import ru.mrchebik.settings.PropertyCollector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Locale {
    public static LocaleType localeType;

    private static Properties localeProperties;
    private static Properties localeLangProperties;

    static {
        localeProperties = new Properties();
        localeLangProperties = new Properties();

        init();
    }

    private static void init() {
        PropertyCollector.initializeProperties();

        var localeCode = PropertyCollector.locale;
        localeType = LocaleType.find(localeCode);

        initPart(localeType.toString());

        Language.init();
    }

    public static void initPart(String part) {
        Properties properties = part.equals(localeType.toString()) ?
                localeProperties
                :
                localeLangProperties;
        try (InputStream stream = Locale.class.getResourceAsStream("/locale/" + localeType + "/" + part + ".properties")) {
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("[ERROR][Locale]: File \"" + part + "\" of \"" + localeType + "\" language not found!");
            System.exit(1);
        }
    }

    public static String getProperty(String key, boolean locale) {
        Properties properties = locale ?
                localeProperties
                :
                localeLangProperties;

        String result = properties.getProperty(key);

        if (Objects.isNull(result)) {
            System.err.println("[ERROR][Locale]: Key \"" + key + "\" of \"" + localeType + "\" language not found!");
            System.exit(1);
        }

        return result;
    }

    public static void reset() {
        init();

        /*StartPlace.setTitle();
        CreateFilePlace.setTitle();
        CreateFolderPlace.setTitle();
        RenameFilePlace.setTitle();
        RenameFolderPlace.setTitle();
        CreateProjectPlace.setTitle();*/
    }
}
