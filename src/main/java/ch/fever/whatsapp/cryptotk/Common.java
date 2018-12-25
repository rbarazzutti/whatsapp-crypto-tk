package ch.fever.whatsapp.cryptotk;

public class Common {
    static public String bytesToHexString(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf)
            sb.append(String.format("%02x", b));


        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}


