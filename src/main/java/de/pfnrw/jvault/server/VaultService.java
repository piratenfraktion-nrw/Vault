package de.pfnrw.jvault.server;

import de.pfnrw.jvault.vault.Entry;
import de.pfnrw.jvault.vault.Vault;
import org.json.JSONException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

/**
 * User: vileda
 * Date: 11/19/12
 * Time: 9:50 PM
 */
@WebService()
public class VaultService {
    Vault vault;

    @WebMethod
    public String search(String phrase) {
        return vault.search(phrase).toString();
    }

    public boolean addEntry(Entry entry) throws IllegalBlockSizeException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, JSONException, BadPaddingException {
        vault.addChild(entry);
        return true;
    }

    public static void main(String[] argv) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, JSONException, BadPaddingException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        VaultService implementor = new VaultService();
        implementor.vault = new Vault();
        String address = "http://localhost:9000/VaultService";
        Endpoint.publish(address, implementor);
    }
}