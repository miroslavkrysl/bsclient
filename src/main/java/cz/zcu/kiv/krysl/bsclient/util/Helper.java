package cz.zcu.kiv.krysl.bsclient.util;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<String> splitString(String string, char separator, char escape) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean isEscaped = false;

        for (char c : string.toCharArray()) {
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

    public static String unescape(String string, char escape) {
        return unescape(string, null, escape);
    }

    public static String unescape(String string, char[] chars, char escape) {
        StringBuilder sb = new StringBuilder();

        boolean isEscape = false;

        for (char c : string.toCharArray()) {
            if (isEscape) {
                isEscape = false;

                if (chars != null) {
                    boolean shouldUnescape = false;
                    for (char e : chars) {
                        if (c == e) {
                            shouldUnescape = true;
                            break;
                        }
                    }
                    if (!shouldUnescape) {
                        sb.append(escape);
                    }
                }
            }

            if (c == escape) {
                isEscape = true;
                continue;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    public static String escape(String string, char[] chars, char escape) {
        StringBuilder sb = new StringBuilder();

        for (char c : string.toCharArray()) {
            for (char e : chars) {
                if (c == e) {
                    sb.append(escape);
                    break;
                }
            }

            sb.append(c);
        }

        return sb.toString();
    }
}
