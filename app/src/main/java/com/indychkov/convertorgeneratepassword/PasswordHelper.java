package com.indychkov.convertorgeneratepassword;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PasswordHelper {
    private final String[] RUSSIAN;
    private final String[] ENGLISH;
    public static final char[] SYMBOLS = {'@', '#', '_', '%', '&', '+', '*'};
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

    public int getQuality(String password) {
        if (password.isEmpty()) {
            return 0;
        }
        int scoreNumber = 0;
        int scoreSymbol = 0;
        int scoreCapitalize = 0;
        int scoreLetter = 0;
        int scoreResult = 0;
        scoreResult += Math.min(3, password.length() / 3);
        Set<Character> uniqueSymbols = new HashSet<>();
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                if (Character.isDigit(c)) {
                    scoreNumber++;
                    uniqueSymbols.add(c);
                } else {
                    scoreLetter++;
                    if (Character.isUpperCase(c)) {
                        scoreCapitalize++;
                    }
                    uniqueSymbols.add(Character.toLowerCase(c));
                }
            } else {
                scoreSymbol++;
                uniqueSymbols.add(c);
            }
        }
        scoreResult += scoreNumber > 0 ? Math.min(2, scoreNumber) : 0;
        scoreResult += scoreSymbol > 0 ? Math.min(2, scoreSymbol) : 0;
        scoreResult += (uniqueSymbols.size() - 3) > 0 ? Math.min(2, uniqueSymbols.size() - 3) : 0;
        scoreResult += scoreCapitalize > 0 ? 1 : 0;


        if (scoreNumber == 0) {
            scoreResult--;
        }
        if (scoreSymbol == 0) {
            scoreResult--;
        }
        if (scoreLetter == 0) {
            scoreResult--;
        }
        if (uniqueSymbols.size() <= 2) {
            scoreResult = 1;
        }
        return Math.max(1, Math.min(10, scoreResult));
    }

    public String generatePassword(int passLength, boolean isCaps, boolean isNumber, boolean isSymbol) {
        StringBuilder password = new StringBuilder();
        int countLetter = 0;
        int countCaps = 0;
        int countNumber = 0;
        int countSymbol = 0;
        for (int i = 0; i < passLength; i++) {
            boolean notLetter = (isNumber || isSymbol) && random.nextBoolean();
            boolean toCaps = isCaps && random.nextBoolean();
            if (notLetter) {
                if ((random.nextInt(4) == 2 && isSymbol) || !isNumber) {
                    password.append(SYMBOLS[random.nextInt(1000) % SYMBOLS.length]);
                    countSymbol++;
                } else {
                    password.append(Character.forDigit(random.nextInt(10), 10));
                    countNumber++;
                }
            } else {
                char c = Character.toChars('a' + random.nextInt(26))[0];
                password.append(toCaps ? Character.toUpperCase(c) : c);
                if (toCaps) {
                    countCaps++;
                } else {
                    countLetter++;
                }
            }
        }
        if (isCaps) {
            if (countCaps < 2) {
                return generatePassword(passLength, isCaps, isNumber, isSymbol);
            }
        }
        if (isNumber) {
            if (countNumber < 2) {
                return generatePassword(passLength, isCaps, isNumber, isSymbol);
            }
        }
        if (isSymbol) {
            if (countSymbol < 2) {
                return generatePassword(passLength, isCaps, isNumber, isSymbol);
            }
        }
        if (countLetter < 2) {
            return generatePassword(passLength, isCaps, isNumber, isSymbol);
        }
        return password.toString();
    }

}
