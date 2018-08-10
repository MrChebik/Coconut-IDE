package ru.mrchebik.language.java.highlight;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import ru.mrchebik.language.java.symbols.SymbolsType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlight {
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", SymbolsType.KEYWORDS.getSymbols()) + ")\\b";
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
    private static final String COMMENT_KEYWORDS_PATTERN = "\\s(" + String.join("|", SymbolsType.COMMENT.getSymbols()) + "):?\\s";
    private static final String JAVADOC_PATTERN = "/\\*\\*(.|\\R)*?\\*/";
    private static final String JAVADOC_KEYWORDS_PATTERN = "\\s(" + String.join("|", SymbolsType.JAVADOC.getSymbols()) + ")\\s";
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

    private Highlight() {
    }

    public static Highlight create() {
        return new Highlight();
    }

    public StyleSpans<Collection<String>> computeHighlighting(String text) {
        matcher = PATTERN.matcher(text);
        spansBuilder = new StyleSpansBuilder<>();
        lastKwEnd = 0;
        while (matcher.find()) {
            String styleClass = null;
            if (isKEYWORD()) {
                styleClass = "keyword";
            } else if (isPAREN()) {
                styleClass = "paren";
            } else if (isBRACE()) {
                styleClass = "brace";
            } else if (isBRACKET()) {
                styleClass = "bracket";
            } else if (isANNOTATION()) {
                styleClass = "annotation";
            } else if (isSEMICOLON()) {
                styleClass = "semicolon";
            } else if (isNUMBER()) {
                styleClass = "number";
            } else if (isSTRING()) {
                styleClass = "string";
            } else if (isCOMMENTONE()) {
                List<GroupClass> groupClasses = new ArrayList<>(
                        Collections.singletonList(new GroupClass("EMPTY", "comment-keyword")));
                subCompute(null, COMMENT_KEYWORDS_PATTERN, "COMMENTONE", "comment-one", groupClasses);

                continue;
            } else if (isCOMMENTMULTI()) {
                List<GroupClass> groupClasses = new ArrayList<>(
                        Collections.singletonList(new GroupClass("EMPTY", "comment-keyword")));
                subCompute(null, COMMENT_KEYWORDS_PATTERN, "COMMENTMULTI", "comment-multi", groupClasses);

                continue;
            } else if (isJAVADOC()) {
                List<GroupClass> groupClasses = new ArrayList<>();
                groupClasses.add(new GroupClass("JAVADOCKEY", "javadoc-keyword"));
                groupClasses.add(new GroupClass("JAVADOCHTML", "javadoc-html"));
                groupClasses.add(new GroupClass("JAVADOCLINK", "javadoc-link"));

                subCompute(DOC_PATTERN, null, "JAVADOC", "javadoc", groupClasses);

                continue;
            }
            int isNumberAdvance0 = "number".equals(styleClass) ? 1 : 0;
            int isNumberAdvance1 = isNumberAdvance0 == 1 ? isNumberAdvance0 + 1 : 0;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd + isNumberAdvance0);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start() - isNumberAdvance1);
            lastKwEnd = matcher.end() - isNumberAdvance0;
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private String getClassSubOfGroup(Matcher matcher, List<GroupClass> groupClasses) {
        for (GroupClass groupClass : groupClasses) {
            if (isEmptyGroupOrEqualsGroup(matcher, groupClass)) {
                return groupClass.getThisClass();
            }
        }

        throw new IllegalArgumentException();
    }

    private boolean isANNOTATION() {
        return matcher.group("ANNOTATION") != null;
    }

    private boolean isBRACE() {
        return matcher.group("BRACE") != null;
    }

    private boolean isBRACKET() {
        return matcher.group("BRACKET") != null;
    }

    private boolean isCOMMENTMULTI() {
        return matcher.group("COMMENTMULTI") != null;
    }

    private boolean isCOMMENTONE() {
        return matcher.group("COMMENTONE") != null;
    }

    private boolean isEmptyGroup(GroupClass groupClass) {
        return "EMPTY".equals(groupClass.getGroup());
    }

    private boolean isEmptyGroupOrEqualsGroup(Matcher matcher, GroupClass groupClass) {
        return isEmptyGroup(groupClass) || isEqualsGroup(matcher, groupClass);
    }

    private boolean isEqualsGroup(Matcher matcher, GroupClass groupClass) {
        return matcher.group(groupClass.getGroup()) != null;
    }

    private boolean isJAVADOC() {
        return matcher.group("JAVADOC") != null;
    }

    private boolean isKEYWORD() {
        return matcher.group("KEYWORD") != null;
    }

    private boolean isNUMBER() {
        return matcher.group("NUMBER") != null;
    }

    private boolean isPAREN() {
        return matcher.group("PAREN") != null;
    }

    private boolean isSEMICOLON() {
        return matcher.group("SEMICOLON") != null;
    }

    private boolean isSTRING() {
        return matcher.group("STRING") != null;
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
}
