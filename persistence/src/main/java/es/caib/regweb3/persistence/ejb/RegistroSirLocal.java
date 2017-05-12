package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RegistroSirLocal extends BaseEjb<RegistroSir, Long> {


    /**
     * Obtiene un RegistroSir a partir de los parámetros
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws Exception
     */
    public RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Obtiene un RegistroSir a partir de los parámetros
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    public RegistroSir getRegistroSir(String identificadorIntercambio) throws Exception;

    /**
     * Obtiene un RegistroSir incluyendo los anexos almancenados en disco
     * @param idRegistroSir
     * @return
     * @throws Exception
     */
    public RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws Exception;

    /**
     * Crea un RegistroSir
     * @throws Exception
     */
    public RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio) throws Exception;

    /**
     * Comrueba si una Oficina tiene RegistroSir
     * @param codigoOficinaActiva código de la Oficina Activa
     * @return
     * @throws Exception
     */
    public Boolean tieneRegistroSir(String codigoOficinaActiva) throws Exception;

    /**
     * Busca los RegistroSir en función de los parámetros, donde sólo mostrará los RegistroSir con codEntidadRegistralDestino = codOficinaActiva
     * @param pageNumber
     * @param any
     * @param registroSir
     * @param oficinaSir
     * @param estado
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Integer any, RegistroSir registroSir, String oficinaSir, String estado) throws Exception;

    /**
     * Obtiene los últimos ASR pendientes de procesar destinados a una OficinaSir
     * @param oficinaSir
     * @param total
     * @return
     * @throws Exception
     */
    public List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws Exception;

    /**
     * Modifica el Estado de un {@link RegistroSir}
     * @param idRegistroSir
     * @param estado
     * @throws Exception
     */
    public void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws Exception;

    /**
     *
     * @param ficheroIntercambio
     * @return
     * @throws Exception
     */
    public RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio)throws Exception;


    /**
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     *
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSir transformarRegistroSalida(RegistroSalida registroSalida)
            throws Exception, I18NException;

    /**
     * Elimina los RegistroSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * @param registroSir
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
    public RegistroEntrada transformarRegistroSirEntrada(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

    /**
     * @param registroSir
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
    public RegistroSalida transformarRegistroSirSalida(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;
}
