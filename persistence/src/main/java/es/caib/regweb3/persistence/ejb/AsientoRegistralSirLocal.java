package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

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
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public AsientoRegistralSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     *
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public AsientoRegistralSir transformarRegistroSalida(RegistroSalida registroSalida)
            throws Exception, I18NException;

    /**
     * Elimina los AsientoRegistralSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroEntrada transformarAsientoRegistralEntrada(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

    /**
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroSalida transformarAsientoRegistralSalida(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;
}
