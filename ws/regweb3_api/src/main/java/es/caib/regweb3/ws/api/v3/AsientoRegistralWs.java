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
 * 2019-04-02T13:20:26.567+02:00
 * Generated source version: 2.6.4
 * 
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "AsientoRegistralWs")
@XmlSeeAlso({ObjectFactory.class})
public interface AsientoRegistralWs {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerOficioExterno", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerOficioExterno")
    @WebMethod
    @ResponseWrapper(localName = "obtenerOficioExternoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerOficioExternoResponse")
    public es.caib.regweb3.ws.api.v3.OficioWs obtenerOficioExterno(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "libro", targetNamespace = "")
        java.lang.String libro
    ) throws WsValidationException, WsI18NException;

    @RequestWrapper(localName = "distribuirAsientoRegistral", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirAsientoRegistral")
    @WebMethod
    @ResponseWrapper(localName = "distribuirAsientoRegistralResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.DistribuirAsientoRegistralResponse")
    public void distribuirAsientoRegistral(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "libro", targetNamespace = "")
        java.lang.String libro
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerAsientoRegistral", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerAsientoRegistral")
    @WebMethod
    @ResponseWrapper(localName = "obtenerAsientoRegistralResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerAsientoRegistralResponse")
    public es.caib.regweb3.ws.api.v3.AsientoRegistralWs_Type obtenerAsientoRegistral(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "libro", targetNamespace = "")
        java.lang.String libro,
        @WebParam(name = "tipoRegistro", targetNamespace = "")
        java.lang.Long tipoRegistro,
        @WebParam(name = "conAnexos", targetNamespace = "")
        boolean conAnexos
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerJustificante", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificante")
    @WebMethod
    @ResponseWrapper(localName = "obtenerJustificanteResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerJustificanteResponse")
    public es.caib.regweb3.ws.api.v3.JustificanteWs obtenerJustificante(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "numeroRegistroFormateado", targetNamespace = "")
        java.lang.String numeroRegistroFormateado,
        @WebParam(name = "libro", targetNamespace = "")
        java.lang.String libro,
        @WebParam(name = "tipoRegistro", targetNamespace = "")
        java.lang.Long tipoRegistro
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "crearAsientoRegistral", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.CrearAsientoRegistral")
    @WebMethod
    @ResponseWrapper(localName = "crearAsientoRegistralResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.CrearAsientoRegistralResponse")
    public es.caib.regweb3.ws.api.v3.AsientoRegistralWs_Type crearAsientoRegistral(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "asientoRegistral", targetNamespace = "")
        es.caib.regweb3.ws.api.v3.AsientoRegistralWs_Type asientoRegistral,
        @WebParam(name = "tipoOperacion", targetNamespace = "")
        java.lang.Long tipoOperacion
    ) throws WsValidationException, WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerAsientosCiudadano", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerAsientosCiudadano")
    @WebMethod
    @ResponseWrapper(localName = "obtenerAsientosCiudadanoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerAsientosCiudadanoResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.AsientoRegistralWs_Type> obtenerAsientosCiudadano(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "documento", targetNamespace = "")
        java.lang.String documento
    ) throws WsValidationException, WsI18NException;
}
