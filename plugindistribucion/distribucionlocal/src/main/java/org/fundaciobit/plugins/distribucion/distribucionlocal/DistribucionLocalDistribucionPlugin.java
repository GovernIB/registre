package org.fundaciobit.plugins.distribucion.distribucionlocal;


import org.apache.log4j.Logger;
import org.fundaciobit.plugins.distribucion.Destinatario;
import org.fundaciobit.plugins.distribucion.Destinatarios;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author mgonzalez
 */
public class DistribucionLocalDistribucionPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());

    private static final String PROPERTY_BASE = DISTRIBUCION_BASE_PROPERTY + "distribucionlocal.";


	/*private static final String PROPERTY_EXEMPLE = PROPERTY_BASE +"exemple";

	public String getExample() throws Exception {
		//return getProperty(PROPERTY_EXEMPLE);

		return getPropertyRequired(PROPERTY_EXEMPLE);
	}*/

    /**
     *
     */
    public DistribucionLocalDistribucionPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionLocalDistribucionPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionLocalDistribucionPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Destinatarios distribuir(String registro, boolean anexos) throws Exception {

        Destinatarios destinatarios = new Destinatarios();

        List<Destinatario> destinatariosPosibles = new ArrayList<Destinatario>();
        List<Destinatario> destinatariosPropuestos = new ArrayList<Destinatario>();
        Destinatario destinatario = new Destinatario();
        destinatario.setId("1");
        destinatario.setName("BANDEJA 1");
        destinatariosPosibles.add(destinatario);

        destinatario = new Destinatario();
        destinatario.setId("2");
        destinatario.setName("BANDEJA 2");
        destinatariosPosibles.add(destinatario);

        destinatario = new Destinatario();
        destinatario.setId("3");
        destinatario.setName("BANDEJA 3");
        destinatariosPropuestos.add(destinatario);


        destinatarios.setPropuestos(destinatariosPropuestos);
        destinatarios.setPosibles(destinatariosPosibles);
        destinatarios.setModificable(false);
        //TODO tratar el tema de los anexos
        if (anexos) {
            //TODO tratar anexos
        }
        return destinatarios;
    }

    @Override
    public Boolean enviarDestinatarios(List<Destinatario> destinatariosDefinitivos, String observaciones) throws Exception {
        log.info("OBSERVACIONES EN PLUGIN " + observaciones);
        for (Destinatario destinatario : destinatariosDefinitivos) {
            log.info("DESTINATARIO ID EN PLUGIN " + destinatario.getId());
            log.info("DESTINATARIO NOMBRE EN PLUGIN " + destinatario.getName());
        }
        return true;
    }
}