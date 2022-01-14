package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroSalidaResponseWs;
import es.caib.regweb3.ws.model.RegistroSalidaWs;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;

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
     * Crea un {@link es.caib.regweb3.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     *
     * @deprecated  A partir de la versión 3.0.9, es reemplazado por {@link #nuevoRegistroSalida(String, RegistroSalidaWs)}
     */
    @WebMethod
    @Deprecated
    public IdentificadorWs altaRegistroSalida(
            @WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs)
            throws Throwable,WsI18NException, WsValidationException;

    /**
     * Crea un {@link es.caib.regweb3.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    @Deprecated
    public IdentificadorWs nuevoRegistroSalida(@WebParam(name = "entidad")String entidad,
        @WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs)
        throws Throwable,WsI18NException, WsValidationException;

    /**
     * Obtiene el Justificante del {@link es.caib.regweb3.model.RegistroSalida} indicado, si es la primera vez lo generará
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad")String entidad, @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;


    /**
     * Anula un {@link es.caib.regweb3.model.RegistroSalida}
     * @param numeroRegistro
     * @param entidad
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    @Deprecated
    public void anularRegistroSalida(
        @WebParam(name = "numeroRegistro") String numeroRegistro,
        @WebParam(name = "entidad") String entidad,
        @WebParam(name = "anular") boolean anular)
    throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene un {@link es.caib.regweb3.model.RegistroSalida}
     * @param numeroRegistro
     * @param entidad
     * @return
     * @throws Throwable
     * @throws org.fundaciobit.genapp.common.ws.WsI18NException
     * @throws org.fundaciobit.genapp.common.ws.WsValidationException
     */
    @WebMethod
    @Deprecated
    public RegistroSalidaResponseWs obtenerRegistroSalida(@WebParam(name = "numeroRegistro") String numeroRegistro, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException;
    
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
    @WebMethod
    @Deprecated
    public IdentificadorWs obtenerRegistroSalidaID(
         @WebParam(name = "any")int any,
         @WebParam(name = "numeroRegistro")int numeroRegistro,
         @WebParam(name = "libro")String libro,
         @WebParam(name = "usuario")String usuario,
         @WebParam(name = "entidad")String entidad) 
          throws Throwable, WsI18NException;

    @WebMethod
    @Deprecated
    public String getVersion();


    @WebMethod
    @Deprecated
    public int getVersionWs();
}
