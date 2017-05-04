package es.caib.regweb3.plugins.justificante;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.plugins.IPlugin;

import java.io.ByteArrayOutputStream;

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
     * @param estampat
     * @param urlVerificacio
     * @return
     * @throws Exception
     */
    ByteArrayOutputStream generarJustificante(RegistroEntrada registroEntrada, String estampat, String urlVerificacio) throws Exception;

    /**
     * Metodo que genera el justificante de un registro de entrada.
     * @param registroSalida registro de entrada del que se genera el justificante
     * @param estampat
     * @param urlVerificacio
     * @return
     * @throws Exception
     */
    ByteArrayOutputStream generarJustificante(RegistroSalida registroSalida, String estampat, String urlVerificacio) throws Exception;

}
