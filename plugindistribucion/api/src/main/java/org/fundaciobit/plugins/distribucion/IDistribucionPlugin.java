package org.fundaciobit.plugins.distribucion;

import es.caib.regweb3.model.RegistroEntrada;
import org.fundaciobit.plugins.IPlugin;

import java.util.List;

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
     * @param anexos variable que indica si el registro lleva incluidos los anexos con los documentos.
     * @return
     * @throws Exception
     */
    Destinatarios distribuir(RegistroEntrada registro, boolean anexos) throws Exception;


    /**
     * Método que envia/distribuye el registro de entrada a la lista de destinatarios  indicada
     * @param registro registro de entrada que se distribuye
     * @param destinatariosDefinitivos destinatarios a los que enviar el registro de entrada
     * @param observaciones observaciones al envio
     * @param anexos variable que indica si el registro lleva incluidos los anexos con los documentos.
     * @return
     * @throws Exception
     */
    Boolean enviarDestinatarios(RegistroEntrada registro, List<Destinatario> destinatariosDefinitivos, String observaciones, boolean anexos) throws Exception;
}
