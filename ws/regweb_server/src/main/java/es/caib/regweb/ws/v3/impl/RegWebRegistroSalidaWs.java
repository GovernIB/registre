package es.caib.regweb.ws.v3.impl;

import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.IdentificadorWs;
import es.caib.regweb.ws.model.RegistroSalidaWs;

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
public interface RegWebRegistroSalidaWs /*extends IBaseWs*/ {

    /**
     * Crea un {@link es.caib.regweb.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    public IdentificadorWs altaRegistroSalida(
        @WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs)
        throws Throwable,WsI18NException, WsValidationException;


    /**
     * Anula un {@link es.caib.regweb.model.RegistroSalida}
     * @param numeroRegistro
     * @param usuario
     * @param entidad
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    public void anularRegistroSalida(
        @WebParam(name = "numeroRegistro") String numeroRegistro,
        @WebParam(name = "usuario") String usuario,
        @WebParam(name = "entidad") String entidad,
        @WebParam(name = "anular") boolean anular)
    throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene un {@link es.caib.regweb.model.RegistroSalida}
     * @param numeroRegistro
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    public RegistroSalidaWs obtenerRegistroSalida(@WebParam(name = "numeroRegistro") String numeroRegistro, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException;
    
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
    public IdentificadorWs obtenerRegistroSalidaID(
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
