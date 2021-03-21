package com.baidetu.util;

public class NotNullUtils {

    public static Integer notNull(Integer i) {
        if (i != null) {
            return i;
        }
        return 1;
    }

    public static String notNull(String s) {
        if (s != null && s.length() > 0) {
            return s;
        }
        return null;
    }

    public static Boolean String(String s) {

        return s != null && !"".equals(s.trim()) && !"null".equals(s);
    }

    public static Boolean obj(Object obj) {

        return obj != null && obj.equals("");
    }


}
