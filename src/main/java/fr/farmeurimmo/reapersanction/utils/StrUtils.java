package fr.farmeurimmo.reapersanction.utils;

public class StrUtils {

    public static String fromArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(' ');
        }
        return sb.toString().trim();
    }
}
