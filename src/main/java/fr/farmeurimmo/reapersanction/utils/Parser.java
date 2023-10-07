package fr.farmeurimmo.reapersanction.utils;

public class Parser {

    public static long PARSE_LONG(Object object) {
        try {
            if (object instanceof String) {
                return Long.parseLong((String) object);
            } else if (object instanceof Integer) {
                return ((Integer) object).longValue();
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int PARSE_INT(Object object) {
        try {
            if (object instanceof Integer) {
                return (Integer) object;
            } else if (object instanceof String) {
                return Integer.parseInt((String) object);
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean PARSE_BOOLEAN(Object object) {
        try {
            if (object instanceof String) {
                return Boolean.parseBoolean((String) object);
            } else if (object instanceof Integer) {
                return ((Integer) object).intValue() == 1;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
