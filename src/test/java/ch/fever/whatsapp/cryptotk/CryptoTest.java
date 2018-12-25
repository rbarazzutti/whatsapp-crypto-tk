package ch.fever.whatsapp.cryptotk;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class CryptoTest {
    String sha256(String path) throws IOException {
        return DigestUtils.sha256Hex(new FileInputStream(path));
    }

    static File getTmpFile(String str) throws IOException {
        File keyFile = File.createTempFile(str, "wactk");
        keyFile.deleteOnExit();
        return keyFile;
    }

    static String loadFile(String resourcePath) throws IOException {
        InputStream is = CryptoTest.class.getResourceAsStream(resourcePath);

        File keyFile = File.createTempFile(resourcePath.replaceAll("/", ""), "tmp");
        keyFile.deleteOnExit();

        OutputStream os = new FileOutputStream(keyFile);

        IOUtils.copy(is, os);

        os.close();
        is.close();
        return keyFile.getAbsolutePath();
    }


    public void decrypt(String key, String input, String output) {
        Main.main(new String[]{"-d", "decrypt", "-k", key, "-i", input, "-o", output});
    }

    public void encrypt(String key, String input, String output) {
        Main.main(new String[]{"-d", "encrypt", "-k", key, "-I", input, "-O", output});
    }

    @Test
    public void testDecrypt() throws IOException {

        String key = loadFile("key");
        String cryptedDbb = loadFile("msgstore.db.crypt12");

        String output = getTmpFile("clear-data").getAbsolutePath();

        decrypt(key, cryptedDbb, output);


        Assert.assertEquals("ef0ce596f74d84a9ce194f7721e08d2334385a35b49063c3b493bfdb077f8792", sha256(output));
    }

    @Test
    public void testEncrypt() throws IOException {

        String key = loadFile("key");
        String cryptedDbb = loadFile("msgstore.db.crypt12");

        String outputClear = getTmpFile("clear-data").getAbsolutePath();

        String outputReclear = getTmpFile("clear-data").getAbsolutePath();

        String outputRecrypt = getTmpFile("encrypted-data").getAbsolutePath();

        decrypt(key, cryptedDbb, outputClear);

        encrypt(key, outputClear, outputRecrypt);

        decrypt(key, outputRecrypt, outputReclear);

        Assert.assertEquals("ef0ce596f74d84a9ce194f7721e08d2334385a35b49063c3b493bfdb077f8792", sha256(outputReclear));
    }
}
