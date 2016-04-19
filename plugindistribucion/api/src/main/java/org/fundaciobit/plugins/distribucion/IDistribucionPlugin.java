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
     * @param registro
     * @param anexos
     * @return
     * @throws Exception
     */
    Destinatarios distribuir(RegistroEntrada registro, boolean anexos) throws Exception;

    /**
     * @param destinatariosDefinitivos
     * @return
     * @throws Exception
     */
    Boolean enviarDestinatarios(RegistroEntrada registro, List<Destinatario> destinatariosDefinitivos, String observaciones, boolean anexos) throws Exception;
}
