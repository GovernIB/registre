package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.ws.model.*;
import org.fundaciobit.genapp.common.ws.WsI18NException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author anadal
 */
@WebService
public interface RegWebInfoWs {


    /**
     * Obtiene todos los TipoDocumental de una Entidad
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @WebMethod
    List<TipoDocumentalWs> listarTipoDocumental(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
            WsI18NException;
    /**
     * Obtiene todos los Tipos Asunto de una Entidad
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @WebMethod
    @Deprecated
    List<TipoAsuntoWs> listarTipoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
      throws Throwable, WsI18NException;
    

    @WebMethod
    List<CodigoAsuntoWs> listarCodigoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto)
      throws Throwable, WsI18NException;

    /**
     * Obtiene los pares Libro-Oficina en los que el usuario autenticado puede registrar
     * @param entidadCodigoDir3
     * @param tipoRegistro
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    List<LibroOficinaWs> obtenerLibrosOficina(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,@WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException;

    /**
     * Obtiene los pares Libro-Oficina en los que el usuario indicado puede registrar
     * @param entidadCodigoDir3
     * @param usuario
     * @param tipoRegistro
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    List<LibroOficinaWs> obtenerLibrosOficinaUsuario(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "usuario") String usuario, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException;
    /**
     * Obtiene las Oficinas donde el usuario tiene permisos para realizar Registros
     * @param entidadCodigoDir3
     * @param autorizacion
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @WebMethod
    List<OficinaWs> listarOficinas(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                          @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException;

    /**
     * Obtiene los Libros a los que una Oficina da servicio y en los que el UsuarioEntidad actual tiene permisos para registrar entradas
     * @param entidadCodigoDir3
     * @param oficinaCodigoDir3
     * @param autorizacion
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @WebMethod
    List<LibroWs> listarLibros(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
        @WebParam(name = "oficinaCodigoDir3") String oficinaCodigoDir3, @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException;


    /**
     * Obtiene el LibroWs donde puede registrar un Organismo
     * @param entidad
     * @param organismo
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    LibroWs listarLibroOrganismo(@WebParam(name = "entidad") String entidad,
                                               @WebParam(name = "organismo") String organismo) throws Throwable, WsI18NException;

    @WebMethod
    List<OrganismoWs> listarOrganismos(
        @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
        throws Throwable, WsI18NException;


    @WebMethod
    String getVersion();


    @WebMethod
    int getVersionWs();
}
