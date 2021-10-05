package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroEntradaResponseWs;
import es.caib.regweb3.ws.model.RegistroEntradaWs;
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
public interface RegWebRegistroEntradaWs /*extends IBaseWs*/ {

    /**
     * Crea un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param registroEntradaWs
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     *
     * @deprecated  A partir de la versión 3.0.9, es reemplazado por {@link #nuevoRegistroEntrada(String, RegistroEntradaWs)}
     */
    @WebMethod
    @Deprecated
    public IdentificadorWs altaRegistroEntrada(@WebParam(name = "registroEntradaWs")RegistroEntradaWs registroEntradaWs) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Crea un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param registroEntradaWs
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public IdentificadorWs nuevoRegistroEntrada(@WebParam(name = "entidad")String entidad, @WebParam(name = "registroEntradaWs")RegistroEntradaWs registroEntradaWs) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene el Justificante del {@link es.caib.regweb3.model.RegistroEntrada} indicado, si es la primera vez lo generará
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad") String entidad, @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Anula un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param entidad
     * @param anular Si true, anula el registro, si es false, entonces quita la anulacion.
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public void anularRegistroEntrada(
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "anular") boolean anular)
            throws Throwable, WsI18NException, WsValidationException;

    /**
     * Tramita un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param entidad
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public void tramitarRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException;

    /**
     *
     * @param numeroRegistroFormateado
     * @param entidad
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public void distribuirRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException ;

    /**
     * Obtiene un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param numeroRegistroFormateado
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    @Deprecated
    public RegistroEntradaResponseWs obtenerRegistroEntrada(
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad)
            throws Throwable, WsI18NException, WsValidationException;

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
    public IdentificadorWs obtenerRegistroEntradaID(
         @WebParam(name = "any")int any,
         @WebParam(name = "numeroRegistro")String numeroRegistro,
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
