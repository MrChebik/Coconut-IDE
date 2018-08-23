package ru.mrchebik.autocomplete.database.cluster.letter.classN;

import ru.mrchebik.autocomplete.AutocompleteItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutocompleteClusterLetterClass {
    public String name;
    public AutocompleteItem classN;
    public List<AutocompleteItem> items;

    public AutocompleteClusterLetterClass(String name) {
        this.name = name;

        items = new ArrayList<>();
    }

    public void addItem(AutocompleteItem item, boolean isNew) {
        Optional<AutocompleteItem> itemOptional = isNew ?
                Optional.empty()
                :
                items.stream()
                        .filter(o -> o.text.equals(item.text))
                        .findFirst();

        if (!itemOptional.isPresent())
            if ("C".equals(item.flag) ||
                    "I".equals(item.flag) ||
                    "A".equals(item.flag) ||
                    "?".equals(item.flag))
                classN = item;
            else
                items.add(item);
    }
}
