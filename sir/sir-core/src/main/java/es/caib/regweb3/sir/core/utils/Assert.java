package es.caib.regweb3.sir.core.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by earrivi on 22/05/2017.
 */
public class Assert {

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Assert that the given String has valid text content; that is, it must not
     * be {@code null} and must contain at least one non-whitespace character.
     * <pre class="code">assert_hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils #hasText
     */
    public static void hasText(String text, String message) {
        if (!hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }


    private static boolean hasText(String str) {
        if (!(str != null && str.length() > 0)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}
