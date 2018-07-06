package utils;

import java.lang.reflect.Array;

public class ArrayUtils {
    public static Object[] removeAt(Object[] array, int index) {
        if (array == null) {
            return null;
        } else if (index >= 0 && index < array.length) {
            Object[] retVal = (Object[]) Array.newInstance(array[0].getClass(), array.length - 1);

            for (int i = 0; i < array.length; ++i) {
                if (i < index) {
                    retVal[i] = array[i];
                } else if (i > index) {
                    retVal[i - 1] = array[i];
                }
            }

            return retVal;
        } else {
            return array;
        }
    }
}
