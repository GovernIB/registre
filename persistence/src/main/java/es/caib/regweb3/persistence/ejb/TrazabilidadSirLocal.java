package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TrazabilidadSir;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface TrazabilidadSirLocal extends BaseEjb<TrazabilidadSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/TrazabilidadSirEJB";


    /**
     * Obtiene las TrazabilidadesSir de un RegistroSir
     *
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    List<TrazabilidadSir> getByRegistroSir(Long idRegistroSir) throws I18NException;

    /**
     * Obtiene todas las TrazabilidadesSir a partir de un Identificador Intercambio
     *
     * @param idIntercambio
     * @return
     * @throws I18NException
     */
    List<TrazabilidadSir> getByIdIntercambio(String idIntercambio, Long idEntidad) throws I18NException;

    /**
     * Obtiene la TrazabilidadSir de un RegistroSir aceptado
     *
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    TrazabilidadSir getByRegistroSirAceptado(Long idRegistroSir) throws I18NException;

    /**
     * Eimina todas las TrazabilidadesSir de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
