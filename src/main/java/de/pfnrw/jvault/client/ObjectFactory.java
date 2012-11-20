
package de.pfnrw.jvault.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.pfnrw.jvault.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IllegalBlockSizeException_QNAME = new QName("http://server.jvault.pfnrw.de/", "IllegalBlockSizeException");
    private final static QName _InvalidKeyException_QNAME = new QName("http://server.jvault.pfnrw.de/", "InvalidKeyException");
    private final static QName _Search_QNAME = new QName("http://server.jvault.pfnrw.de/", "search");
    private final static QName _IOException_QNAME = new QName("http://server.jvault.pfnrw.de/", "IOException");
    private final static QName _AddEntry_QNAME = new QName("http://server.jvault.pfnrw.de/", "addEntry");
    private final static QName _SearchResponse_QNAME = new QName("http://server.jvault.pfnrw.de/", "searchResponse");
    private final static QName _NoSuchPaddingException_QNAME = new QName("http://server.jvault.pfnrw.de/", "NoSuchPaddingException");
    private final static QName _InvalidKeySpecException_QNAME = new QName("http://server.jvault.pfnrw.de/", "InvalidKeySpecException");
    private final static QName _NoSuchAlgorithmException_QNAME = new QName("http://server.jvault.pfnrw.de/", "NoSuchAlgorithmException");
    private final static QName _BadPaddingException_QNAME = new QName("http://server.jvault.pfnrw.de/", "BadPaddingException");
    private final static QName _JSONException_QNAME = new QName("http://server.jvault.pfnrw.de/", "JSONException");
    private final static QName _AddEntryResponse_QNAME = new QName("http://server.jvault.pfnrw.de/", "addEntryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.pfnrw.jvault.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IllegalBlockSizeException }
     * 
     */
    public IllegalBlockSizeException createIllegalBlockSizeException() {
        return new IllegalBlockSizeException();
    }

    /**
     * Create an instance of {@link SearchResponse }
     * 
     */
    public SearchResponse createSearchResponse() {
        return new SearchResponse();
    }

    /**
     * Create an instance of {@link AddEntry }
     * 
     */
    public AddEntry createAddEntry() {
        return new AddEntry();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link Search }
     * 
     */
    public Search createSearch() {
        return new Search();
    }

    /**
     * Create an instance of {@link InvalidKeyException }
     * 
     */
    public InvalidKeyException createInvalidKeyException() {
        return new InvalidKeyException();
    }

    /**
     * Create an instance of {@link NoSuchAlgorithmException }
     * 
     */
    public NoSuchAlgorithmException createNoSuchAlgorithmException() {
        return new NoSuchAlgorithmException();
    }

    /**
     * Create an instance of {@link InvalidKeySpecException }
     * 
     */
    public InvalidKeySpecException createInvalidKeySpecException() {
        return new InvalidKeySpecException();
    }

    /**
     * Create an instance of {@link NoSuchPaddingException }
     * 
     */
    public NoSuchPaddingException createNoSuchPaddingException() {
        return new NoSuchPaddingException();
    }

    /**
     * Create an instance of {@link AddEntryResponse }
     * 
     */
    public AddEntryResponse createAddEntryResponse() {
        return new AddEntryResponse();
    }

    /**
     * Create an instance of {@link JSONException }
     * 
     */
    public JSONException createJSONException() {
        return new JSONException();
    }

    /**
     * Create an instance of {@link BadPaddingException }
     * 
     */
    public BadPaddingException createBadPaddingException() {
        return new BadPaddingException();
    }

    /**
     * Create an instance of {@link Entry }
     * 
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalBlockSizeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "IllegalBlockSizeException")
    public JAXBElement<IllegalBlockSizeException> createIllegalBlockSizeException(IllegalBlockSizeException value) {
        return new JAXBElement<IllegalBlockSizeException>(_IllegalBlockSizeException_QNAME, IllegalBlockSizeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidKeyException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "InvalidKeyException")
    public JAXBElement<InvalidKeyException> createInvalidKeyException(InvalidKeyException value) {
        return new JAXBElement<InvalidKeyException>(_InvalidKeyException_QNAME, InvalidKeyException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Search }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "search")
    public JAXBElement<Search> createSearch(Search value) {
        return new JAXBElement<Search>(_Search_QNAME, Search.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "addEntry")
    public JAXBElement<AddEntry> createAddEntry(AddEntry value) {
        return new JAXBElement<AddEntry>(_AddEntry_QNAME, AddEntry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "searchResponse")
    public JAXBElement<SearchResponse> createSearchResponse(SearchResponse value) {
        return new JAXBElement<SearchResponse>(_SearchResponse_QNAME, SearchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchPaddingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "NoSuchPaddingException")
    public JAXBElement<NoSuchPaddingException> createNoSuchPaddingException(NoSuchPaddingException value) {
        return new JAXBElement<NoSuchPaddingException>(_NoSuchPaddingException_QNAME, NoSuchPaddingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidKeySpecException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "InvalidKeySpecException")
    public JAXBElement<InvalidKeySpecException> createInvalidKeySpecException(InvalidKeySpecException value) {
        return new JAXBElement<InvalidKeySpecException>(_InvalidKeySpecException_QNAME, InvalidKeySpecException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchAlgorithmException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "NoSuchAlgorithmException")
    public JAXBElement<NoSuchAlgorithmException> createNoSuchAlgorithmException(NoSuchAlgorithmException value) {
        return new JAXBElement<NoSuchAlgorithmException>(_NoSuchAlgorithmException_QNAME, NoSuchAlgorithmException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadPaddingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "BadPaddingException")
    public JAXBElement<BadPaddingException> createBadPaddingException(BadPaddingException value) {
        return new JAXBElement<BadPaddingException>(_BadPaddingException_QNAME, BadPaddingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JSONException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "JSONException")
    public JAXBElement<JSONException> createJSONException(JSONException value) {
        return new JAXBElement<JSONException>(_JSONException_QNAME, JSONException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddEntryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.jvault.pfnrw.de/", name = "addEntryResponse")
    public JAXBElement<AddEntryResponse> createAddEntryResponse(AddEntryResponse value) {
        return new JAXBElement<AddEntryResponse>(_AddEntryResponse_QNAME, AddEntryResponse.class, null, value);
    }

}
