package es.caib.regweb3.ws.api.v3;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.4
 * 2019-05-03T12:12:00.479+02:00
 * Generated source version: 2.6.4
 * 
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "RegWebRegistroSalidaWs")
@XmlSeeAlso({ObjectFactory.class})
public interface RegWebRegistroSalidaWs {

    @RequestWrapper(localName = "anularRegistroSalida", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroSalida")
    @WebMethod
    @ResponseWrapper(localName = "anularRegistroSalidaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AnularRegistroSalidaResponse")
    public void anularRegistroSalida(
        @WebParam(name = "numeroRegistro", targetNamespace = "")
        java.lang.String numeroRegistro,
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "anular", targetNamespace = "")
        boolean anular
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersion")
    @WebMethod
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionResponse")
    public java.lang.String getVersion();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerRegistroSalidaID", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroSalidaID")
    @WebMethod
    @ResponseWrapper(localName = "obtenerRegistroSalidaIDResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroSalidaIDResponse")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs obtenerRegistroSalidaID(
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

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerRegistroSalida", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroSalida")
    @WebMethod
    @ResponseWrapper(localName = "obtenerRegistroSalidaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerRegistroSalidaResponse")
    public es.caib.regweb3.ws.api.v3.RegistroSalidaResponseWs obtenerRegistroSalida(
        @WebParam(name = "numeroRegistro", targetNamespace = "")
        java.lang.String numeroRegistro,
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
    @RequestWrapper(localName = "getVersionWs", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWs")
    @WebMethod
    @ResponseWrapper(localName = "getVersionWsResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWsResponse")
    public int getVersionWs();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "altaRegistroSalida", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroSalida")
    @WebMethod
    @ResponseWrapper(localName = "altaRegistroSalidaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.AltaRegistroSalidaResponse")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs altaRegistroSalida(
        @WebParam(name = "registroSalidaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroSalidaWs registroSalidaWs
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "nuevoRegistroSalida", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroSalida")
    @WebMethod
    @ResponseWrapper(localName = "nuevoRegistroSalidaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.NuevoRegistroSalidaResponse")
    public es.caib.regweb3.ws.api.v3.IdentificadorWs nuevoRegistroSalida(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "registroSalidaWs", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.RegistroSalidaWs registroSalidaWs
    ) throws WsValidationException, WsI18NException;
}
