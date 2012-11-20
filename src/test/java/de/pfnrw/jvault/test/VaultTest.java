package de.pfnrw.jvault.test;

import de.pfnrw.jvault.client.VaultService;
import de.pfnrw.jvault.client.VaultServiceService;
import de.pfnrw.jvault.vault.Entry;
import de.pfnrw.jvault.vault.Vault;
import org.junit.*;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: vileda
 * Date: 11/16/12
 * Time: 12:50 AM
 */

public class VaultTest {
    @Test
    public void jsonExport() {
        try {
            Vault vault = new Vault();
            Entry entry = new Entry();
            Entry entry2 = new Entry();
            Entry entry3 = new Entry();
            entry.setName("foobarddeee");
            entry2.setName("22foobarddeee");
            entry3.setName("33foobarddeee");
            entry.setDescription("foobardd");
            entry2.setDescription("22foobardd");
            entry3.setDescription("33foobardd");
            entry3.setPassword("gen20");
            vault.addChild(entry);
            vault.addChild(entry, entry2);
            vault.addChild(entry, entry3);
            System.out.println(vault.getJSONString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jsonImport() {
        try {
            Vault vault = new Vault();
            System.out.println(vault.getJSONString());
            vault.save();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void search() {
        try {
            Vault vault = new Vault();
            System.out.println(vault.search("*"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void webservice() {
        try {
            VaultServiceService vaultService = new VaultServiceService(new URL("http://localhost:9000/VaultService?wsdl"));
            VaultService vault = vaultService.getVaultServicePort();
            System.out.println(vault.search("*"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
