package zh.bookreader.utils;

public class ClassUtils {
    public static Byte[] cast(byte[] array) {
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];
        return result;
    }
}
