package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.Respuesta;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface PreRegistroLocal extends BaseEjb<PreRegistro, Long> {


    /**
     * Guarda un PreRegistro y le asocia un número de preRegistro
     * @param preRegistro
     * @return
     * @throws Exception
     */
    public PreRegistro preRegistrar(PreRegistro preRegistro) throws Exception;

    /**
     * Busca los PreRegistros en función de los parámetros, donde sólo mostrará los PreRegistros con codEntidadRegistralDestino = codOficinaActiva
     * @param pageNumber
     * @param any
     * @param preRegistro
     * @param codigoOficinaActiva
     * @param estado
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Integer any, PreRegistro preRegistro, String codigoOficinaActiva, Long estado) throws Exception;


    /**
     * Crea un PreRegistro a partir de un asiento registral recibido desde SIR
     * @param sicres3
     * @return
     * @throws Exception
     */
    public Respuesta<PreRegistro> crearPreRegistro(String sicres3) throws Exception;


    /**
     * Devuelve los PreRegistros pendientes de procesar de la Oficina Activa
     * @param codigoOficinaActiva
     * @return
     * @throws Exception
     */
    public List<PreRegistro> preRegistrosPendientesProcesar(String codigoOficinaActiva) throws Exception;


    /**
     * Devuelve los Últimos PreRegistros pendientes de procesar de la Oficina Activa
     * @param codigoOficinaActiva
     * @param total
     * @return
     * @throws Exception
     */
    public List<PreRegistro> getUltimosPreRegistrosPendientesProcesar(String codigoOficinaActiva, Integer total) throws Exception;


    /**
     * Obtiene una lista de PreRegistros de una oficina
     * @param codigoOficinaActiva código de la Oficina Activa
     * @return
     * @throws Exception
     */
    public Boolean tienePreRegistros(String codigoOficinaActiva) throws Exception;

}
