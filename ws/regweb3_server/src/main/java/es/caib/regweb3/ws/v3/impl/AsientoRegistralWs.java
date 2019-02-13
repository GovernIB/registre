package es.caib.regweb3.ws.v3.impl;


import es.caib.regweb3.ws.model.AsientoRegistralBean;
import es.caib.regweb3.ws.model.JustificanteWs;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author mgonzalez
 */
@WebService
public interface AsientoRegistralWs {

    /**
     *
     * @param entidad
     * @param asientoRegistral
     * @return
     */
    @WebMethod
    AsientoRegistralBean crearAsientoRegistral(
            @WebParam(name = "entidad")String entidad,
            @WebParam(name = "asientoRegistral")AsientoRegistralBean asientoRegistral,
            @WebParam(name = "tipoOperacion") Long tipoOperacion)throws WsI18NException, WsValidationException;

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
    AsientoRegistralBean obtenerAsientoRegistral(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "libro") String libro) throws WsI18NException, WsValidationException;

    /**
     * Obtiene el Justificante del {@link es.caib.regweb3.model.RegistroEntrada} indicado, si es la primera vez lo generará
     * @param entidad
     * @param numeroRegistroFormateado
     * @param libro
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    JustificanteWs obtenerJustificante(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
            @WebParam(name = "libro") String libro) throws WsI18NException, WsValidationException;

    /**
     *
     * @param numeroRegistroFormateado
     * @param entidad
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    void distribuirAsientoRegistral(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "libro") String libro) throws  WsI18NException, WsValidationException ;
}
