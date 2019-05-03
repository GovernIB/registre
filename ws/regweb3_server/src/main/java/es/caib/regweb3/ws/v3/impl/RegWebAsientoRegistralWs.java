package es.caib.regweb3.ws.v3.impl;


import es.caib.regweb3.ws.model.AsientoRegistralWs;
import es.caib.regweb3.ws.model.JustificanteReferenciaWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.OficioWs;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author mgonzalez
 */
@WebService
public interface RegWebAsientoRegistralWs {

    /**
     * Crea un {@link es.caib.regweb3.ws.model.AsientoRegistralWs }
     * @param entidad
     * @param asientoRegistral
     * @return
     */
    @WebMethod
    AsientoRegistralWs crearAsientoRegistral(
            @WebParam(name = "entidad")String entidad,
            @WebParam(name = "asientoRegistral") AsientoRegistralWs asientoRegistral,
            @WebParam(name = "tipoOperacion") Long tipoOperacion)throws Throwable, WsI18NException, WsValidationException;


    /**
     * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs }
     * Se podrá obtener con los anexos o sin los anexos. conAnexos=true los devuelve, conAnexos=false no
     * @param numeroRegistroFormateado
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    AsientoRegistralWs obtenerAsientoRegistral(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "tipoRegistro") Long tipoRegistro,
            @WebParam(name = "conAnexos") boolean conAnexos) throws  Throwable, WsI18NException, WsValidationException;


    /**
     * Obtiene el Justificante del {@link es.caib.regweb3.ws.model.AsientoRegistralWs } indicado, si es la primera vez lo generará
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    JustificanteWs obtenerJustificante(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
            @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene la referencia del Justificante del {@link es.caib.regweb3.ws.model.AsientoRegistralWs } indicado, si es la primera vez lo generará
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    JustificanteReferenciaWs obtenerReferenciaJustificante(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Distribuye el {@link es.caib.regweb3.ws.model.AsientoRegistralWs }
     * @param numeroRegistroFormateado
     * @param entidad
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    void distribuirAsientoRegistral(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;



    /**
     * Obtiene el documento del oficioExterno del asiento indicado por numeroRegistroFormateado.
     * Este asiento debe formar parte de un Oficio Externo.
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    OficioWs obtenerOficioExterno(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene los Asientos registral de un ciudadano
     * @param entidad
     * @param documento
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    List<AsientoRegistralWs> obtenerAsientosCiudadano(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento) throws Throwable, WsI18NException, WsValidationException;
}
