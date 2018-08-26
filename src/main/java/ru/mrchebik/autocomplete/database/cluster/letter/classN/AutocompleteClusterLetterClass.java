package ru.mrchebik.autocomplete.database.cluster.letter.classN;

import ru.mrchebik.autocomplete.database.AutocompleteItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutocompleteClusterLetterClass {
    public int name;
    public AutocompleteItem classN;
    public List<AutocompleteItem> items;

    public AutocompleteClusterLetterClass(int name) {
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
            if (item.flag == 2 ||
                    item.flag == 3 ||
                    item.flag == 1 ||
                    item.flag == 6)
                classN = item;
            else
                items.add(item);
    }
}
