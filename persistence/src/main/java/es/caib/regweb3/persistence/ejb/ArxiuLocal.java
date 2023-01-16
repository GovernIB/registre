package es.caib.regweb3.persistence.ejb;


import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface ArxiuLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/ArxiuEJB";

    /**
     *
     * @param expediente
     * @param apiArxiu
     * @throws I18NException
     */
    void cerrarExpediente(Expediente expediente, ApiArchivoDigital apiArxiu, Long idEntidad) throws I18NException;

    /**
     *
     * @param idEntidad
     * @param fechaInicio
     * @throws I18NException
     */
    public void cerrarExpedientesScheduler(Long idEntidad, String fechaInicio) throws I18NException;

}
