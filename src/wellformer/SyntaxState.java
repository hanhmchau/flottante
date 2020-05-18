package wellformer;

import java.util.Arrays;
import java.util.List;

class SyntaxState {
    public enum State {
        CONTENT,
        OPEN_BRACKET,
        OPEN_TAG_NAME,
        TAG_INNER,
        ATTRIBUTE_NAME,
        EQUAL_WAIT,
        EQUAL,
//        VALUE_WAIT,
        NON_QUOTED_ATTRIBUTE_VALUE,
        QUOTED_ATTRIBUTE_VALUE,
        EMPTY_SLASH,
        CLOSE_BRACKET,
        CLOSE_TAG_NAME,
        CLOSE_TAG_SLASH,
        WAIT_END_TAG_CLOSE
    }

    static final char LESS_THAN = '<';
    static final char SLASH = '/';
    static final char GREATER_THAN = '>';
    static final char EQUAL_TO = '=';
    static final char DOUBLE_QUOTE = '"';
    static final char BACKSLASH = '\\';

    private static final char UNDERSCORE = '_';
    private static final char COLON = ':';
    private static final char HYPHEN = '-';
    private static final char PERIOD = '.';

    private static final List<String> INLINE_TAGS = Arrays.asList(
            "area", "base", "br", "col", "command", "embed", "hr",
            "img", "input", "keygen", "link", "meta", "param", "source",
            "track", "wbr"
    );

    private static boolean isStartChar(char c) {
        return Character.isLetter(c) || c == UNDERSCORE || c == COLON;
    }

    private static boolean isNamedChar(char c) {
        return Character.isLetterOrDigit(c) || c == UNDERSCORE || c == HYPHEN || c== PERIOD;
    }

    static boolean isStartTagChars(char c) {
        return isStartChar(c);
}
    static boolean isStartAttributeChars(char c) {
        return isStartChar(c);
    }
    static boolean isTagChars(char c) {
        return isNamedChar(c);
    }
    static boolean isAttributeChars(char c) {
        return isNamedChar(c);
    }
    static boolean isSpace(char c) {
        return Character.isSpaceChar(c);
    }
    static boolean isInlineTag(String tag) {
        return INLINE_TAGS.contains(tag);
    }
}
