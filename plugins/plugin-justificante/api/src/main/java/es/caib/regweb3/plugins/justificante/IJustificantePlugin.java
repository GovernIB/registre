package es.caib.regweb3.plugins.justificante;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.IPlugin;

/**
 * Created by Fundació BIT.
 * @author jpernia
 * Date: 01/03/2017
 *
 */
public interface IJustificantePlugin extends IPlugin {

    public static final String JUSTIFICANTE_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "justificante.";

    /**
     * Metodo que genera el justificante de un registro de entrada.
     * @param registroEntrada registro de entrada del que se genera el justificante
     * @param url
     * @param specialValue
     * @param csv
     * @param idioma
     * @return
     * @throws I18NException
     */

    byte[] generarJustificanteEntrada(RegistroEntrada registroEntrada, String url, String specialValue, String csv, String idioma, Boolean sir) throws I18NException;
    /**
     * Metodo que genera el justificante de un registro de entrada.
     * @param registroSalida registro de entrada del que se genera el justificante
     * @param url
     * @param specialValue
     * @param csv
     * @param idioma
     * @return
     * @throws I18NException
     */
    byte[] generarJustificanteSalida(RegistroSalida registroSalida, String url, String specialValue, String csv, String idioma, Boolean sir) throws I18NException;

}
