package ru.mrchebik.autocomplete.database;

import ru.mrchebik.autocomplete.AutocompleteItem;
import ru.mrchebik.autocomplete.EditWord;
import ru.mrchebik.autocomplete.database.cluster.AutocompleteCluster;
import ru.mrchebik.language.java.symbols.SymbolsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TODO multi-thread search
 * <p>
 * Priority of searching autocomplete items:
 * 0. Cache
 * 1. User source
 * 2. Plugin / Dependency / Libraries
 * 3. Global - `java` / `java.lang`
 * 4. Keywords
 * 5. Global - `javax`
 * 6. Global - `javafx`
 * 7. Global - others
 * <p>
 * Blacklist of packages for global (beta):
 * 1. com.sun
 * 2. com.oracle
 * 3. sun
 * 4. jdk
 * <p>
 * Every cluster have `Letter` delimiter, which determine
 * the first character of `Class` name.
 *
 * @since 0.3.1
 */
public class AutocompleteDatabase {
    public static List<AutocompleteItem> cache;
    public static List<AutocompleteCluster> clusters;
    public static List<AutocompleteItem> keywords;

    static {
        cache = new ArrayList<>();
        clusters = new ArrayList<>();
        IntStream.range(0, 8).forEach(i -> clusters.add(new AutocompleteCluster()));

        keywords = Arrays.stream(SymbolsType.KEYWORDS.getSymbols())
                .map(word -> new AutocompleteItem(" ", word, "", ""))
                .collect(Collectors.toList());
    }

    public static void addItem(int cluster,
                               AutocompleteItem item,
                               boolean isNew) {
        char letter = item.classN.charAt(0);

        clusters.get(cluster)
                .searchLetter(letter)
                .searchClass(item.classN)
                .addItem(item, isNew);
    }

    public static List<AutocompleteItem> searchClusters() {
        List<AutocompleteItem> result = new ArrayList<>();
        String word = EditWord.word.toString();

        for (int i = 0; i < 8; i++)
            if (i == 0 && cache.size() > 0)
                return doFilter(cache, word);
            else if (i == 4)
                result.addAll(doFilter(keywords, word));
            else {
                AutocompleteCluster cluster = clusters.get(i);

                result.addAll(EditWord.classN == null ?
                        cluster.globalSearch(word)
                        :
                        cluster.normalSearch());
            }

        return result;
    }

    private static List<AutocompleteItem> doFilter(List<AutocompleteItem> items, String word) {
        return items.stream()
                .filter(item -> item.text.startsWith(word))
                .collect(Collectors.toList());
    }
}
