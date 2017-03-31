package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.AsientoRegistralSir;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AsientoRegistralSirLocal extends BaseEjb<AsientoRegistralSir, Long> {


    /**
     * Obtiene un AsientoRegistral a partir de los parámetros
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Obtiene un AsientoRegistral a partir de los parámetros
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio) throws Exception;

    /**
     * Obtiene un AsientoRegistral incluyendo los anexos almancenados en disco
     * @param idAsientoRegistralsir
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir getAsientoRegistralConAnexos(Long idAsientoRegistralsir) throws Exception;

    /**
     * Crea un AsientoRegistralSir
     * @throws Exception
     */
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception;

    /**
     * Comrueba si una Oficina tiene AsientoRegistralSir
     * @param codigoOficinaActiva código de la Oficina Activa
     * @return
     * @throws Exception
     */
    public Boolean tieneAsientoRegistralSir(String codigoOficinaActiva) throws Exception;

    /**
     * Busca los AsientoRegistralSir en función de los parámetros, donde sólo mostrará los AsientoRegistralSir con codEntidadRegistralDestino = codOficinaActiva
     * @param pageNumber
     * @param any
     * @param asientoRegistralSir
     * @param organismos
     * @param estado
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Integer any, AsientoRegistralSir asientoRegistralSir, Set<String> organismos, String estado) throws Exception;

    /**
     * Obtiene los últimos ASR pendientes de procesas de un conjunto de Organismos
     * @param organismos
     * @param total
     * @return
     * @throws Exception
     */
    public List<AsientoRegistralSir> getUltimosPendientesProcesar(Set<String> organismos, Integer total) throws Exception;

    /**
     * Modifica el Estado de un {@link AsientoRegistralSir}
     * @param idAsientoRegistralSir
     * @param estado
     * @throws Exception
     */
    public void modificarEstado(Long idAsientoRegistralSir, EstadoAsientoRegistralSir estado) throws Exception;

    /**
     *
     * @param ficheroIntercambio
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio)throws Exception;

    /**
     * Elimina los AsientoRegistralSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
