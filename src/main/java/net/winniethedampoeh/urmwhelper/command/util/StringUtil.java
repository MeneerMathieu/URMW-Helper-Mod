package net.winniethedampoeh.urmwhelper.command.util;

public class StringUtil {

    public static String gridBuilder(String text, int length, Location location){
        StringBuilder textBuilder = new StringBuilder(text);
        while (textBuilder.length() < length){
            switch (location){
                case AFTER -> textBuilder.append(" ");
                case BEFORE -> textBuilder.insert(0, " ");
            }
        }
        text = textBuilder.toString();
        return text;
    }
}
