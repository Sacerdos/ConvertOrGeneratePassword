package com.indychkov.convertorgeneratepassword;

public class PasswordHelper {
    private final String[] RUSSIAN;
    private final String[] ENGLISH;

    public PasswordHelper(String[] russian, String[] english) {
        if (russian.length != english.length) {
            throw new IllegalArgumentException();
        }
        RUSSIAN = russian;
        ENGLISH = english;
    }
    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String s = String.valueOf(c).toLowerCase();
            boolean found = false;
            for (int j = 0; j < RUSSIAN.length; j++) {
                if (RUSSIAN[j].equals(s)) {
                    result.append(Character.isUpperCase(c)?
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
    public int getQuality(CharSequence password){
        return Math.min(password.length(), 10);
    }
}
