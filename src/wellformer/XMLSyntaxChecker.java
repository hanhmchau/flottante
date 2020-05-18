package wellformer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static wellformer.SyntaxState.*;

class XMLSyntaxChecker {
    String check(String html) {
        StringBuilder writer = new StringBuilder();
        html += " ";
        char[] chars = html.toCharArray();
        boolean isEmptyTag = false;
        boolean isOpenTag = false;
        boolean isCloseTag = false;
        StringBuilder openTag = new StringBuilder();
        StringBuilder closeTag = new StringBuilder();
        StringBuilder attributeName = new StringBuilder();
        StringBuilder attributeValue = new StringBuilder();
        Map<String, String> attributes = new HashMap<>();
        StringBuilder content = new StringBuilder();
        Stack<String> stack = new Stack<>();
        State state = State.CONTENT;

        int i = 0;
        while (i < chars.length) {
            char ch = chars[i];
            i++;
            switch (state) {
                case CONTENT:
                    if (ch == LESS_THAN) {
                        state = State.OPEN_BRACKET;
                        writer.append(content.toString().trim().replace("&", "&amp;"));
                    } else {
                        content.append(ch);
                    }
                    break;
                case OPEN_BRACKET:
                    if (isStartTagChars(ch)) {
                        state = State.OPEN_TAG_NAME;
                        isOpenTag = true;
                        isCloseTag = false;
                        isEmptyTag = false;
                        openTag = getStringBuilderFrom(ch);
                    } else if (ch == SLASH) {
                        state = State.CLOSE_TAG_SLASH;
                        isOpenTag = false;
                        isCloseTag = true;
                        isEmptyTag = false;
                    }
                    break;
                case OPEN_TAG_NAME:
                    if (isTagChars(ch)) {
                        openTag.append(ch);
                    } else if (isSpace(ch)) {
                        state = State.TAG_INNER;
                        attributes.clear();
                    } else if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                    } else if (ch == SLASH) {
                        state = State.EMPTY_SLASH;
                    }
                    break;
                case TAG_INNER:
                    if (isStartAttributeChars(ch)) {
                        state = State.ATTRIBUTE_NAME;
                        attributeName = getStringBuilderFrom(ch);
                    } else if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                    } else if (ch == SLASH) {
                        state = State.EMPTY_SLASH;
                    }
                    break;
                case ATTRIBUTE_NAME:
                    if (isAttributeChars(ch)) {
                        attributeName.append(ch);
                    } else if (ch == EQUAL_TO) {
                        state = State.EQUAL;
                    } else if (isSpace(ch)) {
                        state = State.EQUAL_WAIT;
                    } else {
                        if (ch == SLASH) {
                            attributes.put(attributeName.toString(), "true");
                            state = State.EMPTY_SLASH;
                        } else if (ch == GREATER_THAN) {
                            attributes.put(attributeName.toString(), "true");
                            state = State.CLOSE_BRACKET;
                        }
                    }
                    break;
                case EQUAL_WAIT:
                    if (ch == EQUAL_TO) {
                        state = State.EQUAL;
                    } else {
                        if (!isSpace(ch) && isStartAttributeChars(ch)) {
                            attributes.put(attributeName.toString(), "true");
                            state = State.ATTRIBUTE_NAME;
                            attributeName = getStringBuilderFrom(ch);
                        }
                    }
                    break;
                case EQUAL:
                    if (ch == DOUBLE_QUOTE || ch == BACKSLASH) {
                        quote = ch;
                        state = State.QUOTED_ATTRIBUTE_VALUE;
                        attributeValue.setLength(0);
                    } else if (!isSpace(ch) && ch != GREATER_THAN) {
                        state = State.NON_QUOTED_ATTRIBUTE_VALUE;
                        attributeValue = getStringBuilderFrom(ch);
                    }
                    break;
                case QUOTED_ATTRIBUTE_VALUE:
                    if (ch != quote) {
                        attributeValue.append(ch);
                    } else {
                        state = State.TAG_INNER;
                        attributes.put(attributeName.toString(), attributeValue.toString());
                    }
                    break;
                case NON_QUOTED_ATTRIBUTE_VALUE:
                    if (!isSpace(ch) && ch != GREATER_THAN) {
                        attributeValue.append(ch);
                    } else if (isSpace(ch)) {
                        state = State.TAG_INNER;
                        attributes.put(attributeName.toString(), attributeValue.toString());
                    } else if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                        attributes.put(attributeName.toString(), attributeValue.toString());
                    }
                    break;
                case EMPTY_SLASH:
                    if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                        isEmptyTag = true;
                    }
                    break;
                case CLOSE_TAG_SLASH:
                    if (isStartTagChars(ch)) {
                        state = State.CLOSE_TAG_NAME;
                        closeTag = getStringBuilderFrom(ch);
                    }
                    break;
                case CLOSE_TAG_NAME:
                    if (isTagChars(ch)) {
                        closeTag.append(ch);
                    } else if (isSpace(ch)) {
                        state = State.WAIT_END_TAG_CLOSE;
                    } else if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                    }
                    break;
                case WAIT_END_TAG_CLOSE:
                    if (ch == GREATER_THAN) {
                        state = State.CLOSE_BRACKET;
                    }
                    break;
                case CLOSE_BRACKET:
                    if (isOpenTag) {
                        String openTagName = openTag.toString().toLowerCase();
                        if (isInlineTag(openTagName)) {
                            isEmptyTag = true;
                        }
                        writer.append(LESS_THAN)
                                .append(openTagName)
                                .append(getAttributesString(attributes))
                                .append(isEmptyTag ? "/" : "")
                                .append(GREATER_THAN);
                        attributes.clear();
                        if (!isEmptyTag) {
                            stack.push(openTagName);
                        }
                    } else if (isCloseTag) {
                        String closeTagName = closeTag.toString().toLowerCase();
                        if (!stack.isEmpty() && stack.contains(closeTagName)) {
                            while (!stack.isEmpty() && !stack.peek().equals(closeTagName)) {
                                writer.append(LESS_THAN)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GREATER_THAN);
                            }
                            if (!stack.isEmpty() && stack.peek().equals(closeTagName)) {
                                writer.append(LESS_THAN)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GREATER_THAN);
                            }
                        }
                    }
                    if (ch == LESS_THAN) {
                        state = State.OPEN_BRACKET;
                    } else {
                        state = State.CONTENT;
                        content = getStringBuilderFrom(ch);
                    }
                    break;
            }
        }

        if (state.equals(State.CONTENT)) {
            writer.append(content.toString().trim().replace("&", "&amp;"));
        }

        while (!stack.isEmpty()) {
            writer.append(LESS_THAN)
                    .append(SLASH)
                    .append(stack.pop())
                    .append(GREATER_THAN);
        }

        return writer.toString();
    }

    private StringBuilder getStringBuilderFrom(char ch) {
        return new StringBuilder(String.valueOf(ch));
    }

    private Character quote;

    private String getAttributesString(Map<String, String> attributes) {
        if (attributes.isEmpty()) { return ""; }

        StringBuilder sb = new StringBuilder();
        attributes.forEach((key, value) -> {
            String cleanedValue = replaceSpecialCharacters(value);
            sb.append(String.format("%s=\"%s\" ", key, cleanedValue));
        });
        return " " + sb.toString().trim();
    }

    private String replaceSpecialCharacters(String original) {
        return original.replace("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
