package ru.mrchebik.autocomplete;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteClass {
    @Getter @Setter
    private String packageClass;
    @Getter @Setter
    private String nameClass;
    @Getter
    private List<String> variables;
    @Getter
    private List<String> methods;

    public AutocompleteClass() {
        variables = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public void addVariable(String name) {
        variables.add(name);
    }

    public void addMethod(String name) {
        methods.add(name);
    }
}
