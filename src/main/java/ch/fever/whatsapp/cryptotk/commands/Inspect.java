package ch.fever.whatsapp.cryptotk.commands;

import ch.fever.whatsapp.cryptotk.BackupCipher;
import ch.fever.whatsapp.cryptotk.BackupKey;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Parameters;

@Command(name = "inspect",
        subcommands = {
                InspectKey.class,
                InspectCryptedFile.class
        })
public class Inspect implements Runnable {
    @Override
    public void run() {
        System.err.println("Please invoke a subcommand");
        (new CommandLine(this)).usage(System.out);
    }
}


@Command(name = "key", description = "inspect a cryptographic key")
class InspectKey implements Runnable {

    @Parameters(description = "path to key")
    private File keyFile;


    @Override
    public void run() {
        try {
            BackupKey backupKey = BackupKey.buildFromInputStream(new FileInputStream(keyFile));
            System.out.println(backupKey);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

@Command(name = "encrypted", description = "inspect an encrypted file")
class InspectCryptedFile implements Runnable {

    @Parameters(description = "path to encrypted file")
    private File encryptedFile;


    @Override
    public void run() {
        try {
            BackupCipher backupKey = BackupCipher.buildFromInputStream(new FileInputStream(encryptedFile));
            System.out.println(backupKey);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}