package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface SirEnvioLocal {


    /**
     * Crear un intercambio SIR entrada, preparado par enviarse
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada crearIntercambioEntrada(RegistroEntrada registroEntrada, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Crear un intercambio SIR salida, preparado par enviarse
     * @param registroSalida
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida crearIntercambioSalida(RegistroSalida registroSalida, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Envia un intercambio a la oficina destino
     * @param tipoRegistro
     * @param registro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    RegistroSir enviarIntercambio(Long tipoRegistro, IRegistro registro, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

//    /**
//     * Renintenta los envíos con ERROR a SIR que pendientes de llegar a destino.
//     *
//     * @param entidad
//     * @throws Exception
//     */
//    void reintentarEnviosConError(Entidad entidad) throws Exception;

    Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws Exception;

//    /**
//     * Realiza un nuevo envío SIR a GEISEr
//     * 
//     * @param rsir
//     * @param entidadId
//     * @return
//     * @throws I18NException
//     */
//	RespuestaRegistroGeiser postProcesoNuevoRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws I18NException;
//
//	/**
//	 * Realiza una búsqueda del estado de tramitación de un registro SIR en GEISER
//	 * @param rsir
//	 * @param entidadId
//	 * @return
//	 * @throws I18NException
//	 */
//	RespuestaBusquedaTramitGeiser postProcesoBuscarEstadoTRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws I18NException;
//
//	/**
//	 * Realiza la búsqueda de un registro SIR en GEISER
//	 * 
//	 * @param rsir
//	 * @param entidadId
//	 * @return
//	 * @throws I18NException
//	 */
//	RespuestaConsultaGeiser postProcesoConsultarRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws I18NException;

	/**
	 * Actualiza el estado de todos los envíos SIR pendientes
	 * 
	 * @param id
	 * @throws Exception
	 * @throws I18NException
	 */
	void actualizarEnviosSir(Entidad entidad) throws Exception, I18NException;

	/**
	 * Actualiza el estado de los envíos SIR de un oficio de remisión
	 * 
	 * @param entidadId
	 * @param registroSir
	 * @return
	 * @throws Exception 
	 * @throws I18NException 
	 */
	List<RegistroSir> actualizarEstadoEnvioSir(Entidad entidad, OficioRemision oficioRemision, UsuarioEntidad usuario) throws Exception, I18NException;

	/**
	 * Actualiza la información de un registro SIR de Regweb con los datos de GEISER
	 * 
	 * @param registroSir
	 * @param usuarioEntidad
	 * @throws I18NException 
	 * @throws Exception 
	 */
	void actualizarEnvioSirRealizado(RegistroSir registroSir, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;
	
	
	/**
	 * Actualiza el identificador de intercambio de los registros recibidos
	 * 
	 * @param id
	 * @throws Exception
	 * @throws I18NException
	 */
	void actualizarIdEnviosSirRecibidos(Entidad entidad) throws Exception, I18NException;
	
	void forzarGuardado();
}

