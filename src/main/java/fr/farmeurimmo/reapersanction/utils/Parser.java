package fr.farmeurimmo.reapersanction.utils;

public class Parser {

    public static long PARSE_LONG(Object object) {
        try {
            if (object instanceof String) {
                return Long.parseLong((String) object);
            } else if (object instanceof Integer) {
                return ((Integer) object).longValue();
            } else if (object instanceof Long) {
                return (Long) object;
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
            } else if (object instanceof Long) {
                return ((Long) object).intValue();
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
                return (Integer) object == 1;
            } else if (object instanceof Boolean) {
                return (Boolean) object;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
