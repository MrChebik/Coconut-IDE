package ru.mrchebik.model;

public class Symbols {
    public static final String[] mirrorSymbols = { "{}", "[]", "<>", "()" };
    public static final String[] sameSymbols = { "\"\"", "\'\'" };

    public static final String CUSTOM_TAB = "    ";

    public static final String[] KEYWORDS = new String[]{
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
    };

    public static final String[] JAVADOC_KEYWORDS = new String[]{
            "@author", "@version", "@since", "@see", "@param",
            "@return", "@exception", "@throws", "@deprecated",
            "@link", "@value"
    };

    public static final String[] COMMENT_KEYWORDS = new String[]{
            "TASK", "TODO"
    };
}
