package ch.fever.whatsapp.cryptotk.util;

import java.io.IOException;
import java.io.InputStream;

public class BytesStreamHelper {
    final private InputStream inputStream;

    public BytesStreamHelper(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private int position = 0;

    public void skip(int i) {
        boolean success = false;
        try {
            if (inputStream.skip(i) == i) success = true;
        } catch (IOException e) {
            //
        }
        if (!success)
            throw new ArrayIndexOutOfBoundsException();
    }

    public byte readByte() {
        return readBlock(1)[0];
    }

    public byte[] readBlock(int l) {

        try {
            byte[] array = new byte[l];
            if (inputStream.read(array) != l)
                throw new ArrayIndexOutOfBoundsException();
            return array;
        } catch (IOException e) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
