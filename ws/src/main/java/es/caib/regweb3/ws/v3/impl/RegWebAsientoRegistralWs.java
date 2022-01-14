package es.caib.regweb3.ws.v3.impl;


import es.caib.regweb3.ws.model.*;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
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
     * Obtiene un idSesion para realizar un crear un nuevo AsientoRegistral
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    Long obtenerSesionRegistro(@WebParam(name = "entidad") String entidad) throws  Throwable, WsI18NException;

    /**
     * Verifa el estado de un idSesion
     * @param entidad
     * @param idSesion
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    AsientoRegistralSesionWs verificarAsientoRegistral(@WebParam(name = "entidad") String entidad, @WebParam(name = "idSesion") Long idSesion) throws  Throwable, WsI18NException;

    /**
     * Crea un {@link es.caib.regweb3.ws.model.AsientoRegistralWs }
     * @param entidad
     * @param asientoRegistral
     * @return
     */
    @WebMethod
    AsientoRegistralWs crearAsientoRegistral(
            @WebParam(name = "idSesion")Long idSesion,
            @WebParam(name = "entidad")String entidad,
            @WebParam(name = "asientoRegistral") AsientoRegistralWs asientoRegistral,
            @WebParam(name = "tipoOperacion") Long tipoOperacion,
            @WebParam(name = "justificante") Boolean justificante,
            @WebParam(name = "distribuir") Boolean distribuir)throws Throwable, WsI18NException, WsValidationException;


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
     * @param pageNumber
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    ResultadoBusquedaWs obtenerAsientosCiudadano(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento, @WebParam(name = "pageNumber") Integer pageNumber) throws Throwable, WsI18NException, WsValidationException;

    /**
     * Obtiene el AsiendoRegistral de un ciudadano a partir de su número de registro
     * @param entidad
     * @param documento
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    AsientoRegistralWs obtenerAsientoCiudadano(@WebParam(name = "entidad") String entidad,  @WebParam(name = "documento") String documento, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException;

    /**
     *
     * @param entidad
     * @param documento
     * @param pageNumber
     * @param idioma
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @WebMethod
    ResultadoBusquedaWs obtenerAsientosCiudadanoCarpeta(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento, @WebParam(name = "pageNumber") Integer pageNumber, @WebParam(name = "idioma") String idioma, @WebParam(name = "fechaInicio") Date fechaInicio, @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "estados") List<Integer> estados, @WebParam(name = "extracto") String extracto, @WebParam(name = "resultPorPagina") Integer resultPorPagina) throws Throwable, WsI18NException, WsValidationException;

    /**
     *
     * @param entidad
     * @param documento
     * @param numeroRegistroFormateado
     * @param idioma
     * @return
     * @throws Throwable
     */
    @WebMethod
    AsientoWs obtenerAsientoCiudadanoCarpeta(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "idioma") String idioma) throws Throwable;

    /**
     *
     * @param entidad
     * @param idAnexo
     * @param idioma
     * @return
     * @throws Throwable
     */
    @WebMethod
    FileContentWs obtenerAnexoCiudadano(@WebParam(name = "entidad") String entidad, @WebParam(name = "idAnexo") Long idAnexo, @WebParam(name = "idioma") String idioma) throws Throwable;

}
