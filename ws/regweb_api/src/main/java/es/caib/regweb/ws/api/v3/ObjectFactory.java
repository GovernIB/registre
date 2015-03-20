
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
    private final static QName _ObtenerRegistroSalida_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "obtenerRegistroSalida");
    private final static QName _GetVersionWsResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionWsResponse");
    private final static QName _ObtenerRegistroSalidaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "obtenerRegistroSalidaResponse");
    private final static QName _AltaRegistroSalida_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "altaRegistroSalida");
    private final static QName _WsValidationErrors_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "WsValidationErrors");
    private final static QName _RegistroWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "registroWs");
    private final static QName _WsI18NError_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "WsI18NError");
    private final static QName _GetVersionWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionWs");
    private final static QName _RegistroSalidaWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "registroSalidaWs");
    private final static QName _ObtenerRegistroSalidaIDResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "obtenerRegistroSalidaIDResponse");
    private final static QName _AnularRegistroSalidaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "anularRegistroSalidaResponse");
    private final static QName _InteresadoWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "interesadoWs");
    private final static QName _ObtenerRegistroSalidaID_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "obtenerRegistroSalidaID");
    private final static QName _AnularRegistroSalida_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "anularRegistroSalida");
    private final static QName _AnexoWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "anexoWs");
    private final static QName _DatosInteresadoWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "datosInteresadoWs");
    private final static QName _GetVersionResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "getVersionResponse");
    private final static QName _AltaRegistroSalidaResponse_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "altaRegistroSalidaResponse");
    private final static QName _IdentificadorWs_QNAME = new QName("http://impl.v3.ws.regweb.caib.es/", "identificadorWs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.caib.regweb.ws.api.v3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link WsI18NTranslation }
     * 
     */
    public WsI18NTranslation createWsI18NTranslation() {
        return new WsI18NTranslation();
    }

    /**
     * Create an instance of {@link AnexoWs }
     * 
     */
    public AnexoWs createAnexoWs() {
        return new AnexoWs();
    }

    /**
     * Create an instance of {@link AltaRegistroSalida }
     * 
     */
    public AltaRegistroSalida createAltaRegistroSalida() {
        return new AltaRegistroSalida();
    }

    /**
     * Create an instance of {@link ObtenerRegistroSalidaIDResponse }
     * 
     */
    public ObtenerRegistroSalidaIDResponse createObtenerRegistroSalidaIDResponse() {
        return new ObtenerRegistroSalidaIDResponse();
    }

    /**
     * Create an instance of {@link AnularRegistroSalida }
     * 
     */
    public AnularRegistroSalida createAnularRegistroSalida() {
        return new AnularRegistroSalida();
    }

    /**
     * Create an instance of {@link GetVersionWs }
     * 
     */
    public GetVersionWs createGetVersionWs() {
        return new GetVersionWs();
    }

    /**
     * Create an instance of {@link AnularRegistroSalidaResponse }
     * 
     */
    public AnularRegistroSalidaResponse createAnularRegistroSalidaResponse() {
        return new AnularRegistroSalidaResponse();
    }

    /**
     * Create an instance of {@link InteresadoWs }
     * 
     */
    public InteresadoWs createInteresadoWs() {
        return new InteresadoWs();
    }

    /**
     * Create an instance of {@link ObtenerRegistroSalida }
     * 
     */
    public ObtenerRegistroSalida createObtenerRegistroSalida() {
        return new ObtenerRegistroSalida();
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
     * Create an instance of {@link WsI18NError }
     * 
     */
    public WsI18NError createWsI18NError() {
        return new WsI18NError();
    }

    /**
     * Create an instance of {@link AltaRegistroSalidaResponse }
     * 
     */
    public AltaRegistroSalidaResponse createAltaRegistroSalidaResponse() {
        return new AltaRegistroSalidaResponse();
    }

    /**
     * Create an instance of {@link DatosInteresadoWs }
     * 
     */
    public DatosInteresadoWs createDatosInteresadoWs() {
        return new DatosInteresadoWs();
    }

    /**
     * Create an instance of {@link GetVersionWsResponse }
     * 
     */
    public GetVersionWsResponse createGetVersionWsResponse() {
        return new GetVersionWsResponse();
    }

    /**
     * Create an instance of {@link ObtenerRegistroSalidaResponse }
     * 
     */
    public ObtenerRegistroSalidaResponse createObtenerRegistroSalidaResponse() {
        return new ObtenerRegistroSalidaResponse();
    }

    /**
     * Create an instance of {@link WsValidationErrors }
     * 
     */
    public WsValidationErrors createWsValidationErrors() {
        return new WsValidationErrors();
    }

    /**
     * Create an instance of {@link RegistroWs }
     * 
     */
    public RegistroWs createRegistroWs() {
        return new RegistroWs();
    }

    /**
     * Create an instance of {@link IdentificadorWs }
     * 
     */
    public IdentificadorWs createIdentificadorWs() {
        return new IdentificadorWs();
    }

    /**
     * Create an instance of {@link RegistroSalidaWs }
     * 
     */
    public RegistroSalidaWs createRegistroSalidaWs() {
        return new RegistroSalidaWs();
    }

    /**
     * Create an instance of {@link ObtenerRegistroSalidaID }
     * 
     */
    public ObtenerRegistroSalidaID createObtenerRegistroSalidaID() {
        return new ObtenerRegistroSalidaID();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerRegistroSalida }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "obtenerRegistroSalida")
    public JAXBElement<ObtenerRegistroSalida> createObtenerRegistroSalida(ObtenerRegistroSalida value) {
        return new JAXBElement<ObtenerRegistroSalida>(_ObtenerRegistroSalida_QNAME, ObtenerRegistroSalida.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerRegistroSalidaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "obtenerRegistroSalidaResponse")
    public JAXBElement<ObtenerRegistroSalidaResponse> createObtenerRegistroSalidaResponse(ObtenerRegistroSalidaResponse value) {
        return new JAXBElement<ObtenerRegistroSalidaResponse>(_ObtenerRegistroSalidaResponse_QNAME, ObtenerRegistroSalidaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AltaRegistroSalida }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "altaRegistroSalida")
    public JAXBElement<AltaRegistroSalida> createAltaRegistroSalida(AltaRegistroSalida value) {
        return new JAXBElement<AltaRegistroSalida>(_AltaRegistroSalida_QNAME, AltaRegistroSalida.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistroWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "registroWs")
    public JAXBElement<RegistroWs> createRegistroWs(RegistroWs value) {
        return new JAXBElement<RegistroWs>(_RegistroWs_QNAME, RegistroWs.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "getVersionWs")
    public JAXBElement<GetVersionWs> createGetVersionWs(GetVersionWs value) {
        return new JAXBElement<GetVersionWs>(_GetVersionWs_QNAME, GetVersionWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistroSalidaWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "registroSalidaWs")
    public JAXBElement<RegistroSalidaWs> createRegistroSalidaWs(RegistroSalidaWs value) {
        return new JAXBElement<RegistroSalidaWs>(_RegistroSalidaWs_QNAME, RegistroSalidaWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerRegistroSalidaIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "obtenerRegistroSalidaIDResponse")
    public JAXBElement<ObtenerRegistroSalidaIDResponse> createObtenerRegistroSalidaIDResponse(ObtenerRegistroSalidaIDResponse value) {
        return new JAXBElement<ObtenerRegistroSalidaIDResponse>(_ObtenerRegistroSalidaIDResponse_QNAME, ObtenerRegistroSalidaIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnularRegistroSalidaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "anularRegistroSalidaResponse")
    public JAXBElement<AnularRegistroSalidaResponse> createAnularRegistroSalidaResponse(AnularRegistroSalidaResponse value) {
        return new JAXBElement<AnularRegistroSalidaResponse>(_AnularRegistroSalidaResponse_QNAME, AnularRegistroSalidaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InteresadoWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "interesadoWs")
    public JAXBElement<InteresadoWs> createInteresadoWs(InteresadoWs value) {
        return new JAXBElement<InteresadoWs>(_InteresadoWs_QNAME, InteresadoWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerRegistroSalidaID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "obtenerRegistroSalidaID")
    public JAXBElement<ObtenerRegistroSalidaID> createObtenerRegistroSalidaID(ObtenerRegistroSalidaID value) {
        return new JAXBElement<ObtenerRegistroSalidaID>(_ObtenerRegistroSalidaID_QNAME, ObtenerRegistroSalidaID.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnularRegistroSalida }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "anularRegistroSalida")
    public JAXBElement<AnularRegistroSalida> createAnularRegistroSalida(AnularRegistroSalida value) {
        return new JAXBElement<AnularRegistroSalida>(_AnularRegistroSalida_QNAME, AnularRegistroSalida.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnexoWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "anexoWs")
    public JAXBElement<AnexoWs> createAnexoWs(AnexoWs value) {
        return new JAXBElement<AnexoWs>(_AnexoWs_QNAME, AnexoWs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatosInteresadoWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "datosInteresadoWs")
    public JAXBElement<DatosInteresadoWs> createDatosInteresadoWs(DatosInteresadoWs value) {
        return new JAXBElement<DatosInteresadoWs>(_DatosInteresadoWs_QNAME, DatosInteresadoWs.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AltaRegistroSalidaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "altaRegistroSalidaResponse")
    public JAXBElement<AltaRegistroSalidaResponse> createAltaRegistroSalidaResponse(AltaRegistroSalidaResponse value) {
        return new JAXBElement<AltaRegistroSalidaResponse>(_AltaRegistroSalidaResponse_QNAME, AltaRegistroSalidaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentificadorWs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.v3.ws.regweb.caib.es/", name = "identificadorWs")
    public JAXBElement<IdentificadorWs> createIdentificadorWs(IdentificadorWs value) {
        return new JAXBElement<IdentificadorWs>(_IdentificadorWs_QNAME, IdentificadorWs.class, null, value);
    }

}
