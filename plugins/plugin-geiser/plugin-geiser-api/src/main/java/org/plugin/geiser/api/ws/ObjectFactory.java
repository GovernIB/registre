
package org.plugin.geiser.api.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.plugin.geiser.api.ws package. 
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

    private final static QName _RechazarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "rechazarResponse");
    private final static QName _ResultadoConsultaType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "resultadoConsultaType");
    private final static QName _BuscarEstadoTramitacion_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "buscarEstadoTramitacion");
    private final static QName _PeticionCambioEstadoType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "peticionCambioEstadoType");
    private final static QName _ConfirmarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "confirmarResponse");
    private final static QName _PeticionBusquedaEstadoTramitacionType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "peticionBusquedaEstadoTramitacionType");
    private final static QName _RespuestaType_QNAME = new QName("http://types.core.ws.rgeco.geiser.minhap.gob.es/", "respuestaType");
    private final static QName _FormularioType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "formularioType");
    private final static QName _Consultar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "consultar");
    private final static QName _RegistrarEnviarHastaUnidad_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrarEnviarHastaUnidad");
    private final static QName _ConsultarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "consultarResponse");
    private final static QName _Registrar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrar");
    private final static QName _AuthenticationType_QNAME = new QName("http://types.core.ws.rgeco.geiser.minhap.gob.es/", "authenticationType");
    private final static QName _Confirmar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "confirmar");
    private final static QName _SeccionType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "seccionType");
    private final static QName _BasePeticionRegistroType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "basePeticionRegistroType");
    private final static QName _PeticionConsultaType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "peticionConsultaType");
    private final static QName _ResultadoBusquedaEstadoTramitacionType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "resultadoBusquedaEstadoTramitacionType");
    private final static QName _ApunteRegistroType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "apunteRegistroType");
    private final static QName _ResultadoCambioEstadoType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "resultadoCambioEstadoType");
    private final static QName _RegistrarEnviarHastaUnidadResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrarEnviarHastaUnidadResponse");
    private final static QName _RegistrarEnviarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrarEnviarResponse");
    private final static QName _AnexoType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "anexoType");
    private final static QName _ResultadoBusquedaType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "resultadoBusquedaType");
    private final static QName _Rechazar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "rechazar");
    private final static QName _IterarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "iterarResponse");
    private final static QName _InteresadoType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "interesadoType");
    private final static QName _Buscar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "buscar");
    private final static QName _BuscarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "buscarResponse");
    private final static QName _PeticionBusquedaType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "peticionBusquedaType");
    private final static QName _ResultadoRegistroType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "resultadoRegistroType");
    private final static QName _EstadoTramitacionRegistroType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "estadoTramitacionRegistroType");
    private final static QName _RegistrarResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrarResponse");
    private final static QName _RegistrarEnviar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "registrarEnviar");
    private final static QName _CampoType_QNAME = new QName("http://types.registro.ws.rgeco.geiser.minhap.gob.es/", "campoType");
    private final static QName _Iterar_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "iterar");
    private final static QName _BuscarEstadoTramitacionResponse_QNAME = new QName("http://registro.ws.rgeco.geiser.minhap.gob.es/", "buscarEstadoTramitacionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.plugin.geiser.api.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ApunteRegistroType }
     * 
     */
    public ApunteRegistroType createApunteRegistroType() {
        return new ApunteRegistroType();
    }

    /**
     * Create an instance of {@link ResultadoBusquedaEstadoTramitacionType }
     * 
     */
    public ResultadoBusquedaEstadoTramitacionType createResultadoBusquedaEstadoTramitacionType() {
        return new ResultadoBusquedaEstadoTramitacionType();
    }

    /**
     * Create an instance of {@link PeticionConsultaType }
     * 
     */
    public PeticionConsultaType createPeticionConsultaType() {
        return new PeticionConsultaType();
    }

    /**
     * Create an instance of {@link SeccionType }
     * 
     */
    public SeccionType createSeccionType() {
        return new SeccionType();
    }

    /**
     * Create an instance of {@link InteresadoType }
     * 
     */
    public InteresadoType createInteresadoType() {
        return new InteresadoType();
    }

    /**
     * Create an instance of {@link ResultadoBusquedaType }
     * 
     */
    public ResultadoBusquedaType createResultadoBusquedaType() {
        return new ResultadoBusquedaType();
    }

    /**
     * Create an instance of {@link AnexoType }
     * 
     */
    public AnexoType createAnexoType() {
        return new AnexoType();
    }

    /**
     * Create an instance of {@link ResultadoCambioEstadoType }
     * 
     */
    public ResultadoCambioEstadoType createResultadoCambioEstadoType() {
        return new ResultadoCambioEstadoType();
    }

    /**
     * Create an instance of {@link CampoType }
     * 
     */
    public CampoType createCampoType() {
        return new CampoType();
    }

    /**
     * Create an instance of {@link PeticionCambioEstadoType }
     * 
     */
    public PeticionCambioEstadoType createPeticionCambioEstadoType() {
        return new PeticionCambioEstadoType();
    }

    /**
     * Create an instance of {@link ResultadoConsultaType }
     * 
     */
    public ResultadoConsultaType createResultadoConsultaType() {
        return new ResultadoConsultaType();
    }

    /**
     * Create an instance of {@link ResultadoRegistroType }
     * 
     */
    public ResultadoRegistroType createResultadoRegistroType() {
        return new ResultadoRegistroType();
    }

    /**
     * Create an instance of {@link EstadoTramitacionRegistroType }
     * 
     */
    public EstadoTramitacionRegistroType createEstadoTramitacionRegistroType() {
        return new EstadoTramitacionRegistroType();
    }

    /**
     * Create an instance of {@link PeticionBusquedaType }
     * 
     */
    public PeticionBusquedaType createPeticionBusquedaType() {
        return new PeticionBusquedaType();
    }

    /**
     * Create an instance of {@link FormularioType }
     * 
     */
    public FormularioType createFormularioType() {
        return new FormularioType();
    }

    /**
     * Create an instance of {@link PeticionBusquedaEstadoTramitacionType }
     * 
     */
    public PeticionBusquedaEstadoTramitacionType createPeticionBusquedaEstadoTramitacionType() {
        return new PeticionBusquedaEstadoTramitacionType();
    }

    /**
     * Create an instance of {@link PeticionRegistroEnvioType }
     * 
     */
    public PeticionRegistroEnvioType createPeticionRegistroEnvioType() {
        return new PeticionRegistroEnvioType();
    }

    /**
     * Create an instance of {@link PeticionRegistroEnvioSimpleType }
     * 
     */
    public PeticionRegistroEnvioSimpleType createPeticionRegistroEnvioSimpleType() {
        return new PeticionRegistroEnvioSimpleType();
    }

    /**
     * Create an instance of {@link PeticionRegistroType }
     * 
     */
    public PeticionRegistroType createPeticionRegistroType() {
        return new PeticionRegistroType();
    }

    /**
     * Create an instance of {@link RespuestaType }
     * 
     */
    public RespuestaType createRespuestaType() {
        return new RespuestaType();
    }

    /**
     * Create an instance of {@link AuthenticationType }
     * 
     */
    public AuthenticationType createAuthenticationType() {
        return new AuthenticationType();
    }

    /**
     * Create an instance of {@link RegistrarEnviarHastaUnidad }
     * 
     */
    public RegistrarEnviarHastaUnidad createRegistrarEnviarHastaUnidad() {
        return new RegistrarEnviarHastaUnidad();
    }

    /**
     * Create an instance of {@link Consultar }
     * 
     */
    public Consultar createConsultar() {
        return new Consultar();
    }

    /**
     * Create an instance of {@link BuscarResponse }
     * 
     */
    public BuscarResponse createBuscarResponse() {
        return new BuscarResponse();
    }

    /**
     * Create an instance of {@link Confirmar }
     * 
     */
    public Confirmar createConfirmar() {
        return new Confirmar();
    }

    /**
     * Create an instance of {@link Registrar }
     * 
     */
    public Registrar createRegistrar() {
        return new Registrar();
    }

    /**
     * Create an instance of {@link ConsultarResponse }
     * 
     */
    public ConsultarResponse createConsultarResponse() {
        return new ConsultarResponse();
    }

    /**
     * Create an instance of {@link BuscarEstadoTramitacion }
     * 
     */
    public BuscarEstadoTramitacion createBuscarEstadoTramitacion() {
        return new BuscarEstadoTramitacion();
    }

    /**
     * Create an instance of {@link RechazarResponse }
     * 
     */
    public RechazarResponse createRechazarResponse() {
        return new RechazarResponse();
    }

    /**
     * Create an instance of {@link Buscar }
     * 
     */
    public Buscar createBuscar() {
        return new Buscar();
    }

    /**
     * Create an instance of {@link ConfirmarResponse }
     * 
     */
    public ConfirmarResponse createConfirmarResponse() {
        return new ConfirmarResponse();
    }

    /**
     * Create an instance of {@link RegistrarEnviarResponse }
     * 
     */
    public RegistrarEnviarResponse createRegistrarEnviarResponse() {
        return new RegistrarEnviarResponse();
    }

    /**
     * Create an instance of {@link RegistrarEnviarHastaUnidadResponse }
     * 
     */
    public RegistrarEnviarHastaUnidadResponse createRegistrarEnviarHastaUnidadResponse() {
        return new RegistrarEnviarHastaUnidadResponse();
    }

    /**
     * Create an instance of {@link Iterar }
     * 
     */
    public Iterar createIterar() {
        return new Iterar();
    }

    /**
     * Create an instance of {@link RegistrarEnviar }
     * 
     */
    public RegistrarEnviar createRegistrarEnviar() {
        return new RegistrarEnviar();
    }

    /**
     * Create an instance of {@link IterarResponse }
     * 
     */
    public IterarResponse createIterarResponse() {
        return new IterarResponse();
    }

    /**
     * Create an instance of {@link Rechazar }
     * 
     */
    public Rechazar createRechazar() {
        return new Rechazar();
    }

    /**
     * Create an instance of {@link BuscarEstadoTramitacionResponse }
     * 
     */
    public BuscarEstadoTramitacionResponse createBuscarEstadoTramitacionResponse() {
        return new BuscarEstadoTramitacionResponse();
    }

    /**
     * Create an instance of {@link RegistrarResponse }
     * 
     */
    public RegistrarResponse createRegistrarResponse() {
        return new RegistrarResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechazarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "rechazarResponse")
    public JAXBElement<RechazarResponse> createRechazarResponse(RechazarResponse value) {
        return new JAXBElement<RechazarResponse>(_RechazarResponse_QNAME, RechazarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoConsultaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "resultadoConsultaType")
    public JAXBElement<ResultadoConsultaType> createResultadoConsultaType(ResultadoConsultaType value) {
        return new JAXBElement<ResultadoConsultaType>(_ResultadoConsultaType_QNAME, ResultadoConsultaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarEstadoTramitacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "buscarEstadoTramitacion")
    public JAXBElement<BuscarEstadoTramitacion> createBuscarEstadoTramitacion(BuscarEstadoTramitacion value) {
        return new JAXBElement<BuscarEstadoTramitacion>(_BuscarEstadoTramitacion_QNAME, BuscarEstadoTramitacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionCambioEstadoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "peticionCambioEstadoType")
    public JAXBElement<PeticionCambioEstadoType> createPeticionCambioEstadoType(PeticionCambioEstadoType value) {
        return new JAXBElement<PeticionCambioEstadoType>(_PeticionCambioEstadoType_QNAME, PeticionCambioEstadoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "confirmarResponse")
    public JAXBElement<ConfirmarResponse> createConfirmarResponse(ConfirmarResponse value) {
        return new JAXBElement<ConfirmarResponse>(_ConfirmarResponse_QNAME, ConfirmarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionBusquedaEstadoTramitacionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "peticionBusquedaEstadoTramitacionType")
    public JAXBElement<PeticionBusquedaEstadoTramitacionType> createPeticionBusquedaEstadoTramitacionType(PeticionBusquedaEstadoTramitacionType value) {
        return new JAXBElement<PeticionBusquedaEstadoTramitacionType>(_PeticionBusquedaEstadoTramitacionType_QNAME, PeticionBusquedaEstadoTramitacionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.core.ws.rgeco.geiser.minhap.gob.es/", name = "respuestaType")
    public JAXBElement<RespuestaType> createRespuestaType(RespuestaType value) {
        return new JAXBElement<RespuestaType>(_RespuestaType_QNAME, RespuestaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FormularioType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "formularioType")
    public JAXBElement<FormularioType> createFormularioType(FormularioType value) {
        return new JAXBElement<FormularioType>(_FormularioType_QNAME, FormularioType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Consultar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "consultar")
    public JAXBElement<Consultar> createConsultar(Consultar value) {
        return new JAXBElement<Consultar>(_Consultar_QNAME, Consultar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrarEnviarHastaUnidad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrarEnviarHastaUnidad")
    public JAXBElement<RegistrarEnviarHastaUnidad> createRegistrarEnviarHastaUnidad(RegistrarEnviarHastaUnidad value) {
        return new JAXBElement<RegistrarEnviarHastaUnidad>(_RegistrarEnviarHastaUnidad_QNAME, RegistrarEnviarHastaUnidad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "consultarResponse")
    public JAXBElement<ConsultarResponse> createConsultarResponse(ConsultarResponse value) {
        return new JAXBElement<ConsultarResponse>(_ConsultarResponse_QNAME, ConsultarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Registrar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrar")
    public JAXBElement<Registrar> createRegistrar(Registrar value) {
        return new JAXBElement<Registrar>(_Registrar_QNAME, Registrar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.core.ws.rgeco.geiser.minhap.gob.es/", name = "authenticationType")
    public JAXBElement<AuthenticationType> createAuthenticationType(AuthenticationType value) {
        return new JAXBElement<AuthenticationType>(_AuthenticationType_QNAME, AuthenticationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Confirmar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "confirmar")
    public JAXBElement<Confirmar> createConfirmar(Confirmar value) {
        return new JAXBElement<Confirmar>(_Confirmar_QNAME, Confirmar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SeccionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "seccionType")
    public JAXBElement<SeccionType> createSeccionType(SeccionType value) {
        return new JAXBElement<SeccionType>(_SeccionType_QNAME, SeccionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasePeticionRegistroType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "basePeticionRegistroType")
    public JAXBElement<BasePeticionRegistroType> createBasePeticionRegistroType(BasePeticionRegistroType value) {
        return new JAXBElement<BasePeticionRegistroType>(_BasePeticionRegistroType_QNAME, BasePeticionRegistroType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionConsultaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "peticionConsultaType")
    public JAXBElement<PeticionConsultaType> createPeticionConsultaType(PeticionConsultaType value) {
        return new JAXBElement<PeticionConsultaType>(_PeticionConsultaType_QNAME, PeticionConsultaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoBusquedaEstadoTramitacionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "resultadoBusquedaEstadoTramitacionType")
    public JAXBElement<ResultadoBusquedaEstadoTramitacionType> createResultadoBusquedaEstadoTramitacionType(ResultadoBusquedaEstadoTramitacionType value) {
        return new JAXBElement<ResultadoBusquedaEstadoTramitacionType>(_ResultadoBusquedaEstadoTramitacionType_QNAME, ResultadoBusquedaEstadoTramitacionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApunteRegistroType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "apunteRegistroType")
    public JAXBElement<ApunteRegistroType> createApunteRegistroType(ApunteRegistroType value) {
        return new JAXBElement<ApunteRegistroType>(_ApunteRegistroType_QNAME, ApunteRegistroType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoCambioEstadoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "resultadoCambioEstadoType")
    public JAXBElement<ResultadoCambioEstadoType> createResultadoCambioEstadoType(ResultadoCambioEstadoType value) {
        return new JAXBElement<ResultadoCambioEstadoType>(_ResultadoCambioEstadoType_QNAME, ResultadoCambioEstadoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrarEnviarHastaUnidadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrarEnviarHastaUnidadResponse")
    public JAXBElement<RegistrarEnviarHastaUnidadResponse> createRegistrarEnviarHastaUnidadResponse(RegistrarEnviarHastaUnidadResponse value) {
        return new JAXBElement<RegistrarEnviarHastaUnidadResponse>(_RegistrarEnviarHastaUnidadResponse_QNAME, RegistrarEnviarHastaUnidadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrarEnviarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrarEnviarResponse")
    public JAXBElement<RegistrarEnviarResponse> createRegistrarEnviarResponse(RegistrarEnviarResponse value) {
        return new JAXBElement<RegistrarEnviarResponse>(_RegistrarEnviarResponse_QNAME, RegistrarEnviarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnexoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "anexoType")
    public JAXBElement<AnexoType> createAnexoType(AnexoType value) {
        return new JAXBElement<AnexoType>(_AnexoType_QNAME, AnexoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoBusquedaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "resultadoBusquedaType")
    public JAXBElement<ResultadoBusquedaType> createResultadoBusquedaType(ResultadoBusquedaType value) {
        return new JAXBElement<ResultadoBusquedaType>(_ResultadoBusquedaType_QNAME, ResultadoBusquedaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rechazar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "rechazar")
    public JAXBElement<Rechazar> createRechazar(Rechazar value) {
        return new JAXBElement<Rechazar>(_Rechazar_QNAME, Rechazar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IterarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "iterarResponse")
    public JAXBElement<IterarResponse> createIterarResponse(IterarResponse value) {
        return new JAXBElement<IterarResponse>(_IterarResponse_QNAME, IterarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InteresadoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "interesadoType")
    public JAXBElement<InteresadoType> createInteresadoType(InteresadoType value) {
        return new JAXBElement<InteresadoType>(_InteresadoType_QNAME, InteresadoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Buscar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "buscar")
    public JAXBElement<Buscar> createBuscar(Buscar value) {
        return new JAXBElement<Buscar>(_Buscar_QNAME, Buscar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "buscarResponse")
    public JAXBElement<BuscarResponse> createBuscarResponse(BuscarResponse value) {
        return new JAXBElement<BuscarResponse>(_BuscarResponse_QNAME, BuscarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionBusquedaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "peticionBusquedaType")
    public JAXBElement<PeticionBusquedaType> createPeticionBusquedaType(PeticionBusquedaType value) {
        return new JAXBElement<PeticionBusquedaType>(_PeticionBusquedaType_QNAME, PeticionBusquedaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoRegistroType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "resultadoRegistroType")
    public JAXBElement<ResultadoRegistroType> createResultadoRegistroType(ResultadoRegistroType value) {
        return new JAXBElement<ResultadoRegistroType>(_ResultadoRegistroType_QNAME, ResultadoRegistroType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstadoTramitacionRegistroType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "estadoTramitacionRegistroType")
    public JAXBElement<EstadoTramitacionRegistroType> createEstadoTramitacionRegistroType(EstadoTramitacionRegistroType value) {
        return new JAXBElement<EstadoTramitacionRegistroType>(_EstadoTramitacionRegistroType_QNAME, EstadoTramitacionRegistroType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrarResponse")
    public JAXBElement<RegistrarResponse> createRegistrarResponse(RegistrarResponse value) {
        return new JAXBElement<RegistrarResponse>(_RegistrarResponse_QNAME, RegistrarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrarEnviar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "registrarEnviar")
    public JAXBElement<RegistrarEnviar> createRegistrarEnviar(RegistrarEnviar value) {
        return new JAXBElement<RegistrarEnviar>(_RegistrarEnviar_QNAME, RegistrarEnviar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CampoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://types.registro.ws.rgeco.geiser.minhap.gob.es/", name = "campoType")
    public JAXBElement<CampoType> createCampoType(CampoType value) {
        return new JAXBElement<CampoType>(_CampoType_QNAME, CampoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Iterar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "iterar")
    public JAXBElement<Iterar> createIterar(Iterar value) {
        return new JAXBElement<Iterar>(_Iterar_QNAME, Iterar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarEstadoTramitacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", name = "buscarEstadoTramitacionResponse")
    public JAXBElement<BuscarEstadoTramitacionResponse> createBuscarEstadoTramitacionResponse(BuscarEstadoTramitacionResponse value) {
        return new JAXBElement<BuscarEstadoTramitacionResponse>(_BuscarEstadoTramitacionResponse_QNAME, BuscarEstadoTramitacionResponse.class, null, value);
    }

}
