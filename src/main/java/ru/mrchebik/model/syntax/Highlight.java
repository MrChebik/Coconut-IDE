package ru.mrchebik.model.syntax;

import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrchebik on 9/17/17.
 */
public class Highlight {
    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String[] JAVADOC_KEYWORDS = new String[] {
            "@author", "@version", "@since", "@see", "@param",
            "@return", "@exception", "@throws", "@deprecated",
            "@link", "@value"
    };

    private static final String[] COMMENT_KEYWORDS = new String[] {
            "TASK", "TODO"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "[()]";
    private static final String BRACE_PATTERN = "[{}]";
    private static final String BRACKET_PATTERN = "[\\[]]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String NUMBER_PATTERN = "[^\\w]\\d+(\\.\\d+)?[^\\w]";
    private static final String ANNOTATION_PATTERN = "\\s@\\w+";
    private static final String HTML_TAG_PATTERN = "</?[^/>\\n]+>";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_ONE_PATTERN = "//[^\\n]*";
    private static final String COMMENT_MULTI_PATTERN = "/\\*[^\\*](.|\\R)*?\\*/";
    private static final String COMMENT_KEYWORDS_PATTERN = "\\s(" + String.join("|", COMMENT_KEYWORDS) + "):?\\s";
    private static final String JAVADOC_PATTERN = "/\\*\\*(.|\\R)*?\\*/";
    private static final String JAVADOC_KEYWORDS_PATTERN = "\\s(" + String.join("|", JAVADOC_KEYWORDS) + ")\\s";
    private static final String JAVADOC_LINK_PATTERN = "\\s\\{@link\\s[\\w|\\.]+\\}\\s";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<ANNOTATION>" + ANNOTATION_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENTONE>" + COMMENT_ONE_PATTERN + ")"
                    + "|(?<COMMENTMULTI>" + COMMENT_MULTI_PATTERN + ")"
                    + "|(?<JAVADOC>" + JAVADOC_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
    );

    private static final Pattern DOC_PATTERN = Pattern.compile(
            "(?<JAVADOCLINK>" + JAVADOC_LINK_PATTERN + ")"
                    + "|(?<JAVADOCKEY>" + JAVADOC_KEYWORDS_PATTERN + ")"
                    + "|(?<JAVADOCHTML>" + HTML_TAG_PATTERN + ")"
    );

    private int lastKwEnd;
    private Matcher matcher;
    private StyleSpansBuilder<Collection<String>> spansBuilder;

    public StyleSpans<Collection<String>> computeHighlighting(String text) {
        matcher = PATTERN.matcher(text);
        spansBuilder = new StyleSpansBuilder<>();
        lastKwEnd = 0;
        while(matcher.find()) {
            String styleClass = null;
            if (matcher.group("KEYWORD") != null) {
                styleClass = "keyword";
            } else if (matcher.group("PAREN") != null) {
                styleClass = "paren";
            } else if (matcher.group("BRACE") != null) {
                styleClass = "brace";
            } else if (matcher.group("BRACKET") != null) {
                styleClass = "bracket";
            } else if (matcher.group("ANNOTATION") != null) {
                styleClass = "annotation";
            } else if (matcher.group("SEMICOLON") != null) {
                styleClass = "semicolon";
            } else if (matcher.group("NUMBER") != null) {
                styleClass = "number";
            } else if (matcher.group("STRING") != null) {
                styleClass = "string";
            } else if (matcher.group("COMMENTONE") != null) {
                List<GroupClass> groupClasses = new ArrayList<>(Collections.singletonList(new GroupClass(null, "comment-keyword")));
                subCompute(null, COMMENT_KEYWORDS_PATTERN, "COMMENTONE", "comment-one", groupClasses);

                continue;
            } else if (matcher.group("COMMENTMULTI") != null) {
                List<GroupClass> groupClasses = new ArrayList<>(Collections.singletonList(new GroupClass(null, "comment-keyword")));
                subCompute(null, COMMENT_KEYWORDS_PATTERN, "COMMENTMULTI", "comment-multi", groupClasses);

                continue;
            } else if (matcher.group("JAVADOC") != null) {
                List<GroupClass> groupClasses = new ArrayList<>();
                groupClasses.add(new GroupClass("JAVADOCKEY", "javadoc-keyword"));
                groupClasses.add(new GroupClass("JAVADOCHTML", "javadoc-html"));
                groupClasses.add(new GroupClass("JAVADOCLINK", "javadoc-link"));

                subCompute(DOC_PATTERN, null, "JAVADOC", "javadoc", groupClasses);

                continue;
            }
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd + ("number".equals(styleClass) ? 1 : 0));
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start() - ("number".equals(styleClass) ? 2 : 0));
            lastKwEnd = matcher.end() - ("number".equals(styleClass) ? 1 : 0);
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private void subCompute(Pattern pattern, String patternString, String group, String initialClass, List<GroupClass> groupClasses) {
        spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);

        Matcher matcherDoc = pattern != null ?
                pattern.matcher(matcher.group(group))
                :
                Pattern.compile(patternString).matcher(matcher.group(group));

        int lastKwEndDoc = 0;
        while (matcherDoc.find()) {
            String styleClassSub = getClassSubOfGroup(matcherDoc, groupClasses);

            spansBuilder.add(Collections.singleton(initialClass), matcherDoc.start() - lastKwEndDoc);
            spansBuilder.add(Collections.singleton(styleClassSub), matcherDoc.end() - matcherDoc.start());
            lastKwEndDoc = matcherDoc.end();
        }
        spansBuilder.add(Collections.singleton(initialClass), (matcher.end() - matcher.start()) - lastKwEndDoc);

        lastKwEnd = matcher.end();
    }

    private String getClassSubOfGroup(Matcher matcher, List<GroupClass> groupClasses) {
        for (GroupClass groupClass: groupClasses)
            if (groupClass.getGroup() == null || matcher.group(groupClass.getGroup()) != null)
                return groupClass.getThisClass();

        return null;
    }
}
