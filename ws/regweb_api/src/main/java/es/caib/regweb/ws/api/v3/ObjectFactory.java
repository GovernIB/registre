
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.caib.regweb.ws.api.v3 package. 
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

    private final static QName _GetVersion_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersion");
    private final static QName _GetVersionWsResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionWsResponse");
    private final static QName _WsValidationErrors_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "WsValidationErrors");
    private final static QName _ActualizarPersona_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "actualizarPersona");
    private final static QName _CrearPersonaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "crearPersonaResponse");
    private final static QName _ListarPersonasResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "listarPersonasResponse");
    private final static QName _WsI18NError_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "WsI18NError");
    private final static QName _PersonaWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "personaWs");
    private final static QName _GetVersionWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionWs");
    private final static QName _ActualizarPersonaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "actualizarPersonaResponse");
    private final static QName _BorrarPersonaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "borrarPersonaResponse");
    private final static QName _BorrarPersona_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "borrarPersona");
    private final static QName _ListarPersonas_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "listarPersonas");
    private final static QName _GetVersionResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionResponse");
    private final static QName _CrearPersona_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "crearPersona");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.caib.regweb.ws.api.v3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BorrarPersonaResponse }
     * 
     */
    public BorrarPersonaResponse createBorrarPersonaResponse() {
        return new BorrarPersonaResponse();
    }

    /**
     * Create an instance of {@link CrearPersona }
     * 
     */
    public CrearPersona createCrearPersona() {
        return new CrearPersona();
    }

    /**
     * Create an instance of {@link PersonaWs }
     * 
     */
    public PersonaWs createPersonaWs() {
        return new PersonaWs();
    }

    /**
     * Create an instance of {@link BorrarPersona }
     * 
     */
    public BorrarPersona createBorrarPersona() {
        return new BorrarPersona();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link ActualizarPersonaResponse }
     * 
     */
    public ActualizarPersonaResponse createActualizarPersonaResponse() {
        return new ActualizarPersonaResponse();
    }

    /**
     * Create an instance of {@link GetVersionWsResponse }
     * 
     */
    public GetVersionWsResponse createGetVersionWsResponse() {
        return new GetVersionWsResponse();
    }

    /**
     * Create an instance of {@link ListarPersonas }
     * 
     */
    public ListarPersonas createListarPersonas() {
        return new ListarPersonas();
    }

    /**
     * Create an instance of {@link WsValidationErrors }
     * 
     */
    public WsValidationErrors createWsValidationErrors() {
        return new WsValidationErrors();
    }

    /**
     * Create an instance of {@link CrearPersonaResponse }
     * 
     */
    public CrearPersonaResponse createCrearPersonaResponse() {
        return new CrearPersonaResponse();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link GetVersionWs }
     * 
     */
    public GetVersionWs createGetVersionWs() {
        return new GetVersionWs();
    }

    /**
     * Create an instance of {@link WsI18NError }
     * 
     */
    public WsI18NError createWsI18NError() {
        return new WsI18NError();
    }

    /**
     * Create an instance of {@link ListarPersonasResponse }
     * 
     */
    public ListarPersonasResponse createListarPersonasResponse() {
        return new ListarPersonasResponse();
    }

    /**
     * Create an instance of {@link ActualizarPersona }
     * 
     */
    public ActualizarPersona createActualizarPersona() {
        return new ActualizarPersona();
    }

    /**
     * Create an instance of {@link WsI18NTranslation }
     * 
     */
    public WsI18NTranslation createWsI18NTranslation() {
        return new WsI18NTranslation();
    }

    /**
     * Create an instance of {@link WsFieldValidationError }
     * 
     */
    public WsFieldValidationError createWsFieldValidationError() {
        return new WsFieldValidationError();
    }

    /**
     * Create an instance of {@link WsI18NArgument }
     * 
     */
    public WsI18NArgument createWsI18NArgument() {
        return new WsI18NArgument();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "getVersion")
    public JAXBElement<GetVersion> createGetVersion(GetVersion value) {
        return new JAXBElement<GetVersion>(_GetVersion_QNAME, GetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionWsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "getVersionWsResponse")
    public JAXBElement<GetVersionWsResponse> createGetVersionWsResponse(GetVersionWsResponse value) {
        return new JAXBElement<GetVersionWsResponse>(_GetVersionWsResponse_QNAME, GetVersionWsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsValidationErrors }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "WsValidationErrors")
    public JAXBElement<WsValidationErrors> createWsValidationErrors(WsValidationErrors value) {
        return new JAXBElement<WsValidationErrors>(_WsValidationErrors_QNAME, WsValidationErrors.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualizarPersona }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "actualizarPersona")
    public JAXBElement<ActualizarPersona> createActualizarPersona(ActualizarPersona value) {
        return new JAXBElement<ActualizarPersona>(_ActualizarPersona_QNAME, ActualizarPersona.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearPersonaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "crearPersonaResponse")
    public JAXBElement<CrearPersonaResponse> createCrearPersonaResponse(CrearPersonaResponse value) {
        return new JAXBElement<CrearPersonaResponse>(_CrearPersonaResponse_QNAME, CrearPersonaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarPersonasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "listarPersonasResponse")
    public JAXBElement<ListarPersonasResponse> createListarPersonasResponse(ListarPersonasResponse value) {
        return new JAXBElement<ListarPersonasResponse>(_ListarPersonasResponse_QNAME, ListarPersonasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsI18NError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "WsI18NError")
    public JAXBElement<WsI18NError> createWsI18NError(WsI18NError value) {
        return new JAXBElement<WsI18NError>(_WsI18NError_QNAME, WsI18NError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonaWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "personaWs")
    public JAXBElement<PersonaWs> createPersonaWs(PersonaWs value) {
        return new JAXBElement<PersonaWs>(_PersonaWs_QNAME, PersonaWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "getVersionWs")
    public JAXBElement<GetVersionWs> createGetVersionWs(GetVersionWs value) {
        return new JAXBElement<GetVersionWs>(_GetVersionWs_QNAME, GetVersionWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualizarPersonaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "actualizarPersonaResponse")
    public JAXBElement<ActualizarPersonaResponse> createActualizarPersonaResponse(ActualizarPersonaResponse value) {
        return new JAXBElement<ActualizarPersonaResponse>(_ActualizarPersonaResponse_QNAME, ActualizarPersonaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BorrarPersonaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "borrarPersonaResponse")
    public JAXBElement<BorrarPersonaResponse> createBorrarPersonaResponse(BorrarPersonaResponse value) {
        return new JAXBElement<BorrarPersonaResponse>(_BorrarPersonaResponse_QNAME, BorrarPersonaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BorrarPersona }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "borrarPersona")
    public JAXBElement<BorrarPersona> createBorrarPersona(BorrarPersona value) {
        return new JAXBElement<BorrarPersona>(_BorrarPersona_QNAME, BorrarPersona.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarPersonas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "listarPersonas")
    public JAXBElement<ListarPersonas> createListarPersonas(ListarPersonas value) {
        return new JAXBElement<ListarPersonas>(_ListarPersonas_QNAME, ListarPersonas.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "getVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearPersona }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "crearPersona")
    public JAXBElement<CrearPersona> createCrearPersona(CrearPersona value) {
        return new JAXBElement<CrearPersona>(_CrearPersona_QNAME, CrearPersona.class, null, value);
    }

}
