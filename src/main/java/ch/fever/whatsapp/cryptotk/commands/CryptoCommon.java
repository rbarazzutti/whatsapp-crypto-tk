package ch.fever.whatsapp.cryptotk.commands;


import ch.fever.whatsapp.cryptotk.BackupKey;
import ch.fever.whatsapp.cryptotk.Main;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import static picocli.CommandLine.Option;
import static picocli.CommandLine.ParentCommand;


public class CryptoCommon implements Runnable {
    private final static Logger logger = LogManager.getLogger(CryptoCommon.class);

    @ParentCommand
    protected Main main;

    @Option(names = "-k", required = true, description = "path to the key")
    protected File key = null;


    private BackupKey backupKey;

    public BackupKey getBackupKey() {
        return backupKey;
    }

    static final byte[] BACKUP_CIPHER_HEADER_V1 = new byte[]{0, 1};

    static {
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    protected static byte[] generateIV() {
        return generateRandomBytes(16);
    }

    private static byte[] generateRandomBytes(final int n) {
        try {
            final byte[] array = new byte[n];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(array);
            return array;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


    BackupKey loadBackupKey() throws IOException {
        logger.debug("Opening key file \"{}\"", key);

        BackupKey backupKey = BackupKey.buildFromInputStream(new FileInputStream(key));
        logger.debug("Key file content  \"{}\"", backupKey);

        return backupKey;
    }


    @Override
    public void run() {
        try {

            Level level = main.isDebug() ? Level.DEBUG : Level.WARN;
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), level);

            Configurator.setRootLevel(level);

            backupKey = loadBackupKey();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }


    }
}

