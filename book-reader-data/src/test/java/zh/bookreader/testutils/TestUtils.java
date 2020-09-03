package zh.bookreader.testutils;

public class TestUtils {
    private TestUtils() {
    }

    public static Byte[] box(byte[] bytes) {
        Byte[] r = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            r[i] = bytes[i];
        return r;
    }

    public static <E> E box(E e) {
        return e;
    }
}
