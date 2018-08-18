package ru.mrchebik.language.java.symbols;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SymbolsType {
    MIRROR(new String[]{"{}", "[]", "<>", "()"}),
    SAME(new String[]{"\"\"", "\'\'"}),
    KEYWORDS(new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "var", "void", "volatile", "while"
    }),
    JAVADOC(new String[]{
            "@author", "@version", "@since", "@see", "@param",
            "@return", "@exception", "@throws", "@deprecated",
            "@link", "@value"
    }),
    COMMENT(new String[]{
            "TASK", "TODO"
    });

    @Getter
    private String[] symbols;
}
