package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TrazabilidadSir;

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
     * @throws Exception
     */
    List<TrazabilidadSir> getByRegistroSir(Long idRegistroSir) throws Exception;

    /**
     * Obtiene todas las TrazabilidadesSir a partir de un Identificador Intercambio
     *
     * @param idIntercambio
     * @return
     * @throws Exception
     */
    List<TrazabilidadSir> getByIdIntercambio(String idIntercambio, Long idEntidad) throws Exception;

    /**
     * Obtiene la TrazabilidadSir de un RegistroSir aceptado
     *
     * @param idRegistroSir
     * @return
     * @throws Exception
     */
    TrazabilidadSir getByRegistroSirAceptado(Long idRegistroSir) throws Exception;

    /**
     * Eimina todas las TrazabilidadesSir de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
