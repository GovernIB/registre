package org.fundaciobit.plugins.distribucion;

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
    Destinatarios distribuir(String registro, boolean anexos) throws Exception;

    /**
     * @param destinatariosDefinitivos
     * @return
     * @throws Exception
     */
    Boolean enviarDestinatarios(List<Destinatario> destinatariosDefinitivos, String observaciones) throws Exception;
}
