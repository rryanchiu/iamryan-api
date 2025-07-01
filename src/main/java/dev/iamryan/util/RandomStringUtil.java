package me.rryan.tinyurl.util;

import java.security.SecureRandom;

public class RandomStringUtil {

    private static final char[] CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /**
     * Get a random string in a required length.
     *
     * @param length the required length
     * @return random string of specified length
     */
    public static String random(int length) {

        StringBuilder result = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        long seed = System.currentTimeMillis() ^ (Thread.currentThread().getId() << 32);
        random.setSeed(seed);

        for (int i = 0; i < length; i++) {
            // Get a random character from the CHARS array
            result.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return result.toString();
    }

}
