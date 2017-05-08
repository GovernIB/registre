package es.caib.regweb3.plugins.distribucion;

import es.caib.regweb3.model.RegistroEntrada;

import org.fundaciobit.plugins.IPlugin;

import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 27/10/2015
 */
public interface IDistribucionPlugin extends IPlugin {

    public static final String DISTRIBUCION_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "distribucion.";

    /**
     * Metodo que obtiene los destinatarios a los que se debe distribuir el registro de entrada.
     * @param registro registro de entrada que se distribuye
     * @return
     * @throws Exception
     */
    Destinatarios distribuir(RegistroEntrada registro) throws Exception;


    /**
     * Método que envia/distribuye el registro de entrada a la lista de destinatarios  indicada
     * @param registro registro de entrada que se distribuye
     * @param destinatariosDefinitivos destinatarios a los que enviar el registro de entrada
     * @param observaciones observaciones al envio
     * @return
     * @throws Exception
     */
    Boolean enviarDestinatarios(RegistroEntrada registro, 
        List<Destinatario> destinatariosDefinitivos, 
        String observaciones, Locale locale) throws Exception;

    /*
      Método que devuelve la configuración de la distribución.
     */
    ConfiguracionDistribucion configurarDistribucion() throws Exception;

}
