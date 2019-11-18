package com.indychkov.convertorgeneratepassword;

import java.util.Random;

public class PasswordHelper {
    private final String[] RUSSIAN;
    private final String[] ENGLISH;
    private Random random;

    public PasswordHelper(String[] russian, String[] english) {
        if (russian.length != english.length) {
            throw new IllegalArgumentException();
        }
        RUSSIAN = russian;
        ENGLISH = english;
        random = new Random();
    }

    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String s = String.valueOf(c).toLowerCase();
            boolean found = false;
            for (int j = 0; j < RUSSIAN.length; j++) {
                if (RUSSIAN[j].equals(s)) {
                    result.append(Character.isUpperCase(c) ?
                            ENGLISH[j].toUpperCase() : ENGLISH[j]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public int getQuality(CharSequence password) {
        return Math.min(password.length(), 10);
    }

    public static final char[] SYMBOLS = {'@', '#', '_', '%', '&'};

    public String generatePassword(int passLength, boolean isCaps, boolean isNumber, boolean isSymbol) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passLength; i++) {
            boolean notLetter = (isNumber || isSymbol) && random.nextBoolean();
            boolean toCaps = isCaps && random.nextBoolean();
            if (notLetter) {
                if ((random.nextInt(4) == 2 && isSymbol) || !isNumber) {
                    password.append(SYMBOLS[random.nextInt(1000) % SYMBOLS.length]);
                } else {
                    password.append(Character.forDigit(random.nextInt(10), 10));
                }
            } else {
                char c = Character.toChars('a' + random.nextInt(26))[0];
                password.append(toCaps ? Character.toUpperCase(c) : c);
            }
        }
        return password.toString();
    }

}
