package ch.fever.whatsapp.cryptotk.commands;

import ch.fever.whatsapp.cryptotk.BackupKey;
import ch.fever.whatsapp.cryptotk.Common;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "encrypt")
public class Encrypt extends CryptoCommon {
    @Option(names = "-I", required = true, description = "path to the clear file")
    protected File inputFile;

    @Option(names = "-O", required = true, description = "path to the encrypted file")
    protected File outputFile;


    @Option(names = "-z", required = false, description = "zlib compression level [1-9]", defaultValue = "-1")
    protected int z;

    @Option(names = "-K", required = false, description = "encryption key")
    protected String encryptionKey;

    void encrypt(BackupKey bk, byte[] generateIv) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);

            fos.write(BACKUP_CIPHER_HEADER_V1);
            fos.write(new byte[]{2});

            fos.write(bk.getServerSalt());
            fos.write(bk.getGoogleIdSalt());
            fos.write(generateIv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(bk.getCipherKey(), "AES"), new IvParameterSpec(generateIv));

            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            Deflater deflater = z > 0 ? new Deflater(z) : new Deflater();

            OutputStream dos = new DeflaterOutputStream(cos, deflater);

            IOUtils.copy(new FileInputStream(inputFile), dos);

            dos.close();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        super.run();

        try {
            byte[] generateIV;
            if (encryptionKey == null)
                generateIV = generateIV();
            else {
                generateIV = Common.hexStringToByteArray(encryptionKey);

                if (generateIV.length != 16)
                    throw new RuntimeException("Invalid encryption key provided (" + encryptionKey + ")");
            }


            BackupKey bk = loadBackupKey();
            encrypt(bk, generateIV);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}
