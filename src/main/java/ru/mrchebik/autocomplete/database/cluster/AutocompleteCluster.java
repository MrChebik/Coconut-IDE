package ru.mrchebik.autocomplete.database.cluster;

import ru.mrchebik.autocomplete.CollectorAutocompleteText;
import ru.mrchebik.autocomplete.EditWord;
import ru.mrchebik.autocomplete.database.AutocompleteItem;
import ru.mrchebik.autocomplete.database.cluster.letter.AutocompleteClusterLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutocompleteCluster {
    public List<AutocompleteClusterLetter> autocompleteClusterLetters;

    public AutocompleteCluster() {
        autocompleteClusterLetters = new ArrayList<>();
    }

    public AutocompleteClusterLetter searchLetter(char letter) {
        Optional<AutocompleteClusterLetter> letterOptional = autocompleteClusterLetters.size() != 0 ?
                autocompleteClusterLetters.stream()
                        .filter(clusterLetter -> clusterLetter.letter == letter)
                        .findFirst()
                :
                Optional.empty();

        AutocompleteClusterLetter letter1 = letterOptional.orElse(new AutocompleteClusterLetter(letter));

        if (!letterOptional.isPresent())
            autocompleteClusterLetters.add(letter1);

        return letter1;
    }

    public List<AutocompleteItem> globalSearch(String wordStart) {
        List<AutocompleteItem> result = new ArrayList<>();

        autocompleteClusterLetters.stream()
                .filter(letter -> letter.letter == wordStart.charAt(0))
                .forEach(letter -> letter.autocompleteClusterLetterClasses.stream()
                        .filter(classN -> CollectorAutocompleteText.returnTypeS.get(classN.name).startsWith(wordStart))
                        .forEach(classN -> result.add(classN.classN)));

        return result;
    }

    public List<AutocompleteItem> normalSearch() {
        return EditWord.classN.items.stream()
                .filter(item -> item.text.startsWith(EditWord.word.toString()))
                .collect(Collectors.toList());
    }
}
