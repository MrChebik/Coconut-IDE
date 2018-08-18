package ru.mrchebik.autocomplete;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AutocompleteDatabase {
    @Getter
    private List<AutocompleteClass> classList;
    @Getter
    @Setter
    private List<String> keywords;

    private AtomicInteger index;

    public AutocompleteDatabase() {
        classList = new ArrayList<>();
        index = new AtomicInteger();
    }

    public void addClass(AutocompleteClass autocompleteClass) {
        for (AutocompleteClass item : classList) {
            if (item.getNameClass().equals(autocompleteClass.getNameClass()) &&
                    item.getPackageClass().equals(autocompleteClass.getPackageClass())) {
                classList.remove(index.get());
                break;
            }
            index.incrementAndGet();
        }
        classList.add(autocompleteClass);

        index.set(0);
    }
}
