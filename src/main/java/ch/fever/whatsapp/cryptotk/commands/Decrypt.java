package ch.fever.whatsapp.cryptotk.commands;

import ch.fever.whatsapp.cryptotk.BackupCipher;
import ch.fever.whatsapp.cryptotk.BackupKey;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "decrypt")
public class Decrypt extends CryptoCommon {

    final static Logger logger = LogManager.getLogger(Decrypt.class);

    @Option(names = "-i", required = true, description = "path to the encrypted file")
    protected File encryptedFile;

    @Option(names = "-o", required = true, description = "path to the decrypted file")
    protected File decryptedFile;


    public BackupCipher loadBackupCipher() throws IOException {
        InputStream fis;
        try {
            logger.debug("Opening encrypted file \"{}\"", encryptedFile);
            fis = new FileInputStream(encryptedFile);

            BackupCipher bc = BackupCipher.buildFromInputStream(fis);

            logger.debug(bc);

            return bc;
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }


    }


    void decrypt(byte[] iv, byte[] key) {
        try {
            InputStream fis;
            try {
                fis = new FileInputStream(encryptedFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException();
            }

            int toSkip = BackupCipher.getTotalSize();
            if (fis.skip(toSkip) < toSkip)
                throw new RuntimeException("Encrypted file seems to be to small, it ends before the actual data are supposed to begin");

            CipherInputStream isCipher;
            Cipher cipher;

            OutputStream os = new FileOutputStream(decryptedFile);

            BufferedInputStream is = new BufferedInputStream(fis);
            cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            isCipher = new CipherInputStream(is, cipher);

            InputStream isz = new InflaterInputStream(isCipher, new Inflater(false));


            IOUtils.copy(isz, os);

            os.close();
            is.close();
        } catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        super.run();


        try {
            BackupCipher bc = loadBackupCipher();
            BackupKey bk = loadBackupKey();
            byte[] iv = bc.getEncryptionIv();
            byte[] key = bk.getCipherKey();

            decrypt(iv, key);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}
