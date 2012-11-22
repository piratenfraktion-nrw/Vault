package de.pfnrw.jvault.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import de.pfnrw.jvault.vault.Entry;
import de.pfnrw.jvault.vault.Vault;
import org.json.JSONException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.net.ssl.*;
import javax.xml.ws.Endpoint;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

/**
 * User: vileda
 * Date: 11/19/12
 * Time: 9:50 PM
 */
@WebService()
public class VaultService {
    private static Vault vault;

    @WebMethod
    public String search(String phrase) {
        return vault.search(phrase).toString();
    }

    public boolean addEntry(Entry entry) throws IllegalBlockSizeException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, JSONException, BadPaddingException {
        vault.addChild(entry);
        return true;
    }

    public static void main(String[] argv) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, JSONException, BadPaddingException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        vault = new Vault();
        vault.save();

        Endpoint endpoint = Endpoint.create(new VaultService());
        SSLContext ssl =  SSLContext.getInstance("SSLv3");


        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());


        //Load the JKS file (located, in this case, at D:\keystore.jks, with password 'test'
        store.load(new FileInputStream("keystore.jks"), "fooooo".toCharArray());

        //init the key store, along with the password 'test'
        kmf.init(store, "fooooo".toCharArray());
        KeyManager[] keyManagers;
        keyManagers = kmf.getKeyManagers();



        //Init the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        //It will reference the same key store as the key managers
        tmf.init(store);

        TrustManager[] trustManagers = tmf.getTrustManagers();


        ssl.init(keyManagers, trustManagers, new SecureRandom());

        //Init a configuration with our SSL context
        HttpsConfigurator configurator = new HttpsConfigurator(ssl);


        //Create a server on localhost, port 443 (https port)
        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(argv[0], 9000), 9000);
        httpsServer.setHttpsConfigurator(configurator);


        //Create a context so our service will be available under this context
        HttpContext context = httpsServer.createContext("/");
        httpsServer.start();


        //Finally, use the created context to publish the service
        endpoint.publish(context);
    }
}