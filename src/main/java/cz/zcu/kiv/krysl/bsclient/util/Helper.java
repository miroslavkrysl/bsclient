package cz.zcu.kiv.krysl.bsclient.util;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<String> splitCharSequence(CharSequence string, char separator, char escape) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean isEscaped = false;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (isEscaped) {
                isEscaped = false;
            } else if (c == escape) {
                isEscaped = true;
            } else if (c == separator) {
                tokens.add(sb.toString());
                sb.setLength(0);
                continue;
            }

            sb.append(c);
        }

        tokens.add(sb.toString());

        return tokens;
    }

    public static int findChar(CharSequence string, int start, char toFind, char escape) {
        boolean isEscaped = false;

        if (start <= 0 || start > string.length()) {
            throw new IllegalArgumentException("Start position must be within 0 and string.length() inclusive.");
        }

        for (int i = start; i < string.length(); i++) {
            char c = string.charAt(i);

            if (isEscaped) {
                isEscaped = false;

                if (c == escape && toFind == escape) {
                    return i;
                }

                continue;
            }

            if (c == escape) {
                isEscaped = true;
                continue;
            }

            if (c == toFind) {
                return i;
            }
        }

        return -1;
    }

    public static String unescapeChars(CharSequence string, CharSequence chars, char escape) {
        StringBuilder sb = new StringBuilder();

        boolean isEscape = false;

        for (int i = 0; i < string.length(); i++) {
            char sc = string.charAt(i);

            if (isEscape) {
                isEscape = false;

                if (chars != null) {
                    boolean shouldUnescape = false;

                    for (int j = 0; j < chars.length(); j++) {
                        char ec = chars.charAt(j);

                        if (sc == ec) {
                            shouldUnescape = true;
                            break;
                        }
                    }
                    if (!shouldUnescape) {
                        sb.append(escape);
                    }
                }
            } else if (sc == escape) {
                isEscape = true;
                continue;
            }

            sb.append(sc);
        }

        return sb.toString();
    }

    public static String escapeChars(CharSequence string, CharSequence chars, char escape) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char sc = string.charAt(i);

            for (int j = 0; j < chars.length(); j++) {
                char ec = chars.charAt(j);
                if (sc == ec) {
                    sb.append(escape);
                    break;
                }
            }

            sb.append(sc);
        }

        return sb.toString();
    }
}
