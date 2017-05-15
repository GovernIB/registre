package es.caib.regweb3.plugins.justificante;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.plugins.IPlugin;

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
     * @throws Exception
     */

    byte[] generarJustificante(RegistroEntrada registroEntrada, String url, String specialValue, String csv, String idioma) throws Exception;

    /**
     * Metodo que genera el justificante de un registro de entrada.
     * @param registroSalida registro de entrada del que se genera el justificante
     * @param url
     * @param specialValue
     * @param csv
     * @param idioma
     * @return
     * @throws Exception
     */
    byte[] generarJustificante(RegistroSalida registroSalida, String url, String specialValue, String csv, String idioma) throws Exception;

}
