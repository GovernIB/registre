package es.caib.regweb3.ws.api.v3;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.2
 * 2021-01-27T13:11:08.619+01:00
 * Generated source version: 3.0.2
 * 
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "RegWebRegistroEntradaWs")
@XmlSeeAlso({ObjectFactory.class})
public interface RegWebRegistroEntradaWs {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersion")
    @WebMethod
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionResponse")
    public java.lang.String getVersion();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "obtenerRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaResponse")
    public es.caib.regweb3.ws.api.v3.RegistroEntradaResponseWs obtenerRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerRegistroEntradaID", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaID")
    @WebMethod
    @ResponseWrapper(localName = "obtenerRegistroEntradaIDResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroEntradaIDResponse")
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

    @RequestWrapper(localName = "tramitarRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.TramitarRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "tramitarRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.TramitarRegistroEntradaResponse")
    public void tramitarRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerJustificante", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificante")
    @WebMethod
    @ResponseWrapper(localName = "obtenerJustificanteResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificanteResponse")
    public es.caib.regweb3.ws.api.v3.JustificanteWs obtenerJustificante(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "nuevoRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "nuevoRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroEntradaResponse")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs nuevoRegistroEntrada(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "registroEntradaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroEntradaWs registroEntradaWs
    ) throws WsValidationException, WsI18NException;

    @RequestWrapper(localName = "anularRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "anularRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroEntradaResponse")
    public void anularRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "anular", targetNamespace = "")
        boolean anular
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersionWs", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWs")
    @WebMethod
    @ResponseWrapper(localName = "getVersionWsResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWsResponse")
    public int getVersionWs();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "altaRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "altaRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroEntradaResponse")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs altaRegistroEntrada(
        @WebParam(name = "registroEntradaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroEntradaWs registroEntradaWs
    ) throws WsValidationException, WsI18NException;

    @RequestWrapper(localName = "distribuirRegistroEntrada", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirRegistroEntrada")
    @WebMethod
    @ResponseWrapper(localName = "distribuirRegistroEntradaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirRegistroEntradaResponse")
    public void distribuirRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad
    ) throws WsValidationException, WsI18NException;
}
