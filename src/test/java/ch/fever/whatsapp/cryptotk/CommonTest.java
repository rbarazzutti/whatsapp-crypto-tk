package ch.fever.whatsapp.cryptotk;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommonTest {

    @Test
    public void emptyBytesArrayToHexString() {
        assertEquals("", Common.bytesToHexString(new byte[]{}));
    }

    @Test
    public void aBytesArrayToHexString() {
        assertEquals("0001ff807f640a", Common.bytesToHexString(new byte[]{0, 1,-1,-128,127,100,10}));
    }

    @Test
    public void emptyHexStringToBytesArray() {
        assertArrayEquals(new byte[0], Common.hexStringToByteArray(""));
    }

    @Test
    public void aHexStringToBytesArray() {
        assertArrayEquals(new byte[]{0, 1,-1,-128,127,100,10}, Common.hexStringToByteArray("0001ff807f640a"));
    }
}
