package de.pfnrw.jvault.client;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Sample application to show how jLine can be used.
 *
 * @author sandarenu
 *
 */
public class VaultCLI {
    private String[] commandsList;
    private VaultServiceService vaultService;
    private VaultService vault;

    public void init() throws MalformedURLException {
        vaultService = new VaultServiceService(new URL("http://localhost:9000/VaultService?wsdl"));
        vault = vaultService.getVaultServicePort();
        commandsList = new String[] { "help", "search", "add", "exit" };
    }

    public void run() throws IOException, InvalidKeySpecException_Exception, BadPaddingException_Exception, NoSuchAlgorithmException_Exception, IllegalBlockSizeException_Exception, JSONException_Exception, NoSuchPaddingException_Exception, InvalidKeyException_Exception, IOException_Exception {
        printWelcomeMessage();
        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();

        completors.add(new SimpleCompletor(commandsList));
        reader.addCompletor(new ArgumentCompletor(completors));

        String line;
        PrintWriter out = new PrintWriter(System.out);

        while ((line = readLine(reader, "jvault")) != null) {
            if ("help".equals(line)) {
                printHelp();
            } else if (line.startsWith("search ")) {
                System.out.println(vault.search(line.substring(7)));
            } else if ("add".equals(line)) {
                Entry entry = new Entry();
                entry.setName(readLine(reader, "Enter Name"));
                entry.setDescription(readLine(reader, "Enter Description"));
                entry.setUrl(readLine(reader, "Enter Url"));
                entry.setUsername(readLine(reader, "Enter Username"));
                entry.setPassword(readLine(reader, "Enter Password"));
                vault.addEntry(entry);
                System.out.println("Entry saved!");
            } else if ("exit".equals(line)) {
                System.out.println("Exiting application");
                return;
            } else {
                System.out
                        .println("Invalid command, For assistance press TAB or type \"help\" then hit ENTER.");
            }
            out.flush();
        }
    }

    private void printWelcomeMessage() {
        System.out
                .println("Welcome to jLine Sample App. For assistance press TAB or type \"help\" then hit ENTER.");

    }

    private void printHelp() {
        System.out.println("help		- Show help");
        System.out.println("search		- Search");
        System.out.println("add 		- Add Entry");
        System.out.println("exit        - Exit jvault clii");

    }

    private String readLine(ConsoleReader reader, String promtMessage)
            throws IOException {
        String line;
        if(promtMessage != null)
            line = reader.readLine(promtMessage + "> ");
        else
            line = reader.readLine("jvault> ");
        return line.trim();
    }

    public static void main(String[] args) throws IOException, InvalidKeySpecException_Exception, BadPaddingException_Exception, IllegalBlockSizeException_Exception, NoSuchAlgorithmException_Exception, JSONException_Exception, NoSuchPaddingException_Exception, IOException_Exception, InvalidKeyException_Exception {
        VaultCLI shell = new VaultCLI();
        shell.init();
        shell.run();
    }
}
