package ch.fever.whatsapp.cryptotk;


import ch.fever.whatsapp.cryptotk.commands.Decrypt;
import ch.fever.whatsapp.cryptotk.commands.Encrypt;
import ch.fever.whatsapp.cryptotk.commands.Inspect;
import picocli.CommandLine;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "whatsapp-crypto-tk",
        mixinStandardHelpOptions = true,
        version = "0.0.1-SNAPSHOT",
        subcommands = {
                Decrypt.class,
                Encrypt.class,
                Inspect.class})
public class Main implements Runnable {
    @Option(names = "-d", description = "show debug information")
    protected boolean debug;

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public void run() {
        System.err.println("Please invoke a subcommand");
        (new CommandLine(this)).usage(System.out);
    }
}
