package com.example.util;

public class ClassUtil {

    public static int obj2int(Object obj) {
        if (obj != null) {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException var2) {
            }
        }

        return 0;
    }

    public static long obj2long(Object obj) {
        if (obj != null) {
            try {
                return Long.parseLong(obj.toString());
            } catch (NumberFormatException var2) {
            }
        }

        return 0L;
    }

    public static double obj2doulbe(Object obj) {
        if (obj != null) {
            try {
                return Double.parseDouble(obj.toString());
            } catch (NumberFormatException var2) {
            }
        }

        return 0.0D;
    }


}
