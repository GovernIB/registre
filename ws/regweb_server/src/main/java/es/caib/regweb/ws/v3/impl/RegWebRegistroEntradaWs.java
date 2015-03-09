package es.caib.regweb.ws.v3.impl;

import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.IdentificadorWs;
import es.caib.regweb.ws.model.RegistroEntradaWs;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@WebService
public interface RegWebRegistroEntradaWs /*extends IBaseWs*/ {

    /**
     * Crea un {@link es.caib.regweb.model.RegistroEntrada}
     * @param registroEntradaWs
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    public IdentificadorWs altaRegistroEntrada(@WebParam(name = "registroEntradaWs")RegistroEntradaWs registroEntradaWs) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Anula un {@link es.caib.regweb.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param usuario
     * @param entidad
     * @param anular Si true, anula el registro, si es false, entonces quita la anulacion.
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    public void anularRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
        @WebParam(name = "usuario")String usuario,
        @WebParam(name = "entidad")String entidad,
        @WebParam(name = "anular") boolean anular)
        throws Throwable, WsI18NException, WsValidationException;

    /**
     * Tramita un {@link es.caib.regweb.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param usuario
     * @param entidad
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    public void tramitarRegistroEntrada(@WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado, @WebParam(name = "usuario")String usuario, @WebParam(name = "entidad")String entidad) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene un {@link es.caib.regweb.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    @WebMethod
    public RegistroEntradaWs obtenerRegistroEntrada(@WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado, @WebParam(name = "usuario")String usuario, @WebParam(name = "entidad")String entidad) throws Throwable, WsI18NException, WsValidationException;
    
    /**
     * 
     * @param any
     * @param numeroRegistro
     * @param libro
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    @WebMethod
    public IdentificadorWs obtenerRegistroEntradaID(
         @WebParam(name = "any")int any,
         @WebParam(name = "numeroRegistro")int numeroRegistro,
         @WebParam(name = "libro")String libro,
         @WebParam(name = "usuario")String usuario,
         @WebParam(name = "entidad")String entidad) 
          throws Throwable, WsI18NException;

    

    @WebMethod
    public String getVersion();


    @WebMethod
    public int getVersionWs();
}
