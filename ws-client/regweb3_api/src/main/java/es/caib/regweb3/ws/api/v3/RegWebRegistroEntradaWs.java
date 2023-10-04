package es.caib.regweb3.ws.api.v3;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.5.redhat-00001
 * 2023-10-04T12:40:52.986+02:00
 * Generated source version: 3.2.5.redhat-00001
 *
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "RegWebRegistroEntradaWs")
@XmlSeeAlso({ObjectFactory.class})
public interface RegWebRegistroEntradaWs {

    @WebMethod
    @RequestWrapper(localName = "obtenerJustificante", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificante")
    @ResponseWrapper(localName = "obtenerJustificanteResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificanteResponse")
    @WebResult(name = "return", targetNamespace = "")
    public es.caib.regweb3.ws.api.v3.JustificanteWs obtenerJustificante(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "obtenerRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntrada")
    @ResponseWrapper(localName = "obtenerRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaResponse")
    @WebResult(name = "return", targetNamespace = "")
    public es.caib.regweb3.ws.api.v3.RegistroEntradaResponseWs obtenerRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "anularRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroEntrada")
    @ResponseWrapper(localName = "anularRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroEntradaResponse")
    public void anularRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "anular", targetNamespace = "")
        boolean anular
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "altaRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroEntrada")
    @ResponseWrapper(localName = "altaRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroEntradaResponse")
    @WebResult(name = "return", targetNamespace = "")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs altaRegistroEntrada(
        @WebParam(name = "registroEntradaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroEntradaWs registroEntradaWs
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String getVersion();

    @WebMethod
    @RequestWrapper(localName = "getVersionWs", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWs")
    @ResponseWrapper(localName = "getVersionWsResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWsResponse")
    @WebResult(name = "return", targetNamespace = "")
    public int getVersionWs();

    @WebMethod
    @RequestWrapper(localName = "obtenerRegistroEntradaID", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaID")
    @ResponseWrapper(localName = "obtenerRegistroEntradaIDResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaIDResponse")
    @WebResult(name = "return", targetNamespace = "")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs obtenerRegistroEntradaID(
        @WebParam(name = "any", targetNamespace = "")
        int any,
        @WebParam(name = "numeroRegistro", targetNamespace = "")
        int numeroRegistro,
        @WebParam(name = "libro", targetNamespace = "")
        java.lang.String libro,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "tramitarRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.TramitarRegistroEntrada")
    @ResponseWrapper(localName = "tramitarRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.TramitarRegistroEntradaResponse")
    public void tramitarRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "distribuirRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirRegistroEntrada")
    @ResponseWrapper(localName = "distribuirRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirRegistroEntradaResponse")
    public void distribuirRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;

    @WebMethod
    @RequestWrapper(localName = "nuevoRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroEntrada")
    @ResponseWrapper(localName = "nuevoRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroEntradaResponse")
    @WebResult(name = "return", targetNamespace = "")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs nuevoRegistroEntrada(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "registroEntradaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroEntradaWs registroEntradaWs
    ) throws WsValidationException, WsI18NException;
}
