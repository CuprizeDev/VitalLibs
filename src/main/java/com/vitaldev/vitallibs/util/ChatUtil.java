package com.vitaldev.vitallibs.util;

import org.bukkit.ChatColor;

public class ChatUtil {
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    public static String color(String text){
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; i++){
            if (texts[i].equalsIgnoreCase("&")){
                i++;
                if (texts[i].charAt(0) == '#'){
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                }else{
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            }else{
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

    public static boolean containsSpecialCharacters(String input) {
        // Regular expression that matches anything that is not a letter or a digit
        return !input.matches("[a-zA-Z0-9]*");
    }

    public static boolean isStringWithinLength(String input, int minLength, int maxLength) {
        if (input == null) {
            return false; // Handle null string
        }
        int length = input.length();
        return length >= minLength && length <= maxLength;
    }
}
