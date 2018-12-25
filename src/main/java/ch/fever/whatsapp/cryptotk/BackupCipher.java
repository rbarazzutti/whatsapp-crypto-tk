package ch.fever.whatsapp.cryptotk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ch.fever.whatsapp.cryptotk.Common.bytesToHexString;

public class BackupCipher {

    public static BackupCipher buildFromInputStream(InputStream fis) throws IOException {
        byte[] data = new byte[BackupCipherBlocksOffsets.getTotalSize()];
        if (fis.read(data) < data.length)
            throw new RuntimeException("Error while reading key, file too small");
        return new BackupCipher(data);
    }

    private byte[] getBlock(BackupCipherBlocks bl) {
        int from = bl.getOffset();
        int to = from + bl.getSize();
        return Arrays.copyOfRange(data, from, to);
    }


    public byte getVersion() {
        return getBlock(BackupCipherBlocks.VERSION)[0];
    }

    public byte[] getServerSalt() {
        return getBlock(BackupCipherBlocks.SERVER_SALT);
    }

    public byte[] getGoogleIdSalt() {
        return getBlock(BackupCipherBlocks.GOOGLE_ID_SALT);
    }

    public byte[] getEncryptionIv() {
        return getBlock(BackupCipherBlocks.ENCRYPTION_IV);
    }


    final private byte[] data;

    private BackupCipher(byte[] data) {
        super();
        this.data = data;
    }

    public String toString() {
        return "BackupCipher [serverSalt=" + bytesToHexString(getServerSalt()) + ", googleIdSalt=" + bytesToHexString(getGoogleIdSalt()) + ", encryptionIv=" + bytesToHexString(getEncryptionIv()) + ", version=" + getVersion() + "]";
    }


    enum BackupCipherBlocks {
        HEADER(2),
        VERSION(1),
        SERVER_SALT(32),
        GOOGLE_ID_SALT(16),
        ENCRYPTION_IV(16);

        private int size;


        public int getSize() {
            return size;
        }


        public int getOffset() {
            return BackupCipherBlocksOffsets.getOffset(this);
        }

        BackupCipherBlocks(int size) {
            this.size = size;
        }
    }


    static public int getTotalSize() {
        return BackupCipherBlocksOffsets.getTotalSize();
    }

    static public class BackupCipherBlocksOffsets {
        static private Map<BackupCipherBlocks, Integer> offsets = new HashMap<>();

        static int getOffset(BackupCipherBlocks block) {
            return offsets.get(block);
        }

        static private int totalSize;

        static int getTotalSize() {
            return totalSize;
        }

        static {
            int offset = 0;
            for (BackupCipherBlocks b : BackupCipherBlocks.values()) {
                offsets.put(b, offset);
                offset += b.getSize();
            }
            totalSize = offset;
        }
    }
}
