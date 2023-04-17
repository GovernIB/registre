package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.AnexoSir;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface AnexoSirLocal extends BaseEjb<AnexoSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/AnexoSirEJB";

    /**
     * Elimina los Archivos del sistema de archivos de los RegistrosSir Aceptados
     * @param idEntidad
     * @throws I18NException
     */
    int purgarAnexosAceptados(Long idEntidad) throws I18NException;

    /**
     * Purga los AnexosSir de un RegistroSir aceptado
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    int purgarAnexosRegistroSirAceptado(Long idRegistroSir) throws I18NException;

    /**
     * Elimina los AnexoSir de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    AnexoSir findByIdFichero(String idFichero) throws I18NException;
}

