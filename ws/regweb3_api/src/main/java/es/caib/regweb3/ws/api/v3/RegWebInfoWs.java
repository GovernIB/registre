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
 * 2021-03-31T09:29:14.843+02:00
 * Generated source version: 3.0.2
 * 
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "RegWebInfoWs")
@XmlSeeAlso({ObjectFactory.class})
public interface RegWebInfoWs {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersion")
    @WebMethod
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionResponse")
    public java.lang.String getVersion();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarCodigoAsunto", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarCodigoAsunto")
    @WebMethod
    @ResponseWrapper(localName = "listarCodigoAsuntoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarCodigoAsuntoResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.CodigoAsuntoWs> listarCodigoAsunto(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3,
        @WebParam(name = "codigoTipoAsunto", targetNamespace = "")
        java.lang.String codigoTipoAsunto
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerLibrosOficinaUsuario", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerLibrosOficinaUsuario")
    @WebMethod
    @ResponseWrapper(localName = "obtenerLibrosOficinaUsuarioResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerLibrosOficinaUsuarioResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.LibroOficinaWs> obtenerLibrosOficinaUsuario(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3,
        @WebParam(name = "usuario", targetNamespace = "")
        java.lang.String usuario,
        @WebParam(name = "tipoRegistro", targetNamespace = "")
        java.lang.Long tipoRegistro
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarLibroOrganismo", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarLibroOrganismo")
    @WebMethod
    @ResponseWrapper(localName = "listarLibroOrganismoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarLibroOrganismoResponse")
    public es.caib.regweb3.ws.api.v3.LibroWs listarLibroOrganismo(
        @WebParam(name = "entidad", targetNamespace = "")
        java.lang.String entidad,
        @WebParam(name = "organismo", targetNamespace = "")
        java.lang.String organismo
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "obtenerLibrosOficina", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerLibrosOficina")
    @WebMethod
    @ResponseWrapper(localName = "obtenerLibrosOficinaResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ObtenerLibrosOficinaResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.LibroOficinaWs> obtenerLibrosOficina(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3,
        @WebParam(name = "tipoRegistro", targetNamespace = "")
        java.lang.Long tipoRegistro
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarLibros", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarLibros")
    @WebMethod
    @ResponseWrapper(localName = "listarLibrosResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarLibrosResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.LibroWs> listarLibros(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3,
        @WebParam(name = "oficinaCodigoDir3", targetNamespace = "")
        java.lang.String oficinaCodigoDir3,
        @WebParam(name = "autorizacion", targetNamespace = "")
        java.lang.Long autorizacion
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersionWs", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWs")
    @WebMethod
    @ResponseWrapper(localName = "getVersionWsResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWsResponse")
    public int getVersionWs();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarTipoAsunto", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarTipoAsunto")
    @WebMethod
    @ResponseWrapper(localName = "listarTipoAsuntoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarTipoAsuntoResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.TipoAsuntoWs> listarTipoAsunto(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarOrganismos", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarOrganismos")
    @WebMethod
    @ResponseWrapper(localName = "listarOrganismosResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarOrganismosResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.OrganismoWs> listarOrganismos(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarOficinas", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarOficinas")
    @WebMethod
    @ResponseWrapper(localName = "listarOficinasResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarOficinasResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.OficinaWs> listarOficinas(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3,
        @WebParam(name = "autorizacion", targetNamespace = "")
        java.lang.Long autorizacion
    ) throws WsI18NException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "listarTipoDocumental", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarTipoDocumental")
    @WebMethod
    @ResponseWrapper(localName = "listarTipoDocumentalResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.ListarTipoDocumentalResponse")
    public java.util.List<es.caib.regweb3.ws.api.v3.TipoDocumentalWs> listarTipoDocumental(
        @WebParam(name = "entidadCodigoDir3", targetNamespace = "")
        java.lang.String entidadCodigoDir3
    ) throws WsI18NException;
}
