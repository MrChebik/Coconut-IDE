package ru.mrchebik.autocomplete.database;

import ru.mrchebik.autocomplete.CollectorAutocompleteText;
import ru.mrchebik.autocomplete.database.cluster.letter.classN.AutocompleteClusterLetterClass;

/**
 * Contains autocomplete item, that show in `PopUp`
 *
 * @since 0.3.0-a
 */
public class AutocompleteItem {
    /**
     * A - abstract
     * C - class
     * I - interface
     * M - method
     * V - varaible
     * ? - unknown
     */
    public int flag;
    public String text;
    public String parameters;
    public int packageName;
    public int returnTypeS;

    public AutocompleteClusterLetterClass returnType;

    public AutocompleteItem(int flag,
                            String text,
                            String parameters,
                            int returnTypeS) {
        this.flag = flag;
        this.text = text;
        this.parameters = parameters;
        this.returnTypeS = returnTypeS;
    }

    public AutocompleteItem(int flag,
                            String text,
                            String parameters,
                            int packageName,
                            int returnTypeS) {
        this.flag = flag;
        this.text = text;
        this.parameters = parameters;
        this.packageName = packageName;
        this.returnTypeS = returnTypeS;
    }

    @Override
    public String toString() {
        return this.getFlag() +
                " " +
                text +
                (isMethodOrClass() ?
                        parameters.length() > 0 ?
                                "(" + parameters + ")"
                                :
                                flag == 2 ?
                                        ""
                                        :
                                        "  "
                        :
                        nonAbstractAndInterface() ?
                                "  "
                                :
                                "") +
                (this.getPackageName().length() == 0 ?
                        "  "
                        :
                        " (" + this.getPackageName() + ")");
    }

    public boolean nonMethodAndVariable() {
        return flag != 4 && flag != 5;
    }

    public boolean isMethodOrClass() {
        return flag == 4 || flag == 2;
    }

    public boolean nonAbstractAndInterface() {
        return flag != 1 && flag != 3;
    }

    public String getFlag() {
        return CollectorAutocompleteText.flag[flag];
    }

    public String getPackageName() {
        return CollectorAutocompleteText.packageName.get(packageName);
    }

    public String getReturnTypeS() {
        return CollectorAutocompleteText.returnTypeS.get(returnTypeS);
    }
}
