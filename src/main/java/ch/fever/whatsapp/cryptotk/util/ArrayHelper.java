package ch.fever.whatsapp.cryptotk.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayHelper {
    final private byte[] array;

    public ArrayHelper(byte[] array) {
        this.array = array;
    }

    private int position = 0;

    public void skip(int i) {
        position += i;
    }

    public byte readByte() {
        return array[position++];
    }

    public byte[] readBlock(int l) {
        int nextPosition = position + l;
        if (nextPosition > array.length)
            throw new ArrayIndexOutOfBoundsException();
        byte[] out = ArrayUtils.subarray(array, position, nextPosition);
        position = nextPosition;
        return out;
    }
}
