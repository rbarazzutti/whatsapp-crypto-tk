package ch.fever.whatsapp.cryptotk;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ch.fever.whatsapp.cryptotk.Common.bytesToHexString;

public class BackupKey {


    private static byte[] getObjectByteArray(InputStream is) {
        byte[] array = null;
        try {
            ObjectInputStream objectInputStream2 = (new ObjectInputStream(is));


            array = (byte[]) objectInputStream2.readObject();


            objectInputStream2.close();

        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        return array;
    }


    static final byte[] BACKUP_CIPHER_HEADER = new byte[]{0, 1};

    private byte[] getBlock(BackupKeyBlocks bl) {
        int from = bl.getOffset();
        int to = from + bl.getSize();
        return Arrays.copyOfRange(data, from, to);
    }


    public byte[] getHeader() {
        return getBlock(BackupKeyBlocks.HEADER);
    }

    public byte getVersion() {
        return getBlock(BackupKeyBlocks.VERSION)[0];
    }


    public byte[] getServerSalt() {
        return getBlock(BackupKeyBlocks.SERVER_SALT);
    }


    public byte[] getGoogleIdSalt() {
        return getBlock(BackupKeyBlocks.GOOGLE_ID_SALT);
    }


    public byte[] getHashedGoogleId() {
        return getBlock(BackupKeyBlocks.HASHED_GOOGLE_ID);
    }


    public byte[] getCipherKey() {
        return getBlock(BackupKeyBlocks.CIPHER_KEY);
    }


    final private byte[] data;

    static public BackupKey buildFromInputStream(InputStream is) {
        return new BackupKey(getObjectByteArray(is));
    }

    private BackupKey(byte[] data) {
        super();

        this.data = data;
        if (!Arrays.equals(getHeader(), BACKUP_CIPHER_HEADER)) {
            throw new UnsupportedOperationException("Error: Header mismatch\n");
        }
    }

    public String toString() {
        return "BackupKey [hashedGoogleId=" + bytesToHexString(getHashedGoogleId()) + ", serverSalt=" + bytesToHexString(getServerSalt()) + ", googleIdSalt=" + bytesToHexString(getGoogleIdSalt()) + ", cipherKey=" + bytesToHexString(getCipherKey()) + ", version=" + getVersion() + "]";
    }
}


enum BackupKeyBlocks {
    HEADER(2),
    VERSION(1),
    SERVER_SALT(32),
    GOOGLE_ID_SALT(16),
    HASHED_GOOGLE_ID(32),
    EMPTY(16),
    CIPHER_KEY(32);

    private int size;


    public int getSize() {
        return size;
    }


    public int getOffset() {
        return BackupKeyBlocksOffsets.getOffset(this);
    }

    BackupKeyBlocks(int size) {
        this.size = size;
    }
}

class BackupKeyBlocksOffsets {
    static private Map<BackupKeyBlocks, Integer> offsets = new HashMap<>();

    static public int getOffset(BackupKeyBlocks block) {
        return offsets.get(block);
    }

    static {
        int offset = 0;
        for (BackupKeyBlocks b : BackupKeyBlocks.values()) {
            offsets.put(b, offset);
            offset += b.getSize();
        }
    }
}